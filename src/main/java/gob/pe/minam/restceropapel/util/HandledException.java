package gob.pe.minam.restceropapel.util;

public class HandledException extends Exception {


    public HandledException(String code, String message) {
        super(message);

    }

    public HandledException(String message, Throwable cause) {
        super(message, cause);

    }


}
