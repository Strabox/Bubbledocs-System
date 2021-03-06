package sdis.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.security.Key;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;
import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosCredential;
import util.kerberos.messages.KerberosReply;
import util.kerberos.messages.KerberosRequest;
import util.kerberos.messages.KerberosServerAuthentication;
import example.ws.uddi.UDDIClient;


public class SdIdClient extends UDDIClient implements SDId{
	
	private static final String STORE_SERVICE = "SD-STORE";
	
	/**
	 * Proxy for webService.
	 */
	private SDId idRemote;
	
	
	public SdIdClient(String uddiURL,String idName) throws Exception {
		super(uddiURL,idName);
		connectUDDI();
	}
	
	@Override
	protected void getSpecificProxy(String endpoint) throws Exception{	
		SDId_Service service = new SDId_Service();
		idRemote = service.getSDIdImplPort();
		
		BindingProvider bindingProvider = (BindingProvider) idRemote;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpoint);
	}
	
	
    /*======================= SD-ID Client =========================== */
    /* Methods encapsulate remote calls in a perfect client. */
    
	
	public void createUser(String username,String email) throws
		EmailAlreadyExists_Exception, InvalidEmail_Exception,
	InvalidUser_Exception,UserAlreadyExists_Exception {
		idRemote.createUser(username, email);
	}
	
	
	public void renewPassword(String username) 
	throws UserDoesNotExist_Exception{
		idRemote.removeUser(username);
	}
	
	
	public void removeUser(String username)
	throws UserDoesNotExist_Exception{
		idRemote.removeUser(username);
	}

	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {
		if(userId == null || reserved == null){
			AuthReqFailed auth = new AuthReqFailed();
			throw new AuthReqFailed_Exception(userId, auth);
		}
		try{
			String sentNonce = Kerberos.generateRandomNumber();
			KerberosRequest req = new KerberosRequest(STORE_SERVICE,sentNonce);
			byte[] ans = idRemote.requestAuthentication(userId, req.serialize());
			KerberosReply serverReply = KerberosReply.deserialize(ans);
			Key kc = Kerberos.getKeyFromBytes(Kerberos.digestPassword(reserved, Kerberos.MD5));
			KerberosServerAuthentication receivedAuth;
			receivedAuth = KerberosServerAuthentication.deserialize(serverReply.getAuthentication(), kc);
			if(!receivedAuth.isValid(sentNonce)){
				AuthReqFailed auth = new AuthReqFailed();
				throw new AuthReqFailed_Exception(userId, auth);
			}
			return new KerberosCredential(serverReply.getTicket(), receivedAuth.getKcs()).serialize();				
		}catch(KerberosException e){
			AuthReqFailed auth = new AuthReqFailed();
			throw new AuthReqFailed_Exception(userId, auth);
		}
	}


}
