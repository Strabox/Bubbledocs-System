package pt.tecnico.bubbledocs.test.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.local.GetUsername4TokenService;
import pt.tecnico.bubbledocs.test.BubbleDocsServiceTest;

public class GetUsername4TokenServiceTest extends BubbleDocsServiceTest{

	private static final String USERNAME = "Andre100";
	
	private static final String PASSWORD = "algo";
	private static final String NAME = "Andre Pires";
	
	private String userToken;
	
	@Override
	public void populate4Test() {
		createUser(USERNAME, PASSWORD, NAME);
		userToken = addUserToSession(USERNAME);
	}
	
	@Test
	public void success(){
		GetUsername4TokenService g4s; 
		g4s = new GetUsername4TokenService(userToken);
		g4s.execute();
	
		assertTrue(USERNAME == g4s.getUsername());
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession(){
		removeUserFromSession(userToken);
		GetUsername4TokenService g4s; 
		g4s = new GetUsername4TokenService(userToken);
		g4s.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void nullToken(){
		GetUsername4TokenService g4s; 
		g4s = new GetUsername4TokenService(null);
		g4s.execute();
	}
	
}
