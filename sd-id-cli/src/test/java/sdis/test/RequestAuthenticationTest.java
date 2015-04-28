package sdis.test;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;

public class RequestAuthenticationTest extends SdIdTest{
	
	private final String user = "alice";
	private final String inexistentUser = "Andre21";
	
	private final String pass = "Aaa1";
	private final String wrongPass = "wrong";
	
	/* Test a successful login. */
	@Test
	public void successLogin(){
		try{
			boolean login = false;
			byte[] ans = idServer.requestAuthentication(user, objectToBytes(pass));
			if(ans[0] == 1){
				login = true;
			}
			Assert.assertTrue(login);
		}
		catch(AuthReqFailed_Exception e){
			fail(e.toString());
		}
	}
	
	/* Test user login with wrong password. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void wrongPassword() throws AuthReqFailed_Exception{
		idServer.requestAuthentication(user, objectToBytes(wrongPass));
	}
	
	/* Test an inexistent user login. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void inexistentUserAuth() throws AuthReqFailed_Exception{
		idServer.requestAuthentication(inexistentUser, objectToBytes(pass));
	}
	
	/* Test with null username. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void nullUsername() throws AuthReqFailed_Exception{
		idServer.requestAuthentication(null, objectToBytes(pass));
	}
	
	/* Test with null password. */
	@Test(expected = AuthReqFailed_Exception.class)
	public void nullPassward() throws AuthReqFailed_Exception{
		idServer.requestAuthentication(user, null);
	}
	
	
}
