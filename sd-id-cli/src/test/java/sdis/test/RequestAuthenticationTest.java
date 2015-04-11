package sdis.test;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;

public class RequestAuthenticationTest extends SdIdTest{
	
	private static final String user = "Andre20";
	private static final String inexistenUser = "Andre21";
	
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
			boolean login = (Boolean) bytesToObject(idServer.requestAuthentication(user, objectToBytes(pass)));
			Assert.assertTrue(login);
		}
		catch(AuthReqFailed_Exception e){
			fail();
		}
	}
	
	/* Test user login with wrong password. */
	@Test
	public void wrongPassword() throws AuthReqFailed_Exception{
		try{
			boolean login = (Boolean) bytesToObject(idServer.requestAuthentication(user, objectToBytes(wrongPass)));
			Assert.assertFalse(login);
		}
		catch(AuthReqFailed_Exception e){
			fail();
		}
	}
	
	/* Test an inexistent user login. */
	@Test
	public void inexistentUserAuth(){
		try{
			boolean login = (Boolean) bytesToObject(idServer.requestAuthentication(inexistenUser, objectToBytes(pass)));
			Assert.assertFalse(login);
		}
		catch(AuthReqFailed_Exception e){
			fail();
		}
	}
	
}
