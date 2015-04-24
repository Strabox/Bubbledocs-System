package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.service.dto.UserDTO;

public class GetUserInfoService extends BubbleDocsService{

	private UserDTO dto;
	
	private String username;
	
	
	public GetUserInfoService(String username) {
		this.username = username;
	}
	
	@Override
	protected void accessControl() throws BubbleDocsException {
		// Empty - This service doest need permissions.
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		User user = Bubbledocs.getInstance().getUserByName(username);
		if(user == null)	// FIXME verifiy if this condition should be done in domain.
			throw new LoginBubbleDocsException();
		dto = new UserDTO(user);
	}
	
	public final UserDTO getUserData(){
		return dto;
	}

}
