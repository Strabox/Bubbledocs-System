package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
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
	
	/*needed for compensation*/
	private String usernameToDelete;
	private String name;
	private String email;


	public DeleteUserIntegrator(String userToken, String toDeleteUsername) {
		delUser = new DeleteUser(userToken,toDeleteUsername);
		idRemote = new IDRemoteServices();
		this.token = userToken;
		this.usernameToDelete = toDeleteUsername;
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
		GetUsername4TokenService u4t = new GetUsername4TokenService(token);
		u4t.execute();
		GetUserInfoService Aux = new GetUserInfoService(usernameToDelete);
		try{
			Aux.execute();
		}catch(LoginBubbleDocsException e){
			throw new InvalidUsernameException();
		}
		UserDTO userdto = Aux.getUserData();
		this.email=userdto.getEmail();
		this.name = userdto.getName();
		delUser.execute();
		try{
			deleteUserRemote(usernameToDelete);
		}catch(Exception e){
			/* Compensation if the remote call fails. */
    		new CreateUserService(this.token, this.usernameToDelete, this.email, this.name);
			throw e;
		}
	}

	public String getUsertoDelete(){
		return User.getUsername();
	}

}
