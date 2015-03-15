package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exceptions.UnknownBubbleDocsUserException;

/*
 * Implements singleton pattern.
 * Bubbledocs is entry point for FenixFramework.
 */
public class Bubbledocs extends Bubbledocs_Base {
    
	  	private Bubbledocs() {
	        super();
	        FenixFramework.getDomainRoot().setBubbledocs(this);
	        super.setUniqueId(0);				//Used to generate Unique Sequential number.
	    }
	  	
	    @Atomic
	    public static Bubbledocs getInstance(){
	    	Bubbledocs s = FenixFramework.getDomainRoot().getBubbledocs();
	    	if(s == null)
	    		s = new Bubbledocs();
	    	return s;
	    }
	   
	    /*
	     * setUniqueId - Overrided so we cant mess with
	     * uniqueId generation.
	     */
	    @Override
	    public void setUniqueId(int id){}
	    
	    /*
	     * gerarUniqueId - Generate Unique Id's to each new
	     * spreadsheet.
	     */
	    @Atomic
	    public int generateUniqueId(){
	    	int id = super.getUniqueId();
	    	super.setUniqueId(getUniqueId() + 1);
	    	return id;
	    }
	    
	     /* 
	      * getUserByName - Get the user given his username. 
	      */
	    @Atomic
	    public User getUserByName(String username){
	    	for(User u: getUtilizadorSet()){
	    		if(username.equalsIgnoreCase(u.getUsername()))
	    			return u;
	    	}
	    	throw new UnknownBubbleDocsUserException();
	    }
	    
	    /* 
	     * listAllUsers - Lists all the users registered in
	     * the application.
	     */
	    @Atomic
	    public void listAllUsers(){
	    	for(User u: getUtilizadorSet()){
	    		System.out.println(u);
	    	}
	    }
    	
	    /* 
	     * listAllSpreadSheets - Lists all the spreadsheets
	     * registered in the application.
	     */
	    @Atomic
	    public void listAllSpreadSheets(){
	    	for(SpreadSheet f: getFolhaCalculoSet()){
	    		System.out.println(f);
	    	}
	    }
}
