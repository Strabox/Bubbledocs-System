package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

// add needed import declarations

public class ExportDocument extends BubbleDocsService {
    private byte[] docXML;

    public byte[] getDocXML() {
	return docXML;
    }
    
    @Override
    protected void accessControl(){
    	// FIX-ME Implement me!!!!!
    }
    
    public ExportDocument(String userToken, int docId) {
	// add code here
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
	// add code here
    }
}
