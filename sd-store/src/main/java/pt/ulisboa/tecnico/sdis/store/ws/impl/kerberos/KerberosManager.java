package pt.ulisboa.tecnico.sdis.store.ws.impl.kerberos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.lang3.SystemUtils;

import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosClientAuthentication;
import util.kerberos.messages.KerberosTicket;

/**
 * Used to manage Kerberos protocol, in servers side,
 * verify the authenticity of the request/client. 
 * @author André
 */
public class KerberosManager {

	public final String KEYS_FILE_WIN = "\\..\\sd-util\\src\\main\\resources\\serverKeys";
	public final String KEYS_FILE_LINUX_MAC = "/../sd-util/src/main/resources/serverKeys";
	
	public String currentKeysFile;
	
	/**
	 * Unique server identifier.
	 */
	private int serverID;
	
	/**
	 * Server secret symmetric key.
	 */
	private Key ks;
	
	/**
	 * Used to maintain client last request timestamp.
	 */
	private HashMap<String, Date> lastRequest;
	
	/**
	 * Used to maintain Kcs keys. 
	 */
	private HashMap<String, Key> kcsKeys;
	
	
	public KerberosManager(int serverID) throws Exception{
		this.serverID = serverID;
		lastRequest = new HashMap<String,Date>();
		kcsKeys = new HashMap<String, Key>();
		if(SystemUtils.IS_OS_WINDOWS)
			currentKeysFile = System.getProperty("user.dir") + KEYS_FILE_WIN;
		else if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
			currentKeysFile = System.getProperty("user.dir") + KEYS_FILE_LINUX_MAC;
		loadServerKey();
	}
	
	
	public void addLastRequest(String client,Date requestTime){
		if(lastRequest.containsKey(client))
			lastRequest.replace(client, requestTime);
		else
			lastRequest.put(client, requestTime);
	}
	
	public void addKcsKey(String client, Key kcs){
		if(kcsKeys.containsKey(client))
			kcsKeys.replace(client, kcs);
		else
			kcsKeys.put(client, kcs);
	}
	
	/**
	 * Load server key from a file.
	 */
	public void loadServerKey() throws Exception{
		BufferedReader br;
		br = new BufferedReader(new FileReader(currentKeysFile));
		String line = "";
		while((line = br.readLine()) != null){
			if(line.matches("[1-9][ \t]+[[a-z][A-Z][0-9]]+")){
				String[] divs = line.split("[ \t]+");
				if(Integer.parseInt(divs[0]) == serverID){
					ks = Kerberos.getKeyFromBytes(divs[1].getBytes("UTF-8"));
					br.close();
					return;
				}
			}
		}
		br.close();
	}
	
	/**
	 * Validate a client request.
	 * @throws Exception
	 */
	public void processRequest(String base64Ticket,String base64Auth,String base64Nonce) 
	throws Exception {
		byte[] byteTicket = DatatypeConverter.parseBase64Binary(base64Ticket);
		byte[] auth = DatatypeConverter.parseBase64Binary(base64Auth);
		//--Validate Nonce--
		DatatypeConverter.parseBase64Binary(base64Nonce).toString();
		//TODO
		//--Validate Ticket--
		KerberosTicket ticket = KerberosTicket.deserialize(byteTicket, ks);
		if(!ticket.isValidTicket(serverID))
			throw new Exception();
		Key kcs = ticket.getKcs();
		//--Validate authenticator--
		KerberosClientAuthentication authentication;
		authentication = KerberosClientAuthentication.deserialize(auth, kcs);
		Date lastReq = lastRequest.get(authentication.getClient());
		if(!authentication.isValid(ticket.getClient(),lastReq))
			throw new Exception();
		addLastRequest(authentication.getClient(), authentication.getRequestTime());
		addKcsKey(authentication.getClient(), kcs);
	}
	
	/**
	 * Process reply passing information to server handlers.
	 * ->1 
	 * @param webContext
	 * @param kcs Client Server Key.
	 */
	public void processReply(Key kcs,WebServiceContext webContext) 
	throws Exception{
		MessageContext msgContext = webContext.getMessageContext();
		String nonceBase64 = (String) msgContext.get("nonce");
		String nonce = DatatypeConverter.parseBase64Binary(nonceBase64).toString();
		byte[] cypheredNonce = Kerberos.cipherText(kcs, nonce.getBytes("UTF-8"));
		String cyph64BaseNonce = DatatypeConverter.printBase64Binary(cypheredNonce);
		msgContext.put("nonce", cyph64BaseNonce);
	}
	
	
	
}
