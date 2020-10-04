package gob.pe.minam.restceropapel.api.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArchivoEdoc {
    private Integer idArchivo;

    private String estado;
    private String fechaCreacion;
    private String nombre;
    private String rutaLocal;
    private Long documento;
    private String esPrincipal;
    private String resultado;
}
