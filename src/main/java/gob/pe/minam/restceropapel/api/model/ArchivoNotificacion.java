package gob.pe.minam.restceropapel.api.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArchivoNotificacion {
    private Integer idExpediente;
    private Long idCiudadano;
    private String numero;
    private String nombre;
    private Integer idArchivo;
    private String idArchivoCod;
    private List<ArchivoNotificacion> listArchivoNotificacion;
}
