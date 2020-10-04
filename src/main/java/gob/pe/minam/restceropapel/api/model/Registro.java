package gob.pe.minam.restceropapel.api.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Registro {
    private Long idRegistro;
    private Integer idFirmadigital;
    private Long idExpediente;
    private String numeroExpediente;
    private Integer areaDestino;
    private String flgEstado;
    private String txtDescripcionTramite;
    private String fecRegistroTramite;
    private String fecEnvioTramite;
    private Long idCiudadano;
    private String numeroCeroPapel;
    private Integer codTestaTrami;
    private String codEestaTrami;
    private String descEestaTrami;
    private Integer codTorgano;
    private String codEorgano;
    private String desOrganoDocumento;
    private String flgDeclaracionJurada;
    private String flgTerminosCondiciones;
    private Long idSesionReg;
    private Long idSesionMod;
    private String fecRegistroDesde;
    private String fecRegistroHasta;
    private String resultado;
    private List<Registro> listRegistros;

}
