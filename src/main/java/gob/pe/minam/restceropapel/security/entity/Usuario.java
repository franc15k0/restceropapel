package gob.pe.minam.restceropapel.security.entity;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long idUsuario;
    private Long idUsuarioValid;
    private String usuario;
    private String usernamenl;
    private String contracena;
    private String confirmContracena;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono1;
    private String telefono2;
    private String estadoRegistro;
    private String idRol;
    private String cargo;
    private String oficina;
    private String idUser;
    private String estado;
    private Long idSesionReg;
    private Long idSesionMod;
    private String fechaReg;
    private String fechaMod;
    private String cambioClave;
    private String ldap;
    private String tipo;
    private String tipoPersona;
    private Integer idInstitucion;
    private String direccion;
    private String apellidoMaterno;
    private String apellidoPaterno;
    private Long usuarioDefecto;
    private String txtUbigeo;
    private String ubigeo;
    private String resultado;
    private String flgSexo;
    private String fecNacimiento;
    private String numAsientoRegistral;
    private String numPartidaElectronica;
    private String idTipoDocumento;
    private String numeroDocumento;
    private String codiEmplPer;
    private String codiDepeTde;
    private String nombreUsuario;
    private String descDepeTde;
    private String vcNombre;
    private String fechaPopupCumple;
    private String claveTempo;
    private Integer idSistema;
    private Integer codigoRol;
    private String descRol;
    private String esUsuarioExterno;
    private List<Usuario> listUsuarios;
    private Valido valido;
    private Sesion sesion;



}



