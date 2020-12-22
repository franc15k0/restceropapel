package gob.pe.minam.restceropapel.security.controller;

import gob.pe.minam.restceropapel.api.controller.RestControllerExpediente;
import gob.pe.minam.restceropapel.security.entity.Password;
import gob.pe.minam.restceropapel.security.entity.Usuario;
import gob.pe.minam.restceropapel.security.entity.Valido;
import gob.pe.minam.restceropapel.security.service.IUsuarioService;
import gob.pe.minam.restceropapel.util.HandledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class ControllerUsuario {
    private Logger logger = LoggerFactory.getLogger(ControllerUsuario.class);
    @Autowired
    IUsuarioService usuarioService;
    @Autowired
    Environment environment;

    @GetMapping("/usuario/confirmacion-correo/{flgAccionUsuario}/{token}")
    public String confirmarCorreo(Model model
                                  ,@PathVariable(name="token") String token
                                  ,@PathVariable(name="flgAccionUsuario") String flgAccionUsuario
                                  , HttpServletRequest request) {

        model.addAttribute("valido", Valido
                .builder()
                .token(token)
                .flgAccionUsuario(flgAccionUsuario)
                .build());
        return "confirmacionRegistro";
    }
   /* @ModelAttribute("usuario")
    public Usuario passwordReset() {
        return new Usuario();
    }*/

    @PostMapping("/usuario/validation-codigo")
    public String validation(@Valid Valido valido, Model model) throws HandledException {
        //System.out.println(valido.getToken().replaceAll("[^0-9]", ""));
        //valido.setLinkAplicativo(request.getServerPort()+request.getContextPath());
        AtomicReference<String> vista = new AtomicReference<>("");
        Long idUsuario = Long.parseLong(valido.getToken().replaceAll("[^0-9]", ""));
        System.out.println(valido.getToken()+"::"+valido.getFlgAccionUsuario());
        logger.info("valido.getFlgAccionUsuario().",valido.getFlgAccionUsuario());
        if(valido.getFlgAccionUsuario().equals("0")){
            System.out.println("valido.getFlgAccionUsuario().equals(0)");
            valido.setIdUsuario(idUsuario);
            usuarioService.validate(valido);
            System.out.println("valido.getResultado()"+valido.getResultado());
            valido.setResultado(Optional.ofNullable(valido.getResultado()).map(r -> {
                valido.setEstado("1");
                return  r;
            }).orElseGet(() -> {
                valido.setEstado("0");
                return "Los datos ingresados son incorrectos, contactese con el administrador ";
            }));
            model.addAttribute("respuesta", valido);
            vista.set("confirmacionRegistro");
        }else{
           usuarioService.getUsuarioValido(valido).map( u ->
                    {
                        model.addAttribute("usuario", Usuario
                                .builder()
                                .idUsuario(u.getIdUsuario())
                                .idUsuarioValid(u.getIdUsuarioValid())
                                .build());
                        vista.set("cambiarClave");
                        return u;
                    }

            ).orElseGet(()->{
                valido.setEstado("0");
                valido.setResultado("Los datos ingresados son incorrectos, contactese con el administrador");
                model.addAttribute("respuesta", valido);
                vista.set("confirmacionRegistro");
             return new Valido();
            });

        }
        //model.addAttribute("valido", valido);
        return vista.get();
    }
    @PostMapping("/usuario/reset-contrasena")
    public String resetPassword(Model model, @Valid Usuario usuario) throws HandledException{
        System.out.println(usuario.getIdUsuario());
        try{
        usuarioService.spResetearContrasena(usuario);

        model.addAttribute("resultado", "1");
        }catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            model.addAttribute("resultado", "0");;
        }
        return "cambiarClave";
    }


}
