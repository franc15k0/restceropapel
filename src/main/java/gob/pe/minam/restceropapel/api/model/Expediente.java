package gob.pe.minam.restceropapel.api.model;

import gob.pe.minam.restceropapel.security.entity.Sesion;
import gob.pe.minam.restceropapel.security.entity.Usuario;
import lombok.*;

import java.util.List;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expediente {
    private ClienteEdoc clienteEdoc;
    private Registro Registro;
    private Documento documento;
    private Usuario usuario;
    private Sesion sesion;
    private PersonalEcodoc personalEcodoc;
    private List<Archivo> archivos;

}
