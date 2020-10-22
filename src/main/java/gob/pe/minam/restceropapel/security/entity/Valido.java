package gob.pe.minam.restceropapel.security.entity;

import lombok.*;

import java.util.Optional;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Valido {
    private Long idUsuarioValid;
    private String contracena;
    private String confirmContracena;
    private Long idUsuario;
    private Long idCiudadano;
    private String token;
    private Long codigo;
    private String estado;
    private String tokenFinal;
    private String flgAccionUsuario;
    private Long idSesionReg;
    private Long idSesionMod;
    private String linkAplicativo;
    private String resultado;
}


