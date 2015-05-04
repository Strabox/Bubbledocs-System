package sdis.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class RemoveUserTest extends SdIdTest {
	
	private final String ExistingUsername = "duarte";
	private final String MissingUsername = "Adama";
	
	/* Remove a user that does exist in the SDID. */
	@Test
	public void removeExistingUser() {
		try{
			idServer.removeUser(ExistingUsername);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}

	// Remove a user that does not exist in the SDID 
	@Test(expected = UserDoesNotExist_Exception.class)
	public void removeMissingUser() throws UserDoesNotExist_Exception{
			idServer.removeUser(MissingUsername);
	}
	
	// Remove a user that does not exist in the SDID 
	@Test(expected = UserDoesNotExist_Exception.class)
	public void removeNullUser() throws UserDoesNotExist_Exception{
			idServer.removeUser(null);
	}
}