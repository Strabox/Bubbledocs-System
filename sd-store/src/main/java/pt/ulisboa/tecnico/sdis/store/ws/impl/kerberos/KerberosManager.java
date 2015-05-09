package pt.ulisboa.tecnico.sdis.store.ws.impl.kerberos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.SystemUtils;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
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
	private String serverID;
	
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
	
	
	public KerberosManager(String serverID) throws Exception{
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
			String[] divs = line.split("[ \t]+");
			if(divs[0].equals(serverID)){
				byte[] k = DatatypeConverter.parseBase64Binary(divs[1]);
				ks = Kerberos.getKeyFromBytes(k);
				br.close();
				return;
			}
		}
		br.close();
	}
	
	/**
	 * Validate a client request, "Kerberos cerebrus".
	 * @param base64Ticket
	 * @param base64Auth
	 * @throws Exception
	 */
	public void processRequest(String base64Ticket,String base64Auth,
	byte[] msgByte,byte[] mac) 
	throws Exception {
		byte[] byteTicket = DatatypeConverter.parseBase64Binary(base64Ticket);
		byte[] auth = DatatypeConverter.parseBase64Binary(base64Auth);
		//================Validate Ticket=====================
		KerberosTicket ticket = KerberosTicket.deserialize(byteTicket, ks);
		if(!ticket.isValidTicket(serverID))
			throw new Exception();
		Key kcs = ticket.getKcs();
		//================Validate MAC =======================
		if(!verifyMAC(mac, msgByte, kcs))
			throw new Exception();
		//==============Validate authenticator================
		KerberosClientAuthentication authentication;
		authentication = KerberosClientAuthentication.deserialize(auth, kcs);
		Date lastReq = lastRequest.get(authentication.getClient());
		if(!authentication.isValid(ticket.getClient(),lastReq))
			throw new Exception();
		//Request is valid (I Hope So).
		addLastRequest(authentication.getClient(), authentication.getRequestTime());
		addKcsKey(authentication.getClient(), kcs);
	}
	
	/**
	 * Verify if the message integrity wasnt compromised between client
	 * and server.
	 * @param mac
	 * @param received
	 * @param kcs
	 * @return true if its correct false otherwise
	 * @throws KerberosException 
	 */
	public boolean verifyMAC(byte[] mac,byte[] received,Key kcs) 
	throws Exception{
		byte [] mac2 = Kerberos.makeMAC(received, (SecretKey) kcs);
		System.out.println(new String(mac));
		System.out.println(new String(mac2));
		return Arrays.equals(mac, mac2);
	}
	
	/**
	 * Process reply to send to client.
	 * @param Client
	 * @return nonce cyphered in base64
	 */
	public String processReply(String client) 
	throws Exception{
		Date lastDate = lastRequest.get(client);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(lastDate); 
		XMLGregorianCalendar t = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		String tString = t.toXMLFormat();
		Key kcs = kcsKeys.get(client);
		byte[] tCyphered = Kerberos.cipherText(kcs, tString.getBytes());
		return DatatypeConverter.printBase64Binary(tCyphered);
	}
	
	
	
}
