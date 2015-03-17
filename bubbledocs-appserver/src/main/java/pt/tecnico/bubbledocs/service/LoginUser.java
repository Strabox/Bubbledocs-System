package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;


public class LoginUser extends BubbleDocsService {

	private String userToken;
	
	private String username;
	
	private String password;

	
	public LoginUser(String user, String pass) {
		this.username = user;
		this.password = pass;
	}

	@Override
	protected void accessControl() {
		// Do Nothing.
		// LoginUser Service dont need previous access to login.
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		Bubbledocs bubble = Bubbledocs.getInstance();
		
		userToken = bubble.loginUser(username, password);
	}

	public final String getUserToken() {
		return userToken;
	}
}
