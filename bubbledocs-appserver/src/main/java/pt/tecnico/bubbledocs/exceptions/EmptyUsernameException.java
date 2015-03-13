package pt.tecnico.bubbledocs.exceptions;

public class EmptyUsernameException extends BubbleDocsException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6254012960065830970L;

	
	public EmptyUsernameException(){
		super();
	}
	
	@Override
	public String toString(){
		return "Username vazio!!!";
	}
}
