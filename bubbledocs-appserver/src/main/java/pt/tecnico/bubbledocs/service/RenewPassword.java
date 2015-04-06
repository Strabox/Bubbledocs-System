package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPassword extends BubbleDocsService {
	
	private IDRemoteServices idRemote;
	private String tokenUser;
	
	public RenewPassword(String tokenUser){
		this.tokenUser = tokenUser;
	}
	
	@Override
	protected void accessControl() throws BubbleDocsException {
		Bubbledocs bubbled = Bubbledocs.getInstance();
		if(tokenUser == null || bubbled.getUserFromSession(tokenUser) == null)
			throw new UserNotInSessionException();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		// TODO Auto-generated method stub
		try{
			IDRemoteServices idrs = new IDRemoteServices();
			idrs.renewPassword(Bubbledocs.getInstance().getUserFromSession(tokenUser).getUsername());
			//password should be printed?
			//revoke local password
		}
		catch(LoginBubbleDocsException e){
			System.out.println("login problem on renewing password");
			throw e;
		}
		catch(RemoteInvocationException e){
			System.out.println("remove invocation problem on renewing password");
			throw e;
			//MAKE THIS BETTER: there should be a different exception, specific to a higher layer.
		}

	}

}
