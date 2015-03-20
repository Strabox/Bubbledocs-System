package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

// add needed import declarations

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
	}

	public CreateSpreadSheet(String userToken, String name, int rows, int columns) {
		usrtoken = userToken; sheetname = name;
		sheetrows = rows; sheetcolumns = columns;
		bubble = Bubbledocs.getInstance();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		SpreadSheet sps = new SpreadSheet(sheetname, sheetrows, sheetcolumns);
		bubble.addFolhaCalculo(sps);
		usr.addOwned(sps);
		sheetId = sps.getId();
	}

}
