package sdis.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.security.Key;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosReply;
import util.kerberos.messages.KerberosRequest;
import util.kerberos.messages.KerberosServerAuthentication;

public class RequestAuthenticationTest extends SdIdTest{
	
	private final String user = "alice";
	private final String inexistentUser = "Andre21";
	
	private final String pass = "Aaa1";
	/* Test a successful authentication login. */
	@Test
	public void successLogin(){
		try{
			String nonce = Kerberos.generateRandomNumber();
			KerberosRequest req = new KerberosRequest(1,nonce);
			byte[] ans = idServer.requestAuthentication(user, req.serialize());
			KerberosReply re = KerberosReply.deserialize(ans);
			Key k = Kerberos.getKeyFromBytes(Kerberos.digestPassword(pass, "MD5"));
			KerberosServerAuthentication a;
			a = KerberosServerAuthentication.deserialize(re.getAuthentication(), k);
			
			assertEquals(a.getNonce(),nonce);
			assertNotNull(re.getTicket());
		}catch(Exception e){
			fail(e.toString());
		}
	}
	
	@Test
	public void failLoginDuplicateNonce(){
		try{
			String nonce = Kerberos.generateRandomNumber();
			KerberosRequest req = new KerberosRequest(1,nonce);
			idServer.requestAuthentication(user, req.serialize());
			idServer.requestAuthentication(user, req.serialize());
		}catch(AuthReqFailed_Exception e){
			//Success
		}
		catch(Exception e){
			fail("Login Error");
		}
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
