package gob.pe.minam.restceropapel.api.service;

import gob.pe.minam.restceropapel.api.model.DetalleCompendio;

import java.util.List;

public interface ICodigoService {
    public List<DetalleCompendio> getMapDetalleCompendiosCorto(Integer idTabla);
    public List<DetalleCompendio> getMapDetalleCompendiosLargo(Integer idTabla);
    public String encodeHash(String cadena);
    public String decodeHash(String cadena);
}

