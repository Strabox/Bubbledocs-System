package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;

import java.util.ArrayList;

public class User extends User_Base {
    

    public User(String nome,String username,String password) throws DuplicateUsernameException{
    	super();
		setName(nome);
		setUsername(username);
		setPassword(password);
		setBubbledocsUtilizadores(Bubbledocs.getInstance());
    }
    
    @Override
    @Atomic
    public void addOwned(SpreadSheet s){
    	super.addOwned(s);
    }
    
    /* */
    @Override
    public void setUsername(String newUsername) throws DuplicateUsernameException{
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
    	/* Delete the relations and the spreadsheets
    	 * he created.
    	 */
    	for(SpreadSheet s: this.getOwnedSet()){
    		s.delete();
    	}
    	/* Delete the relation with all spreadsheets
    	 * he can use.
    	 */
    	for(AcessType type : this.getUsedBySet()){
    		type.delete();
    	}
    	deleteDomainObject();
    }
    
    /*
     * getSpreadSheetByName - Get all the spreadsheets with the
     * given name.
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
     * listOwnedSpreadSheets - Return and prints all spreadsheets
     * created by the user.
     */
    @Atomic
    public ArrayList<SpreadSheet> listOwnedSpreadSheets(){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(SpreadSheet f: getOwnedSet()){
    		folhas.add(f);
    		System.out.println(f);
    	}
    	if(folhas.isEmpty())
    		System.out.println(getUsername()+" has 0 spreadsheets.");
    	return folhas;
    }
    
    /*
     * listUsedSpreadSheet - Returns all spreadsheets that user
     * can access except the ones he has created.
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
