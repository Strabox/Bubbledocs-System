package pt.tecnico.bubbledocs.exceptions;

public class DuplicateUsernameException extends BubbleDocsException{

	private String username;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5864091120608617698L;
	
	public DuplicateUsernameException(String u){
		this.setUsername(u);
	}
	
	@Override
	public String toString(){
		return "The user "+getUsername()+" already exists!!!!";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
