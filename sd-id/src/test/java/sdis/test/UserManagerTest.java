package sdis.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;
import sdis.domain.User;
import sdis.domain.UserManager;

public class UserManagerTest {

	UserManager manager;
	
	User user1;
	private final String username1 = "user1";
	private final String email1 = "user1@sd.pt";
	
	User user2;
	private final String username2 = "user2";
	private final String email2 = "user2@sd.pt";
	
	private final String invalidEmail1 = "invalid1" ;
	private final String invalidEmail2 = "invalid2@sdIsAwesome" ;
	private final String invalidEmail3 = "invalid3.sdIsAwesome" ;
	
	@Before
	public void populate4Test() throws Exception{
		manager = new UserManager();
		user1 = new User(username1, email1);
		user2 = new User(username2, email2);
		manager.addUser(user1);
	}
	/*
	 * These tests also verify all the other methods of the manager (excluiding removeUser)
	 * Those methods won't be tested by themselves.
	 */
	@Test(expected=UserAlreadyExists_Exception.class)
	public void testAddUserNameAlreadyExists() throws Exception{
		User usernameAlreadyExists = new User(username1,"namealready@sd.pt");
		manager.addUser(usernameAlreadyExists);
	}
	
	@Test(expected = EmailAlreadyExists_Exception.class)
	public void testAddUserMailAlreadyExists() throws Exception{
		User emailAlreadyExists = new User("mailalready",email1);
		manager.addUser(emailAlreadyExists);
	}
	
	@Test(expected=InvalidEmail_Exception.class)
	public void testAddUserInvalidEmail1() throws Exception{
		User userInvalidEmail1 = new User("invalidemail1",invalidEmail1);
		manager.addUser(userInvalidEmail1);
	}
	
	@Test(expected=InvalidEmail_Exception.class)
	public void testAddUserInvalidEmail2() throws Exception{
		User userInvalidEmail2 = new User("invalidemail2",invalidEmail2);
		manager.addUser(userInvalidEmail2);
	}
	
	@Test(expected=InvalidEmail_Exception.class)
	public void testAddUserInvalidEmail3() throws Exception{
		User userInvalidEmail3 = new User("invalidemail3",invalidEmail3);
		manager.addUser(userInvalidEmail3);
	}
	
	@Test(expected=InvalidEmail_Exception.class)
	public void testAddUserInvalidEmail4() throws Exception{
		User userInvalidEmail4 = new User("invalidemail3",null);
		manager.addUser(userInvalidEmail4);
	}
	
	@Test(expected=InvalidUser_Exception.class)
	public void testAddUserNullUsername() throws Exception{
		User userInvalidName1 = new User(null,"nullusername1@sd.pt");
		manager.addUser(userInvalidName1);
	}
	
	@Test(expected=InvalidUser_Exception.class)
	public void testAddUserEmptyUsername() throws Exception{
		User userInvalidName2 = new User("","nullusername2@sd.pt");
		manager.addUser(userInvalidName2);
	}
	
	@Test
	public void testRemoveUserExists() throws Exception{
		String usernameToDelete = "userToDeleteExists";
		User userExistsToDelete = new User(usernameToDelete,"userdelete1@sd.pt");
		manager.addUser(userExistsToDelete);
		manager.removeUser(usernameToDelete);
	}
	
	@Test
	public void testRemoveUserDoesNotExist() throws Exception{
		String usernameToDelete = "userToDeleteDoesntExist";
		manager.removeUser(usernameToDelete);
	}

}
