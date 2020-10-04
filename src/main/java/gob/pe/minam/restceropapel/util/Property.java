package gob.pe.minam.restceropapel.util;

import java.io.InputStream;
import java.util.Properties;

public class Property {
    private static Property property ;
    private static final String PROPERTY_FILE = "application.properties";
    public static Property getInstance() {
        if(property==null) {
            property = new Property();
        }
        return property;
    }
    public Properties getPropertiesAseg(){

        String resourceName = "/" + PROPERTY_FILE;
        InputStream fis = null;
        Properties props = new Properties();
        try{
            fis = this.getClass().getResourceAsStream(resourceName);
            if(fis==null){
                return null;
            }
            props.load(fis);
        }catch(Exception e ){
            e.printStackTrace();
        }
        return props;
    }
    public String getValorCp(String key){
        String endPoint = "";
        try{
            Properties props = getPropertiesAseg();
            endPoint =  props.getProperty(key);
        }catch(Exception e){
         e.printStackTrace();
        }
        return endPoint;

    }
}
