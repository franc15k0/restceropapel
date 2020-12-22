package gob.pe.minam.restceropapel.api.service;

import gob.pe.minam.restceropapel.api.model.*;
import gob.pe.minam.restceropapel.api.repository.*;
import gob.pe.minam.restceropapel.security.entity.Sesion;
import gob.pe.minam.restceropapel.security.service.IUsuarioService;
import gob.pe.minam.restceropapel.util.Constante;
import gob.pe.minam.restceropapel.util.HandledException;
import gob.pe.minam.restceropapel.util.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EcodocService implements IEcodocService{
    private Logger logger = LoggerFactory.getLogger(ExpedienteService.class);
    @Autowired
    IEcodocMapper ecodocMapper;
    @Autowired
    ICiudadanoService ciudadanoService;
    @Autowired
    IRegistroMapper registroMapper;
    @Autowired
    IArchivoMapper archivoMapper;
    @Autowired
    IDocumentoMapper documentoMapper;
    @Autowired
    private IUploadFileService uploadService;
    @Autowired
    ICodigoService iCodigoService;
    @Value("${file.upload-dir-ecodoc}")
    String uploadDirEcodoc;
    @Autowired
    private IUsuarioService usuarioService;
    public String getParametro(List<DetalleCompendio> parametros, String filter){
        return parametros.stream().filter(p -> filter.equals(p.getCodElementoTabla())).findAny().get().getTxtReferenciaEcodoc();
    }

    @Transactional(rollbackFor={Exception.class})
    public ExpedienteEdoc enviarExpedienteEcodoc(Expediente expedienteCP) throws HandledException {

        ClienteEdoc cliente =
                buscarCliente(expedienteCP.getClienteEdoc())
                .orElseGet(()-> {
                    Ciudadano ciudadano = ciudadanoService.buscarCiudadanoConsolidado(expedienteCP.getRegistro().getIdCiudadano()).get();
                    ClienteEdoc c = ClienteEdoc
                            .builder()
                            .estado(Constante.ESTADO_CLIENTE)
                            .numeroIdentificacion(expedienteCP.getClienteEdoc().getNumeroIdentificacion())
                            .razonSocial((expedienteCP.getClienteEdoc().getIdTipoCliente().equals("1"))?ciudadano.getRazonSocial():"")
                            .apellidoPaterno((expedienteCP.getClienteEdoc().getIdTipoCliente().equals("2"))?ciudadano.getTxtApellidoPaterno():"")
                            .apellidoMaterno((expedienteCP.getClienteEdoc().getIdTipoCliente().equals("2"))?ciudadano.getTxtApellidoMaterno():"")
                            .nombre((expedienteCP.getClienteEdoc().getIdTipoCliente().equals("2"))?ciudadano.getNombres():"")
                            .telefono(ciudadano.getNumTelefonoCelular())
                            .idTipoCliente(expedienteCP.getClienteEdoc().getIdTipoCliente())
                            .idTipoMedioEnvio(Constante.ID_TIPO_ENVIO)
                            .build();
                    ecodocMapper.spInsertClienteEco(c);

                    ecodocMapper.spInsertDireccionEco(DireccionEdoc
                    .builder()
                     .direccion(ciudadano.getTxtDireccion())
                     .idCliente(c.getIdCliente())
                     .ubigeo(ciudadano.getIdUbigeoDomicilio())
                    .build());
                return c;
            });
        Optional.ofNullable(expedienteCP.getPersonalEcodoc()).map(pe -> {
            pe.setIdCliente(cliente.getIdCliente());
            ecodocMapper.spInsertPersonalEco(pe);
            return pe;
        });
        String plazo = "";
        List<DetalleCompendio> expedienteParametros = iCodigoService.getMapDetalleCompendiosCorto(Constante.PARAMETROS_EXPEDIENTE);
        List<DetalleCompendio> documentosParametros = iCodigoService.getMapDetalleCompendiosCorto(Constante.PARAMETROS_DOCUMENTO);
        Documento documento = Documento.builder().idRegistro(expedienteCP.getRegistro().getIdRegistro()).build();
        documentoMapper.spBuscarDocumento(documento);
        if(documento.getCodEtipoDocumento().equals(Constante.ES_TUPA1)){
            plazo = getParametro(expedienteParametros, Constante.EXPEDIENTE_PLAZOTUPA1);
        }else if(documento.getCodEtipoDocumento().equals(Constante.ES_TUPA2)){
            plazo =  getParametro(expedienteParametros, Constante.EXPEDIENTE_PLAZOTUPA2);
        }else{
            plazo = getParametro(expedienteParametros, Constante.EXPEDIENTE_PLAZOTUPADEMAS);
        }

        ExpedienteEdoc expediente = ExpedienteEdoc
                .builder()
                .copiado(getParametro(expedienteParametros, Constante.EXPEDIENTE_COPIADO))
                .estado(getParametro(expedienteParametros, Constante.EXPEDIENTE_ESTADO))
                .titulo(expedienteCP.getRegistro().getTxtDescripcionTramite())
                .idCliente(cliente.getIdCliente())
                .idPersona(Integer.parseInt(getParametro(expedienteParametros, Constante.EXPEDIENTE_IDPERSONA)))
                .idProceso(Integer.parseInt(getParametro(expedienteParametros, Constante.EXPEDIENTE_IDPROCESO)))
                .idPersonaActualiza(Integer.parseInt(getParametro(expedienteParametros, Constante.EXPEDIENTE_IDPERSONACTUALIZA)))
                .plazo(plazo)
                .idEstacionActual(Integer.parseInt(getParametro(expedienteParametros, Constante.EXPEDIENTE_IDESTACIONACTUAL)))
                .idEstacionPrevia(Integer.parseInt(getParametro(expedienteParametros, Constante.EXPEDIENTE_IDESTACIONPREVIA)))
                .idPlazoTipoDias(Integer.parseInt(getParametro(expedienteParametros, Constante.EXPEDIENTE_IDPLAZOTIPODIAS)))
                .idSede(Integer.parseInt(getParametro(expedienteParametros, Constante.EXPEDIENTE_IDSEDE)))
                .claveExterno(PasswordGenerator.generateClaveEdoc1())
                .idTipoOrigen(Integer.parseInt(getParametro(expedienteParametros, Constante.EXPEDIENTE_IDTIPOORIGEN)))
                .build();
        ecodocMapper.spInsertExpedienteEco(expediente);
        Archivo archivoP = Archivo.builder().idDocumento(documento.getIdDocumento()).build();
        archivoMapper.spBuscarAchivos(archivoP);
        List<Archivo> listArchivos = archivoP.getListArchivos();
        Archivo archivoFlgPrincipal = listArchivos
                .stream()
                .filter(v -> ("1".equals(v.getFlgEsprincipal())))
                .findAny()
                .orElse(null);
        DocumentoEdoc documentoEdoc = DocumentoEdoc
                .builder()
                .estado(getParametro(documentosParametros, Constante.DOCUMENTO_ESTADO))
                .folios(getParametro(documentosParametros, Constante.DOCUMENTO_FOLIOS))
                .titulo(expediente.getTitulo())
                .autor(getParametro(documentosParametros, Constante.DOCUMENTO_AUTOR))
                .numero(archivoFlgPrincipal.getTxtNumeroArchivo())
                .expediente(expediente.getIdExpediente())
                .idTipoDocumento(documento.getCodEtipoDocumento())
                .idAreaAutor(Integer.parseInt(getParametro(documentosParametros, Constante.DOCUMENTO_IDAREAAUTOR)))
                .presentaCd(getParametro(documentosParametros, Constante.DOCUMENTO_PRESENTACD))
                .foliado(getParametro(documentosParametros, Constante.DOCUMENTO_FOLIADO))
                .esPrincipal(getParametro(documentosParametros, Constante.DOCUMENTO_ESPRINCIPAL))
                .estadoDigitalizacion(getParametro(documentosParametros, Constante.DOCUMENTO_ESTADODIGITALIZACION))
                .cd(getParametro(documentosParametros, Constante.DOCUMENTO_CD))
                .build();
        ecodocMapper.spInsertDocumentoEco(documentoEdoc);

        listArchivos.forEach(a-> insertarArchivoEcodoc(a, documentoEdoc.getIdDocumento()));
        if(expedienteCP.getRegistro().getFlgDeclaracionJurada().equals(Constante.FLG_AFIRMACION)) cargarArchivoDeclaracionJuradaTerminosCondiciones(documentoEdoc.getIdDocumento());
        ecodocMapper.spInsertResponsRegisEco(ResponsableRegEdoc
                .builder()
                .idExpediente(expediente.getIdExpediente())
                .idPersona(Constante.RESPONSABLE_REGISTRO_CONSTANTE)
                .responsable(Constante.RESPONSABLE)
                .build());
        Sesion sesion= usuarioService.obtenerSesion(Sesion
                .builder()
                .idUsuario(expedienteCP.getUsuario().getIdUsuario())
                .build());
        expedienteCP.getRegistro().setIdSesionMod(sesion.getIdSesion());
        expedienteCP.getRegistro().setIdExpediente(expediente.getIdExpediente());
        expedienteCP.getRegistro().setCodEestaTrami(Constante.ESTADO_ENVIADO);
        registroMapper.spActualizarRegistroExp(expedienteCP.getRegistro());

        return expediente;
    }
    public void insertarArchivoEcodoc(Archivo archivo, Long idDocumento) {
        try{
            System.out.println("getTxtNombreArchivo::"+archivo.getTxtNombreArchivo());
            uploadService.replicar(archivo.getTxtNombreArchivo());
            ArchivoEdoc archivoEdoc = ArchivoEdoc
                    .builder()
                    .estado("A")
                    .nombre(archivo.getTxtNombreArchivo().split("-")[1].trim())
                    .rutaLocal(uploadDirEcodoc+"/"+archivo.getTxtNombreArchivo())
                    .documento(idDocumento)
                    .esPrincipal(archivo.getFlgEsprincipal())
                    .build();
            ecodocMapper.spInsertArchivoEco(archivoEdoc);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }
    public List<PersonalEcodoc> traerPersonal(ClienteEdoc clienteP) throws Exception{
                Optional<ClienteEdoc> cliente = buscarCliente(clienteP);
                List<PersonalEcodoc> listEcodoc = new ArrayList<>();
                if(cliente.isPresent()){
                    PersonalEcodoc personal = PersonalEcodoc
                            .builder()
                            .idCliente(cliente.get().getIdCliente())
                            .build();
                    ecodocMapper.spBuscarPersonal(personal);
                    listEcodoc = personal.getListPersonal();
                }
              return listEcodoc;
    }
    public List<PersonalEcodoc>  existePersonal(ClienteEdoc clienteP) throws HandledException{
        Optional<ClienteEdoc> cliente = buscarCliente(clienteP);
        List<PersonalEcodoc> listEcodoc = new ArrayList<>();
        if(cliente.isPresent()){
            PersonalEcodoc personal = PersonalEcodoc
                    .builder()
                    .idCliente(cliente.get().getIdCliente())
                    .dni(clienteP.getDni())
                    .build();
            ecodocMapper.spBuscarPersonalDni(personal);
            listEcodoc = personal.getListPersonal();
        }

        return listEcodoc;
    }
    public Optional<ClienteEdoc> buscarCliente(ClienteEdoc cliente){
        ecodocMapper.spBuscarCliente(cliente);
        return  Optional.ofNullable(cliente).map(c -> {
            if(c.getIdCliente()== null)return null;
            return c;
        });
    }
    public void cargarArchivoDeclaracionJuradaTerminosCondiciones(Long idDocumento)  {
        try {
            logger.info(Constante.NOMBRE_DECLARACION_JURADA);
            uploadService.replicar(Constante.NOMBRE_DECLARACION_JURADA);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        ArchivoEdoc archivoEdoc = ArchivoEdoc
                .builder()
                .estado("A")
                .nombre(Constante.NOMBRE_DECLARACION_JURADA.split("-")[1].trim())
                .rutaLocal(uploadDirEcodoc+"/"+Constante.NOMBRE_DECLARACION_JURADA)
                .documento(idDocumento)
                .esPrincipal("0")
                .build();
        ecodocMapper.spInsertArchivoEco(archivoEdoc);
    }
}
