package gob.pe.minam.restceropapel.security.entity;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sesion {
    private Long idSesion;
    private String smacaddress;
    private String dfechainicio;
    private String dfechafin;
    private String sversionmodulo;
    private Long idUsuario;
    private String idSistema;
    private String sipaddress;
    private String sappserver;
    private String susuariored;
    private String ssistoperativo;
    private String linkAplicativo;
    private List<Sesion> idSesionCursor;
}
