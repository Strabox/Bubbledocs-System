package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.BadSpreadSheetValuesException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;


public class CreateSpreadSheet extends BubbleDocsService {
	
	private int sheetId;  // id of the new sheet
	private User usr;
	private String usrtoken;
	private String sheetname;
	private int sheetrows;
	private int sheetcolumns;
	private Bubbledocs bubble;

	public int getSheetId() {
		return sheetId;
	}

	@Override
	protected void accessControl(){
		usr = bubble.getUserFromSession(usrtoken);
		if(usrtoken == null || usr == null)
    		throw new UserNotInSessionException();
		//IMPORTANT!! Resets the user session time.
		bubble.resetsSessionTime(usrtoken);
	}

	public CreateSpreadSheet(String userToken, String name, int lines, int columns) {
		if (lines < 0 || columns < 0 || name == null) {
			throw new BadSpreadSheetValuesException();
		}
		usrtoken = userToken; sheetname = name;
		sheetrows = lines; sheetcolumns = columns;
		bubble = Bubbledocs.getInstance();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		SpreadSheet sps = new SpreadSheet(sheetname, sheetrows, sheetcolumns);
		bubble.addBubbleSpreadsheet(sps);
		usr.addOwned(sps);
		sheetId = sps.getId();
	}

}
