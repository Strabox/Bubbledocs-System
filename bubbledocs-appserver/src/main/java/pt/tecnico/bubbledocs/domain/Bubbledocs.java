package pt.tecnico.bubbledocs.domain;

import org.joda.time.LocalTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;

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
		this.addUser(new User("Super User","root","root","root@email.pt"));
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
	 * userExists(String) - Verify if the user exists already.
	 */
	public void userExists(String newUsername)
		throws DuplicateUsernameException{
		for(User user: getUserSet()){
			if(user.getUsername().equals(newUsername)){
				throw new DuplicateUsernameException(newUsername);
			}
		}
	}
	
	/*
	 * getSpreadSheet(String) - Get the spreadsheet by name.
	 */
	public SpreadSheet getSpreadSheet(String name){
		for (SpreadSheet s : getBubbleSpreadsheetSet()) {
			if (s.getName().equals(name))
				return s;
		}
		return null;
	}
	
	/*
	 * getSpreadsheet(int) - Get the spreadsheet by ID.
	 */
	public SpreadSheet getSpreadSheet(int id){
		for (SpreadSheet s : getBubbleSpreadsheetSet()) {
			if (s.getId() == id)
				return s;
		}
		return null;
	}
	
	/*
	 * delete(String) - Delete username from bubbledocs. 
	 */
	public void delete(String username) throws LoginBubbleDocsException{
		User user = getUserByName(username);
		String token = getUserInSessionToken(username);
		if(user == null)
			throw new LoginBubbleDocsException();
		if(token != null)
			removeUserFromSession(token);
		user.delete();
	}
	
	/*
	 * getUserByName(String) - Get the user given his username.
	 */
	public User getUserByName(String username) {
		for (User u : getUserSet()) {
			if (username.equals(u.getUsername()))
				return u;
		}
		return null;
	}

	/*
	 * getUserFromSession(String) - Returns user from his 
	 * session given his secret token if session exists and is valid.
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
				s.delete();					//Deletes user session.
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
	 * createSession(String) - Create a session for the user and returns
	 * a random token to user services.
	 */
	public String createSession(String username){
		String token = generateToken(username);
		for(Session s : getSessionSet()){
			if(s.getName().equals(username)){		//User already in session.
				s.setToken(token);    				//Generates a new token.
				s.setLoginTime(new LocalTime());    //Resets login time.
				return token;						//No need to see more users.
			}			
		}
		//User not in session, create a new one.
		addSession(new Session(new LocalTime(),username,token));	
		return token;
	}
	
	/*
	 * loginUser(String,String) - Create a new session for the user and put him 
	 * logged in returns the generated token.
	 */
	public void loginUser(String user, String pass) throws UnavailableServiceException{
		for (User u : getUserSet()) {
			if (user.equals(u.getUsername()) && u.getPassword().equals(pass)) {
				//LOGIN SUCCESSFUL
				return;
			}else if (user.equals(u.getUsername()) && !u.getPassword().equals(pass))
				//Wrong username - password combination.
				throw new UnavailableServiceException();
		}
		//User doesnt exist in local Database.
		throw new UnavailableServiceException();
	}
	
	
	/*
	 * printAllUsers - Print all the users registered in the application.
	 */
	public void printAllUsers() {
		for (User u : getUserSet()) {
			System.out.println(u);
		}
	}

	/*
	 * printAllSpreadSheets - Print all the spreadsheets registered in the
	 * application.
	 */
	public void printAllSpreadSheets() {
		if(getBubbleSpreadsheetSet().isEmpty())
			System.out.println("0 Spreadsheets in Bubbledocs");
		for (SpreadSheet f : getBubbleSpreadsheetSet()) {
			System.out.println(f);
		}
	}
}
