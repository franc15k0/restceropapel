package gob.pe.minam.restceropapel.security.service;


import gob.pe.minam.restceropapel.api.model.Ubigeo;
import gob.pe.minam.restceropapel.security.entity.*;
import gob.pe.minam.restceropapel.util.HandledException;

import java.util.List;
import java.util.Optional;


public interface IUsuarioService {
    public Usuario insertUsuario(Usuario usuario) throws HandledException;
    public void validate(Valido valido) throws HandledException;
    public void listarUbigeos(Ubigeo ubigeo);
    public Sesion obtenerSesion(Sesion sesion);
    public Optional<Usuario> getUsuarioInterno(String usuarioName);
    public Optional<Usuario> getUsuarioExterno(String usuarioName);
    public Integer obtenerUbigeo(String txtUbigeo);
    public Optional<Valido> getUsuarioValido(Valido valid);
    public void spResetearContrasena(Usuario usuario) throws HandledException;
    public Usuario enviarInformacionRecuperarContrasena(String numeroDocumento);
    public Reniec buscarReniec(String DNI);
    public void regenerarCodigoValidacion(String token, String linkApp);
    public List<Menu> listMenuSistema(Long idSistema);
}
