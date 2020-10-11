package gob.pe.minam.restceropapel.api.controller;

import gob.pe.minam.restceropapel.api.service.ICodigoService;
import gob.pe.minam.restceropapel.api.service.IUploadFileService;
import gob.pe.minam.restceropapel.security.entity.Valido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;


@Controller
@RequestMapping("/firma")
public class ControllerFirmaDigital {
    private static final Logger logger = LoggerFactory.getLogger(ControllerFirmaDigital.class);
    @Autowired
    private IUploadFileService uploadService;
    @Autowired
    ICodigoService codigoService;
    @GetMapping("/index/{nombreArchivo}")
    public String firmaIndex( Model model, @PathVariable(name="nombreArchivo") String nombreArchivo,   HttpServletRequest request) {
        model.addAttribute("nombreArchivo", nombreArchivo);
        /*model.addAttribute("usuario", usuario);*/
        return "firmaDigital";
    }

    @RequestMapping(value="/descargarArchivo/{nombreArchivo}",method= RequestMethod.GET)
    public ResponseEntity<InputStreamResource> descargar(@PathVariable String nombreArchivo) throws Exception {

        logger.info("nombreArchivo:"+nombreArchivo);
        String cadenaNombreArchivo = codigoService.decodeHash(nombreArchivo);
        ByteArrayInputStream bis = uploadService.cargarFileFirma(cadenaNombreArchivo);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename="+nombreArchivo);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
