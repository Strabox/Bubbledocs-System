package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class LoginUser extends BubbleDocsService {

	private IDRemoteServices id;
	
	private String userToken;
	
	private String username;
	
	private String password;

	
	public LoginUser(String user, String pass) {
		this.username = user;
		this.password = pass;
		id = new IDRemoteServices();
	}

	@Override
	protected void accessControl() {
		// Do Nothing.
		// LoginUser Service dont need previous access to login.
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		Bubbledocs bubble = Bubbledocs.getInstance();
		
		try{	//Trying Remote Login in SD-ID
			id.loginUser(username, password);
		}catch(RemoteInvocationException e){
			try{//Remote failed... Trying a local login...
				bubble.loginUser(username, password);
			}
			catch(BubbleDocsException bube){
				/* Well not now my friend.. not now... */
				throw new UnavailableServiceException();
			}
		}
		//Update User password locally to use in future local logins.
		User user = bubble.getUserByName(username);
		user.setPassword(password);
		//Create/update the session for the user.
		userToken = bubble.createSession(username);
		//SUCCESSFUL LOGIN!!!!
		bubble.removeAllInvalidSessions();
	}

	public final String getUserToken() {
		return userToken;
	}
}
