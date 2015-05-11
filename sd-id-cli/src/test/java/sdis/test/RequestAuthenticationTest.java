package sdis.test;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosRequest;

public class RequestAuthenticationTest extends SdIdTest{
	
	public RequestAuthenticationTest() throws Exception {
		super();
	}
	
	private static final String SERVICE = "SD-STORE";
	
	private static final String USER = "alice";
	
	private static final String INEXISTENT_USER = "Andre21";
	
	private static final String PASS = "Aaa1";
	private static final String WRONG_PASS = "wrongPass";
	
	private static KerberosRequest request;
	
	@BeforeClass
	public static void before() throws KerberosException{
		request = new KerberosRequest("wrongServer", Kerberos.generateRandomNumber());
	}
	
	
	/* Test a successful authentication login. */
	@Test
	public void successLogin() throws Exception{
		byte[] credentials = idClient.requestAuthentication(USER,PASS.getBytes());
		assertNotNull(credentials);
	}
	
	
	/* Test an inexistent user login. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void inexistentUserAuth() throws Exception{
		idClient.requestAuthentication(INEXISTENT_USER,PASS.getBytes());
	}
	
	/* Test with null username. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void nullUsername() throws Exception{
		idClient.requestAuthentication(null,PASS.getBytes());
	}
	
	/* Test with wrong password. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void wrongPassword() throws AuthReqFailed_Exception{
		idClient.requestAuthentication(USER, WRONG_PASS.getBytes());
	}
	
	/* Test with null password. */ 
	@Test(expected = AuthReqFailed_Exception.class)
	public void nullPassword() throws Exception{
		idClient.requestAuthentication(USER,null);
	}
	
	/*========= TEST DIRECT TO SERVER INTERFACE WITHOUT CLIENT CLASS ======*/
	/* Trying blow up server with wrong and bad formed request (XML) */
	
	/* Try a request to an inexistent server. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void inexistentServer() throws AuthReqFailed_Exception, KerberosException{
		idRemote.requestAuthentication(USER, request.serialize());
	}
	
	/* Try sending random bytes in reserved. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void randomReservedCredentials() 
	throws KerberosException, AuthReqFailed_Exception{
		String random = Kerberos.generateRandomNumber();
		idRemote.requestAuthentication(USER, random.getBytes());
	}
	
	/* Send null server in request.*/
	@Test(expected = AuthReqFailed_Exception.class)
	public void nullServer() throws AuthReqFailed_Exception, KerberosException{
		KerberosRequest req; 
		req = new KerberosRequest(null, Kerberos.generateRandomNumber());
		idRemote.requestAuthentication(USER, req.serialize());
	}
	
	/* Send null nonce in request */
	@Test(expected = AuthReqFailed_Exception.class)
	public void nullNonce() throws AuthReqFailed_Exception, KerberosException{
		KerberosRequest req; 
		req = new KerberosRequest(SERVICE, null);
		idRemote.requestAuthentication(USER, req.serialize());
	}
}
