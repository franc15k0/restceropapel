package gob.pe.minam.restceropapel.api.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpedienteEdoc {
    private Long idExpediente;
    private String copiado;
    private String estado;
    private String fechaCreacion;
    private String numero;
    private String titulo;
    private Long idCliente;
    private Integer idPersona;
    private Integer idProceso;
    private String referencia;
    private String claveExterno;
    private Integer idTipoOrigen;
    private String plazo;
    private Integer idEstacionActual;
    private Integer idEstacionPrevia;
    private Integer idPlazoTipoDias;
    private Integer idSede;
    private Integer idPersonaActualiza;
    private String resultado;
}
