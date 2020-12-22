package gob.pe.minam.restceropapel.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.net.*;
import java.util.Enumeration;


public class App {
    public static void main(String[] args) {
        try {
            /*Example example = new Example();
            example.setCampo1("1");
            example.setCampo2("2");
            System.out.println(example.getCampo3());*/
           /* final NetworkInterface netInf = NetworkInterface.getNetworkInterfaces().nextElement();
            final byte[] mac = netInf.getHardwareAddress();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            System.out.println(sb.toString());*/
           /*String ubigeo = "CALLAO\\/BELLAVISTA\\/BELLAVISTA";
            String[] tramaUbigeo = StringUtils.split(ubigeo, "\\/");
            String trama3 = "";
            System.out.println(tramaUbigeo.length);
            if(tramaUbigeo.length==2 ){

                trama3 = tramaUbigeo[1];
            }else{
                trama3 = tramaUbigeo[2];
            }
            System.out.println(tramaUbigeo[0]);
            System.out.println(trama3);*/
          /* String archivo = "undefined";
            //System.out.println(archivo.split("-")[1].trim());
            byte[] encodedBytes = Base64.encodeBase64(archivo.getBytes());
            String jaja = new String(encodedBytes);
            System.out.println(jaja);
           // System.out.println("encodedBytes " + new String(encodedBytes));
            //dW5kZWZpbmVk
            byte[] decodedBytes = Base64.decodeBase64("dW5kZWZpbmVk");
            System.out.println("decodedBytes " + new String(decodedBytes));*/
            System.out.println(System.getProperties().getProperty("os.name"));

            String valor = "CAR.¢∞∞∞{}}^[][N°2246-   GCRRRTIC-19.pdf";
            //System.out.println(valor.replaceAll("[^\\dA-Za-z]", ""));
            /* Otro Regex*/
            //System.out.println(valor.replaceAll("[\\W]|_", ""));
            /* Otro Regex*/
            System.out.println(valor.replaceAll("[^a-zA-Z0-9. ]", ""));
        } catch (Exception ex) {

        ex.printStackTrace();

        }


    }
    public static String GetAddress(String addressType) {
        String address = "";
        InetAddress lanIp = null;
        try {

            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            net = NetworkInterface.getNetworkInterfaces();

            while (net.hasMoreElements()) {
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();

                while (addresses.hasMoreElements() && element.getHardwareAddress().length > 0 && !isVMMac(element.getHardwareAddress())) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {

                        if (ip.isSiteLocalAddress()) {
                            ipAddress = ip.getHostAddress();
                            lanIp = InetAddress.getByName(ipAddress);
                        }

                    }

                }
            }

            if (lanIp == null)
                return null;

            if (addressType.equals("ip")) {

                address = lanIp.toString().replaceAll("^/+", "");

            } else if (addressType.equals("mac")) {

                address = getMacAddress(lanIp);

            } else {

                throw new Exception("Specify \"ip\" or \"mac\"");

            }

        } catch (UnknownHostException ex) {

            ex.printStackTrace();

        } catch (SocketException ex) {

            ex.printStackTrace();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return address;

    }

    private static String getMacAddress(InetAddress ip) {
        String address = null;
        try {

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            address = sb.toString();

        } catch (SocketException ex) {

            ex.printStackTrace();

        }

        return address;
    }

    private static boolean isVMMac(byte[] mac) {
        if(null == mac) return false;
        byte invalidMacs[][] = {
                {0x00, 0x05, 0x69},             //VMWare
                {0x00, 0x1C, 0x14},             //VMWare
                {0x00, 0x0C, 0x29},             //VMWare
                {0x00, 0x50, 0x56},             //VMWare
                {0x08, 0x00, 0x27},             //Virtualbox
                {0x0A, 0x00, 0x27},             //Virtualbox
                {0x00, 0x03, (byte)0xFF},       //Virtual-PC
                {0x00, 0x15, 0x5D}              //Hyper-V
        };

        for (byte[] invalid: invalidMacs){
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2]) return true;
        }

        return false;
    }
}

