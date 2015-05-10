package util.test.kerberos;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.Key;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosReply;
import util.kerberos.messages.KerberosServerAuthentication;
import util.kerberos.messages.KerberosTicket;

public class KerberosReplyTest {

	private static final String CLIENT = "Andre10";
	private static final String SERVER = "SD-AWESOME";
	private static final int HOUR_DURATION = 2;
	
	private static Key kcs;
	
	private static Key ks;
	
	private static Key kc;
	
	private static String nonce;
	
	private static KerberosTicket ticket;
	
	private static KerberosServerAuthentication auth;
	
	@BeforeClass
	public static void create() throws KerberosException{
		nonce = Kerberos.generateRandomNumber();
		kcs = Kerberos.generateKerberosKey();
		kc = Kerberos.generateKerberosKey();
		ks = Kerberos.generateKerberosKey();
		ticket = new KerberosTicket(CLIENT,SERVER,HOUR_DURATION, kcs);
		auth = new KerberosServerAuthentication(kcs, nonce);
	}
	
	@Test
	public void successSerialzeDeserialize() throws KerberosException{
		byte[] t = ticket.serialize(ks);
		byte[] a = auth.serialize(kc);
		KerberosReply reply;
		KerberosReply reply2;
		reply = new KerberosReply(t, a);
		byte[] re = reply.serialize();
		assertNotNull(re);
		reply2 = KerberosReply.deserialize(re);
		assertTrue(Arrays.equals(t, reply2.getTicket()));
		assertTrue(Arrays.equals(a, reply2.getAuthentication()));
	}
	
	
}
