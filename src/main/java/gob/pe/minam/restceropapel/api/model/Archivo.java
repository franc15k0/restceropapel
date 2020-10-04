package gob.pe.minam.restceropapel.api.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Archivo {
    private Long idArchivo;
    private Long idDocumento;
    private String flgEsprincipal;
    private String flgEstaFirmado;
    private Integer codTsustDocumento;
    private String codEsustDocumento;
    private String descTipoSustento;

    private String txtNumeroArchivo;
    private String txtRutaLocal;
    private String txtNombreArchivo;
    private String accion;
    private String nombreArchivo;
    private Long idSesionReg;
    private Long idSesionMod;
    private List<Archivo> listArchivos;
    private String resultado;

}
