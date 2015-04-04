package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
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
		//chamar metodo renew do remote
		//tentar mudar a password local
		//fear the flying killer pigs!
	}

}
