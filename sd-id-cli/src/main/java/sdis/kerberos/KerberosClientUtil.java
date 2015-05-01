package sdis.kerberos;

import java.util.Base64;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.store.ws.SDStore;

/*
 * Methods to id clients.
 */
public class KerberosClientUtil {
	
	/*
	 * request - Used to pass request arguments to handler.
	 */
	public static void request(SDStore store,byte[] ticket,byte[] auth,byte[] nonce){
		BindingProvider bp = (BindingProvider) store;
		Map<String, Object> requestContext = bp.getRequestContext();
        requestContext.put("ticket", Base64.getEncoder().encode(ticket));
        requestContext.put("auth", Base64.getEncoder().encode(auth));
        requestContext.put("nonce", Base64.getEncoder().encode(nonce));
        requestContext.put("type", true);
	}

	
}
