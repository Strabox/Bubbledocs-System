package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class GetUsername4TokenService extends BubbleDocsService{

	private String username;
	
	private String token;
	
	
	public GetUsername4TokenService(String token) {
		this.token = token;
	}
	
	@Override
	protected void accessControl() throws BubbleDocsException {
		Bubbledocs bubble = Bubbledocs.getInstance();
		if(token == null || 
		   bubble.getUserFromSession(token) == null)
			throw new UserNotInSessionException();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		Bubbledocs bubble = Bubbledocs.getInstance(); 
		username = bubble.getUserFromSession(token).getUsername();
	}
	
	public final String getUsername(){
		return username;
	}

}
