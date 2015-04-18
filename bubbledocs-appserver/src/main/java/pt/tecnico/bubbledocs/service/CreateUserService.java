package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;


public class CreateUserService extends BubbleDocsService {
	
	private static final String root = "root"; 
	
	private String userToken;
	
	private String newUsername;
	
	private String newUserEmail;
	
	private String newUserIRLName;
	
	
    public CreateUserService(String userToken, String newUsername,
            String email, String name) {
		this.userToken 	 = userToken;
		this.newUsername = newUsername;
		this.newUserEmail = email;
		this.newUserIRLName  = name;
    }
    
    
    @Override
    protected void accessControl(){
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	String rootToken  = bubble.getUserInSessionToken(root);
    	if (userToken == null 
    	   || bubble.getUserFromSession(userToken) == null 
    	   || rootToken==null)
    		throw new UserNotInSessionException();
		else if(!userToken.equals(rootToken))
			throw new UnauthorizedOperationException();
    	//IMPORTANT!! Resets the user session time.
    	bubble.resetsSessionTime(userToken);
    } 
    
    @Override
    protected void dispatch() throws BubbleDocsException,
    		UnavailableServiceException {
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	// Remote creation success, add user locally.	
    	bubble.addUser(new User(newUserIRLName,newUsername,newUserEmail));
    }
}