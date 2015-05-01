package sdis.kerberos;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;

public class KerberosManager {

	@SuppressWarnings("unused")
	private HashMap<String,ArrayList<String>> userNounces;
	
	@SuppressWarnings("unused")
	private HashMap<String,Key> serverKeys;
	
	public KerberosManager(){
		userNounces = new HashMap<String,ArrayList<String>>();
		serverKeys = new HashMap<String,Key>();
	}
	
}
