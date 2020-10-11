package gob.pe.minam.restceropapel.api.service;

import gob.pe.minam.restceropapel.api.model.*;
import gob.pe.minam.restceropapel.util.HandledException;

import java.util.List;
import java.util.Optional;

public interface IExpedienteService {
    public Object cargarExpedienteCeroPapel(Expediente expediente) throws HandledException;
    public Expediente buscarExpediente(Long idregistro);
    public Registro actualizarExpediente(Expediente expediente) throws HandledException;
    public List<Reporte> lstReporte(Reporte filtro) throws HandledException;
    public List<Registro> listExpedienteBandeja(Registro registro) throws HandledException;
    public List<Notificacion> listaNotificacion(Notificacion notificacion) throws HandledException;
    public List<ArchivoNotificacion> listarArchivosNotificacion(ArchivoNotificacion archivoNotificacion)throws HandledException;

}
