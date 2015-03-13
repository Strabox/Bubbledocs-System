package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;

import java.util.ArrayList;

public class User extends User_Base {
    
	/*
     * Throws unchecked exception UsernameAlreadyTakenException !!!!
     */
    public User(String nome,String username,String password){
    	super();
		setName(nome);
		setUsername(username);
		setPassword(password);
		setBubbledocsUtilizadores(Bubbledocs.getInstance());
    }
    
    /*
     * 
     * Throws unchecked exception UsernameAlreadyTakenException !!!!
     */
    @Override
    public void setUsername(String newUsername){
    	if(newUsername != null){
	    	for(User user: Bubbledocs.getInstance().getUtilizadorSet()){
	    		if(user.getUsername().equals(newUsername)){
	    			throw new DuplicateUsernameException(newUsername);
	    		}
	    	}
	    	super.setUsername(newUsername);
    	}
    }
    
    /*
     * 
     */
    public void delete(){
    	//FIX-ME!!!!!!!!!!!!!
    	deleteDomainObject();
    }
    
    /*
     * obterFolhaPorNome - ...
     */
    public ArrayList<SpreadSheet> getSpreadSheet(String nome){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(SpreadSheet f: getOwnedSet()){
    		if(f.getName().equals(nome))
    			folhas.add(f);
    	}
    	return folhas;
    }
    
    /*
     * listOwnedSpreadSheets - Retorna todas as folhas usadas pelo utilizador.
     */
    public ArrayList<SpreadSheet> listOwnedSpreadSheets(){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(AcessType t: this.getUsedBySet()){
    		folhas.add(t.getFolha());
    		System.out.println(t.getFolha());
    	}
    	return folhas;
    }
    
    /*
     * listUsedSpreadSheet - Retorna todas as folhas criadas pelo utilizador.
     */
    public ArrayList<SpreadSheet> listUsedSpreadSheets(){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(SpreadSheet f: getOwnedSet()){
    		folhas.add(f);
    		System.out.println(f);
    	}
    	return folhas;
    }
    
    /*
     * listUsedSpreadSheets - Retorna todas as folhas associadas ao utilizador.
     */
    public void listAllSpreadSheets(){
    	System.out.println(getUsername()+"'s SpreadSheets");
    	listUsedSpreadSheets();
    	listOwnedSpreadSheets();
    	System.out.println();
    	System.out.println("----------------------------------");
    }
    
    
    
    @Override
    public String toString(){
    	String s = "Nome: "+getName()+"\nUsername: "+getUsername();
    	return s;
    }
    
}
