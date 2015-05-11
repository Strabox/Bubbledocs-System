package sdis.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class RenewPasswordIT extends SdIdIT {
	
	public RenewPasswordIT() throws Exception {
		super();
	}

	private final String usernameExists1 = "carla";
	private final String usernameDoesntExist = "renewSD";
	
	@Test
	public void renewPasswordTestSuccess() {
		try{
			idClient.renewPassword(usernameExists1);
			// ATTENTION: don't we need to make sure the new password is indeed new?
		}
		catch(Exception e){
			fail();
		}
	}
	
	@Test(expected = UserDoesNotExist_Exception.class)
	public void renewPasswordTestNoUser() throws UserDoesNotExist_Exception{
		idClient.renewPassword(usernameDoesntExist);
	}
	
	@Test(expected = UserDoesNotExist_Exception.class)
	public void renewPasswordTestNullUsername() throws UserDoesNotExist_Exception{
		idClient.renewPassword(null);
	}
	
	@Test(expected = UserDoesNotExist_Exception.class)
	public void renewPasswordTestEmptyUsername() throws UserDoesNotExist_Exception{
		idClient.renewPassword("");
	}
}
