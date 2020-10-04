package gob.pe.minam.restceropapel.api.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalEcodoc {
    private Long idPersonal;
    private String nombres;
    private String apellidos;
    private String cargo;
    private String unidadOrganica;
    private String correo;
    private String correoEnotificacion;
    private Long idCliente;
    private String dni;
    private String estado;
    private String resultado;
    private List<PersonalEcodoc> listPersonal;
}
