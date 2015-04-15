package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class CreateUser extends BubbleDocsService {

	private IDRemoteServices idRemote;
	
	private static final String root = "root"; 
	
	private String userToken;
	
	private String newUsername;
	
	private String newUserEmail;
	
	private String newUserIRLName;
	
	
    public CreateUser(String userToken, String newUsername,
            String email, String name) {
    	idRemote = new IDRemoteServices();
		this.userToken 	 = userToken;
		this.newUsername = newUsername;
		this.newUserEmail = email;
		this.newUserIRLName  = name;
    }
    
    
    @Override
    protected void accessControl(){
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	String rootToken  = bubble.getUserInSessionToken(root);
    	if (userToken==null || bubble.getUserFromSession(userToken)==null || rootToken==null)
    		throw new UserNotInSessionException();
		else if(!userToken.equals(rootToken))
			throw new UnauthorizedOperationException();
    	//IMPORTANT!! Resets the user session time.
    	bubble.resetsSessionTime(userToken);
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	Bubbledocs bubble = Bubbledocs.getInstance();
    	try{
    		idRemote.createUser(newUsername, newUserEmail);
    	}catch(RemoteInvocationException rie){
    		//Problems contacting remote service.
    		throw new UnavailableServiceException();
    	}
    	// Remote creation success, add user locally.	
    	bubble.addUser(new User(newUserIRLName,newUsername,newUserEmail));
    }
}