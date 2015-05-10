package util.test.kerberos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Key;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosCredential;
import util.kerberos.messages.KerberosTicket;

public class KerberosCredentialTest {

	private static final String CLIENT = "Client";
	private static final String SERVER = "server";
	
	private static Key kcs;
	private static Key ks;
	
	private static byte[] ticket;
	
	@BeforeClass
	public static void create() throws KerberosException{
		kcs = Kerberos.generateKerberosKey();
		ks = Kerberos.generateKerberosKey();
		ticket = new KerberosTicket(CLIENT, SERVER,1, kcs).serialize(ks);
	}
	
	/**
	 * Success Serialize and Deserialize.
	 */
	@Test
	public void successSerialize() throws KerberosException{
		KerberosCredential cred = new KerberosCredential(CLIENT, ticket, kcs);
		byte[] credByte = cred.serialize();
		KerberosCredential cred2 = KerberosCredential.deserialize(credByte);
		assertEquals(cred.getKcs(),cred2.getKcs());
		assertEquals(cred.getClient(),cred2.getClient());
		assertTrue(Arrays.equals(cred2.getTicket(), ticket));
		assertTrue(Arrays.equals(cred.getTicket(), cred2.getTicket()));
	}
	
	/**
	 * Try Deserialize null.
	 */
	@Test(expected = KerberosException.class)
	public void nullDeserialize() throws KerberosException{
		KerberosCredential.deserialize(null);
	}
	
}
