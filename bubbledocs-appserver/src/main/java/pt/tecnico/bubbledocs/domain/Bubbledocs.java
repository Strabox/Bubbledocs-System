package pt.tecnico.bubbledocs.domain;

import org.joda.time.LocalTime;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exceptions.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exceptions.WrongPasswordException;

/*
 * Implements singleton pattern.
 * Bubbledocs is entry point for FenixFramework.
 */
public class Bubbledocs extends Bubbledocs_Base {

	private Bubbledocs() {
		super();
		FenixFramework.getDomainRoot().setBubbledocs(this);
		// Used to generate Unique Sequential number.
		super.setUniqueId(0);
		// 1st time Bubbledocs run creates a super user to it.
		this.addUser(new User("Super User","root","root"));
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
	public void setUniqueId(int id) {}

	/*
	 * gerarUniqueId - Generate Unique Id's to each new spreadsheet.
	 */
	protected int generateUniqueId() {
		int id = super.getUniqueId();
		super.setUniqueId(getUniqueId() + 1);
		return id;
	}
	
	/*
     * generateToken - Generates a random token for a user session
     * given a username.
     */
	public String generateToken(String username) {
		String token = username;
		Long l = Math.round(Math.random()*9);
		token = token + l.toString();
		return token;
	}
	
	/*
	 * getSpreadSheet - Get the spreadsheet by name.
	 */
	public SpreadSheet getSpreadSheet(String name){
		for (SpreadSheet s : getBubbleSpreadsheetSet()) {
			if (s.getName().equals(name))
				return s;
		}
		return null;
	}
	
	/*
	 * getSpreadsheet - Get the spreadsheet by ID.
	 */
	public SpreadSheet getSpreadSheet(int id){
		for (SpreadSheet s : getBubbleSpreadsheetSet()) {
			if (s.getId() == id)
				return s;
		}
		return null;
	}
	
	
	/*
	 * getUserByName - Get the user given his username.
	 */
	public User getUserByName(String username) {
		for (User u : getUserSet()) {
			if (username.equals(u.getUsername()))
				return u;
		}
		return null;
	}

	/*
	 * getUserFromSession - Returns user from his session given
	 * his secret token if session exists and is valid.
	 */
	public User getUserFromSession(String token){
		for(Session s: getSessionSet()){
			if(s.getToken().equals(token) && s.isValid())
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
				s.delete();						//Deletes user session.
		}	
	}
	
	/*
	 * removeAllInvalidSessions - Remove all sessions that are no
	 * longer valid in the system.
	 */
	public void removeAllInvalidSessions(){
		for(Session s: getSessionSet()){
			if(!s.isValid())
				s.delete();
		}
	}
	
	/*
	 * resetsSessionTime - Reset user session time given his token
	 * if session is valid, otherwise do nothing.
	 */
	public void resetsSessionTime(String token){
		for(Session s : getSessionSet()){
			if(s.getToken().equals(token) && s.isValid()){
				s.setLoginTime(new LocalTime());
			}			
		}
	}
	
	/*
	 * getLastTime - Get last time user made a change to refresh
	 * his login.
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
	 * getUserInSessionToken - Get user's token if in session.
	 */
	public String getUserInSessionToken(String username){
		for(Session s : getSessionSet()){
			if(s.getName().equals(username)){
				return s.getToken();
			}			
		}
		return null;
	}
	
	/*
	 * loginUser - Create a new session for the user and he stays logged in
	 * returns the generated token.
	 */
	public String loginUser(String user, String pass) throws WrongPasswordException,	
		UnknownBubbleDocsUserException {
		String token = "";
		for(Session s : getSessionSet()){
			if(s.getName().equals(user)){			//User already in session.
				token = generateToken(user);
				s.setToken(token);    				//Generates a new token.
				s.setLoginTime(new LocalTime());    //Resets login time.
				return token;
			}			
		}
		for (User u : getUserSet()) {
			if (user.equals(u.getUsername()) && u.getPassword().equals(pass)) {
				token = generateToken(user);
				addSession(new Session(new LocalTime(),user,token));
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
		for (User u : getUserSet()) {
			System.out.println(u);
		}
	}

	/*
	 * listAllSpreadSheets - Lists all the spreadsheets registered in the
	 * application.
	 */
	public void listAllSpreadSheets() {
		for (SpreadSheet f : getBubbleSpreadsheetSet()) {
			System.out.println(f);
		}
	}
}
