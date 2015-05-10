package util.test.kerberos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.Key;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosServerAuthentication;

public class KerberosServerAuthenticatorTest {

	private static String NONCE;
	
	private static Key KCS;
	
	private static Key KC;
	
	private static KerberosServerAuthentication auth;
	
	
	@Before
	public void populateTest() throws KerberosException{
		KCS = Kerberos.generateKerberosKey();
		KC = Kerberos.generateKerberosKey();
		NONCE = Kerberos.generateRandomNumber();
		auth = new KerberosServerAuthentication(KCS, NONCE);	
	}
	
	
	@Test
	public void successSerialize() throws KerberosException{
		KerberosServerAuthentication a;
		a = new KerberosServerAuthentication(KCS, NONCE);
		byte[] ser = a.serialize(KC);
		KerberosServerAuthentication a2;
		a2 = KerberosServerAuthentication.deserialize(ser, KC);
		assertEquals(a.getNonce(),NONCE);
		assertTrue(a2.getKcs().equals(KCS));
		assertEquals(a.getNonce(),a2.getNonce());
		assertTrue(a2.getKcs().equals(a.getKcs()));
		byte[] ar = new String("asdasd12jhas").getBytes();
		assertTrue(Arrays.equals(Kerberos.cipherText(a.getKcs(), ar), Kerberos.cipherText(a2.getKcs(), ar)));
	}

	@Test(expected = KerberosException.class)
	public void nullKeyKcs() throws KerberosException{
		KerberosServerAuthentication a;
		a = new KerberosServerAuthentication(null, NONCE);
		a.serialize(KC);
	}
	
	@Test
	public void nullNonce() throws KerberosException{
		KerberosServerAuthentication a;
		a = new KerberosServerAuthentication(KCS, null);
		a.serialize(KC);
	}
	
	@Test(expected = KerberosException.class)
	public void nullKeyKcSerializing() throws KerberosException{
		KerberosServerAuthentication a;
		a = new KerberosServerAuthentication(KCS, NONCE);
		a.serialize(null);
	}
	
	@Test(expected = KerberosException.class)
	public void nullKeyDeserialize() throws KerberosException{
		KerberosServerAuthentication a;
		a = new KerberosServerAuthentication(KCS, NONCE);
		byte[] bytes = a.serialize(KC);
		KerberosServerAuthentication.deserialize(bytes, null);
	}
	
	@Test(expected = KerberosException.class)
	public void nullAuthDeserialize2() throws KerberosException{
		KerberosServerAuthentication.deserialize(null, KC);
	}
	
	@Test
	public void invvalidAuthenticatorNull() throws KerberosException{
		assertFalse(auth.isValid(null));
	}
	
	@Test
	public void validAuthenticator() throws KerberosException{
		assertTrue(auth.isValid(NONCE));
	}
	
	@Test
	public void invalidAuthenticator() throws KerberosException{
		assertFalse(auth.isValid("wrongNonce"));
	}
	
}
