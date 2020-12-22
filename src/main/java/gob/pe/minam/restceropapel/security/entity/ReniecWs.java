package gob.pe.minam.restceropapel.security.entity;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReniecWs {
    private String status;
    private Reniec data;
}
