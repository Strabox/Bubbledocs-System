package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class DeleteUser extends BubbleDocsService {
	
	private static final String root = "root";
	
	private String token;
	private String usertodelete;
	
	public DeleteUser(String userToken, String toDeleteUsername) {
		this.token = userToken;
		this.usertodelete = toDeleteUsername;
    }
    
    public String getUsertodelete() {
		return usertodelete;
	}

	/* Verifies if give token is equal to root's token.
     * Only root can delete other users. */
    @Override
    protected void accessControl() throws BubbleDocsException{
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	String rootToken = bubble.getUserInSessionToken(root);
    	//User not in session cant execute the command.
    	if(token == null || bubble.getUserFromSession(token) == null || rootToken == null)
    		throw new UserNotInSessionException();
    	//If the user tokens isnt root's token cant execute command.
		else if(!token.equals(rootToken))
			throw new UnauthorizedOperationException();
    	//IMPORTANT!! Resets the user session time.
		bubble.resetsSessionTime(token);
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	bubble.delete(usertodelete);
    }

}
