package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.service.local.LoginUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;


public class LoginUserIntegrator extends BubbleDocsIntegrator {

	
	private LoginUser localLoginUser;
	private IDRemoteServices remote;
	private final String username;
	private final String pass;

	public LoginUserIntegrator(String username,String password){
		localLoginUser = new LoginUser(username, password);
		this.username = username;
		this.pass = password;
		remote = new IDRemoteServices();
	}

	public void execute() throws BubbleDocsException{
		localLoginUser.execute();
		try{
			remote.loginUser(username, pass);
		}catch(RemoteInvocationException e){
			throw e;
		}catch(LoginBubbleDocsException e){
			throw e;
		}
	}

	public final String getUserToken() {
		return localLoginUser.getUserToken();
	}
}
