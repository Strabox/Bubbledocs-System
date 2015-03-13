package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exceptions.UsernameAlreadyTakenException;

import java.util.ArrayList;

public class User extends User_Base {
    
	/*
     * Throws unchecked exception UsernameAlreadyTakenException !!!!
     */
    public User(String nome,String username,String password){
    	super();
		setNome(nome);
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
	    			throw new UsernameAlreadyTakenException(newUsername);
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
    public ArrayList<SpreadSheet> obterFolhaPorNome(String nome){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(SpreadSheet f: getOwnedSet()){
    		if(f.getName().equals(nome))
    			folhas.add(f);
    	}
    	return folhas;
    }
    
    /*
     * listarFolhasUsadas - Retorna todas as folhas usadas pelo utilizador.
     */
    public ArrayList<SpreadSheet> listarFolhasUsadas(){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(AcessType t: this.getUsedBySet())
    		folhas.add(t.getFolha());
    	return folhas;
    }
    
    /*
     * listarFolhasCriadas - Retorna todas as folhas criadas pelo utilizador.
     */
    public ArrayList<SpreadSheet> listarFolhasCriadas(){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(SpreadSheet f: getOwnedSet()){
    		folhas.add(f);
    	}
    	return folhas;
    }
    
    @Override
    public String toString(){
    	String s = "Nome: "+getNome()+"\nUsername: "+getUsername()+"\n";
    	return s;
    }
    
}