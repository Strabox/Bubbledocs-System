package pt.tecnico.bubbledocs.service;

import java.util.ArrayList;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BadSpreadSheetValuesException;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.InvalidLiteralException;
import pt.tecnico.bubbledocs.exceptions.NoValueForReferenceException;
import pt.tecnico.bubbledocs.exceptions.SpreadSheetNotFoundException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

/*
 * Service assign a integer constant to a cell.
 */
public class AssignLiteralCell extends BubbleDocsService {

	private int result;
	private String tokenUser;
	private int sheetID;
	private String cellID;
	private String literal;

	public AssignLiteralCell(String accessUsername, int docId, String cellId, String lite){
		tokenUser = accessUsername;
		sheetID = docId;
		cellID = cellId;
		literal = lite;
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
		
		int aux;
		try{
			aux = Integer.parseInt(literal);
			
		} catch (NumberFormatException e){
			throw new InvalidLiteralException();
		}

		Literal lit = new Literal(aux);
		sheet.addContentToCell(l, c, lit);
		result = sheet.getSingleCell(l, c).getResult();
	}

	public final int getResult() {
		return result;
	}

}
