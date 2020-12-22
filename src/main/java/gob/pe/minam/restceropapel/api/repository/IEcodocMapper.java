package gob.pe.minam.restceropapel.api.repository;

import gob.pe.minam.restceropapel.api.model.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEcodocMapper {
    public void spInsertClienteEco(ClienteEdoc cliente);
    public void spInsertExpedienteEco(ExpedienteEdoc expediente);
    public void spInsertDocumentoEco(DocumentoEdoc documento);
    public void spInsertDireccionEco(DireccionEdoc direccion);
    public void spInsertResponsRegisEco(ResponsableRegEdoc responsable);
    public void spInsertArchivoEco(ArchivoEdoc archivoEdoc);
    /*public Optional<ClienteEdoc> buscarCliente(ClienteEdoc cliente);*/
    public void spBuscarCliente(ClienteEdoc cliente);
    public void spBuscarPersonal(PersonalEcodoc personal);
    public void spBuscarPersonalDni(PersonalEcodoc personal);
    public void spInsertPersonalEco(PersonalEcodoc personal);
}
