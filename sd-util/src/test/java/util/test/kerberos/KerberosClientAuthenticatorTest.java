package util.test.kerberos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.Key;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosClientAuthentication;

public class KerberosClientAuthenticatorTest {

	private final static String CLIENT = "client2";
	
	private static Date beforeDate;
	private static Date afterDate;
	
	private static Key kcs;
	
	private static KerberosClientAuthentication auth;
	
	@Before
	public void cleanBeforeTest() throws KerberosException,
	InterruptedException{
		kcs = Kerberos.generateKerberosKey();
		beforeDate = new Date();
		Thread.sleep(2);
		auth = new KerberosClientAuthentication(CLIENT);
		auth.setDate(new Date());
		Thread.sleep(2);
		afterDate = new Date();
	}
	
	@Test
	public void successSerializeDeserialize() throws KerberosException{
		byte[] byteAuth = auth.serialize(kcs);
		KerberosClientAuthentication auth2;
		auth2 = KerberosClientAuthentication.deserialize(byteAuth, kcs);
		assertEquals(auth.getClient(),auth2.getClient());
		assertTrue(auth2.getRequestTime().getTime() < new Date().getTime());
	}
	

	@Test
	public void nullFieldsSerializeDeserialize() throws KerberosException{
		KerberosClientAuthentication a = new KerberosClientAuthentication(null);
		byte[] byteAuth = a.serialize(kcs);
		a = KerberosClientAuthentication.deserialize(byteAuth, kcs);
	}
	
	@Test
	public void invalidClient(){
		assertFalse(auth.isValid("wrong", new Date()));
	}
	
	@Test
	public void repeatedAuthenticator(){
		assertFalse(auth.isValid(CLIENT, afterDate));
	}
	
	@Test
	public void repeatedAuthenticator2(){
		Date da = new Date();
		auth.setDate(da);
		assertFalse(auth.isValid(CLIENT, da));
	}
	
	@Test
	public void validAuthenticator(){
		assertTrue(auth.isValid(CLIENT, beforeDate));
	}
	
	@Test
	public void validAuthenticatorFirstTimeForClient(){
		assertTrue(auth.isValid(CLIENT, null));
	}
	
	
}
