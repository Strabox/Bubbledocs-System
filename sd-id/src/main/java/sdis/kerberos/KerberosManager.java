package sdis.kerberos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.SystemUtils;

import util.kerberos.Kerberos;

/**
 * Used to mange Kerberos in SD-ID server (Kerberos SAut).
 * @author André
 *
 */
public class KerberosManager {

	public final String KEYS_FILE_WIN = "\\..\\sd-util\\src\\main\\resources\\serverKeys";
	public final String KEYS_FILE_LINUX_MAC = "/../sd-util/src/main/resources/serverKeys";
	
	public String currentKeysFile;
	
	/**
	 * Max nonces server maintain in record.
	 */
	private final int MAX_NONCES = 300;
	
	private int currentNonce = 0;
	
	/**
	 * Used to save all nonces server receive. 
	 */
	private ArrayList<String> nonces;	//FIXME !!!!!!!!!!!
	
	/**
	 * Used do maintain pairs <servers, secret keys>.
	 */
	private HashMap<String,Key> serverKeys;
	
	
	public KerberosManager() throws Exception{
		nonces = new ArrayList<String>();
		serverKeys = new HashMap<String,Key>();
		if(SystemUtils.IS_OS_WINDOWS)
			currentKeysFile = System.getProperty("user.dir") + KEYS_FILE_WIN;
		else if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
			currentKeysFile =System.getProperty("user.dir") + KEYS_FILE_LINUX_MAC;
		generateServerKeys();
	}
	
	public void generateServerKeys() throws Exception{
		BufferedWriter br = new BufferedWriter(new FileWriter(currentKeysFile));
		Key k;
		br.write("SD-STORE-1 ");
		k = Kerberos.generateKerberosKey();
		serverKeys.put("SD-STORE-1", k);
		br.write(DatatypeConverter.printBase64Binary(k.getEncoded()));
		br.newLine();
		br.write("SD-STORE-2 ");
		k = Kerberos.generateKerberosKey();
		serverKeys.put("SD-STORE-2", k);
		br.write(DatatypeConverter.printBase64Binary(k.getEncoded()));
		br.newLine();
		br.write("SD-STORE-3 ");
		k = Kerberos.generateKerberosKey();
		serverKeys.put("SD-STORE-3", k);
		br.write(DatatypeConverter.printBase64Binary(k.getEncoded()));
		br.newLine();
		br.close();
	}
	
	/**
	 * 
	 * @param nonce Nonce to save in server.
	 */
	public void addNonce(String nonce){
		nonces.add(currentNonce, nonce);
		currentNonce++;
		if(currentNonce == MAX_NONCES)
			currentNonce = 0;
	}
	
	/**
	 * 
	 * @param nonce
	 * @return
	 */
	public boolean nonceExists(String nonce){
		System.out.println(nonces.toString());
		return nonces.contains(nonce);
	}
	
	/**
	 * 
	 * @param server
	 * @return Server secure key.
	 */
	public Key getServerKey(String server){
		return serverKeys.get(server);
	}
	
}
