package pt.tecnico.bubbledocs.exceptions;

public class BadCellContentException extends BubbleDocsException {

	private String contentString;
	/**
	 * 
	 */
	private static final long serialVersionUID = 6417412676664839381L;

	public BadCellContentException(String cont){
		this.setContentString(cont);
	}
	
	@Override
	public String toString(){
		return "Can't create content from "+getContentString();
	}

	public String getContentString() {
		return contentString;
	}

	public void setContentString(String cont) {
		this.contentString = cont;
	}
}
