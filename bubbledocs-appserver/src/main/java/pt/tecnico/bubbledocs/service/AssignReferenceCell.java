package pt.tecnico.bubbledocs.service;

import java.util.ArrayList;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BadSpreadSheetValuesException;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.SpreadSheetNotFoundException;
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
    	
    	User user = bubbled.getUserFromSession(tokenUser);
    	SpreadSheet sheet = bubbled.getSpreadSheet(sheetID);
    	if (sheet == null) throw new SpreadSheetNotFoundException();
    	/*
    	 * OK if it's the owner or has writing permissions
    	 * and then if the cell is unprotected.
    	 */
    	boolean hasWritePermissions = false;
    	ArrayList<SpreadSheet> writable = user.listWritableSpreadSheets();
    	/* 
    	 * getting the sheets the user can write on and checking if the sheet
    	 * is there
    	 */
    	for(SpreadSheet ss : writable){
    		if (ss==sheet) hasWritePermissions = true;
    	}
    	if( !(sheet.getOwner()==user || hasWritePermissions)){
			throw new UnauthorizedOperationException();
    	}
    	/*
    	 * if the cell exists, it can be protected.
    	 * if it is protected, we can't change its content either.
    	 * should this be done first, for efficiency?
    	 */
    	String[] coords = cellID.split(";");
    	if(coords.length!=2) throw new BadSpreadSheetValuesException();
    	int l, c;
    	try{
    		l = Integer.parseInt(coords[0]);
    		c = Integer.parseInt(coords[1]);
    	}catch(NumberFormatException e){
    		throw new BadSpreadSheetValuesException();
    	}
    	if(sheet.getSingleCell(l,c)!=null && sheet.getSingleCell(l, c).getProtect()){
    		throw new UnauthorizedOperationException();
    	}
    	//IMPORTANT!! Resets the user session time.
    	bubbled.resetsSessionTime(tokenUser);
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	Bubbledocs bubbled = Bubbledocs.getInstance();
    	SpreadSheet sheet = bubbled.getSpreadSheet(sheetID);
    	
    	/*
    	 * doing the actual work may still fail if the cell is out of bounds
    	 * or if the referred cell has no value (exceptions in both cases).
    	 */
    	int l, c, lref, cref;
    	try{
	    	String[] coords = cellID.split(";");
	    	if(coords.length!=2) throw new BadSpreadSheetValuesException();
	    	l = Integer.parseInt(coords[0]);
	    	c = Integer.parseInt(coords[1]);
	    	String[] refcoords = reference.split(";");
	    	if(refcoords.length!=2) throw new BadSpreadSheetValuesException();
	    	lref = Integer.parseInt(refcoords[0]);
	    	cref = Integer.parseInt(refcoords[1]);
    	} catch(NumberFormatException e){
    		throw new BadSpreadSheetValuesException();
    	}
    	sheet.addReferenceToCell(l, c, lref, cref);
    	result = sheet.getSingleCell(l, c).getResult();
    }

    public final int getResult() {
        return result;
    }
}
