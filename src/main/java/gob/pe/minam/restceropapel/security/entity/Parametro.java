package gob.pe.minam.restceropapel.security.entity;

import lombok.*;

import java.util.List;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parametro {
    private String snombre;
    private String svalor;
    private List<Parametro> valorCursor;
}
