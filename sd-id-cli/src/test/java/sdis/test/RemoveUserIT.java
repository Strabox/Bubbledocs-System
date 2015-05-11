package sdis.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class RemoveUserIT extends SdIdIT {
	
	public RemoveUserIT() throws Exception {
		super();
	}

	private final String ExistingUsername = "duarte";
	private final String MissingUsername = "Adama";
	
	/* Remove a user that does exist in the SDID. */
	@Test
	public void removeExistingUser() {
		try{
			idClient.removeUser(ExistingUsername);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}

	// Remove a user that does not exist in the SDID 
	@Test(expected = UserDoesNotExist_Exception.class)
	public void removeMissingUser() throws UserDoesNotExist_Exception{
		idClient.removeUser(MissingUsername);
	}
	
	// Remove a user that does not exist in the SDID 
	@Test(expected = UserDoesNotExist_Exception.class)
	public void removeNullUser() throws UserDoesNotExist_Exception{
		idClient.removeUser(null);
	}
}