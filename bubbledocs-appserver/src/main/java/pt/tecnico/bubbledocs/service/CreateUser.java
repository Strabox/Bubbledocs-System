package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;


public class CreateUser extends BubbleDocsService {

	private static final String root = "root"; 
	
	private String UserToken;
	
	private String newUserUsername;
	
	private String newUserPassword;
	
	private String newUserIRLName;
	
	
    public CreateUser(String userToken, String newUsername,
            String password, String name) {
		this.UserToken 	 = userToken;
		this.newUserUsername = newUsername;
		this.newUserPassword = password;
		this.newUserIRLName  = name;
    }
    
    @Override
    protected void accessControl(){
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	String rootToken  = bubble.getUserInSessionToken(root);
    	if (UserToken==null || bubble.getUserFromSession(UserToken)==null || rootToken==null)
    		throw new UserNotInSessionException();
		else if(!UserToken.equals(rootToken))
			throw new UnauthorizedOperationException();
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	
    	bubble.addUser(new User(newUserIRLName,newUserUsername,newUserPassword));
    }
}