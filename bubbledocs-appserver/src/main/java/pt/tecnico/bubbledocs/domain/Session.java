package pt.tecnico.bubbledocs.domain;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

/* class Session - Represents a user in session. */
public class Session extends Session_Base {
	
	// Max minutes user can be loggedin without activity.
	private final int MAX_LOGIN_TIME = 2 * 60;
	
    public Session(LocalTime time,String name,String token) {
        super();
        setLoginTime(time);
        setName(name);
        setToken(token);
    }
    
    /*
     * isValid() - Verifies if the session is valid with in
     * login time.
     */
    public boolean isValid(){
    	LocalTime time = getLoginTime();
    	LocalTime now = new LocalTime();
    	int difference = Minutes.minutesBetween(now, time).getMinutes();
    	if(difference > MAX_LOGIN_TIME)
    		return false;
    	else
    		return true;
    }
    
    /*
     * delete() - Remove Session from persistent state.
     */
    public void delete(){
    	setBubble(null);
    	deleteDomainObject();
    }
    
}
