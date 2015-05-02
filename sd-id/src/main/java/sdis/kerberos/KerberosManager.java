package sdis.kerberos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;

import util.kerberos.Kerberos;

public class KerberosManager {

	public final String KEYS_FILE = "C:\\Maven\\A_15_03_17-project\\sd-util\\src\\main\\resources\\serverKeys";
	/*
	 * Max nonces server maintain in record.
	 */
	private final int MAX_NONCES = 300;
	
	private int currentNonce = 0;
	
	/*
	 * Used to save all nonces server receive. 
	 */
	private ArrayList<String> nonces;
	
	/*
	 * Used do maintain servers secret keys.
	 */
	private HashMap<Integer,Key> serverKeys;
	
	
	public KerberosManager() throws Exception{
		nonces = new ArrayList<String>();
		serverKeys = new HashMap<Integer,Key>();
		loadServerKeys();
	}
	
	/**
	 * 
	 * 
	 */
	public void loadServerKeys() throws Exception{
		BufferedReader br;
		br= new BufferedReader(new FileReader(KEYS_FILE));
		String line = "";
		while((line = br.readLine()) != null){
			if(line.matches("[1-9][ \t]+[[a-z][A-Z][0-9]]+")){
				String[] divs = line.split("[ \t]+");
				Integer s = Integer.parseInt(divs[0]);
				Key k = Kerberos.getKeyFromBytes(divs[1].getBytes("UTF-8"));
				serverKeys.put(s, k);
			}
		}
		System.out.println(serverKeys.toString());
		br.close();
	}
	
	/**
	 * 
	 * @param nonce Nounce to save in server.
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
	public Key getServerKey(Integer server){
		return serverKeys.get(server);
	}
	
}
