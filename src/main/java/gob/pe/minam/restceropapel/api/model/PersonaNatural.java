package gob.pe.minam.restceropapel.api.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaNatural {
    private Long idNatural;
    private String numDni;
    private String txtNombres;
    private String txtApellidos;
    private String txtApePaterno;
    private String txtApeMaterno;
    private String flgSexo;
    private String fecNacimiento;
    private Long idSesionReg;
    private Long idSesionMod;
    private String resultado;
}
