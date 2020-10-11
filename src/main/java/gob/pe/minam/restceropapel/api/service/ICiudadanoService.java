package gob.pe.minam.restceropapel.api.service;

import gob.pe.minam.restceropapel.api.model.Ciudadano;
import gob.pe.minam.restceropapel.api.model.PersonaJuridica;
import gob.pe.minam.restceropapel.api.model.PersonaNatural;
import gob.pe.minam.restceropapel.util.HandledException;

import java.util.List;
import java.util.Optional;

public interface ICiudadanoService {
    public List<Ciudadano> getCiudadanoRepresentanteLegal(Long idCiudadano);
    public Optional<Ciudadano> getCiudadanoNroDocumento(String TipoDocumento,String NroDocumento);
    public Ciudadano grabarRepresentanteLegal(Ciudadano ciudadano)throws HandledException;
    public Ciudadano asignarRepresentanteLegal(Ciudadano ciudadano) throws HandledException;
    public Optional<Ciudadano> getCiudadanoId(Long idCiudadano);
    public void insertCiudadano(Ciudadano administrado);
    public void insertPersonaNatural(PersonaNatural personaNatural);
    public void insertPersonaJuridica(PersonaJuridica personaJuridica);
    public Optional<Ciudadano> buscarCiudadanoConsolidado(Long ide);
}
