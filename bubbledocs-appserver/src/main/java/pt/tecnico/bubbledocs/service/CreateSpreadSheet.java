package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

// add needed import declarations

public class CreateSpreadSheet extends BubbleDocsService {
    private int sheetId;  // id of the new sheet

    public int getSheetId() {
        return sheetId;
    }
    
    @Override
    protected void accessControl(){
    	// FIX-ME Implement me!!!!!
    }
    
    public CreateSpreadSheet(String userToken, String name, int rows,
            int columns) {
	// add code here
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
	// add code here
    }

}
