package sdis.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;

public class RequestAuthenticationTest extends SdIdTest{
	
	public RequestAuthenticationTest() throws Exception {
		super();
	}

	private final String user = "alice";
	
	private final String inexistentUser = "Andre21";
	
	private final String pass = "Aaa1";
	
	
	/* Test a successful authentication login. */
	@Test
	public void successLogin() throws Exception{
		byte[] credentials = idClient.requestAuthentication(user,pass.getBytes());
		assertNotNull(credentials);
	}
	
	
	/* Test an inexistent user login. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void inexistentUserAuth() throws Exception{
		idClient.requestAuthentication(inexistentUser,pass.getBytes());
	}
	
	/* Test with null username. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void nullUsername() throws Exception{
		idClient.requestAuthentication(null,pass.getBytes());
	}
	
	/* Test with null password. */ 
	@Test(expected = AuthReqFailed_Exception.class)
	public void nullPassword() throws Exception{
		idClient.requestAuthentication(user,null);
	}
	
	
}
