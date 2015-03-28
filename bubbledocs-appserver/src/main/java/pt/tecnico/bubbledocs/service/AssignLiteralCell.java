package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

/*
 * Service assign a integer constant to a cell.
 */
public class AssignLiteralCell extends BubbleDocsService {
	
    private String result;
    
    private Bubbledocs bubble;
    
    private String token;
    
    private int docId;
    
    private String cellId;
    
    private String literal;
    
    public AssignLiteralCell(String tokenUser, int docId, String cellId,
            String literal) {
    	this.token = tokenUser;
    	this.docId = docId;
    	this.cellId = cellId;
    	this.literal = literal;
    }
    
    @Override
    protected void accessControl(){
    	bubble = Bubbledocs.getInstance();
    	User user = bubble.getUserFromSession(token);
    	if(token == null || user == null)
    		throw new UserNotInSessionException();
    	
    	
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	

    	
    }

    public String getResult() {
        return result;
    }

}
