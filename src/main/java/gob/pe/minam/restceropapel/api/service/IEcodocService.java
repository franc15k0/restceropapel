package gob.pe.minam.restceropapel.api.service;

import gob.pe.minam.restceropapel.api.model.*;
import gob.pe.minam.restceropapel.util.HandledException;

import java.util.List;

public interface IEcodocService {
    public ExpedienteEdoc enviarExpedienteEcodoc(Expediente expedienteCP) throws HandledException;
    public List<PersonalEcodoc> traerPersonal(ClienteEdoc cliente) throws Exception;
    public List<PersonalEcodoc>  existePersonal(ClienteEdoc clienteP) throws Exception;
}
