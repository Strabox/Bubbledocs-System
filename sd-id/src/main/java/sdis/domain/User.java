package sdis.domain;

import java.util.UUID;

/* Class represents system user. */
public class User {

	private String username;
	
	private String password;
	
	private String email;

	public User(String user,String mail){
		username = user;
		email = mail;
		password = generatePassword();
	}
	
	private String generatePassword(){
		return UUID.randomUUID().toString();
	}
	
	/* Verify if the password matches  */
	public boolean verifyPassword(String givenPass){
		if(password == givenPass)
			return true;
		else
			return false;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
