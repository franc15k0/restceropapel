package gob.pe.minam.restceropapel.security.entity;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    private Integer idRol;
    private Long  idUsuario;
    private Integer idSistema;
    private String nombre;
    private String descripcion;
    private String estadoRegistro;
    private String dFechaReg;
    private List<Rol> listRol;
}
