package gob.pe.minam.restceropapel.api.repository;


import gob.pe.minam.restceropapel.api.model.Ubigeo;
import org.springframework.stereotype.Repository;

@Repository
public interface IUbigeoMapper {
    public void spListarUbigeos(Ubigeo ubigeo);
    public Integer getFuncionUbigeo(Ubigeo ubigeo);
}
