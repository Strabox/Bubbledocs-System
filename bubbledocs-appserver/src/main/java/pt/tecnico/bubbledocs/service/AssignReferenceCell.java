package pt.tecnico.bubbledocs.service;

import java.util.ArrayList;
import java.util.Arrays;

import pt.tecnico.bubbledocs.domain.AccessMode;
import pt.tecnico.bubbledocs.domain.Permission;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.NoValueForReferenceException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

// add needed import declarations

public class AssignReferenceCell extends BubbleDocsService {

    private int result;
    private String tokenUser;
    private int sheetID;
    private String cellID;
    private String reference;
    
    public AssignReferenceCell(String _tokenUser, int _docId, String _cellId, String _reference) {
		tokenUser = _tokenUser;
		sheetID = _docId;
		cellID = _cellId;
		reference = _reference;
    }
    
    @Override
    protected void accessControl(){
    	Bubbledocs bubbled = Bubbledocs.getInstance();
    	if(tokenUser == null || bubbled.getUserFromSession(tokenUser) == null)
    		throw new UserNotInSessionException();
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	Bubbledocs bubbled = Bubbledocs.getInstance();
    	SpreadSheet sheet = bubbled.getSpreadSheet(sheetID);
    	User user = bubbled.getUserFromSession(tokenUser);
    	/*
    	 * checking permissions. OK if it's the owner or has writing permissions
    	 * and then if the cell is unprotected.
    	 */
    	/*getting the sheets the user can write on*/
    	ArrayList<SpreadSheet> writable = user.listWritableSpreadSheets();
    	/*
    	 * check if it's the owner or if they have write permissions.
    	 * if denied, throw exception.
    	 */
    	if( sheet.getOwner()!=user && !Arrays.asList(writable).contains(sheet)){
    			throw new UnauthorizedOperationException();
    	}
    	/*
    	 * if the cell exists, it can be protected.
    	 * if it is protected, we can't change its content either.
    	 * should this be done first, for efficiency?
    	 */
    	String[] coords = cellID.split(";");
    	int l = Integer.parseInt(coords[0]);
    	int c = Integer.parseInt(coords[1]);
    	if(sheet.getSingleCell(l,c)!=null && sheet.getSingleCell(l, c).getProtect()){
    		throw new UnauthorizedOperationException();
    	}
    	/*
    	 * Congratulations! you have permissions.
    	 * doing the actual work may still fail if the cell is out of bounds
    	 * or if the referred cell has no value (exceptions in both cases).
    	 */
    	sheet.addContentToCellFromString(l, c,"="+reference);
    	result = sheet.getSingleCell(l, c).getResult();
    }

    public final int getResult() {
        return result;
    }
}
