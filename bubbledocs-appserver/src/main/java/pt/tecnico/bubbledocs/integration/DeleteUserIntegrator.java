package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.local.DeleteUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;



public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	/* Delegate do CreateUser service. */
	private DeleteUser delUser;

	/* Used to interact with remote id service. */
	private IDRemoteServices idRemote;

	


	public DeleteUserIntegrator(String userToken, String toDeleteUsername) {
		delUser = new DeleteUser(userToken,toDeleteUsername);
		idRemote = new IDRemoteServices();
	}

	
	private void deleteUserRemote(String username) throws BubbleDocsException {
		try{
			idRemote.removeUser(username);
    	}catch(RemoteInvocationException e){
    		//Problems contacting remote service.
    		throw new UnavailableServiceException();
    	}
	}


	@Override
	public void execute() throws BubbleDocsException {
		delUser.execute();
		String username = delUser.getUsertodelete();
		try{
			deleteUserRemote(username);
		}catch(Exception e){
			/* Compensation if the remote call fails. */
			throw e;
		}
	}

	public String getUsertoDelete(){
		return delUser.getUsertodelete();
	}

}
