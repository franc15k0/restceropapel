package gob.pe.minam.restceropapel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Utilitario {
    private static final Logger logger = LoggerFactory.getLogger(Utilitario.class);

    /**
     * @author esolano
     *
     * */

    public static String obtenerIPRemota()
    {
        InetAddress ip;
        String ipAddress = "";

        try {

            ip = InetAddress.getLocalHost();

            ipAddress = ip.getHostAddress();

        } catch (UnknownHostException e) {
            ipAddress = "";

        }

        return ipAddress;
    }



    public static String obtenerDireccionIpCliente(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.info("IP" + ip);
        return ip;
    }



    public static String obtenerMACRemota()
    {
        String idrresMac= "00:00:00:00";
        try {
            final NetworkInterface netInf = NetworkInterface.getNetworkInterfaces().nextElement();
            final byte[] mac = netInf.getHardwareAddress();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            idrresMac = sb.toString();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return idrresMac;
    }

    public static String obtenerIpAddress(HttpServletRequest request)
    {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.info("IP obtenerIpAddress: " + ip);
        return ip;
    }


    public static byte[] obtenerArchivo(String rutaArchivo)
    {
        File archivo = null;
        byte[] bytesArchivo = null;
        try
        {
            archivo = new File(rutaArchivo);
            bytesArchivo = new byte[(int) archivo.length()];

            FileInputStream fis = new FileInputStream(archivo);
            fis.read(bytesArchivo);
            fis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return bytesArchivo;
    }

    /*********Alfresco*****************/
    public static void isNotEmptyString(Object obj, String message)
    {
        if (isEmpty(obj))
        {
            throw new IllegalArgumentException(message);
        }
    }

    public static boolean isEmpty(Object obj)
    {
        return (obj == null) || (((String) obj).trim().length() == 0);
    }

    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }

    public static String getFullPathWithoutSeparatorAtTheEnd(String... paths)
    {
        String result = "";
        for (String path : paths)
        {
            result += "/" + path;
        }
        result = result.replaceAll("[/]+", "/");
        return (result.endsWith("/")) ? result.substring(0, result.lastIndexOf("/")) : result;
    }

    public static boolean vacio(String cadena){
        return cadena == null || cadena.trim().equals("");
    }


    public static String obtenerSistemaOperativo(HttpServletRequest request )
    {
        String  browserDetails  =   request.getHeader("User-Agent");
        String  userAgent       =   browserDetails;
        String  user            =   userAgent.toLowerCase();

        String os = "";
        String browser = "";

        //=================OS=======================
        if (userAgent.toLowerCase().indexOf("windows") >= 0 )
        {
            os = "Windows";
        } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
        {
            os = "Mac";
        } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
        {
            os = "Unix";
        } else if(userAgent.toLowerCase().indexOf("android") >= 0)
        {
            os = "Android";
        } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
        {
            os = "IPhone";
        }else{
            os = "UnKnown, More-Info: "+userAgent;
        }
        //===============Browser===========================
        if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera"))
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if(user.contains("opr"))
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            browser="IE";
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        return os;
    }

}
