package sdis.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;

/* NOTE: In this tests we assume that server is fresh and running!!!!!*/


/* Test suit for CreateUser service. */
public class CreateUserTest extends SdIdTest {

	private final String username = "Andre69";
	private final String bruno = "bruno";
	private final String username2 = "Andre70";
	private final String username3 = "Andre71";
	private final String invalidUsername = "";
	
	private final String brunoEmail = "bruno@tecnico.pt";
	private final String correctEmail = "andre69@sdIsAwesome.ist"  ;
	private final String correctEmail2 = "hmm@sdIsAwesome.ist";
	
	private final String invalidEmail1 = "andre" ;
	private final String invalidEmail2 = "andre@sdIsAwesome" ;
	private final String invalidEmail3 = "andre.sdIsAwesome" ;
	
	/* Create a user that doesnt exist in the SDID. */
	@Test
	public void createUserSuccess() {
		try{
			idServer.createUser(username, correctEmail);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	/* Try create a existing user. */
	@Test(expected = UserAlreadyExists_Exception.class)
	public void createDuplicateUser() throws UserAlreadyExists_Exception{
		try {
			idServer.createUser(bruno, brunoEmail);
		} catch (EmailAlreadyExists_Exception e) {
			fail();
		} catch (InvalidEmail_Exception e) {
			fail();
		} catch (InvalidUser_Exception e) {
			fail();
		} 
	}
	
	/* Try create a user with invalid username. */
	@Test(expected = InvalidUser_Exception.class)
	public void createUserWithInvalidUsername() throws InvalidUser_Exception{
		try {
			idServer.createUser(invalidUsername, correctEmail2);
		} catch (EmailAlreadyExists_Exception e) {
			fail();
		} catch (InvalidEmail_Exception e) {
			fail();
		} catch (UserAlreadyExists_Exception e) {
			fail();
		}
		
	}
	
	/* Try create a user with invalid username(null). */
	@Test(expected = InvalidUser_Exception.class)
	public void createUserWithNullUsername() throws InvalidUser_Exception{
		try {
			idServer.createUser(null, correctEmail2);
		} catch (EmailAlreadyExists_Exception e) {
			fail();
		} catch (InvalidEmail_Exception e) {
			fail();
		} catch (UserAlreadyExists_Exception e) {
			fail();
		}
		
	}
	
	/* Try create a different user with same email. */
	@Test(expected = EmailAlreadyExists_Exception.class)
	public void createUserWithInvalidEmail() throws EmailAlreadyExists_Exception{
		try {
			idServer.createUser(username2, brunoEmail);
		} catch (InvalidEmail_Exception e) {
			fail();
		} catch (InvalidUser_Exception e) {
			fail();
		} catch (UserAlreadyExists_Exception e) {
			fail();
		}
	}
	
	/* Try create a user with a invalid email. */
	@Test(expected = InvalidEmail_Exception.class)
	public void createUserWithInvalidEmail1() throws InvalidEmail_Exception{
		try {
			idServer.createUser(username3, invalidEmail1);
		} catch (EmailAlreadyExists_Exception e) {
			fail();
		} catch (InvalidUser_Exception e) {
			fail();
		} catch (UserAlreadyExists_Exception e) {
			fail();
		}
	}
	
	/* Try create a user with a invalid email. */
	@Test(expected = InvalidEmail_Exception.class)
	public void createUserWithInvalidEmail2() throws InvalidEmail_Exception{
		try {
			idServer.createUser(username3, invalidEmail2);
		} catch (EmailAlreadyExists_Exception e) {
			fail();
		} catch (InvalidUser_Exception e) {
			fail();
		} catch (UserAlreadyExists_Exception e) {
			fail();
		}
	}
	
	/* Try create a user with a invalid email. */
	@Test(expected = InvalidEmail_Exception.class)
	public void createUserWithInvalidEmail3() throws InvalidEmail_Exception {
		try {
			idServer.createUser(username3, invalidEmail3);
		} catch (EmailAlreadyExists_Exception e) {
			fail();
		} catch (InvalidUser_Exception e) {
			fail();
		} catch (UserAlreadyExists_Exception e) {
			fail();
		}
	}
	
	/* Try create a user with a invalid email (null). */
	@Test(expected = InvalidEmail_Exception.class)
	public void createUserWithNullEmail4() throws InvalidEmail_Exception {
		try {
			idServer.createUser(username3, null);
		} catch (EmailAlreadyExists_Exception e) {
			fail();
		} catch (InvalidUser_Exception e) {
			fail();
		} catch (UserAlreadyExists_Exception e) {
			fail();
		}
	}
	
	
}
