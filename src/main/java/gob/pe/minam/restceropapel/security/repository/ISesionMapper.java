package gob.pe.minam.restceropapel.security.repository;

import gob.pe.minam.restceropapel.security.entity.Parametro;
import gob.pe.minam.restceropapel.security.entity.Sesion;
import org.springframework.stereotype.Repository;

@Repository
public interface ISesionMapper {
    public void spInsertSesion(Sesion sesion);
    public void spBuscarParametroSesion(Parametro parametro);
}
