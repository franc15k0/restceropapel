package gob.pe.minam.restceropapel.api.service;


import gob.pe.minam.restceropapel.api.controller.RestControllerExpediente;
import gob.pe.minam.restceropapel.util.FileStorageException;
import gob.pe.minam.restceropapel.util.FileStorageProperties;
import gob.pe.minam.restceropapel.util.PasswordGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UploadFileService implements  IUploadFileService{
    private org.slf4j.Logger logger = LoggerFactory.getLogger(UploadFileService.class);
    private Path fileStorageLocation;
    private Path fileStorageLocationEcodoc;

    @Autowired
    public UploadFileService(FileStorageProperties fileStorageProperties ) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileStorageLocationEcodoc= Paths.get(fileStorageProperties.getUploadDirEcodoc())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.fileStorageLocationEcodoc);
        } catch (Exception ex) {
            throw new FileStorageException("No se pudo crear el directorio donde se almacenarán los archivos cargados.", ex);
        }
    }

    public String copiar(MultipartFile archivo) throws IOException {

        String nombreArchivo = "["+PasswordGenerator.generateCodigoFiles()+"] - "+archivo.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "");
        Path targetLocation = this.fileStorageLocation.resolve(nombreArchivo);
        logger.info(targetLocation.toString());
        Files.copy(archivo.getInputStream(), targetLocation);
        return nombreArchivo;
    }
    public String copiarFirma(MultipartFile archivo, String nombre) throws IOException {
        Path targetLocation = this.fileStorageLocation.resolve(nombre);
        Files.copy(archivo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return nombre;
    }

    @Override
    public boolean eliminar(String nombre) throws IOException {
        if(nombre !=null && nombre.length() >0) {
            Path targetLocation = this.fileStorageLocation.resolve(nombre).toAbsolutePath();
            File archivoAnterior = targetLocation.toFile();
            if(archivoAnterior.exists() && archivoAnterior.canRead()) {
                archivoAnterior.delete();
                return true;
            }
        }
        return false;
    }

    public Resource cargar(String nombreArchivo) throws MalformedURLException {

        Path rutaArchivo = this.fileStorageLocation.resolve(nombreArchivo).toAbsolutePath();
        logger.info(rutaArchivo.toString());

        Resource recurso = new UrlResource(rutaArchivo.toUri());

        if(!recurso.exists() && !recurso.isReadable()) {
            logger.error("Error no se pudo cargar El archivo: " + nombreArchivo);
        }
        return recurso;
    }
   public ByteArrayInputStream cargarFileFirma(String nombreArchivo) throws MalformedURLException, IOException{

        Path rutaArchivo = this.fileStorageLocation.resolve(nombreArchivo).toAbsolutePath();
       logger.info(rutaArchivo.toString());
        byte[] content= Files.readAllBytes(rutaArchivo);
        return new ByteArrayInputStream(content);
    }
    public String replicar(String nombre) throws IOException {
        Path origen = this.fileStorageLocation.resolve(nombre).toAbsolutePath();
        Path destino = this.fileStorageLocationEcodoc.resolve(nombre).toAbsolutePath();
        Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        return nombre;
    }
}
