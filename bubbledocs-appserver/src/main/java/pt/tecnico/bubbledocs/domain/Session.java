package pt.tecnico.bubbledocs.domain;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

/* class Session - Represents a user in session. */
public class Session extends Session_Base {
	
    public Session(LocalTime time,String name,String token) {
        super();
        setLoginTime(time);
        setName(name);
        setToken(token);
        super.setMAX_LOGIN_TIME_MINUTES(2 * 60);
    }
    
    @Override
    public void setMAX_LOGIN_TIME_MINUTES(int time){/*DO nothing*/}
    
    /*
     * isValid() - Verifies if the session is valid with in
     * login time.
     */
    public boolean isValid(){
    	LocalTime time = getLoginTime();
    	LocalTime now = new LocalTime();
    	int difference = Minutes.minutesBetween(time, now).getMinutes();
    	if(difference > getMAX_LOGIN_TIME_MINUTES())
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
