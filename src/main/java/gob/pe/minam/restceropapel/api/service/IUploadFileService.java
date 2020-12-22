package gob.pe.minam.restceropapel.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public interface IUploadFileService {
    public Resource cargar(String nombreFoto) throws MalformedURLException;
    public String copiar(MultipartFile archivo) throws IOException;
    public String copiarFirma(MultipartFile archivo, String nombre) throws IOException;
    public boolean eliminar(String nombre) throws IOException;
    public String replicar(String nombre) throws IOException;
    public ByteArrayInputStream cargarFileFirma(String nombreArchivo) throws MalformedURLException, IOException;
}
