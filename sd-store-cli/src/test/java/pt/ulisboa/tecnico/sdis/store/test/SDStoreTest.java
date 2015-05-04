package pt.ulisboa.tecnico.sdis.store.test;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.Key;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.commons.lang3.SystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import pt.ulisboa.tecnico.sdis.store.cli.kerberos.KerberosClientUtil;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosClientAuthentication;
import util.kerberos.messages.KerberosTicket;
import util.uddi.UDDINaming;


public class SDStoreTest {

	public final String KEYS_FILE_WIN = "\\..\\sd-util\\src\\main\\resources\\serverKeys";
	public final String KEYS_FILE_LINUX_MAC = "/../sd-util/src/main/resources/serverKeys";
	
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
	 * @throws Exception 
	 */
	public void uploadKerberosInfo(SDStore store, String username) 
	throws Exception{
		String nonce = Kerberos.generateRandomNumber();
		KerberosClientUtil util = new KerberosClientUtil();
		Key kcs = Kerberos.generateSymKey(Kerberos.DES, 56);
		KerberosTicket ticket = new KerberosTicket(username,1,8,kcs);
		KerberosClientAuthentication auth;
		auth = new KerberosClientAuthentication(username);
		Key ks = loadServerKey(1);
		util.request(store, ticket.serialize(ks), auth.serialize(kcs), nonce.getBytes());
	}
	
	public Key loadServerKey(int serverID) throws Exception{
		BufferedReader br;
		String currentKeysFile = "";
		if(SystemUtils.IS_OS_WINDOWS)
			currentKeysFile = System.getProperty("user.dir") + KEYS_FILE_WIN;
		else if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
			currentKeysFile = System.getProperty("user.dir") + KEYS_FILE_LINUX_MAC;
		
		br = new BufferedReader(new FileReader(currentKeysFile));
		String line = "";
		while((line = br.readLine()) != null){
			if(line.matches("[1-9][ \t]+[[a-z][A-Z][0-9]]+")){
				String[] divs = line.split("[ \t]+");
				if(Integer.parseInt(divs[0]) == serverID){
					br.close();
					return Kerberos.getKeyFromBytes(divs[1].getBytes("UTF-8"));
				}
			}
		}
		br.close();
		return null;
	}
	
	@AfterClass
	/* cleanClient() - delete proxy in end of tests. */
	public static void cleanClient(){
		port = null;
	}
	
	
}
