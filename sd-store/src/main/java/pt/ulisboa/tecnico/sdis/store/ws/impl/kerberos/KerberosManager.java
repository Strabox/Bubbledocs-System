package pt.ulisboa.tecnico.sdis.store.ws.impl.kerberos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.Key;
import org.apache.commons.lang3.SystemUtils;

import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosTicket;

/**
 * Used to manage Kerberos protocol, in servers side. 
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
	
	
	public KerberosManager(int serverID) throws Exception{
		this.serverID = serverID;
		if(SystemUtils.IS_OS_WINDOWS)
			currentKeysFile = System.getProperty("user.dir") + KEYS_FILE_WIN;
		else if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
			currentKeysFile = System.getProperty("user.dir") + KEYS_FILE_LINUX_MAC;
		loadServerKey();
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
	 * Decypher a Kerberos Ticket and returns Kcs
	 * (Key used to talk with client)
	 * @param ticket Kerberos Ticket in bytes.
	 * @return Client/Server key
	 * @throws Exception 
	 */
	public Key decypherTicket(byte[] byteTicket) throws Exception{
		KerberosTicket ticket = KerberosTicket.deserialize(byteTicket, ks);
		if(!ticket.isValidTicket(serverID))
			throw new Exception();
		return ticket.getKcs();
	}
	
	
}
