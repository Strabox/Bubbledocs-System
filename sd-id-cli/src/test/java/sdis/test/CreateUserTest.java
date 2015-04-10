package sdis.test;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;

/* NOTE: In this tests we assume that server is empty
 and running!!!!!*/


/* Test suit for CreateUser service. */
public class CreateUserTest extends SdIdTest {

	private static final String username = "Andre69";
	private static final String username2 = "Andre70";
	private static final String username3 = "Andre71";
	
	private static final String correctEmail = "andre@sdIsAwesome.pt"  ;
	
	private static final String invalidEmail1 = "andre" ;
	private static final String invalidEmail2 = "andre@sdIsAwesome" ;
	private static final String invalidEmail3 = "andre.sdIsAwesome" ;
	
	/* Create a user that doesnt exist in the SDID. */
	@Test
	public void createUserSuccess() throws EmailAlreadyExists_Exception,
	InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		idServer.createUser(username, correctEmail);
		// Nothing should happen
	}
	
	/* Try create the same user. */
	@Test(expected = UserAlreadyExists_Exception.class)
	public void createDuplicateUser() throws UserAlreadyExists_Exception,
	EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception{
		idServer.createUser(username, correctEmail);
	}
	
	/* Try create a differente user with same email. */
	@Test(expected = EmailAlreadyExists_Exception.class)
	public void createUserWithInvalidEmail() throws EmailAlreadyExists_Exception,
	InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		idServer.createUser(username2, correctEmail);
	}
	
	/* */
	@Test(expected = InvalidEmail_Exception.class)
	public void createUserWithInvalidEmail1() throws EmailAlreadyExists_Exception,
	InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		idServer.createUser(username3, invalidEmail1);
	}
	
	/* */
	@Test(expected = InvalidEmail_Exception.class)
	public void createUserWithInvalidEmail2() throws EmailAlreadyExists_Exception,
	InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		idServer.createUser(username3, invalidEmail2);
	}
	
	/* */
	@Test(expected = InvalidEmail_Exception.class)
	public void createUserWithInvalidEmail3() throws EmailAlreadyExists_Exception,
	InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		idServer.createUser(username3, invalidEmail3);
	}
	
	
}
