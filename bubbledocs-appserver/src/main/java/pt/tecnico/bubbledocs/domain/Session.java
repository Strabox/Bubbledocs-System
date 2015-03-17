package pt.tecnico.bubbledocs.domain;

import org.joda.time.LocalTime;

/* class Session - Represents a user in session. */
public class Session extends Session_Base {
	
    public Session(LocalTime time,String name,String token) {
        super();
        setLoginTime(time);
        setName(name);
        setToken(token);
    }
    
    /*
     * delete() - Remove Session from persistent state.
     */
    public void delete(){
    	setBubble(null);
    	deleteDomainObject();
    }
    
}
