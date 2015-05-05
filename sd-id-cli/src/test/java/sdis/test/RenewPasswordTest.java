package sdis.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class RenewPasswordTest extends SdIdTest {
	
	public RenewPasswordTest() throws Exception {
		super();
	}

	private final String usernameExists1 = "carla";
	private final String usernameDoesntExist = "renewSD";
	
	@Test
	public void renewPasswordTestSuccess() {
		try{
			idServer.renewPassword(usernameExists1);
			// ATTENTION: don't we need to make sure the new password is indeed new?
		}
		catch(Exception e){
			fail();
		}
	}
	
	@Test(expected = UserDoesNotExist_Exception.class)
	public void renewPasswordTestNoUser() throws UserDoesNotExist_Exception{
		idServer.renewPassword(usernameDoesntExist);
	}
	
	@Test(expected = UserDoesNotExist_Exception.class)
	public void renewPasswordTestNullUsername() throws UserDoesNotExist_Exception{
		idServer.renewPassword(null);
	}
	
	@Test(expected = UserDoesNotExist_Exception.class)
	public void renewPasswordTestEmptyUsername() throws UserDoesNotExist_Exception{
		idServer.renewPassword("");
	}
}
