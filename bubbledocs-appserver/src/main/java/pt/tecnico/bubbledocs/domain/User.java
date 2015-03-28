package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.EmptyUsernameException;

import java.util.ArrayList;

public class User extends User_Base {
    

    public User(String nome,String username,String password) throws DuplicateUsernameException{
    	super();
		setName(nome);
		setUsername(username);
		setPassword(password);
		setBubbledocsUsers(Bubbledocs.getInstance());
    }
    
    /* */
    @Override
    public void setUsername(String newUsername) throws DuplicateUsernameException,
    	EmptyUsernameException{
    	if(newUsername == "")
    		throw new EmptyUsernameException();
    	if(newUsername != null){
	    	for(User user: Bubbledocs.getInstance().getUserSet()){
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
    	setBubbledocsUsers(null);
    	/* Delete the relations and the spreadsheets
    	 * he created.
    	 */
    	for(SpreadSheet s: this.getOwnedSet()){
    		s.delete();
    	}
    	/* Delete the relation with all spreadsheets
    	 * he can use.
    	 */
    	for(Permission type : this.getUsedBySet()){
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
    	for(Permission t: this.getUsedBySet()){
    		folhas.add(t.getSpreadsheet());
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
    	}
    	return folhas;
    }
    
    public ArrayList<SpreadSheet> listWritableSpreadSheets(){
    	ArrayList<SpreadSheet> writable = new ArrayList<SpreadSheet>();
    	for(Permission ae : getUsedBySet()){
    		if (ae.getMode()==AccessMode.WRITE){
    			writable.add(ae.getSpreadsheet());
    		}
    	}    	
    	return writable;
    	
    }
    
    public ArrayList<SpreadSheet> listReadableSpreadSheets(){
    	ArrayList<SpreadSheet> readable = new ArrayList<SpreadSheet>();
    	for(Permission ae : getUsedBySet()){
    		if (ae.getMode()==AccessMode.READ || ae.getMode()==AccessMode.WRITE){
    			readable.add(ae.getSpreadsheet());
    		}
    	}    	
    	return readable;
    	
    }
    
    /*
     * listUsedSpreadSheet - Returns all spreadsheets that user
     * can access except the ones he has created.
     */
    @Atomic
    public ArrayList<SpreadSheet> listUsedSpreadSheets(){
    	ArrayList<SpreadSheet> folhas = new ArrayList<SpreadSheet>();
    	for(Permission t: getUsedBySet()){
    		folhas.add(t.getSpreadsheet());
    	}
    	return folhas;
    }
    
    
    @Override
    public String toString(){
    	String s = "Nome: "+getName()+"\nUsername: "+getUsername()+"\nPassword: "+getPassword();
    	return s;
    }
    
}
