package gob.pe.minam.restceropapel.security.repository;

import gob.pe.minam.restceropapel.security.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface IUsuarioMapper {
    public void spFindByRol(Rol rol);
    public void spInsertUsuario(Usuario usuario);
    public void spInsertUsuarioValid(Valido valid);
    public void spModificarUserValid(Valido valid);
    public void spRegenerarUserValid(Valido valid);
    public void spUsuarioExterno(Usuario usuario);
    public void spBuscarUsuarioDefault(Usuario usuario);
    public void spGetUsuarioValido(Valido valid);
    public void spResetearContrasena(Usuario usuario);
    public void spModificarUsuarioInactivo(Usuario usuario);
    public void spSelPersonalUsuario(Usuario usuario);
    public void spSelUsuarioIdSegfys(Usuario usuario);
    public void spBuscarMenuUsuarioRol(Menu menu);
}

