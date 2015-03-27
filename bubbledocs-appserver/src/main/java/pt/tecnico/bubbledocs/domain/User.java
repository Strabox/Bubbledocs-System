package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

import java.util.ArrayList;
import java.util.Arrays;

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
    	for(AccessType type : this.getUsedBySet()){
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
    	for(AccessType t: this.getUsedBySet()){
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
    
    public ArrayList<SpreadSheet> listWritableSpreadSheets(){
    	ArrayList<SpreadSheet> writable = new ArrayList<SpreadSheet>();
    	for(AccessType ae : getUsedBySet()){
    		if (ae.getMode()==AccessMode.WRITE){
    			writable.add(ae.getFolha());
    		}
    	}    	
    	return writable;
    	
    }
    
    public ArrayList<SpreadSheet> listReadableSpreadSheets(){
    	ArrayList<SpreadSheet> readable = new ArrayList<SpreadSheet>();
    	for(AccessType ae : getUsedBySet()){
    		if (ae.getMode()==AccessMode.READ || ae.getMode()==AccessMode.WRITE){
    			readable.add(ae.getFolha());
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
    	for(AccessType t: getUsedBySet()){
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
