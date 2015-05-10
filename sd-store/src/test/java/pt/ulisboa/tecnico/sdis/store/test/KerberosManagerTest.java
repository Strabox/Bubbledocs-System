package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.*;

import javax.crypto.SecretKey;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.impl.kerberos.KerberosManager;
import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;

public class KerberosManagerTest {

	private static final String SERVICE_ID = "Service";
	private static final String MAC_TEXT = "sad<<<sdas>as1231 sd2dA";
	private static final String WRONG_MAC_TEXT = "sbd<<<sdas>as1231 sd2dA";
	
	private static SecretKey macKey;
	
	private static KerberosManager manager;
	
	@BeforeClass
	public static void init() throws Exception{
		macKey = (SecretKey) Kerberos.generateKerberosKey();
		manager = new KerberosManager(SERVICE_ID);
	}
	
	@Test
	public void validMac() throws KerberosException{
		byte[] mac = Kerberos.makeMAC(MAC_TEXT.getBytes(), macKey);
		assertTrue(manager.verifyMAC(mac, MAC_TEXT.getBytes(), macKey));
	}
	
	@Test
	public void invalidMac() throws KerberosException{
		byte[] mac = Kerberos.makeMAC(MAC_TEXT.getBytes(), macKey);
		assertFalse(manager.verifyMAC(mac, WRONG_MAC_TEXT.getBytes(), macKey));
	}
}
