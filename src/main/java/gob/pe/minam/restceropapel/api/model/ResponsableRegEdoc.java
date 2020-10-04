package gob.pe.minam.restceropapel.api.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsableRegEdoc {
        private Long idExpediente;
        private Integer  idPersona ;
		private String responsable;
        private String resultado;
}
