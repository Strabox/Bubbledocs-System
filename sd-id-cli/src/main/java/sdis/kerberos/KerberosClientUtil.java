package sdis.kerberos;

import java.util.Map;

import javax.xml.ws.BindingProvider;

/*
 * Methods to id clients (PROBABLY USELESS)!!!!!!!!!!.
 */
public class KerberosClientUtil {
	
	public void addRequestContext(BindingProvider bp,String propertyName,
			Object property){
		Map<String, Object> requestContext = bp.getRequestContext();
        requestContext.put(propertyName, property);
	}

	
}
