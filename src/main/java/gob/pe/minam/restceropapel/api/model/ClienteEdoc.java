package gob.pe.minam.restceropapel.api.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEdoc {
    private Long idCliente;
    private String estado;
    private String numeroIdentificacion;
    private String razonSocial;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombre;
    private String telefono;
    private String idTipoCliente;
    private String idTipoMedioEnvio;
    private String dni;
    private String resultado;
}
