package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.service.local.LoginUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


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
		
		try{
			remote.loginUser(username, pass);//remote login
			localLoginUser.updatePassLocaly(username, pass);
		}
		catch(RemoteInvocationException rie){ //remoto falhou, tentar local
			localLoginUser.execute();//local login
		}
	}

	public final String getUserToken() {
		return localLoginUser.getUserToken();
	}
}
