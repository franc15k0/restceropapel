package gob.pe.minam.restceropapel.api.model;

import gob.pe.minam.restceropapel.security.entity.Sesion;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ciudadano {
    private Long idCiudadano;
    private Long idUsuario;
    private Long idNatural;
    private Long idJuridica;
    private Long idCiudadanoEmpresa;
    private String numDni;
    private String txtApellidoPaterno;
    private String txtApellidoMaterno;
    private String nombres;
    private String nombresCompleto;
    private String estadoReprese;
    private String fecInicioRep;
    private String fecFinRep;
    private String numRuc;
    private String razonSocial;
    private String flgSexo;
    private String fecNacimiento;
    private String txtDireccion;
    private String txtUbigeo;
    private String numTelefonoCelular;
    private String txtCorreoElectronico;
    private Integer idUbigeoDomicilio;
    private Long idSesionReg;
    private Long idSesionMod;
    private String numeroDocumento;
    private String idTipoDocumento;
    private String resultado;
    private Sesion sesion;
    List<Ciudadano> listCiudadano;
}
