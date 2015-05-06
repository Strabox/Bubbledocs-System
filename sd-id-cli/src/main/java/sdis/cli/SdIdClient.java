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
import util.kerberos.messages.KerberosCredential;
import util.kerberos.messages.KerberosReply;
import util.kerberos.messages.KerberosRequest;
import util.kerberos.messages.KerberosServerAuthentication;
import util.uddi.UDDIClient;


public class SdIdClient extends UDDIClient implements SDId{
	
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
    /* Mehtods encapsulate remote calls in a perfect client. */
    
	
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
		try{
			String sentNonce = Kerberos.generateRandomNumber();
			KerberosRequest req = new KerberosRequest(1,sentNonce);		//FIXME server identifier
			byte[] ans = idRemote.requestAuthentication(userId, req.serialize());
			System.out.println("aaaa");
			KerberosReply serverReply = KerberosReply.deserialize(ans);
			Key kc = Kerberos.getKeyFromBytes(Kerberos.digestPassword(reserved, "MD5"));
			KerberosServerAuthentication receivedAuth;
			receivedAuth = KerberosServerAuthentication.deserialize(serverReply.getAuthentication(), kc);
			if(!sentNonce.equals(receivedAuth.getNonce()))
				throw new Exception();									//FIXME can i use wsdl exceptions ?
			return new KerberosCredential(serverReply.getTicket(), receivedAuth.getKcs()).serialize();				
		}catch(Exception e){
			AuthReqFailed auth = new AuthReqFailed();
			throw new AuthReqFailed_Exception(userId, auth);
		}
	}


}
