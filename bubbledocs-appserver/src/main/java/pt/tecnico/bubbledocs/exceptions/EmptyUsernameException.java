package pt.tecnico.bubbledocs.exceptions;

public class EmptyUsernameException extends BubbledocsException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmptyUsernameException(){
		super();
	}
	
	@Override
	public String toString(){
		return "Username vazio!!!";
	}
}
