package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

// add needed import declarations

public class CreateUser extends BubbleDocsService {

    public CreateUser(String userToken, String newUsername,
            String password, String name) {
	// add code here
    }
    
    @Override
    protected void accessControl(){
    	// FIX-ME Implement me!!!!!
    }
    
    @Override
    protected void dispatch() throws BubbleDocsException {
	// add code here
    }
}