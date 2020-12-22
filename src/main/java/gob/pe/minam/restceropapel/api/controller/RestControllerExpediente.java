package gob.pe.minam.restceropapel.api.controller;
import gob.pe.minam.restceropapel.api.model.*;
import gob.pe.minam.restceropapel.api.service.*;
import gob.pe.minam.restceropapel.util.Constante;
import gob.pe.minam.restceropapel.util.HandledException;
import org.apache.commons.codec.binary.Base64;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class RestControllerExpediente {
    private static final Logger logger = LoggerFactory.getLogger(RestControllerExpediente.class);

    @Autowired
    IExpedienteService expedienteService;
    @Autowired
    ICiudadanoService ciudadanoService;
    @Autowired
    IEcodocService ecodocService;
    @Autowired
    ICodigoService codigoService;
    @Autowired
    private IUploadFileService uploadService;
    @Value("${ecodoc.url.upload}")
    private String ecodocpath;

    @PostMapping("/expediente")
    public ResponseEntity<?> cargar(@Valid @RequestBody Expediente expediente) {
        logger.info("expediente::"+expediente.getRegistro().getTxtDescripcionTramite());
        Map<String, Object> response = new HashMap<>();
        try{
            expedienteService.cargarExpedienteCeroPapel(expediente);
        }catch (Exception e){
            logger.error(e.getMessage());
            response.put("mensaje","error al insertar data!");
            response.put("error","Se presento un Error al Insertar Data");
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("expediente.getRegistro().getIdRegistro()::"+expediente.getRegistro().getIdRegistro());
        response.put("mensaje","Se grabo satifactoriamente el registro");
        response.put("registro",expediente.getRegistro());
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);


    }
    @PutMapping("/expediente")
    public ResponseEntity<?> envio(@Valid @RequestBody Expediente expedienteCP) {

        Map<String, Object> response = new HashMap<>();
        ExpedienteEdoc expediente = null;
        try{
            expediente =  ecodocService.enviarExpedienteEcodoc(expedienteCP);
        }catch (HandledException e){
            logger.error(e.getMessage());
            response.put("mensaje","error al insertar data!");
            response.put("error","Se presento un Error al Insertar Data");
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Se grabo satifactoriamente el registro");
        response.put("expediente",expediente);
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
    }
    @GetMapping("/expediente/{idregistro}")
    @ResponseStatus(HttpStatus.OK)
    public Expediente getExpediente(@PathVariable Long idregistro) {
        return expedienteService.buscarExpediente(idregistro);
    }
    @PostMapping("/expediente/upd")
    public ResponseEntity<?> actualizarExpediente(@Valid @RequestBody Expediente expediente) {
        logger.info("expediente::"+expediente.getRegistro().getTxtDescripcionTramite());
        Map<String, Object> response = new HashMap<>();
        try{
            expedienteService.actualizarExpediente(expediente);
        }catch (Exception e){
            logger.error(e.getMessage());
            response.put("mensaje","error al insertar data!");
            response.put("error","Se presento un Error al Insertar Data");
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("expediente.getRegistro().getIdRegistro()::"+expediente.getRegistro().getIdRegistro());
        response.put("mensaje","Se actualizo satifactoriamente el registro");
        response.put("registro",expediente.getRegistro());
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
    }

    @GetMapping("/expediente/parametricacorta/{tabla}")
    @ResponseStatus(HttpStatus.OK)
    public List<DetalleCompendio> vinculos(@PathVariable String tabla) {
        return codigoService.getMapDetalleCompendiosCorto(Integer.parseInt(tabla));
    }

    @PostMapping("/expediente/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo){
        Map<String, Object> response = new HashMap<>();

        if(!archivo.isEmpty()) {
            String nombreArchivoFinal = null;
            try {
                nombreArchivoFinal = uploadService.copiar(archivo);

            } catch (IOException e) {
                response.put("mensaje", "Error al subir el archivo del expediente");
                response.put("error", e.getMessage());
                logger.error(e.getMessage());
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.put("nombreFinal", nombreArchivoFinal);
            response.put("mensaje", "Has subido correctamente el Archvo: ");

        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/uploads/file/{nombreFile:.+}")
    public ResponseEntity<Resource> verArchivo(@PathVariable String nombreFile){
        logger.info("nombreFile:"+nombreFile);
        Resource recurso = null;
        try {
            recurso = uploadService.cargar(nombreFile);
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        }

        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");

        return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
    }


    @PutMapping("/expediente/buscarPersonal")
    public ResponseEntity<?> buscarPersonal(@Valid @RequestBody ClienteEdoc cliente) {

        Map<String, Object> response = new HashMap<>();
        List<PersonalEcodoc> listaPersonal = new ArrayList<>();
        try{
            listaPersonal = ecodocService.traerPersonal(cliente);
        }catch (Exception e){
            response.put("mensaje","resultado de Busqueda de personal!");
            response.put("error","Internal Error");
            logger.error(e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Resultado Personal");
        response.put("personal",listaPersonal);
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }
    @PutMapping("/expediente/buscarPersonalDni")
    public ResponseEntity<?> buscarPersonalDni(@Valid @RequestBody ClienteEdoc cliente) {

        Map<String, Object> response = new HashMap<>();
        List<PersonalEcodoc> listaPersonal = new ArrayList<>();
        try{
            listaPersonal = ecodocService.existePersonal(cliente);
        }catch (Exception e){
            response.put("mensaje","resultado de Busqueda de personal!");
            response.put("error","Internal Error");
            logger.error(e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Resultado Personal");
        response.put("personal",listaPersonal);
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }

    @GetMapping("/expediente/representantelegal/{idCiudadano}")
    @ResponseStatus(HttpStatus.OK)
    public List<Ciudadano> getRepresentante(@PathVariable Long idCiudadano) {

        return ciudadanoService.getCiudadanoRepresentanteLegal(idCiudadano);
    }
    @GetMapping("/expediente/ciudadanonatural/{numDni}")
    @ResponseStatus(HttpStatus.OK)
    public Ciudadano getCiudadanoNatural(@PathVariable String numDni) {

        return ciudadanoService.getCiudadanoNroDocumento(Constante.ES_PERSONANATURAL,numDni).orElseGet(()->null);
    }
    @PostMapping("/expediente/representantelegal/grabar")
    public ResponseEntity<?> altarepresentantelegal(@Valid @RequestBody Ciudadano ciudadano) {

        Map<String, Object> response = new HashMap<>();
        try{
            ciudadanoService.grabarRepresentanteLegal(ciudadano);
        }catch (Exception e){
            response.put("mensaje","error al insertar data!");
            response.put("error","Se presento un Error al Insertar Data");
            logger.error(e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Se grabo satifactoriamente el representante legal");
        response.put("ciudadano",ciudadano.getIdCiudadano());
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
    }
    @PostMapping("/expediente/representantelegal/asignar")
    public ResponseEntity<?> asignarepresentantelegal(@Valid @RequestBody Ciudadano ciudadano) {

        Map<String, Object> response = new HashMap<>();
        try{
            ciudadanoService.asignarRepresentanteLegal(ciudadano);
        }catch (Exception e){
            response.put("mensaje","error al insertar data!");
            response.put("error","Se presento un Error al Insertar Data");
            logger.error(e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Se grabo satifactoriamente el representante legal");
        response.put("ciudadano",ciudadano.getIdCiudadano());
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
    }

    @GetMapping("/expediente/reporte")
    @ResponseStatus(HttpStatus.OK)
    public List<Reporte> reporteFull() throws HandledException {
        return expedienteService.lstReporte(new Reporte());
    }

    @PutMapping("/expediente/reporte")
    public ResponseEntity<?> reporteFiltro(@Valid @RequestBody Reporte reporte) {
        Map<String, Object> response = new HashMap<>();
        List<Reporte> listaReporte = new ArrayList<>();
        try{
            listaReporte = expedienteService.lstReporte(reporte);
        }catch (Exception e){
            response.put("mensaje","error al insertar data!");
            response.put("error","Se presento un Error");
            logger.error(e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Se obtubo satifactoriamente los reportes");
        response.put("listaReporte",listaReporte);
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }
    @PutMapping("/expediente/bandejaelectronica")
    public ResponseEntity<?> bandeja(@Valid @RequestBody Registro registro) {
        Map<String, Object> response = new HashMap<>();
        List<Registro> listRegistro = new ArrayList<>();
        try{
            listRegistro = expedienteService.listExpedienteBandeja(registro);
        }catch (Exception e){
            response.put("mensaje","error!");
            response.put("error","Se presento un Error");
            logger.error(e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Se obtubo satifactoriamente los regsitros");
        response.put("listaRegistro",listRegistro);
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }
    @PutMapping("/expediente/notificacion")
    public ResponseEntity<?> getNotificacion(@Valid @RequestBody Notificacion notificacion) {
        Map<String, Object> response = new HashMap<>();
        List<Notificacion> lisaNotifiacion = new ArrayList<>();
        try{
            lisaNotifiacion = expedienteService.listaNotificacion(notificacion);
        }catch (Exception e){
            response.put("mensaje","error!");
            response.put("error","Se presento un Error");
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Se obtubo satifactoriamente los registros");
        response.put("listaNotificacion",lisaNotifiacion);
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }

    @GetMapping("/expediente/archivonotificacion")
    @ResponseStatus(HttpStatus.OK)
    public List<ArchivoNotificacion> archivoNotificacion(@RequestParam("idExpediente") String idExpediente,
                                                         @RequestParam("idCiudadano") String idCiudadano
    ) throws HandledException {
        List<ArchivoNotificacion> lstArchivoNotificacion  = expedienteService.listarArchivosNotificacion(ArchivoNotificacion
                .builder()
                .idExpediente(Integer.parseInt(idExpediente))
                .build());

        return lstArchivoNotificacion;
    }

    @GetMapping("/uploads/ecodoc/{idArchivoCod}")
    @ResponseStatus(HttpStatus.OK)
    public  ResponseEntity<byte[]>   buscarReniec(@PathVariable String idArchivoCod){
    /*    byte[] decodedBytes = Base64.decodeBase64(idArchivoCod);*/
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(ecodocpath + idArchivoCod,
                HttpMethod.GET, entity, byte[].class, "1");

        return response;
    }

    @GetMapping("/uploads/eliminar/{nombreArchivo}")
    public  ResponseEntity<?>   eliminarArchivo(@PathVariable String nombreArchivo){
        Map<String, Object> response = new HashMap<>();
        boolean isRemove = false;
        try{
            isRemove = uploadService.eliminar(nombreArchivo);
        }catch (Exception e){
            response.put("mensaje","error!");
            response.put("error","Se presento un Error");
            logger.error(e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","Se elimino correctamente el archivo");
        response.put("isRemove",""+isRemove);

        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }
    @GetMapping("/expediente/encodearchivo/{nombreArchivo}")
    @ResponseStatus(HttpStatus.OK)
    public String getDecodeNombreArchivo(@PathVariable String nombreArchivo) {
        return codigoService.encodeHash(nombreArchivo);
    }
}

