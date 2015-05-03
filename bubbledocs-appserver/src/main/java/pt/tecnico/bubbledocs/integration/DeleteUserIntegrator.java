package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.dto.UserDTO;
import pt.tecnico.bubbledocs.service.local.CreateUserService;
import pt.tecnico.bubbledocs.service.local.DeleteUser;
import pt.tecnico.bubbledocs.service.local.GetUserInfoService;
import pt.tecnico.bubbledocs.service.local.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;



public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	/* Delegate do CreateUser service. */
	private DeleteUser delUser;
	private GetUsername4TokenService User;
	private String token;

	/* Used to interact with remote id service. */
	private IDRemoteServices idRemote;

	


	public DeleteUserIntegrator(String userToken, String toDeleteUsername) {
		delUser = new DeleteUser(userToken,toDeleteUsername);
		User = new GetUsername4TokenService(userToken);
		idRemote = new IDRemoteServices();
		this.token = userToken;
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
		String username = User.getUsername();
		try{
			deleteUserRemote(username);
		}catch(Exception e){
			/* Compensation if the remote call fails. */
			GetUserInfoService Aux = new GetUserInfoService(username);
    		UserDTO userdto = Aux.getUserData();
			new CreateUserService(this.token, userdto.getUsername(), userdto.getEmail(), userdto.getName());
			throw e;
		}
	}

	public String getUsertoDelete(){
		return User.getUsername();
	}

}
