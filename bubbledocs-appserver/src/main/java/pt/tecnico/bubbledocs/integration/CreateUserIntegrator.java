package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.local.CreateUserService;
import pt.tecnico.bubbledocs.service.local.DeleteUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIntegrator extends BubbleDocsIntegrator {
	
	/* Delegate do CreateUser service. */
	private CreateUserService createUserService;
	
	/* Used to interact with remote id service. */
	private IDRemoteServices idRemote;
	
	private final String username;
	private final String email;
	private final String token;
	
	public CreateUserIntegrator(String token,String username,
			String email, String name){
		createUserService = new CreateUserService(token,username,email,name);
		idRemote = new IDRemoteServices();
		this.token = token;
		this.username = username;
		this.email = email;
	}
	
	private void createUserRemote(String username,String email)
		throws BubbleDocsException {
		try{
			idRemote.createUser(username, email);
    	}catch(RemoteInvocationException rie){
    		//Problems contacting remote service.
    		throw new UnavailableServiceException();
    	}
	}
	
	public void execute() throws BubbleDocsException{
		createUserService.execute();
		try{
			createUserRemote(username,email);
		}catch(BubbleDocsException e){
			/* Compensation if the remote call fails. */
			DeleteUser du = new DeleteUser(token, username);
			du.execute();
			throw e;
		}
	}
	
}
