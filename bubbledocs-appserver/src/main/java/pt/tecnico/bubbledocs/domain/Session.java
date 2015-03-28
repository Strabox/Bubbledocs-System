package pt.tecnico.bubbledocs.domain;

import org.joda.time.LocalTime;

/* class Session - Represents a user in session. */
public class Session extends Session_Base {
	
	// 
	private final int MAX_LOGIN_TIME = 2;
	
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
    	return false;
    }
    
    /*
     * delete() - Remove Session from persistent state.
     */
    public void delete(){
    	setBubble(null);
    	deleteDomainObject();
    }
    
}
