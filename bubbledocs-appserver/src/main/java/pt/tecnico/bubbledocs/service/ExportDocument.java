package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;

// add needed import declarations

public class ExportDocument extends BubbleDocsService {
    private byte[] docXML;
    private String token;
    private int docId;
    
    public byte[] getDocXML() {
	return docXML;
    }
    
    @Override
    protected void accessControl(){
    	Bubbledocs bubbled = Bubbledocs.getInstance();
    	if(token == null || bubbled.getUserFromSession(token) == null)
    		throw new UserNotInSessionException();
    
    }
    
    public ExportDocument(String _userToken, int _docId) {
	token=_userToken;
	docId=_docId;
	}

    @Override
    protected void dispatch() throws BubbleDocsException {
    	//Bubbledocs bubbled = Bubbledocs.getInstance();
    	//SpreadSheet sheet = bubbled.getSpreadSheet(docId);
    	//User user = bubbled.getUserFromSession(token);
    	
    }
}
