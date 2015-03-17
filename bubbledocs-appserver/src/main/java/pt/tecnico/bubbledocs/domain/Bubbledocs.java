package pt.tecnico.bubbledocs.domain;


import org.joda.time.LocalTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exceptions.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.exceptions.WrongPasswordException;

/*
 * Implements singleton pattern.
 * Bubbledocs is entry point for FenixFramework.
 */
public class Bubbledocs extends Bubbledocs_Base {

	private Bubbledocs() {
		super();
		FenixFramework.getDomainRoot().setBubbledocs(this);
		super.setUniqueId(0); // Used to generate Unique Sequential number.
	}

	
	public static Bubbledocs getInstance() {
		Bubbledocs s = FenixFramework.getDomainRoot().getBubbledocs();
		if (s == null)
			s = new Bubbledocs();
		return s;
	}

	/*
	 * setUniqueId - Overrided so we cant mess with uniqueId generation.
	 */
	@Override
	public void setUniqueId(int id) {
	}

	/*
	 * gerarUniqueId - Generate Unique Id's to each new spreadsheet.
	 */
	protected int generateUniqueId() {
		int id = super.getUniqueId();
		super.setUniqueId(getUniqueId() + 1);
		return id;
	}

	/*
	 * getUserByName - Get the user given his username.
	 */
	public User getUserByName(String username) {
		for (User u : getUtilizadorSet()) {
			if (username.equals(u.getUsername()))
				return u;
		}
		throw new UnknownBubbleDocsUserException();
	}

	/*
     * generateToken - Generates a random token for a user session.
     */
	protected String generateToken(String username) {
		String token = username;
		Long l = Math.round(Math.random()*9);
		token = token + l.toString();
		return token;
	}
	
	/*
	 * getUserFromSession - Returns user from his session given
	 * his secret token.
	 */
	public User getUserFromSession(String token){
		for(Session s: getSessionSet()){
			if(s.getToken().equals(token))
				return getUserByName(s.getName());
		}
		return null;
	}
	
	/*
	 * removeUserFromSession - Remove user from his session given
	 * his secret token.
	 */
	public void removeUserFromSession(String token){
		for(Session s: getSessionSet()){
			if(s.getToken().equals(token))
				s.delete();						//Deletes user session
		}	
		throw new UserNotInSessionException();
	}
	
	/*
	 * resetsSessionTime - Reset user session time given his token.
	 */
	public void resetsSessionTime(String token){
		for(Session s : getSessionSet()){
			if(s.getToken().equals(token)){
				s.setLoginTime(new LocalTime());
			}			
		}
	}
	
	/*
	 * getLastTime - ...
	 */
	public LocalTime getLastTime(String token){
		for(Session s : getSessionSet()){
			if(s.getToken().equals(token)){
				return s.getLoginTime();
			}			
		}
		return null;
	}
	
	/*
	 * loginUser - Create a new session for the user and he stays logged in
	 * returns the generated token.
	 * 
	 * - throws WrongPasswordException 	!!!
	 * - throws UnknownBubbleDocsUserException !!!
	 */
	public String loginUser(String user, String pass) {
		String token = "";
		for(Session s : getSessionSet()){
			if(s.getName().equals(user)){			//User already in session.
				token = generateToken(user);
				s.setToken(token);    				//Generates a new token.
				s.setLoginTime(new LocalTime());    //Resets login time.
				return token;
			}			
		}
		for (User u : getUtilizadorSet()) {
			if (user.equals(u.getUsername()) && u.getPassword().equals(pass)) {
				token = generateToken(user);
				Session s = new Session(new LocalTime(), user, token);
				addSession(s);
				return token;
			}else if (user.equals(u.getUsername()) && !u.getPassword().equals(pass))
				throw new WrongPasswordException();
		}
		throw new UnknownBubbleDocsUserException();
	}
	
	
	/*
	 * listAllUsers - Lists all the users registered in the application.
	 */
	public void listAllUsers() {
		for (User u : getUtilizadorSet()) {
			System.out.println(u);
		}
	}

	/*
	 * listAllSpreadSheets - Lists all the spreadsheets registered in the
	 * application.
	 */
	public void listAllSpreadSheets() {
		for (SpreadSheet f : getFolhaCalculoSet()) {
			System.out.println(f);
		}
	}
}
