package gob.pe.minam.restceropapel.api.repository;
import gob.pe.minam.restceropapel.api.model.Documento;
import org.springframework.stereotype.Repository;



@Repository
public interface IDocumentoMapper {
    public void spBuscarDocumento(Documento documento);
    public void spInsertDocumento(Documento documento);
    public void spActualizarDocumento(Documento documento);
}
