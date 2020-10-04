package gob.pe.minam.restceropapel.api.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Documento {
    private Long idDocumento;
    private Integer codTtipoDocumento;
    private String codEtipoDocumento;
    private String descTipoDocumento;
    private String flgEstado;
    private Long idRegistro;
    private Long idSesionReg;
    private Long idSesionMod;
    private String resultado;
}

