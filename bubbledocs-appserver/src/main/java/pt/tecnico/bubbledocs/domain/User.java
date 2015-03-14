package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.Atomic;
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
     *  Delete() - Delete a user from persistent state. 
     */
    public void delete(){
    	setBubbledocsUtilizadores(null);
    	/* Elimina todas as folhas que o user criou. */
    	for(SpreadSheet s: this.getOwnedSet()){
    		s.delete();
    	}
    	/* Elimina todas a relação com todas as folhas que usa.*/
    	for(AcessType type : this.getUsedBySet()){
    		type.delete();
    	}
    	deleteDomainObject();
    }
    
    /*
     * getSpreadSheetByName - Obtém todas as folhas do utilizador
     * com o nome dado.
     */
    @Atomic
    public ArrayList<SpreadSheet> getSpreadSheet(String nome){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(SpreadSheet f: getOwnedSet()){
    		if(f.getName().equals(nome))
    			folhas.add(f);
    	}
    	for(AcessType t: this.getUsedBySet()){
    		folhas.add(t.getFolha());
    	}
    	return folhas;
    }
    
    /*
     * listOwnedSpreadSheets - Retorna todas as folhas usadas pelo utilizador.
     */
    @Atomic
    public ArrayList<SpreadSheet> listOwnedSpreadSheets(){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(SpreadSheet f: getOwnedSet()){
    		folhas.add(f);
    		System.out.println(f);
    	}
    	return folhas;
    }
    
    /*
     * listUsedSpreadSheet - Retorna todas as folhas criadas pelo utilizador.
     */
    @Atomic
    public ArrayList<SpreadSheet> listUsedSpreadSheets(){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(AcessType t: getUsedBySet()){
    		folhas.add(t.getFolha());
    	}
    	return folhas;
    }

    
    @Override
    public String toString(){
    	String s = "Nome: "+getName()+"\nUsername: "+getUsername()+"\nPassword: "+getPassword();
    	return s;
    }
    
}
