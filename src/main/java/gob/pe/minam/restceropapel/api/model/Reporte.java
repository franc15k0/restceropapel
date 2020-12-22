package gob.pe.minam.restceropapel.api.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    private  String numero;
    private  String descTipoDoc;
    private  String numDocumentoPersona;
    private  String nombres;
    private  String txtCorreoElectronico;
    private  String txtDescripcionTramite;
    private  String descEstaTrami;
    private  String fecRegistroTramite;
    private  String fecEnvioTramite;
    private  String fecEnviaDesd;
    private  String fecEnviaHast;
    private  String fecRegistroDesd;
    private  String fecRegistroHast;
    private  String codEtipoDocumento;
    private  String txtRazonSocial;
    private  String codEestaTrami;
    private List<Reporte> listReporte;
}
