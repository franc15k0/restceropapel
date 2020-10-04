package gob.pe.minam.restceropapel.api.repository;

import gob.pe.minam.restceropapel.api.model.Reporte;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReporteMapper {
    /*public List<Reporte> lstReporte(Reporte filtro);*/
    public void spLstReporte(Reporte filtro);
}
