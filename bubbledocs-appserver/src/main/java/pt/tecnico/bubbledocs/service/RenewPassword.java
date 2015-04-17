package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPassword extends BubbleDocsService {
	
	//private IDRemoteServices idRemote;
	private String tokenUser;
	
	public RenewPassword(String tokenUser){
		this.tokenUser = tokenUser;
	}
	
	@Override
	protected void accessControl() throws BubbleDocsException {
		Bubbledocs bubbled = Bubbledocs.getInstance();
		if(tokenUser == null || bubbled.getUserFromSession(tokenUser) == null)
			throw new UserNotInSessionException();
		//IMPORTANT!! Resets the user session time.
		bubbled.resetsSessionTime(tokenUser);
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		try{
			IDRemoteServices idrs = new IDRemoteServices();
			User u = Bubbledocs.getInstance().getUserFromSession(tokenUser);
			idrs.renewPassword(u.getUsername());
			u.setPassword(null);
		}
		catch(LoginBubbleDocsException e){
			throw e;
		}
		catch(RemoteInvocationException e){
			throw new UnavailableServiceException();
		}

	}

}
