package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;

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

		try{//Trying a local login...
			bubble.loginUser(username, password);
		}
		catch(BubbleDocsException bube){
			/* Well not now my friend.. not now... */
			throw new UnavailableServiceException();
		}
	}

	public final String getUserToken() {
		return userToken;
	}

	public void updatePassLocaly(String username, String password) {
		Bubbledocs bubble = Bubbledocs.getInstance();
		//Update User password locally to use in future local logins.
		User user = bubble.getUserByName(username);
		user.setPassword(password);
		//Create/update the session for the user.
		userToken = bubble.createSession(username);
		//SUCCESSFUL LOGIN!!!!
		bubble.removeAllInvalidSessions();
	}
	
}
