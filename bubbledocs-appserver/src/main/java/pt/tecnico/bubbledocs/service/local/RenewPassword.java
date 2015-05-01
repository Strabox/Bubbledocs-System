package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

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
		User u = Bubbledocs.getInstance().getUserFromSession(tokenUser);
		u.setPassword(null);
	}

}

