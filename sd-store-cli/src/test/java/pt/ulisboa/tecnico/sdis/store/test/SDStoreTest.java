package pt.ulisboa.tecnico.sdis.store.test;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import pt.ulisboa.tecnico.sdis.store.cli.kerberos.KerberosClientUtil;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosClientAuthentication;
import util.kerberos.messages.KerberosTicket;
import util.uddi.UDDINaming;


public class SDStoreTest {

	
	protected static SDStore port;
	
	
	@BeforeClass
	public static void setUpClient() throws Exception{
		
		 String uddiURL = "http://localhost:8081";
	        String name = "SD-STORE";	       
	        UDDINaming uddiNaming = new UDDINaming(uddiURL);	        
	        String endpointAddress = uddiNaming.lookup(name);
	        if (endpointAddress == null) {	            
	            return;
	        } else {	           
	        }	      
	        SDStore_Service service = new SDStore_Service();
	        port = service.getSDStoreImplPort();

	      
	        BindingProvider bindingProvider = (BindingProvider) port;
	        Map<String, Object> requestContext = bindingProvider.getRequestContext();
	        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

	        	
	}
	
	/**
	 * Used to access SD-STRE this function simulates asking
	 * Kerberos Authenticator Server.
	 * @throws NoSuchAlgorithmException
	 * @throws KerberosException 
	 */
	public void uploadKerberosInfo(SDStore store, String username) 
	throws NoSuchAlgorithmException, KerberosException{
		String nonce = Kerberos.generateRandomNumber();
		KerberosClientUtil util = new KerberosClientUtil();
		Key kcs = Kerberos.generateSymKey(Kerberos.DES, 56);
		KerberosTicket ticket = new KerberosTicket(username,1,8,kcs);
		KerberosClientAuthentication auth;
		auth = new KerberosClientAuthentication(username);
		util.request(store, ticket.serialize(kcs), auth.serialize(kcs), nonce.getBytes());
	}
	
	@AfterClass
	/* cleanClient() - delete proxy in end of tests. */
	public static void cleanClient(){
		port = null;
	}
	
	
}
