package gob.pe.minam.restceropapel.api.repository;

import gob.pe.minam.restceropapel.api.model.Ciudadano;
import gob.pe.minam.restceropapel.api.model.PersonaJuridica;
import gob.pe.minam.restceropapel.api.model.PersonaNatural;
import gob.pe.minam.restceropapel.security.entity.Usuario;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICiudadanoMapper {
    public void spInsertCiudadano(Ciudadano administrado);
    public void spInsertPersonaNatural(PersonaNatural personaNatural);
    public void spInsertPersonaJuridica(PersonaJuridica personaJuridica);
    public void spBuscarCiudadano(Ciudadano ciudadano);
    public void spBuscarCiudadanoId(Ciudadano ciudadano);
    public void spBuscarCiudadanoConsolidado(Ciudadano ciudadano);
    /*public List<Ciudadano> buscarCiudadanoRepresentanteLegal(Long id);*/
    public void spBuscarCiudadanoRepresentanteLegal(Ciudadano ciudadano);
    public void spQuitarRepresentates(Ciudadano ciudadano);
    public void spAsignarRepresentanteLegal(Ciudadano ciudadano);
}
