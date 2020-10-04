package gob.pe.minam.restceropapel.api.repository;

import gob.pe.minam.restceropapel.api.model.DetalleCompendio;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompendioMapper {
    public void spListDetalleCompendiosCorto(DetalleCompendio detalleCompendio);
    public void spListDetalleCompendiosLarga(DetalleCompendio detalleCompendio);
}
