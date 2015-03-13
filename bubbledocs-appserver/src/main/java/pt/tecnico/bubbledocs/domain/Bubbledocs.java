package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;

/*
 * Implements singleton pattern.
 */
public class Bubbledocs extends Bubbledocs_Base {
    
	  	private Bubbledocs() {
	        super();
	        FenixFramework.getDomainRoot().setBubbledocs(this);
	        super.setUniqueId(0);				//Used to generate Unique Sequential number.
	    }
	    
	    public static Bubbledocs getInstance(){
	    	Bubbledocs s = FenixFramework.getDomainRoot().getBubbledocs();
	    	if(s == null)
	    		s = new Bubbledocs();
	    	return s;
	    }
	   
	    /*
	     * setUniqueId - Faz-se Override para que não se possa mexer directamente.
	     * no geracao de inteiros.
	     */
	    @Override
	    public void setUniqueId(int id){}
	    
	    /*
	     * gerarUniqueId - Gera inteiros unicos para o ID de cada folha.
	     */
	    public int generateUniqueId(){
	    	int id = super.getUniqueId();
	    	super.setUniqueId(getUniqueId() + 1);
	    	return id;
	    }
	    
	     /* 
	      * getUserByName - Obtém o utilizador dado um username. 
	      */
	    public User getUserByName(String username){
	    	for(User u: getUtilizadorSet()){
	    		if(username.equalsIgnoreCase(u.getUsername()))
	    			return u;
	    	}
	    	return null;
	    }
	    
	    /* 
	     * listarUtilizadores - Lista todos os utilizadores registados na aplicacao.
	     */
	    public void listAllUsers(){
	    	for(User u: getUtilizadorSet()){
	    		System.out.println(u);
	    	}
	    }
    	
	    /* 
	     * listarFolhas - Lista todas as folha registadas na aplicacao.
	     */
	    public void listAllSpreadSheets(){
	    	for(SpreadSheet f: getFolhaCalculoSet()){
	    		System.out.println(f);
	    	}
	    }
}
