package gob.pe.minam.restceropapel.api.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaJuridica {
    private Long idJuridica;
    private String numRuc;
    private String txtRazonSocial;
    private Long idRepresentanteLegal;
    private String numPartidaElectronica;
    private String numAsientoRegistral;
    private Long idSesionReg;
    private Long idSesionMod;
    private String resultado;
}
