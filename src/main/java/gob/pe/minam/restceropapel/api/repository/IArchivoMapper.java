package gob.pe.minam.restceropapel.api.repository;

import gob.pe.minam.restceropapel.api.model.Archivo;
import gob.pe.minam.restceropapel.api.model.Documento;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IArchivoMapper {
    /*public List<Archivo> buscarAchivos(Long idDocumento);*/
    public void spBuscarAchivos(Archivo archivo);
    public void spInsertArchivo(Archivo archivo);
    public void spEliminarArchivo(Archivo archivo);
}
