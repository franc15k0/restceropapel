package gob.pe.minam.restceropapel.security.entity;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private Integer idMenu;
    private String nombre;
    private String descripcion;
    private String accion;
    private String estadoRegistro;
    private Integer idMenuPadre;
    private Long idUsuario;
    private Integer idSistema;
    private Integer nvisibleMenu;
    private String nnivelMenu;
    private String parametro;
    private String orden;
    private String tipoMenu;
    private List<Menu> listMenu;
}
