package pt.tecnico.bubbledocs.test.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.service.dto.UserDTO;
import pt.tecnico.bubbledocs.service.local.GetUserInfoService;
import pt.tecnico.bubbledocs.test.BubbleDocsServiceTest;

public class GetUserInfoServiceTest extends BubbleDocsServiceTest{
	
	private static final String USERNAME_DOESNT_EXIST = "no-one";
	
	private static final String USERNAME = "Andre39";
	private static final String PASSWORD = "heil";
	private static final String NAME =	"Andre Pires";
	
	
	@Override
	public void populate4Test() {
		createUser(USERNAME, PASSWORD, NAME);
	}
	
	@Test
	public void success(){
		GetUserInfoService gis = new GetUserInfoService(USERNAME);
		gis.execute();
		
		UserDTO dto = gis.getUserData();
		User user = getUserFromUsername(USERNAME);
		
		assertTrue(dto.getEmail() == user.getEmail());
		assertTrue(dto.getName() == user.getName());
		assertTrue(dto.getPassword() == user.getPassword());
		assertTrue(dto.getUsername() == user.getUsername());
	}
	
	@Test(expected = LoginBubbleDocsException.class)
	public void usernameDoesntExist(){
		GetUserInfoService gis;
		gis = new GetUserInfoService(USERNAME_DOESNT_EXIST);
		gis.execute();	
	}

}
