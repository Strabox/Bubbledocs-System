package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.FenixFramework;


public class SistemaLogin extends SistemaLogin_Base {
    
    private SistemaLogin() {
        super();
        FenixFramework.getDomainRoot().setSistemaLogin(this);
    }
    
    /*
     * Implementa o Singleton.
     */
    public static SistemaLogin getInstance(){
    	SistemaLogin s = FenixFramework.getDomainRoot().getSistemaLogin();
    	if(s == null)
    		s = new SistemaLogin();
    	return s;
    }

    /*
     * Obtém o utilizador dado um username.
     */
    public Utilizador obterUtilizador(String username){
    	for(Utilizador u: this.getUtilizadorSet()){
    		if(username.equalsIgnoreCase(u.getUsername()))
    			return u;
    	}
    	return null;
    }
    
}
