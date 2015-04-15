package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;


public class DeleteUser extends BubbleDocsService {
	
	private static final String root = "root"; 
	
	private String token;
	
	private String deleteUsername;
	
	
    public DeleteUser(String userToken, String toDeleteUsername) {
		this.token = userToken;
		this.deleteUsername = toDeleteUsername;
    }
    
    /* Verifies if give token is equal to root's token.
     * Only root can delete other users. */
    @Override
    protected void accessControl() throws BubbleDocsException{
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	String rootToken = bubble.getUserInSessionToken(root);
    	//User not in session cant execute the command.
    	if(token == null || bubble.getUserFromSession(token) == null ||rootToken == null)
    		throw new UserNotInSessionException();
    	//If the user tokens isnt root's token cant execute command.
		else if(!token.equals(rootToken))
			throw new UnauthorizedOperationException();
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	String token = bubble.getUserInSessionToken(deleteUsername);
    	if(token != null)										//If user in session remove it first.
    		bubble.removeUserFromSession(token);
    	if(bubble.getUserByName(deleteUsername) != null)		//If user exists delete it.
    		bubble.getUserByName(deleteUsername).delete();
    	else													//User doesnt exist.
    		throw new LoginBubbleDocsException();
    }

}
