package gob.pe.minam.restceropapel.api.model;

import gob.pe.minam.restceropapel.security.entity.Sesion;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {
    private Long idExpediente;
    private Long idRegistro;
    private Long idUsuario;
    private Long idPerJuridica;
    private Long idPerNatural;
    private Long idCiudadano;
    private String estadoProceso;
    private String descripcion;
    private String fechaNotificacion;
    private String numeroExpediente;
    private String fechaSalida;
    private String estadoNotificacion;
    private String fecSalidaDesde;
    private String fecSalidaHasta;
    private String nombreDocumento;
    private String numeroCeroPapel;
    private Sesion sesion;
    private List<Notificacion> listNotificacion;
}
