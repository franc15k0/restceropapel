package gob.pe.minam.restceropapel.api.service;

import gob.pe.minam.restceropapel.api.model.*;
import gob.pe.minam.restceropapel.api.repository.*;
import gob.pe.minam.restceropapel.security.entity.Sesion;
import gob.pe.minam.restceropapel.security.service.IUsuarioService;
import gob.pe.minam.restceropapel.util.Constante;
import gob.pe.minam.restceropapel.util.HandledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpedienteService implements  IExpedienteService {
    final Logger logger = LoggerFactory.getLogger(ExpedienteService.class);
    @Autowired
    IDocumentoMapper iDocumentoMapper;
    @Autowired
    IArchivoMapper iArchivoMapper;
    @Autowired
    IRegistroMapper iRegistroMapper;
    @Autowired
    INotificacionMapper iNotificacionMapper;
    @Autowired
    ICiudadanoService ciudadanoService;
    @Autowired
    IReporteMapper iReporteMapper;
    @Autowired
    private IUploadFileService uploadService;
    @Value("${file.upload-dir}")
    String uploadDir;

    @Autowired
    private IUsuarioService usuarioService;

    @Transactional(rollbackFor={Exception.class})
    public String cargarExpedienteCeroPapel(Expediente expediente) throws HandledException {
        String resultado;
        try {
            expediente.getSesion().setIdUsuario(expediente.getUsuario().getIdUsuario());
            expediente.setSesion(usuarioService.obtenerSesion(expediente.getSesion()));
            expediente.getRegistro().setCodTestaTrami(Constante.TABLA_ESTADO);
            expediente.getRegistro().setCodEestaTrami(Constante.ESTADO_REGISTRADO);
            expediente.getRegistro().setCodTorgano(Constante.TABLA_ORGANO);
            expediente.getRegistro().setIdSesionMod(expediente.getSesion().getIdSesion());
            expediente.getRegistro().setIdSesionReg(expediente.getSesion().getIdSesion());
            iRegistroMapper.spInsertRegistro(expediente.getRegistro());
            expediente.getDocumento().setCodTtipoDocumento(Constante.TABLA_DOCUMENTO);
            expediente.getDocumento().setIdRegistro(expediente.getRegistro().getIdRegistro());
            expediente.getDocumento().setIdSesionMod(expediente.getSesion().getIdSesion());
            expediente.getDocumento().setIdSesionReg(expediente.getSesion().getIdSesion());
            iDocumentoMapper.spInsertDocumento(expediente.getDocumento());
            expediente.getArchivos().forEach(a -> insertarArchivo(a, expediente));
            resultado = "Se Grabo con exito el expediente";
        }catch (Exception ex){
            logger.error(ex.getMessage());
            throw new HandledException("Failed to register cargarExpedienteCeroPapel", ex);
        }

        return resultado;
    }

    public void  insertarArchivo(Archivo archivo, Expediente expediente) {
        archivo.setCodTsustDocumento(Constante.TABLA_SUSTENTO);
        archivo.setIdDocumento(expediente.getDocumento().getIdDocumento());
        archivo.setTxtRutaLocal(uploadDir);
        archivo.setIdSesionMod(expediente.getSesion().getIdSesion());
        archivo.setIdSesionReg(expediente.getSesion().getIdSesion());
        iArchivoMapper.spInsertArchivo(archivo);
    }

    public Expediente buscarExpediente(Long idregistro) {
        Registro registro = Registro.builder().idRegistro(idregistro).build();
        iRegistroMapper.spBuscarRegistro(registro);
        logger.info("flgdeclaracionjurada:"+registro.getFlgDeclaracionJurada());
        Documento documento = Documento.builder().idRegistro(idregistro).build();
        iDocumentoMapper.spBuscarDocumento(documento);
        Archivo archivoP = Archivo.builder().idDocumento(documento.getIdDocumento()).build();
        iArchivoMapper.spBuscarAchivos(archivoP);
        List<Archivo> archivoList = archivoP.getListArchivos();
        return Expediente
                .builder()
                .Registro(registro)
                .documento(documento)
                .archivos(archivoList)
                .build();
    }
    @Transactional(rollbackFor={Exception.class})
    public Registro actualizarExpediente(Expediente expediente) throws HandledException {
        try {
        expediente.getSesion().setIdUsuario(expediente.getUsuario().getIdUsuario());
        expediente.setSesion(usuarioService.obtenerSesion(expediente.getSesion()));
        expediente.getRegistro().setIdSesionMod(expediente.getSesion().getIdSesion());
        iRegistroMapper.spActualizarRegistroEdicion(expediente.getRegistro());
        expediente.getDocumento().setIdSesionMod(expediente.getSesion().getIdSesion());
        iDocumentoMapper.spActualizarDocumento(expediente.getDocumento());
        expediente.getArchivos().forEach(a -> procesarArchivo(a, expediente));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new HandledException("Failed to register actualizarExpediente", ex);
        }
        return expediente.getRegistro();
    }
    public void procesarArchivo(Archivo archivo, Expediente expediente) {
        if(archivo.getAccion()!=null){
            archivo.setIdDocumento(expediente.getDocumento().getIdDocumento());
           if(archivo.getAccion().equals(Constante.ELIMINAR_ARCHIVO)){
               try{
                   uploadService.eliminar(archivo.getTxtNombreArchivo());
                   archivo.setIdSesionMod(expediente.getSesion().getIdSesion());
                   iArchivoMapper.spEliminarArchivo(archivo);
               }catch (Exception ex){
                logger.error(ex.getMessage());
               }
            }else if(archivo.getAccion().equals(Constante.INSERTAR_ARCHIVO)){
               insertarArchivo(archivo,expediente);
            }
        }
    }

    public List<Reporte> lstReporte(Reporte filtro) throws HandledException{
        try {
        iReporteMapper.spLstReporte(filtro);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new HandledException("Failed to lstReporte", ex);
        }
        return filtro.getListReporte();
    }
    public List<Registro> listExpedienteBandeja(Registro registro) throws HandledException{
        try {
        iRegistroMapper.spListExpedienteBandeja(registro);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new HandledException("Failed to listExpedienteBandeja", ex);
        }
        return registro.getListRegistros();
    }
    @Transactional(rollbackFor={Exception.class})
    public List<Notificacion> listaNotificacion(Notificacion notificacion) throws HandledException{
        List<Notificacion> listNotificacion = new ArrayList<>();
        try {
        Ciudadano ciudadano = ciudadanoService.getCiudadanoId(notificacion.getIdCiudadano()).get();
        notificacion.setIdPerNatural(ciudadano.getIdNatural());
        notificacion.setIdPerJuridica(ciudadano.getIdJuridica());
       logger.info("notificacion.getIdUsuario():::",notificacion.getIdUsuario());
        Long idUsuario = notificacion.getIdUsuario();
        iNotificacionMapper.spListarNotificacion(notificacion);
        listNotificacion = notificacion.getListNotificacion();
        Sesion sesion= new Sesion();

        if(listNotificacion.size()>0){
            notificacion.setSesion(Sesion.builder().idUsuario(idUsuario).build());
            sesion = usuarioService.obtenerSesion(notificacion.getSesion());
        }
        Sesion finalSesion = sesion;
        listNotificacion.forEach(n ->{
            iRegistroMapper.spActualizarRegistroExp(Registro
                    .builder()
                    .idSesionMod(finalSesion.getIdSesion())
                    .idRegistro(n.getIdRegistro())
                    .idExpediente(n.getIdExpediente())
                    .codEestaTrami((n.getEstadoNotificacion().equals(Constante.NOTIFICADO))?Constante.CODIGO_NOTFICADO:Constante.CODIGO_RECHAZADO)
                    .build());
        });
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new HandledException("Failed to listaNotificacion", ex);
        }
        return listNotificacion;
    }
    public List<ArchivoNotificacion> listarArchivosNotificacion(ArchivoNotificacion archivoNotificacion) throws HandledException{
        try {
        iNotificacionMapper.spListarArchivosNotificacion(archivoNotificacion);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new HandledException("Failed to listarArchivosNotificacion", ex);
        }
        return archivoNotificacion.getListArchivoNotificacion();
    }

}
