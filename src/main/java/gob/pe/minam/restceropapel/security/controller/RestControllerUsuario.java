package gob.pe.minam.restceropapel.security.controller;

import gob.pe.minam.restceropapel.api.controller.RestControllerExpediente;
import gob.pe.minam.restceropapel.api.model.Ubigeo;
import gob.pe.minam.restceropapel.security.entity.Reniec;
import gob.pe.minam.restceropapel.security.entity.Sunat;
import gob.pe.minam.restceropapel.security.entity.SunatWs;
import gob.pe.minam.restceropapel.security.entity.Usuario;
import gob.pe.minam.restceropapel.security.service.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuario")
public class RestControllerUsuario {
    private Logger logger = LoggerFactory.getLogger(RestControllerExpediente.class);

    @Autowired
    IUsuarioService usuarioService;
    @Autowired
    Environment environment;

    @GetMapping("/validacion/reniec/{dni}")
    @ResponseStatus(HttpStatus.OK)
    public Reniec buscarReniec(@PathVariable String dni){
        return usuarioService.buscarReniec(dni);
    }
    @GetMapping("/validacion/sunat/{ruc}")
    @ResponseStatus(HttpStatus.OK)
    public Sunat buscarSunat(@PathVariable String ruc){

        final String uri = environment.getProperty("servicios.rest.consulta.minam")+"/sunat?ruc="+ruc;

        RestTemplate restTemplate = new RestTemplate();
        SunatWs sunatWs = restTemplate.getForObject(uri, SunatWs.class);


        return sunatWs.getData();
    }
    @PostMapping("/alta")
    public ResponseEntity<?> alta(@Valid @RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        usuarioService.insertUsuario(usuario);
        return  Optional.ofNullable(usuario.getIdUsuario())
                .map(b -> {
                        response.put("mensaje", usuario.getResultado());
                        response.put("usuario",usuario);
                        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
                })
                .orElseGet(() -> {
                    response.put("mensaje","error interno!");
                    response.put("error",usuario.getResultado());
                    return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CONFLICT);
                });
    }
    @GetMapping("/ubigeo/{padre}")
    @ResponseStatus(HttpStatus.OK)
    public List<Ubigeo> ubigeo(@PathVariable String padre) {
        Ubigeo ubigeo;
        if(padre == null || padre.equals("null")){
            ubigeo = new Ubigeo();
        }else{

            ubigeo = Ubigeo.builder().padre(Integer.parseInt(padre)).build();
        }
        usuarioService.listarUbigeos(ubigeo);
        return ubigeo.getListUbigeos();
    }
    @GetMapping("/recuperar/{numDocumento}")
    @ResponseStatus(HttpStatus.OK)
    public String recuperar(@PathVariable String numDocumento) {
        Usuario user = usuarioService.enviarInformacionRecuperarContrasena(numDocumento);
                Optional.ofNullable(user).orElseThrow(IllegalArgumentException::new);

        return "ok";
    }
    @GetMapping("/regenerarsms/{token}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity regenerarSms(@PathVariable String token) {
        System.out.println("jajaja______"+token);
        try{
            usuarioService.regenerarCodigoValidacion(token);
         }catch (Exception e){
          e.printStackTrace();
          return new ResponseEntity(HttpStatus.CONFLICT);
         }
        return  new ResponseEntity(HttpStatus.OK);
    }
    @PutMapping("/buscar")
    public ResponseEntity<?> buscarPersonalDni(@Valid @RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        return usuarioService.getUsuarioExterno(usuario.getUsuario()).map(u->{
            response.put("mensaje","El usuario se encuentra en condiciones para Recuperar!");
            response.put("usuario",u);
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
        }).orElseGet(()->{
            response.put("mensaje","Errorbusqueda del usuario");
            response.put("error","No se encuentra al usuario");
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CONFLICT);
        });
    }

}
