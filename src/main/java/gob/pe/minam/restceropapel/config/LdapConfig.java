package gob.pe.minam.restceropapel.config;


import gob.pe.minam.restceropapel.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class LdapConfig {
    private Logger logger = LoggerFactory.getLogger(LdapConfig.class);


    private static String LDAP_CONTEXT_FACTORY = "";
    private static String LDAP_SECURITY_AUTHENTICATION = "";
    private static String LDAP_PROVIDER_URL = "";
    private static String DOMINIO_MAIL = "";

    protected static Hashtable<String, String> env = new Hashtable<String, String>(11);

    static{
        try{
            LDAP_CONTEXT_FACTORY = Property.getInstance().getValorCp("ldap.context_factory");
            LDAP_SECURITY_AUTHENTICATION = Property.getInstance().getValorCp("ldap.security_authentication");
            LDAP_PROVIDER_URL = Property.getInstance().getValorCp("ldap.provider_url");
            DOMINIO_MAIL = Property.getInstance().getValorCp("ldap.domain");
            env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
            env.put(Context.SECURITY_AUTHENTICATION, LDAP_SECURITY_AUTHENTICATION);
           // env.put(Context.PROVIDER_URL,LDAP_PROVIDER_URL);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Boolean validarUsuarioLDAP(final String usuario, final String clave){


        DirContext ctx = null;
        Boolean validacion = Boolean.FALSE;

        try {

            StringBuilder sbUsuario = new StringBuilder();
            sbUsuario.append(usuario).append(DOMINIO_MAIL);

           env.put(Context.SECURITY_PRINCIPAL, sbUsuario.toString());
           env.put(Context.SECURITY_CREDENTIALS, clave);

            ctx = new InitialDirContext(env);
            validacion = Boolean.TRUE;

        } catch (AuthenticationException e)
			{
                logger.error(e.getMessage());
			}
			catch (Exception e) {
                logger.error(e.getMessage());
        } finally {
				if (ctx != null) {
					try {
						ctx.close();
					} catch (NamingException e) {
                        System.out.println(e.getMessage());
					}
				}
        }

        return validacion;
    }
}
