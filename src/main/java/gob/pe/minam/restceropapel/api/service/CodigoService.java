package gob.pe.minam.restceropapel.api.service;

import gob.pe.minam.restceropapel.api.model.DetalleCompendio;
import gob.pe.minam.restceropapel.api.repository.ICompendioMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodigoService implements ICodigoService{
    @Autowired
    ICompendioMapper compendioMapper;

    public List<DetalleCompendio> getMapDetalleCompendiosCorto(Integer idTabla){
        DetalleCompendio detalle = DetalleCompendio.builder().idTabla(idTabla).build();
        compendioMapper.spListDetalleCompendiosCorto(detalle);
        return detalle.getListDetalle();
    }
    public List<DetalleCompendio> getMapDetalleCompendiosLargo(Integer idTabla){
        DetalleCompendio detalle =  DetalleCompendio.builder().idTabla(idTabla).build();
        compendioMapper.spListDetalleCompendiosLarga(detalle);
        return detalle.getListDetalle();
    }
    public String encodeHash(String cadena){
        byte[] archivoBites = Base64.encodeBase64(cadena.trim().getBytes());
        return new String(archivoBites);
    }
    public String decodeHash(String cadena){
        byte[] decodedBytes = Base64.decodeBase64(cadena);
        return new String(decodedBytes);
    }
}
