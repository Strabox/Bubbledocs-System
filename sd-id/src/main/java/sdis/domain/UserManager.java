package sdis.domain;

import java.util.ArrayList;

import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;

/* Class manager - Manages all users in the system. */
public class UserManager {

	/* System users. */
	private ArrayList<User> users;
	
	
	public UserManager(){
		users = new ArrayList<User>();
	}
	
	/* addUser(User) - Add given user to the system. */
	public void addUser(User user) throws UserAlreadyExists_Exception,
	EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception {
		if(!validateUsername(user.getUsername())){
			InvalidUser u = new InvalidUser();
			u.setUserId(user.getUsername());
			throw new InvalidUser_Exception("User inválido!!",u);	
		}
		if(!validateEmail(user.getEmail())){
			InvalidEmail email = new InvalidEmail();
			email.setEmailAddress(user.getEmail());
			throw new InvalidEmail_Exception("Email inválido!!",email);	
		}
		if(usernameExists(user.getUsername())){
			UserAlreadyExists u = new UserAlreadyExists();
			u.setUserId(user.getUsername());
			throw new UserAlreadyExists_Exception("Por favor insira outro username!!",u);
		}
		if(emailExists(user.getEmail())){
			EmailAlreadyExists email = new EmailAlreadyExists();
			email.setEmailAddress(user.getEmail());
			throw new EmailAlreadyExists_Exception("Por favor insira outro mail!!",email);	
		}
		users.add(user);
	}
	
	/* getUserByUsername(String) - returns User with given username if exists
	 * otherwise returns null.
	 */
	public User getUserByUsername(String username){
		for(User u : users){
			if(u.getUsername().equals(username))
				return u;
		}
		return null;
	}

	/* validateUsername(String) - checks if the username has a valid
	 * format */
	private boolean validateUsername(String username){
		if(!username.equals(""))
			return true;
		else
			return false;
	}
	
	/* validateEmail(String) - check if the email has a valid format. */
	private boolean validateEmail(String mail){
		if(mail.matches(".+@.+\\..+"))
			return true;
		else
			return false;
		
	}
	
	/*
	 * usernameExists(String) - Verifies if there is one user with
	 * same username.
	 */
	public boolean usernameExists(String user){
		for(User u : users){
			if(u.getUsername().equals(user))
				return true;
		}
		return false;
	}
	
	/*
	 * emailExists(String) - Verifies it there is one user with same
	 * emai.
	 */
	public boolean emailExists(String email){
		for(User u : users){
			if(u.getEmail().equals(email))
				return true;
		}
		return false;
	}
	
	/*
	 * verifyUserPassword(String,String) - Verifies if the user 
	 * password is correct.
	 */
	public boolean verifyUserPassword(String username,String password) {
		for(User u : users){
			if(u.getUsername().equals(username)){
				if(u.getPassword().equals(password))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
}
