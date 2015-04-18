package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.CreateUser;


public class CreateUserIntegrator extends BubbleDocsIntegrator {
	
	/* Delegate do CreateUser service. */
	private CreateUser cu;
	
	public CreateUserIntegrator(String token,String username,
			String email, String name){
		cu = new CreateUser(token,username,email,name);
	}
	
	public void execute() throws BubbleDocsException{
		cu.execute();
	}
	
}
