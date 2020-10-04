package gob.pe.minam.restceropapel.security.entity;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Password {
    private String password;
    private String passwordConf;
}
