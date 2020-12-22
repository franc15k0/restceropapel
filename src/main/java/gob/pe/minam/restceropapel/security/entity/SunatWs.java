package gob.pe.minam.restceropapel.security.entity;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SunatWs {
    private String status;
    private Sunat data;
}
