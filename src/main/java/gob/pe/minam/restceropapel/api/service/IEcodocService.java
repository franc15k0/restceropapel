package gob.pe.minam.restceropapel.api.service;

import gob.pe.minam.restceropapel.api.model.*;

import java.util.List;

public interface IEcodocService {
    public ExpedienteEdoc enviarExpedienteEcodoc(Expediente expedienteCP) ;
    public List<PersonalEcodoc> traerPersonal(ClienteEdoc cliente) throws Exception;
    public List<PersonalEcodoc>  existePersonal(ClienteEdoc clienteP) throws Exception;
}
