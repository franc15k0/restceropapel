package gob.pe.minam.restceropapel.api.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompendio {
    private Integer idDetalle;
    private Integer idTabla;
    private String codElementoTabla;
    private String txtDescripcionCodigo;
    private String txtDescripcionCorta;
    private String txtReferenciaEcodoc;
    private String txtReferenciaMpi;
    private String txtReferencia1;
    private String txtReferencia2;
    private String flgEstado;
    private Integer idSesionReg;
    private Integer idSesionMod;
    private String fecCreacion;
    private List<DetalleCompendio> listDetalle;
}
