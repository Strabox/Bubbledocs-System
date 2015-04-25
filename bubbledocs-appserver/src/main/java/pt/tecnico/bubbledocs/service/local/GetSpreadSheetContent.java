package pt.tecnico.bubbledocs.service.local;


import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.SpreadSheetNotFoundException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class GetSpreadSheetContent extends BubbleDocsService {

	private String tokenUser;
	private int docId;
	String[][] matrix;
	
	public GetSpreadSheetContent(String _tokenUser, int _docId){
		this.tokenUser=_tokenUser;
		this.docId = _docId;
	}
	
	@Override
	protected void accessControl() throws BubbleDocsException {
		Bubbledocs bubbled = Bubbledocs.getInstance();
		if(tokenUser == null || bubbled.getUserFromSession(tokenUser) == null)
    		throw new UserNotInSessionException();
    	
		User user = bubbled.getUserFromSession(tokenUser);
    	SpreadSheet sheet = bubbled.getSpreadSheet(docId);
    	if (sheet == null) throw new SpreadSheetNotFoundException();
    	if( !(sheet.getOwner()==user || user.canWriteSpreadSheet(sheet) || user.canReadSpreadSheet(sheet))){
			throw new UnauthorizedOperationException();
    	}
    	bubbled.resetsSessionTime(tokenUser);
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		Bubbledocs bubbled = Bubbledocs.getInstance();
    	SpreadSheet sheet = bubbled.getSpreadSheet(docId);
    	matrix = sheet.contentsToMatrix();
	}
	
	public final String[][] getResult(){
		return matrix;
	}

}
