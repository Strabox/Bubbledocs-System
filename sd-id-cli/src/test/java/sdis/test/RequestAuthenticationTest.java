package sdis.test;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;

public class RequestAuthenticationTest extends SdIdTest{
	
	private static final String user = "Andre20";
	private static final String inexistentUser = "Andre21";
	
	private static final String email = "andre20@SdIsAwesome.ist";
	
	private static final String pass = "pass";
	private static final String wrongPass = "ssap";
	
	@Before
	@Override
	public void populate4Test() {
		try {
			idServer.createUser(user, email);
		} catch (Exception e){}
	}
	
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
			fail();
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
	
	/* */
	@Test(expected = AuthReqFailed_Exception.class)
	public void nullUsername() throws AuthReqFailed_Exception{
		idServer.requestAuthentication(null, objectToBytes(pass));
	}
	
	/* */
	@Test(expected = AuthReqFailed_Exception.class)
	public void nullPassward() throws AuthReqFailed_Exception{
		idServer.requestAuthentication(user, null);
	}
	
	
}
