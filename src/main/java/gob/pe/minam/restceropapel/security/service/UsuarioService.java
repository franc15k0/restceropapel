package gob.pe.minam.restceropapel.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gob.pe.minam.restceropapel.api.model.*;

import gob.pe.minam.restceropapel.api.repository.IUbigeoMapper;
import gob.pe.minam.restceropapel.api.service.ICiudadanoService;
import gob.pe.minam.restceropapel.api.service.ICodigoService;
import gob.pe.minam.restceropapel.config.LdapConfig;
import gob.pe.minam.restceropapel.security.entity.*;
import gob.pe.minam.restceropapel.security.repository.ISesionMapper;
import gob.pe.minam.restceropapel.util.*;
import gob.pe.minam.restceropapel.security.repository.IUsuarioMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import gob.pe.minam.restceropapel.util.Constante;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements  IUsuarioService, UserDetailsService{

    private Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    @Autowired
    private IUsuarioMapper usuarioMapper;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    ICiudadanoService ciudadanoService;
    @Autowired
    private IUbigeoMapper ubigeoMapper;
    @Autowired
    private ISesionMapper sesionMapper;
    @Autowired
    ICodigoService iCodigoService;

    @Value("${build.version}")
    private String buildVersion;
    @Value("${servicios.rest.consulta.minam.persona}")
    private String consultaPersona;
    @Value("${aplication.hostname}")
    private String hostAplicacion;
    @Value("${aplication.idsistema}")
    private int idAplicacion;
    @Value("${aplication.redirect.enlace}")
    private String redirectEnlace;
    @Value("${aplication.api.sms}")
    private String apiSMS;
    @Value("${servicios.rest.correo.minam}")
    private String apiMail;

    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String tramaUsuario) throws UsernameNotFoundException {
        String[] trama = StringUtils.split(tramaUsuario, '|');
        Boolean validoLdap = false;
        String usuarioName = trama[0];
        String password = trama[1];
        if (tramaUsuario == null) {
            throw new UsernameNotFoundException("Username no ha sido proveido");
        }
        Usuario usuario = null;
        LdapConfig ldapVal = new LdapConfig();
        validoLdap = ldapVal.validarUsuarioLDAP(usuarioName,password);
        if(validoLdap){
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                String passwordEncripted=  encoder.encode(password);
                usuario= getUsuarioInterno(usuarioName).orElseThrow(() -> new UsernameNotFoundException("Error en el login: no existe el usuario '"+usuarioName+"' en el sistema!"));
                usuario.setClaveTempo(passwordEncripted);
        }else{
            usuario = getUsuarioExterno(usuarioName).orElseThrow(() -> new UsernameNotFoundException("Error en el login: no existe el usuario '"+usuarioName+"' en el sistema!"));
        }
        if(usuario == null) {
            logger.error("Error en el login: no existe el usuario '"+usuarioName+"' en el sistema!");
            throw new UsernameNotFoundException("Error en el login: no existe el usuario '"+usuarioName+"' en el sistema!");
        }
        Rol rol = Rol
                .builder()
                .idUsuario(usuario.getIdUsuario())
                .idSistema(idAplicacion)
                .build();
        usuarioMapper.spFindByRol(rol);
        List<Rol> roles =  rol.getListRol();
        List<GrantedAuthority> authorities = roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                .peek(authority -> logger.info("Role: " + authority.getAuthority()))
                .collect(Collectors.toList());
        UserDetails userDetails =(UserDetails)new User(usuario.getUsuario(),usuario.getClaveTempo(), authorities);
        return userDetails;
    }

    @Transactional(rollbackFor={Exception.class})
    public Usuario insertUsuario(Usuario usuario) throws HandledException {
        if(getUsuarioExterno(usuario.getUsuario()).isPresent()){
            usuario.setResultado("El Usuario ya existe");
            throw new HandledException("Exception message", "El Usuario ya existe");
        }
        try {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            List<DetalleCompendio> roles = iCodigoService.getMapDetalleCompendiosCorto(Constante.CODIGO_ROLES);
            Sesion session = obtenerParametroRegistroUsuario(usuario.getSesion());
            usuario.setContracena(encoder.encode(usuario.getContracena()));
            usuario.setEstadoRegistro(Constante.USUARIO_ESTADO_REGISTRO);//("I");
            usuario.setTipo(Constante.USUARIO_TIPO);//("1");
            usuario.setIdSesionReg(session.getIdSesion());
            usuario.setCambioClave(Constante.USUARIO_CAMBIO_CLAVE);//("0");
            usuario.setIdRol((usuario.getIdTipoDocumento()
                    .equals(Constante.ES_PERSONANATURAL))?
                    roles.stream().filter(r -> Constante.NOMBRE_ROL_NATURAL.equals(r.getTxtDescripcionCorta())).findAny().get().getTxtReferencia1():
                    roles.stream().filter(r -> Constante.NOMBRE_ROL_JURIDICA.equals(r.getTxtDescripcionCorta())).findAny().get().getTxtReferencia1());
            usuario.setNumeroDocumento(usuario.getUsuario());
            usuarioMapper.spInsertUsuario(usuario);
            Ciudadano ciudadanoCargado =  ciudadanoService.getCiudadanoNroDocumento(usuario.getIdTipoDocumento(),usuario.getUsuario()).orElseGet(() -> {
                Ciudadano ciudadano = new Ciudadano();
                if(usuario.getIdTipoDocumento().equals(Constante.ES_PERSONANATURAL)){
                    PersonaNatural personaNatural = PersonaNatural
                            .builder()
                            .numDni(usuario.getUsuario())
                            .txtApePaterno(usuario.getApellidoPaterno())
                            .txtApeMaterno(usuario.getApellidoMaterno())
                            .txtNombres(usuario.getNombres())
                            .flgSexo(usuario.getFlgSexo())
                            .fecNacimiento(usuario.getFecNacimiento())
                            .idSesionMod(session.getIdSesion())
                            .idSesionReg(session.getIdSesion())
                            .build();
                    ciudadanoService.insertPersonaNatural(personaNatural);
                    ciudadano.setIdNatural(personaNatural.getIdNatural());
                    ciudadano.setIdUbigeoDomicilio(obtenerUbigeo(usuario.getTxtUbigeo()));
                }else {
                    PersonaJuridica personaJuridica = PersonaJuridica
                            .builder()
                            .numRuc(usuario.getUsuario())
                            .txtRazonSocial(usuario.getNombres())
                            .numAsientoRegistral(usuario.getNumAsientoRegistral())
                            .numPartidaElectronica(usuario.getNumPartidaElectronica())
                            .idSesionMod(session.getIdSesion())
                            .idSesionReg(session.getIdSesion())
                            .build();
                    ciudadanoService.insertPersonaJuridica(personaJuridica);
                    ciudadano.setIdJuridica(personaJuridica.getIdJuridica());
                    ciudadano.setIdUbigeoDomicilio(Integer.parseInt(usuario.getUbigeo()));
                }
                ciudadano.setIdUsuario(usuario.getIdUsuario());
                ciudadano.setTxtDireccion(usuario.getDireccion());
                ciudadano.setNumTelefonoCelular(usuario.getTelefono1());
                ciudadano.setTxtCorreoElectronico(usuario.getCorreo());
                ciudadano.setIdSesionMod(session.getIdSesion());
                ciudadano.setIdSesionReg(session.getIdSesion());
                ciudadanoService.insertCiudadano(ciudadano);
                return ciudadano;
            });

            usuario.setValido(Valido
                    .builder()
                    .idUsuario(usuario.getIdUsuario())
                    .token(PasswordGenerator.generateTokenDefault())
                    .codigo(Long.parseLong(PasswordGenerator.generateCodigoDefault()))
                    .flgAccionUsuario(Constante.FLG_NUEVO_USUARIO)
                    .idSesionMod(session.getIdSesion())
                    .idSesionReg(session.getIdSesion())
                    .estado("0")
                    .idCiudadano(ciudadanoCargado.getIdCiudadano())
                    .build());
            usuarioMapper.spInsertUsuarioValid(usuario.getValido());
            usuario.getValido().setResultado("Se valido Correctamente");
            logger.info(usuario.getValido().getResultado());
            logger.info(usuario.getValido().getCodigo()+"");
            enviarCorreo(usuario.getCorreo(),redirectEnlace+Constante.FLG_NUEVO_USUARIO+"/"+usuario.getValido().getTokenFinal(), usuario.getNombres()+" "+Optional.ofNullable(usuario.getApellidoPaterno()).orElseGet(()->""));
            enviarSMS(usuario.getTelefono1(),usuario.getValido().getCodigo());

            usuario.setResultado(Optional.ofNullable(usuario.getResultado()).orElseGet(()->"Se ha presentado un error inesperado"));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
            throw new HandledException("Fallo al Registar Usuario - Existen Parametros Invalidos", ex);
        }

        return usuario;
    };

    @Transactional(rollbackFor={Exception.class})
    public void validate(Valido valido) throws HandledException {
        try{

            Sesion session = obtenerSesion(Sesion
                    .builder()
                    .idUsuario(valido.getIdUsuario())
                    .linkAplicativo(valido.getLinkAplicativo())
                    .build());
            valido.setIdSesionMod(session.getIdSesion());
            usuarioMapper.spModificarUserValid(valido);
        }catch (Exception ex){
                  logger.error(ex.getMessage());
                  valido.setResultado(null);
        }
    }
   public Optional<Usuario> getUsuarioExterno(String userName){
        Usuario usuario = Usuario.builder().usuario(userName).build();
        usuarioMapper.spUsuarioExterno(usuario);
        return usuario.getListUsuarios().stream().findFirst();
   }
   public Optional<Usuario> getUsuarioInterno(String userName){
       Usuario usuarioFys = Usuario.builder().usuario(userName).build();
       usuarioMapper.spSelPersonalUsuario(usuarioFys);
       return usuarioFys.getListUsuarios().stream().findFirst().map(uf -> {
               Usuario usuarioMinam = Usuario.builder().idUser(uf.getIdUser()).build();
               usuarioMapper.spSelUsuarioIdSegfys(usuarioMinam);
               return usuarioMinam.getListUsuarios().stream().findFirst().orElseGet(()->null);
               });

   }
    public void listarUbigeos(Ubigeo ubigeo){
        ubigeoMapper.spListarUbigeos(ubigeo);

    }
    public Integer obtenerUbigeo(String txtUbigeo){
        String[] tramaUbigeo = StringUtils.split(txtUbigeo, "\\/");
        String trama2 = "";
        String trama3 = "";
        if(tramaUbigeo.length==2){
            trama2 = tramaUbigeo[0];
            trama3 =tramaUbigeo[1];
        }
        else{
            trama2 = tramaUbigeo[1];
            trama3 = tramaUbigeo[2];
        }
         return Optional.ofNullable(ubigeoMapper.getFuncionUbigeo(Ubigeo.builder()
                .txtUbigeoRegion(tramaUbigeo[0])
                .txtUbigeoProvincia(trama2)
                .txtUbigeoDistrito(trama3).build())).orElseGet(()-> Constante.INTERNACIONAL);
    }

    public Sesion obtenerParametroRegistroUsuario(Sesion sesion){
        Parametro parametro = Parametro.builder().snombre(Constante.CERO_PAPEL).build();
        sesionMapper.spBuscarParametroSesion(parametro);
        parametro.setSvalor(parametro.getValorCursor().stream().findFirst().get().getSvalor());
        Usuario usuario =  Usuario.builder().usuario(parametro.getSvalor()).build();
        usuarioMapper.spBuscarUsuarioDefault(usuario);
        sesion.setIdUsuario(usuario.getListUsuarios().stream().findFirst().get().getIdUsuario());
        return obtenerSesion(sesion);
    }
    public Sesion obtenerSesion(Sesion sesion){

        try {
            sesion.setSmacaddress(Utilitario.obtenerMACRemota());
            sesion.setSipaddress(Utilitario.obtenerIpAddress(request));
            sesion.setSappserver(Utilitario.obtenerIPRemota());
            sesion.setSusuariored(Optional.ofNullable(sesion.getSusuariored()).orElseGet(() ->System.getProperties().getProperty("user.name")));
            sesion.setSsistoperativo(System.getProperties().getProperty("os.name"));
            sesion.setSversionmodulo(buildVersion);
            sesion.setIdSistema(""+idAplicacion);
            sesion.setLinkAplicativo(Optional.ofNullable(sesion.getLinkAplicativo()).orElseGet(() ->hostAplicacion));
            sesionMapper.spInsertSesion(sesion);
            //sesion.getIdSesionCursor().stream().forEach( v -> logger.info("lllll",v.getIdSesion()));
           sesion.setIdSesion(sesion.getIdSesionCursor().stream().findFirst().get().getIdSesion());
            System.out.println(sesion.getIdSesion());
            return sesion;
        }catch (Exception e){
           logger.error(e.getMessage());
            return null;
        }

    }
    private void enviarSMS(String nroCelular, Long codigo){
        final String uri = apiSMS+nroCelular+"/MINAM:"+codigo;
        RestTemplate restTemplate = new RestTemplate();
        String res = restTemplate.getForObject(uri, String.class);
        System.out.println(res);
    }
    private void enviarCorreo(String mail, String mensaje, String nombreUsuario){

        final String uri = apiMail;
        RestTemplate restTemplate = new RestTemplate();
        String cuerpo = "Estimado Sr.(a) "+nombreUsuario+", para validar el requerimiento de su cuenta en el sistema Cero Papel, debe dar click en la siguiente dirección "+
                "e ingresar el código enviado a su número de celular.\n" +
                mensaje +"\n" +
                "\n" +
                "Ministerio del Ambiente - MINAM\n" +
                "Av. Antonio Miroquesada 425, Magdalena del Mar, Lima - Perú.";
        HttpEntity<MensajeCorreo> request = new HttpEntity<>(MensajeCorreo
                .builder()
                .hacia(mail)
                .contenido(cuerpo)
                .build());
        String res = restTemplate.postForObject(uri,request, String.class);
        System.out.println(res);
    }
    public String getAdress(){
        return hostAplicacion;
    }
    public Optional<Valido> getUsuarioValido(Valido valid){
        usuarioMapper.spGetUsuarioValido(valid);
        return Optional.ofNullable(valid).map(uv ->{
            if(uv.getIdUsuarioValid()==null) return null;
            return uv;
        });
    }
    @Transactional(rollbackFor={Exception.class})
    public void spResetearContrasena(Usuario usuario) throws HandledException{
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Sesion session = obtenerSesion(Sesion.builder().idUsuario(usuario.getIdUsuario()).build());
        usuario.setIdSesionMod(session.getIdSesion());
        usuario.setContracena(encoder.encode(usuario.getContracena()));
        usuario.setIdSistema(idAplicacion);
        usuario.setCodigoRol(Constante.CODIGO_ROLES);
        usuario.setDescRol(Constante.NOMBRE_ROL_NATURAL);
        usuarioMapper.spResetearContrasena(usuario);
        logger.info("usuario.getEsUsuarioExterno()"+usuario.getEsUsuarioExterno());
        if(usuario.getEsUsuarioExterno().equals("1")){
            ciudadanoService.getCiudadanoNroDocumento(Constante.ES_PERSONANATURAL,usuario.getUsuario()).orElseGet(() -> {
                Usuario usuarioExterno = getUsuarioExterno(usuario.getUsuario()).get();
                PersonaNatural personaNatural = PersonaNatural
                            .builder()
                            .numDni(usuarioExterno.getUsuario())
                            .txtApePaterno(usuarioExterno.getApellidos().split(" ")[0])
                            .txtApeMaterno(usuarioExterno.getApellidos().split(" ")[1])
                            .txtNombres(usuarioExterno.getNombres())
                            .idSesionMod(session.getIdSesion())
                            .idSesionReg(session.getIdSesion())
                            .build();
                ciudadanoService.insertPersonaNatural(personaNatural);
                ciudadanoService.insertCiudadano(Ciudadano
                        .builder()
                        .idNatural(personaNatural.getIdNatural())
                        .idUsuario(usuario.getIdUsuario())
                        .numTelefonoCelular(usuarioExterno.getTelefono1())
                        .txtCorreoElectronico(usuarioExterno.getCorreo())
                        .idSesionMod(session.getIdSesion())
                        .idSesionReg(session.getIdSesion())
                        .build());
                return new Ciudadano();
            });
        }
    }
    public Usuario enviarInformacionRecuperarContrasena(String numeroDocumento){

        Usuario usuario = getUsuarioExterno(numeroDocumento).map(u->{
            Sesion session = obtenerSesion(Sesion
                    .builder()
                    .idUsuario(u.getIdUsuario())
                    .build());
            u.setIdSesionMod(session.getIdSesion());
            usuarioMapper.spModificarUsuarioInactivo(u);
            Valido valido = Valido
                    .builder()
                    .idUsuario(u.getIdUsuario())
                    .token(PasswordGenerator.generateTokenDefault())
                    .codigo(Long.parseLong(PasswordGenerator.generateCodigoDefault()))
                    .flgAccionUsuario(Constante.FLG_CAMBIAR_CONTRASENA)
                    .idSesionMod(session.getIdSesion())
                    .idSesionReg(session.getIdSesion())
                    .estado("0")
                    .build();
            usuarioMapper.spInsertUsuarioValid(valido);
            enviarCorreo(u.getCorreo(),redirectEnlace+Constante.FLG_CAMBIAR_CONTRASENA+"/"+valido.getTokenFinal(), u.getNombres()+" "+Optional.ofNullable(u.getApellidoPaterno()).orElseGet(()->""));
            enviarSMS(u.getTelefono1(),valido.getCodigo());
            return  u;
        }).orElseGet(()->null);

        return usuario;
    }
    public Reniec buscarReniec(String DNI){
        final String uri = consultaPersona+DNI;
        RestTemplate restTemplate = new RestTemplate();
        ReniecWs reniecWs = restTemplate.getForObject(uri, ReniecWs.class);
        return reniecWs.getData();
    }
    @Transactional(rollbackFor={Exception.class})
    public void regenerarCodigoValidacion(String token, String linkApp){

            Long idUsuario = Long.parseLong(token.replaceAll("[^0-9]", ""));

            Sesion session = obtenerSesion(Sesion
                    .builder()
                    .idUsuario(idUsuario)
                    .linkAplicativo(linkApp)
                    .build());
            Valido valido = Valido
                    .builder()
                    .token(token)
                    .codigo(Long.parseLong(PasswordGenerator.generateCodigoDefault()))
                    .idSesionMod(session.getIdSesion())
                    .build();
            usuarioMapper.spRegenerarUserValid(valido);
            Ciudadano ciudadano = ciudadanoService.buscarCiudadanoConsolidado(valido.getIdCiudadano()).get();
            String numeroTelefonoCelular = ciudadano.getNumTelefonoCelular();
            enviarSMS(numeroTelefonoCelular,valido.getCodigo());

    }
    public List<Menu> listMenuSistema(Long idUsuario){
        Menu menu = Menu
                .builder()
                .idUsuario(idUsuario)
                .idSistema(idAplicacion)
                .build();
        usuarioMapper.spBuscarMenuUsuarioRol(menu);
        return menu.getListMenu();
    }

}

