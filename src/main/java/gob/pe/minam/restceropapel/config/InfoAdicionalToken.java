package gob.pe.minam.restceropapel.config;

import gob.pe.minam.restceropapel.api.model.Ciudadano;
import gob.pe.minam.restceropapel.api.service.ICiudadanoService;
import gob.pe.minam.restceropapel.security.entity.Sesion;
import gob.pe.minam.restceropapel.security.entity.Usuario;
import gob.pe.minam.restceropapel.security.service.IUsuarioService;
import gob.pe.minam.restceropapel.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InfoAdicionalToken implements TokenEnhancer {

    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private ICiudadanoService ciudadanoService;

    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Usuario usuario = usuarioService.getUsuarioExterno(authentication.getName()).orElseGet(()->usuarioService.getUsuarioInterno(authentication.getName()).orElseGet(()->null));
        Ciudadano ciudadano = ciudadanoService.getCiudadanoNroDocumento(Optional.ofNullable(usuario.getIdTipoDocumento()).orElseGet(()->Constante.ES_PERSONANATURAL),usuario.getUsuario()).orElseGet(()-> Ciudadano.builder().idCiudadano(0L).build());

        usuarioService.obtenerSesion(Sesion
                        .builder()
                        .idUsuario(usuario.getIdUsuario())
                        .susuariored((usuario.getTipo().equals(Constante.TIPO_USUARIO_MINAM))? "minam/"+usuario.getUsuario():null)
                        .build());
        Map<String, Object> info = new HashMap<>();
        info.put("idUsuario", usuario.getIdUsuario());
        info.put("nombres", usuario.getNombres());
        info.put("apellidos", usuario.getApellidos());
        info.put("tipo", usuario.getIdTipoDocumento());
        info.put("tipoUsuario", usuario.getTipo());
        info.put("usuario", usuario.getUsuario());
        info.put("correo", usuario.getCorreo());
        info.put("idCiudadano", ciudadano.getIdCiudadano());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

        return accessToken;
    }
}
