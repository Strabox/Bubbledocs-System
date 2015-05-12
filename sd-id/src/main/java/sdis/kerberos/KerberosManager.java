package sdis.kerberos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.SystemUtils;

import util.kerberos.Kerberos;

/**
 * Used to manage Kerberos in SD-ID server (Kerberos SAut).
 * @author André
 *
 */
public class KerberosManager extends TimerTask{

	public final String KEYS_FILE_WIN = "\\..\\sd-util\\src\\main\\resources\\serverKeys";
	public final String KEYS_FILE_LINUX_MAC = "/../sd-util/src/main/resources/serverKeys";
	
	public String currentKeysFile;
	
	public static final int TICKET_HOUR_DURATION = 5;
	
	private static final long TIME_GARBAGE_NONCE = 18000000;	//5 Hours
	
	/**
	 * Maintain nonces. 
	 */
	private HashMap<String,Date> nonces;
	
	/**
	 * Used do maintain pairs <servers service, secret keys>.
	 */
	private HashMap<String,Key> serverKeys;
	
	
	public KerberosManager() throws Exception{
		serverKeys = new HashMap<String,Key>();
		nonces = new HashMap<String, Date>();
		if(SystemUtils.IS_OS_WINDOWS)
			currentKeysFile = System.getProperty("user.dir") + KEYS_FILE_WIN;
		else if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
			currentKeysFile =System.getProperty("user.dir") + KEYS_FILE_LINUX_MAC;
		generateServerKeys();
		Timer timer = new Timer();
		Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    cal.add(Calendar.HOUR_OF_DAY, TICKET_HOUR_DURATION);
		timer.schedule(this, cal.getTime() ,TIME_GARBAGE_NONCE);
	}
	
	/**
	 * Generate Ks Secret key do Services Servers.
	 * @throws Exception
	 */
	public void generateServerKeys() throws Exception{
		BufferedWriter br = new BufferedWriter(new FileWriter(currentKeysFile));
		Key k;
		br.write("SD-STORE ");
		k = Kerberos.generateKerberosKey();
		serverKeys.put("SD-STORE", k);
		br.write(DatatypeConverter.printBase64Binary(k.getEncoded()));
		br.close();
	}
	
	/**
	 * 
	 * @param nonce Nonce to save in server.
	 */
	public void addNonce(String nonce,Date endTime){
		nonces.put(nonce,endTime);
	}
	
	/**
	 * 
	 * @param nonce
	 * @return
	 */
	public boolean nonceIsValid(String nonce){
		if(!nonces.containsKey(nonce))
			return true;
		else{
			Date endTime = nonces.get(nonce);
			if(new Date().after(endTime)){
				return true;
			}
			else{
				return false;
			}
		}
	}
	
	/**
	 * 
	 * @param server
	 * @return Server secure key.
	 */
	public Key getServerKey(String server){
		return serverKeys.get(server);
	}

	@Override
	public void run() {
		for(Entry<String, Date> entry: nonces.entrySet()){
			if(new Date().after(entry.getValue()))
				nonces.remove(entry);
		}
	}
	
}
