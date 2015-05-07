package pt.ulisboa.tecnico.sdis.store.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.Key;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.SystemUtils;
import org.junit.AfterClass;

import pt.ulisboa.tecnico.sdis.store.cli.SDStoreClient;
import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosClientAuthentication;
import util.kerberos.messages.KerberosTicket;


public class SDStoreTest {
	
	private static final String UDDI_URL = "http://localhost:8081";
	
	private static final String ID_NAME = "SD-STORE-1";
	
	public final String KEYS_FILE_WIN = "\\..\\sd-util\\src\\main\\resources\\serverKeys";
	public final String KEYS_FILE_LINUX_MAC = "/../sd-util/src/main/resources/serverKeys";
	
	protected static SDStoreClient port;
	
	public SDStoreTest() throws Exception{
		port = new SDStoreClient(UDDI_URL, ID_NAME);
	}
	
	
	/**
	 * Used to access SD-STRE this function simulates asking
	 * Kerberos Authenticator Server.
	 * @throws Exception 
	 */
	public void uploadKerberosInfo(SDStoreClient store, String username) 
	throws Exception{
		Key kcs = Kerberos.generateKerberosKey();
		KerberosTicket ticket = new KerberosTicket(username,ID_NAME,8,kcs);
		KerberosClientAuthentication auth;
		auth = new KerberosClientAuthentication(username);
		Key ks = loadServerKey(ID_NAME);
		port.processRequest(ticket.serialize(ks), auth.serialize(kcs));
	}
	
	public Key loadServerKey(String serverID) throws Exception{
		BufferedReader br;
		String currentKeysFile = "";
		if(SystemUtils.IS_OS_WINDOWS)
			currentKeysFile = System.getProperty("user.dir") + KEYS_FILE_WIN;
		else if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
			currentKeysFile = System.getProperty("user.dir") + KEYS_FILE_LINUX_MAC;
		
		br = new BufferedReader(new FileReader(currentKeysFile));
		String line = "";
		while((line = br.readLine()) != null){
			String[] divs = line.split("[ \t]+");
			if(divs[0].equals(serverID)) {
				byte[] k = DatatypeConverter.parseBase64Binary(divs[1]);
				br.close();
				return Kerberos.getKeyFromBytes(k);
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
