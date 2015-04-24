package pt.tecnico.bubbledocs.service.dto;

import pt.tecnico.bubbledocs.domain.User;

/* UserDTO - used to pass information without reveal
 * domain implementation. */
public class UserDTO {

	private String username;
	
	private String email;
	
	private String password;
	
	private String name;

	public UserDTO(String username, String email, String password, String name) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.name = name;
	}
	
	public UserDTO(User user){
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.name = user.getName();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
}
