package gob.pe.minam.restceropapel.api.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ubigeo {
    private Integer idUbigeo;
    private String codigo;
    private String nombre;
    private Integer padre;
    private String txtUbigeoRegion;
    private String txtUbigeoProvincia;
    private String txtUbigeoDistrito;
    private List<Ubigeo> listUbigeos;
}

