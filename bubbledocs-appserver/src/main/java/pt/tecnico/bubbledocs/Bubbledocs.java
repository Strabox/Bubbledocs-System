package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exceptions.UsernameAlreadyTakenException;

public class Bubbledocs extends Bubbledocs_Base {
    
	  private Bubbledocs() {
	        super();
	        FenixFramework.getDomainRoot().setBubbledocs(this);
	        setUniqueId(0);				//Used to generate Unique Sequential number.
	    }
	    
	    /*
	     * Implementa o Singleton.
	     */
	    public static Bubbledocs getInstance(){
	    	Bubbledocs s = FenixFramework.getDomainRoot().getBubbledocs();
	    	if(s == null)
	    		s = new Bubbledocs();
	    	return s;
	    }
	    
	    /*
	     * adiciona um utilizador ao sistema passando a figurar na
	     * persistencia da Bubbledocs.
	     */
	    public void adicionaUtilizador(Utilizador u) throws UsernameAlreadyTakenException{
	    	for(Utilizador user: getUtilizadorSet()){
	    		if(user.getUsername().equals(u.getUsername()))
	    			throw new UsernameAlreadyTakenException(user.getUsername());
	    	}
    		addUtilizador(u);
	    }
	    
	    /*
	     * adiciona uma folha ao sistema passando a figurar na
	     * persistencia da Bubbledocs.
	     */
	    public void adicionaFolhaCalculo(FolhaCalculo f) {
    		addFolhaCalculo(f);
	    }
	    
	     /* 
	      * obterUtilizador - Obtém o utilizador dado um username. 
	      */
	    public Utilizador obterUtilizador(String username){
	    	for(Utilizador u: getUtilizadorSet()){
	    		if(username.equalsIgnoreCase(u.getUsername()))
	    			return u;
	    	}
	    	return null;
	    }
	    
	    /* 
	     * listarUtilizadores - Lista todos os utilizadores registados na aplicacao.
	     */
	    public void listarUtilizadores(){
	    	for(Utilizador u: getUtilizadorSet()){
	    		System.out.println(u);
	    	}
	    }
    	
	    /* 
	     * listarFolhas - Lista todas as folha registadas na aplicacao.
	     */
	    public void listarFolhas(){
	    	for(FolhaCalculo f: getFolhaCalculoSet()){
	    		System.out.println(f);
	    	}
	    }
}
