package sdis.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import sdis.domain.User;

public class UserTest {

	User user1;
	private final String username1 = "user1";
	private final String email1 = "user1";
	
	User user2;
	private final String username2 = "user2";
	private final String email2 = "user2";
	
	@Before
	public void populate4Test(){
		user1 = new User(username1, email1);
		user2 = new User(username2, email2);
	}
	
	@Test
	public void testVerifyPasswordCorrect(){
		String passBefore = user1.getPassword();
		assertTrue(user1.verifyPassword(passBefore));
	}
	
	@Test
	public void testVerifyPasswordIncorrect(){
		String passBefore = user1.getPassword();
		assertFalse(user1.verifyPassword(passBefore+"aaa"));
	}
	
	@Test
	public void testVerifyPasswordNull(){
		assertFalse(user1.verifyPassword(null));
	}
	
	@Test
	public void testVerifyPasswordEmpty(){
		assertFalse(user1.verifyPassword(""));
	}
	
	@Test
	public void testSetNewPassword(){
		String passBefore = user2.getPassword();
		String passAfter = user2.setNewPassword();
		assertFalse(passBefore == passAfter);
	}
}
