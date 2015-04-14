package sdis.domain;

//import org.apache.commons.lang.RandomStringUtils;

/* User - Class represents system user. */
public class User {

	private String username;
	
	private String password;
	
	private String email;

	
	public User(String user,String mail){
		username = user;
		email = mail;
		password = generatePassword();
	}
	
	/* Generate a new random password for user. */
	private String generatePassword(){
		/*
		For now we return always the same for testing!!
		RandomStringUtils rsu;
		return rsu.randomAlphanumeric(12);
		*/
		return "pass";
	}
	
	/* Verify if the password matches.  */
	public boolean verifyPassword(String givenPass){
		if(password.equals(givenPass))
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
	
	public String setNewPassword(){
		String pass = generatePassword();
		setPassword(pass);
		return pass;
	}
}
