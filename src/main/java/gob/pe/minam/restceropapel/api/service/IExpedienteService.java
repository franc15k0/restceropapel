package gob.pe.minam.restceropapel.api.service;

import gob.pe.minam.restceropapel.api.model.*;

import java.util.List;
import java.util.Optional;

public interface IExpedienteService {
    public Object cargarExpedienteCeroPapel(Expediente expediente);
    public Expediente buscarExpediente(Long idregistro);
    public Registro actualizarExpediente(Expediente expediente) ;
    //public void spInsertFirma(FirmaDigital firmaDigital);
  //  public Integer getFuncionIdFirma();
    //public FirmaDigital buscarFirma(Integer idFirma);
    public List<Reporte> lstReporte(Reporte filtro);
    public List<Registro> listExpedienteBandeja(Registro registro);
    public List<Notificacion> listaNotificacion(Notificacion notificacion);
    public List<ArchivoNotificacion> listarArchivosNotificacion(ArchivoNotificacion archivoNotificacion);

}
