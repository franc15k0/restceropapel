package gob.pe.minam.restceropapel.security.controller;

import gob.pe.minam.restceropapel.security.entity.Password;
import gob.pe.minam.restceropapel.security.entity.Usuario;
import gob.pe.minam.restceropapel.security.entity.Valido;
import gob.pe.minam.restceropapel.security.service.IUsuarioService;
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
    @ModelAttribute("usuario")
    public Usuario passwordReset() {
        return new Usuario();
    }

    @PostMapping("/usuario/validation-codigo")
    public String validation(@Valid Valido valido, Model model) {
        System.out.println(valido.getToken().replaceAll("[^0-9]", ""));
        AtomicReference<String> vista = new AtomicReference<>("");
        Long idUsuario = Long.parseLong(valido.getToken().replaceAll("[^0-9]", ""));
        if(valido.getFlgAccionUsuario().equals("0")){
            valido.setIdUsuario(idUsuario);
            usuarioService.validate(valido);
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
                        model.addAttribute("usuario", u);
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
        model.addAttribute("valido", valido);
        return vista.get();
    }
    @PostMapping("/usuario/reset-contrasena")
    public String resetPassword(Model model, @ModelAttribute("usuario") @Valid Usuario usuario) {

        try{
        usuarioService.spResetearContrasena(usuario);

        model.addAttribute("resultado", "1");
        }catch (Exception e) {
            model.addAttribute("resultado", "0");;
        }
        return "cambiarClave";
    }


}