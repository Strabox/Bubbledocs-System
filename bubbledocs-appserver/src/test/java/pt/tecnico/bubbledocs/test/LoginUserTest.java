package pt.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;
import org.joda.time.LocalTime;
import org.joda.time.Seconds;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.service.LoginUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class LoginUserTest extends BubbleDocsServiceTest {
	
	/* Mock RemoteServices to cover all test cases and avoid the need
	 * for put the real remote service working.*/
	@Mocked
	private IDRemoteServices idRemote;
	
    private static final String USERNAME = "jp";  
    private static final String EMAIL = "jp@ESisAwesome.ist";
    private static final String PASSWORD = "jp#";
    
    
    @Override
    public void populate4Test() {
        createUser(USERNAME,EMAIL ,PASSWORD, "João Pereira");
    }

	// returns the time of the last access for the user with token userToken.
	// It must get this data from the session object of the application
	private LocalTime getLastAccessTimeInSession(String userToken) {
		return Bubbledocs.getInstance().getLastTime(userToken);
	}

    @Test
    public void success() {
        LoginUser service = new LoginUser(USERNAME, PASSWORD);
        
        new Expectations(){
        	{
        		idRemote.loginUser(USERNAME,PASSWORD);
        	}
        }; 
        service.execute();
        
        LocalTime currentTime = new LocalTime();
	
        String token = service.getUserToken();
   
        User user = getUserFromSession(service.getUserToken());
    	
        assertEquals(USERNAME, user.getUsername());

		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
	
		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
    }

    @Test
    public void succesLoginLocally(){
    	LoginUser service = new LoginUser(USERNAME, PASSWORD);
    	
    	 new Expectations(){
         	{
         		idRemote.loginUser(USERNAME,PASSWORD);
         		result = new RemoteInvocationException();
         	}
         };
         
         service.execute();
         
         LocalTime currentTime = new LocalTime();
         
         String token = service.getUserToken();
         
         User user = getUserFromSession(token);
         
         assertEquals(USERNAME,user.getUsername());
         
         int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
     	
 		assertTrue("Access time in session not correctly set", difference >= 0);
 		assertTrue("diference in seconds greater than expected", difference < 2);
    }
    	
    @Test
    public void successLoginTwice() {
        LoginUser service = new LoginUser(USERNAME, PASSWORD);
        
        new Expectations(){
        	{
        		idRemote.loginUser(USERNAME,PASSWORD);
        	}
        };
        
        service.execute();
        String token1 = service.getUserToken();
        
        new Expectations(){
        	{
        		idRemote.loginUser(USERNAME,PASSWORD);
        	}
        };
        
        service.execute();
        String token2 = service.getUserToken();

        User user = getUserFromSession(token1);
        assertNull(user);
        user = getUserFromSession(token2);
        assertEquals(USERNAME, user.getUsername());
    }

    @Test(expected = LoginBubbleDocsException.class)
    public void loginUnknownUser() {
        LoginUser service = new LoginUser("jp2", "jp");
        
        new Expectations(){
        	{
        		idRemote.loginUser("jp2","jp");
        		result = new LoginBubbleDocsException();
        	}
        };
        
        service.execute();
    }

    @Test(expected = LoginBubbleDocsException.class)
    public void loginUserWithinWrongPassword() {
        LoginUser service = new LoginUser(USERNAME, "jp2");
        
        new Expectations(){
        	{
        		idRemote.loginUser(USERNAME,"jp2");
        		result = new LoginBubbleDocsException();
        	}
        };
        
        service.execute();
    }
    
}
