package gob.pe.minam.restceropapel.security.entity;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reniec {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String direccion;
    private String estadoCivil;
    private String ubigeo;
    private String restricccion;
    private String dni;
    private String coResultado;
}
