package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

// add needed import declarations

public class AssignReferenceCell extends BubbleDocsService {
	
    private String result;
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
    	/*checking permissions. OK if it's the owner or has writing permissions and then if the cell is unprotected.*/
    	//if( sheet.getOwner()!=user &&
    	
    	String[] coords = cellID.split(";");
    	int l = Integer.parseInt(coords[0]);
    	int c = Integer.parseInt(coords[1]);
    	sheet.addContentToCellFromString(l, c,"="+cellID);
    	result = Integer.toString(sheet.getSingleCell(l, c).getResult());
    }

    public final String getResult() {
        return result;
    }
}
