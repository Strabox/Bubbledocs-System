package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BadSpreadSheetValuesException;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.NoValueForReferenceException;
import pt.tecnico.bubbledocs.exceptions.SpreadSheetNotFoundException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class AssignBinaryFunctionToCell extends BubbleDocsService {
	
	private int result;
    private boolean hasResult;
    private String tokenUser;
    private int sheetID;
    private String cellID;
    private String expression;
    
    public AssignBinaryFunctionToCell(String _tokenUser, int _docId, String _cellId, String _expression) {
		tokenUser = _tokenUser;
		sheetID = _docId;
		cellID = _cellId;
		expression = _expression;
    }
	@Override
	protected void accessControl() throws BubbleDocsException {
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
    	if( !(sheet.getOwner()==user || user.canWriteSpreadSheet(sheet))){
			throw new UnauthorizedOperationException();
    	}
    	/*
    	 * if the cell exists, it can be protected.
    	 * if it is protected, we can't change its content either.
    	 * should this be done first, for efficiency?
    	 */
    	if(cellID==null || expression==null) throw new BadSpreadSheetValuesException();
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
    	int l, c;
    	try{
	    	String[] coords = cellID.split(";");
	    	if(coords.length!=2) throw new BadSpreadSheetValuesException();
	    	l = Integer.parseInt(coords[0]);
	    	c = Integer.parseInt(coords[1]);
		} catch(NumberFormatException e){
    		throw new BadSpreadSheetValuesException();
    	}
    	sheet.addBinaryFunctionToCell(l, c, expression);
    	try{
    		hasResult = true;
    		result = sheet.getSingleCell(l, c).getResult();
    	}
    	catch(NoValueForReferenceException e){
    		hasResult = false;
    	}
	}

	public final int getResult() {
    	if(hasResult) return result;
    	else throw new NoValueForReferenceException();
    }
}
