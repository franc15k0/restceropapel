package gob.pe.minam.restceropapel.api.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DireccionEdoc {
    private Integer idDireccion;
    private String direccion;
    private Long idCliente;
    private Integer ubigeo;
    private String resultado;
}
