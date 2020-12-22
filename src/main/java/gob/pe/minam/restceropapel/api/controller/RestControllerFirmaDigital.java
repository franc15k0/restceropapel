package gob.pe.minam.restceropapel.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gob.pe.minam.restceropapel.api.service.ICodigoService;
import gob.pe.minam.restceropapel.api.service.IExpedienteService;
import gob.pe.minam.restceropapel.api.service.IUploadFileService;
import gob.pe.minam.restceropapel.security.service.IUsuarioService;
import gob.pe.minam.restceropapel.util.ConfigurationFirma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


@RestController
@RequestMapping("/firma")
public class RestControllerFirmaDigital {
    @Autowired
    private IUploadFileService uploadService;
    @Autowired
    IUsuarioService usuarioService;
    @Autowired
    IExpedienteService expedienteService;
    @Autowired
    ICodigoService codigoService;
    @Value("${Protocol}")
    private String protocol;

    private final ConfigurationFirma config = ConfigurationFirma.getInstance();

    @GetMapping("/getArguments/{nombreArchivo}")
    @ResponseStatus(HttpStatus.OK)
    public String enviarArgumentosFirma(@PathVariable String nombreArchivo) {
        return nombreArchivo;
    }

    @PostMapping("/getArguments")
    public String argumentos(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        String pathServlet = request.getServletPath();
        String fullPathServlet = request.getRequestURL().toString();
        int resInt = fullPathServlet.length() - pathServlet.length();
        String serverPath = fullPathServlet.substring(0, resInt + 1);
        if (!serverPath.contains("localhost")) {
            // EN PRODUCCIÓN: config.getProtocol() define el protocolo.

           // serverPath = config.getProtocol() + "://" + serverPath.replace("http://", "").replace("https://", "");
            serverPath = protocol+ "://" + serverPath.replace("http://", "").replace("https://", "");
        }
        String arguments = "";
        try {
            String type = request.getParameter("type").toString();
            String documentName = request.getParameter("documentName").toString();
         /*   String nrodoc = request.getParameter("usuario").toString();
            String idfirma = request.getParameter("idfirma").toString();*/
          /*  Usuario usuario = usuarioService.getUsuarioExterno(nrodoc).get();*/
            String protocol = "";
            if (serverPath.contains("https://")) {
                protocol = "S";
            } else {
                protocol = "T";
            }
            /*arguments = paramWeb(protocol, serverPath, documentName, nrodoc, usuario.getNombres()+"."+usuario.getApellidos(), idfirma, usuario.getIdUsuario());*/
            arguments = paramWeb(protocol, serverPath, documentName);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        System.out.println("entrega argumentos");
        return arguments;
    }

    public String paramWeb(String protocol, String ServerPath, String documentName) {
        /*public String paramWeb(String protocol, String ServerPath, String documentName, String nrodoc, String nombre, String idfirma, Long idUsuario) {*/
        String param = "";
        ObjectMapper mapper = new ObjectMapper();
        //Java to JSON
        try {
         /*   System.out.println("config.getClientId()" + config.getClientId());
            System.out.println("protocol" + protocol);
            System.out.println("ServerPath" + ServerPath);
*/
            Map<String, String> map = new HashMap<>();
            map.put("app", "pdf");
            map.put("clientId", config.getClientId());
            map.put("clientSecret", config.getClientSecret());
            map.put("idFile", "001"); //FieldName Multipart, al momento de recibir el archivo subido se puede utilizar este argumento como identificador.
            map.put("type", "W"); //L=Documento está en la PC , W=Documento está en la Web.
            map.put("protocol", protocol); //T=http, S=https (SE RECOMIENDA HTTPS)
            map.put("fileDownloadUrl", ServerPath + "firma/descargarArchivo/"+documentName);
            map.put("fileDownloadLogoUrl", ServerPath + "assets/img/iLogo1.png");
            map.put("fileDownloadStampUrl", ServerPath + "assets/img/iFirma1.png");
            map.put("fileUploadUrl", ServerPath + "firma/uploadArchivo/" + documentName);
            map.put("contentFile", "demo.pdf");
            map.put("reason", "Soy el autor del documento");
            map.put("isSignatureVisible", "true");
            map.put("stampAppearanceId", "0"); //0:(sello+descripcion) horizontal, 1:(sello+descripcion) vertical, 2:solo sello, 3:solo descripcion
            map.put("pageNumber", "0");
            map.put("posx", "5");
            map.put("posy", "5");
            map.put("fontSize", "7");
            map.put("dcfilter", ".*FIR.*|.*FAU.*"); //".*" todos, solo firma ".*FIR.*|.*FAU.*"
            map.put("timestamp", "false");
            map.put("outputFile", documentName);
            map.put("maxFileSize", "5242880"); //Por defecto será 5242880 5MB - Maximo 100MB
            //JSON
            param = mapper.writeValueAsString(map);
            System.out.println(param);

            //Base64 (JAVA 8)
            param = Base64.getEncoder().encodeToString(param.getBytes(StandardCharsets.UTF_8));
            System.out.println(param);
        } catch (JsonProcessingException ex) {
        }

        return param;
    }

    @RequestMapping(value = "/uploadArchivo/{nombreArchivo}", method = RequestMethod.POST)
    @ResponseBody
    public void uploadArchivo(@PathVariable String nombreArchivo, HttpServletRequest request) {
        System.out.println("uploadArchivo" + nombreArchivo);
        try {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            String cadenaNombreArchivo = codigoService.decodeHash(nombreArchivo);
            MultipartFile multiFile = multiRequest.getFile("001");
            uploadService.copiarFirma(multiFile, cadenaNombreArchivo);

            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }

    }
}
