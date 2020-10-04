package gob.pe.minam.restceropapel.security.service;


import gob.pe.minam.restceropapel.api.model.Ubigeo;
import gob.pe.minam.restceropapel.security.entity.Reniec;
import gob.pe.minam.restceropapel.security.entity.Sesion;
import gob.pe.minam.restceropapel.security.entity.Usuario;
import gob.pe.minam.restceropapel.security.entity.Valido;

import java.util.Optional;


public interface IUsuarioService {
    /*public Usuario buscarUsuario(String usuarioName);*/
    /*public Usuario buscarUsuarioNroDoc(String numeroDocumento);*/
    public Usuario insertUsuario(Usuario usuario);
    public void validate(Valido valido);
    public void listarUbigeos(Ubigeo ubigeo);
    public Sesion obtenerSesion(Long ideUsuario);
    public Optional<Usuario> getUsuarioInterno(String usuarioName);
    public Optional<Usuario> getUsuarioExterno(String usuarioName);
    public Integer obtenerUbigeo(String txtUbigeo);
    public Optional<Valido> getUsuarioValido(Valido valid);
    public void spResetearContrasena(Usuario usuario);
    public Usuario enviarInformacionRecuperarContrasena(String numeroDocumento);
    public Reniec buscarReniec(String DNI);
    public void regenerarCodigoValidacion(String token);
}
