package pt.tecnico.bubbledocs.exceptions;

public class UsernameAlreadyTakenException extends IllegalArgumentException{

	private String username;
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsernameAlreadyTakenException(String u){
		this.setUsername(u);
	}
	
	@Override
	public String toString(){
		return "O utilizador "+getUsername()+" já existe!!";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
