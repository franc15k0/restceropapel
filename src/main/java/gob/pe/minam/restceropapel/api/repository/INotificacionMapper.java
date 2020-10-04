package gob.pe.minam.restceropapel.api.repository;

import gob.pe.minam.restceropapel.api.model.ArchivoNotificacion;
import gob.pe.minam.restceropapel.api.model.Notificacion;
import gob.pe.minam.restceropapel.api.model.PersonalEcodoc;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INotificacionMapper {
    public void spListarNotificacion(Notificacion notificacion);
    public void spListarArchivosNotificacion(ArchivoNotificacion archivoNotificacion);
    /*public List<ArchivoNotificacion> listarArchivosNotificacion(Notificacion notificacion);*/

    /*public List<Notificacion> listaNotificacion(Notificacion notificacion);*/
}
