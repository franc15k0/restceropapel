package gob.pe.minam.restceropapel.api.service;

import gob.pe.minam.restceropapel.api.model.Ciudadano;
import gob.pe.minam.restceropapel.api.model.PersonaJuridica;
import gob.pe.minam.restceropapel.api.model.PersonaNatural;
import gob.pe.minam.restceropapel.api.repository.ICiudadanoMapper;
import gob.pe.minam.restceropapel.security.entity.Sesion;
import gob.pe.minam.restceropapel.security.service.IUsuarioService;
import gob.pe.minam.restceropapel.util.HandledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CiudadanoService implements ICiudadanoService {
    private Logger logger = LoggerFactory.getLogger(ExpedienteService.class);
    @Autowired
    ICiudadanoMapper iCiudadanoMapper;
    @Autowired
    private IUsuarioService usuarioService;


    public List<Ciudadano> getCiudadanoRepresentanteLegal(Long idCiudadano){
        AtomicReference<List<Ciudadano>> representantes = new AtomicReference<>();
        getCiudadanoId(idCiudadano).map(c ->{
                    Ciudadano representantesP = Ciudadano.builder().idJuridica(c.getIdJuridica()).build();
                    iCiudadanoMapper.spBuscarCiudadanoRepresentanteLegal(representantesP);
                    representantes.set(representantesP.getListCiudadano());
                    return c;
                }

        ).orElseGet(()->null);

        return representantes.get();
    }
    public Optional<Ciudadano> getCiudadanoNroDocumento(String TipoDocumento,String NroDocumento){
        Ciudadano ciudadano = Ciudadano
                .builder()
                .idTipoDocumento(TipoDocumento)
                .numeroDocumento(NroDocumento)
                .build();
        iCiudadanoMapper.spBuscarCiudadano(ciudadano);
        return Optional.ofNullable(ciudadano).map(c -> {
            if(c.getIdCiudadano() == null) return null;
            return c;
        });
    }
    @Transactional(rollbackFor={Exception.class})
    public Ciudadano grabarRepresentanteLegal(Ciudadano ciudadano)throws HandledException {
        try {
        ciudadano.getSesion().setIdUsuario(ciudadano.getIdUsuario());
        Sesion sesion =  usuarioService.obtenerSesion(ciudadano.getSesion());
        PersonaNatural personaNatural = PersonaNatural
                .builder()
                .numDni(ciudadano.getNumDni())
                .txtApePaterno(ciudadano.getTxtApellidoPaterno())
                .txtApeMaterno(ciudadano.getTxtApellidoMaterno())
                .txtNombres(ciudadano.getNombres())
                .flgSexo(ciudadano.getFlgSexo())
                .fecNacimiento(ciudadano.getFecNacimiento())
                .idSesionMod(sesion.getIdSesion())
                .idSesionReg(sesion.getIdSesion())
                .build();
        iCiudadanoMapper.spInsertPersonaNatural(personaNatural);
        ciudadano.setIdNatural(personaNatural.getIdNatural());
        ciudadano.setIdUbigeoDomicilio(usuarioService.obtenerUbigeo(ciudadano.getTxtUbigeo()));
        ciudadano.setTxtDireccion(ciudadano.getTxtDireccion());
        ciudadano.setNumTelefonoCelular(ciudadano.getNumTelefonoCelular());
        ciudadano.setTxtCorreoElectronico(ciudadano.getTxtCorreoElectronico());
        ciudadano.setIdSesionMod(sesion.getIdSesion());
        ciudadano.setIdSesionReg(sesion.getIdSesion());
        iCiudadanoMapper.spInsertCiudadano(ciudadano);

        Ciudadano ciudadanoJuridico = getCiudadanoId(ciudadano.getIdCiudadanoEmpresa()).get();
        ciudadanoJuridico.setIdSesionMod(sesion.getIdSesion());
        iCiudadanoMapper.spQuitarRepresentates(ciudadanoJuridico);
        ciudadanoJuridico.setIdNatural(personaNatural.getIdNatural());
        iCiudadanoMapper.spAsignarRepresentanteLegal(ciudadanoJuridico);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            throw new HandledException("Failed to register grabarRepresentanteLegal", ex);
        }

        return ciudadano;

    }
    @Transactional(rollbackFor={Exception.class})
    public Ciudadano asignarRepresentanteLegal(Ciudadano ciudadano)throws HandledException{
        ciudadano.getSesion().setIdUsuario(ciudadano.getIdUsuario());
        Sesion sesion =  usuarioService.obtenerSesion(ciudadano.getSesion());
        Ciudadano ciudadanoJuridico = getCiudadanoId(ciudadano.getIdCiudadanoEmpresa()).get();
        ciudadanoJuridico.setIdSesionMod(sesion.getIdSesion());
        iCiudadanoMapper.spQuitarRepresentates(ciudadanoJuridico);
        ciudadanoJuridico.setIdNatural(ciudadano.getIdNatural());
        iCiudadanoMapper.spAsignarRepresentanteLegal(ciudadanoJuridico);
        return ciudadano;

    }
    public Optional<Ciudadano> getCiudadanoId(Long idCiudadano){
        Ciudadano ciudadano = Ciudadano
                .builder()
                .idCiudadano(idCiudadano)
                .build();
        iCiudadanoMapper.spBuscarCiudadanoId(ciudadano);
        return Optional.ofNullable(ciudadano).map(c -> {
            if(c.getIdUsuario() == null) return null;
            return c;
        });
    }
    public void insertCiudadano(Ciudadano administrado){
        iCiudadanoMapper.spInsertCiudadano(administrado);
    }
    public void insertPersonaNatural(PersonaNatural personaNatural){
        iCiudadanoMapper.spInsertPersonaNatural(personaNatural);
    }
    public void insertPersonaJuridica(PersonaJuridica personaJuridica){
        iCiudadanoMapper.spInsertPersonaJuridica(personaJuridica);
    }
    public Optional<Ciudadano> buscarCiudadanoConsolidado(Long ide){
        Ciudadano ciudadano = Ciudadano.builder().idCiudadano(ide).build();
        iCiudadanoMapper.spBuscarCiudadanoConsolidado(ciudadano);
        return  Optional.ofNullable(ciudadano).map(c -> {
            System.out.println("jijijij"+c.getIdUsuario());
            if(c.getIdUsuario()== null) return null;
                return c;
        });
    }
}

