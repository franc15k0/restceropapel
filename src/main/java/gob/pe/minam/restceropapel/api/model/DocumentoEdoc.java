package gob.pe.minam.restceropapel.api.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoEdoc {
    private Long idDocumento;
    private String estado;
    private String fechaCreacion;
    private String folios;
    private String numero;
    private String titulo;
    private String autor;
    private long expediente;
    private String idTipoDocumento;
    private Integer idAreaAutor;
    private String presentaCd;
    private String foliado;
    private String esPrincipal;
    private String cd;
    private String estadoDigitalizacion;
    private String resultado;
}
