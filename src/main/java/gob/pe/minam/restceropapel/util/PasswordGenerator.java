package gob.pe.minam.restceropapel.util;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static SecureRandom random = new SecureRandom();


    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";

    public static String generateRandom(int len, String dic) {
        String result = "";
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }

    public static String generateTokenDefault() {
        return generateRandom(10, ALPHA_CAPS + ALPHA);
    }
    public static String generateCodigoDefault() {
        return generateRandom(9, NUMERIC);
    }
    public static String generateCodigoFiles() {
        return generateRandom(10, NUMERIC);
    }
    public static String generateClaveEdoc1() { return generateRandom(6, NUMERIC) +generateRandom(1, ALPHA) ;  }


}
