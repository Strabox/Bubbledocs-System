package pt.tecnico.bubbledocs.service;

import java.util.ArrayList;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.SpreadSheetDoesNotExistException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

/*
 * Service assign a integer constant to a cell.
 */
public class AssignLiteralCell extends BubbleDocsService {
	
    private int result;
    
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
    	boolean canWrite = false;
    	bubble = Bubbledocs.getInstance();
    	User user = bubble.getUserFromSession(token);
    	SpreadSheet sheet = bubble.getSpreadSheet(docId);
    	
    	if(token == null || user == null)
    		throw new UserNotInSessionException();
    	
    	if(sheet == null)
    		throw new SpreadSheetDoesNotExistException();
    	
    	ArrayList<SpreadSheet> writable = user.listWritableSpreadSheets();
    	for(SpreadSheet s : writable){
    		if (s == sheet) 
    			canWrite = true;
    	}
    	if(!canWrite || !(sheet.getOwner() == user))
    		throw new UnauthorizedOperationException();
    	
    }
    
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	SpreadSheet sheet = bubble.getSpreadSheet(docId); 
    	String[] coords = cellId.split(";");
    	int l = Integer.parseInt(coords[0]);
    	int c = Integer.parseInt(coords[1]);	
    	int Literal;
    	Literal cont;
    	
    	if(sheet.getSingleCell(l,c) != null && sheet.getSingleCell(l, c).getProtect()){
    		throw new UnauthorizedOperationException();
    	}
    	
    	Literal = Integer.parseInt(literal);
    	
    	cont = new Literal(Literal);
    	sheet.addContentToCell(l, c, cont);
    	result = cont.getResult();
    }

    public final int getResult() {
        return result;
    }

}
