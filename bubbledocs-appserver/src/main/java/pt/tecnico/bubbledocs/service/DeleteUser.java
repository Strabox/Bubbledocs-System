package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
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
    protected void accessControl(){
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	String rootToken = bubble.getUserInSessionToken(root);
    	if(!rootToken.equals(token))
    		throw new UserNotInSessionException();
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	String token = bubble.getUserInSessionToken(deleteUsername);
    	if(token != null)
    		bubble.removeUserFromSession(token);
    	bubble.getUserByName(deleteUsername).delete();
    }

}
