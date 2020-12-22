package gob.pe.minam.restceropapel.api.repository;


import gob.pe.minam.restceropapel.api.model.Registro;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegistroMapper {
    public void spBuscarRegistro(Registro registro);
    public void spInsertRegistro(Registro registro);
    public void spActualizarRegistroExp(Registro registro);
    public void spActualizarRegistroEdicion(Registro registro);
    public void spListExpedienteBandeja(Registro registro);
}
