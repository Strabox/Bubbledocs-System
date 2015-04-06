package sdis.domain;

import java.util.ArrayList;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

/* Class manager - manages all users in the system. */
public class UserManager {

	/* System users. */
	private ArrayList<User> users;
	
	
	public UserManager(){
		users = new ArrayList<User>();
	}
	
	/* addUser - Add given user to the system. */
	public void addUser(User user) throws UserAlreadyExists_Exception,
	EmailAlreadyExists_Exception, InvalidEmail_Exception {
		if(usernameExists(user.getUsername()))
			throw new UserAlreadyExists_Exception("Por favor insira outro username!!",new UserAlreadyExists());
		if(emailExists(user.getEmail()))
			throw new EmailAlreadyExists_Exception("Por favor insira outro mail!!",new EmailAlreadyExists());
		if(!validateEmail(user.getEmail()))
			throw new InvalidEmail_Exception("Email inválido!!",new InvalidEmail());
		users.add(user);
	}
	
	public User getUserByUsername(String username){
		for(User u : users){
			if(u.getUsername().equals(username))
				return u;
		}
		return null;
	}

	/* validateEmail - check if the email has a valid format. */
	private boolean validateEmail(String mail){
		if(mail.matches(".+@.+\\..+"))
			return true;
		else
			return false;
		
	}
	
	public boolean usernameExists(String user){
		for(User u : users){
			if(u.getUsername().equals(user))
				return true;
		}
		return false;
	}
	
	public boolean emailExists(String email){
		for(User u : users){
			if(u.getEmail().equals(email))
				return true;
		}
		return false;
	}
	
	public boolean verifyUserPassword(String username,String password)
	 throws UserDoesNotExist_Exception {
		for(User u : users){
			if(u.getUsername() == username){
				if(u.getPassword().equals(password))
					return true;
				else
					return false;
			}
		}
		throw new UserDoesNotExist_Exception("Utilizador Inexistente!!",new UserDoesNotExist());
	}
	
}
