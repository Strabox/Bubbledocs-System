package pt.tecnico.bubbledocs.test.integration.component;

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
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.test.BubbleDocsServiceTest;

public class LoginUserTestIntegratorTest extends BubbleDocsServiceTest {
	
	/* Mock RemoteServices to cover all test cases and avoid the need
	 * for put the real remote service working.*/
	@Mocked
	private IDRemoteServices idRemote;
	
    private static final String USERNAME = "jpa";
    private static final String USERNAME_INVALID_PASS = "jna";
    private static final String UNKNOWN_USERNAME ="unknown";
    private static final String EMAIL = "jp@ESisAwesome.ist";
    private static final String EMAIL2 = "jn@ESisAwesome.ist";
    private static final String PASSWORD = "jp#";
    private static final String NEW_PASSWORD = "new_pass";
    
    @Override
    public void populate4Test() {
        createUser(USERNAME,EMAIL ,PASSWORD, "João Pereira");
        createUser(USERNAME_INVALID_PASS,EMAIL2,null,"Joana Antunes");
    }

	// returns the time of the last access for the user with token userToken.
	// It must get this data from the session object of the application
	private LocalTime getLastAccessTimeInSession(String userToken) {
		return Bubbledocs.getInstance().getLastTime(userToken);
	}
	
	private void changeUserPassword(String username,String password){
		User user = Bubbledocs.getInstance().getUserByName(username);
		user.setPassword(password);
	}
	
   @Test
    public void success() {
    	LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
        
        new Expectations(){
        	{
        		idRemote.loginUser(USERNAME,PASSWORD);
        	}
        }; 
        service.execute();
        
        LocalTime currentTime = new LocalTime();
	
        String token = service.getUserToken();//invocar o metodo do loginuserintegrator que invoca um metodo do loginuser
   
        User user = getUserFromSession(service.getUserToken());
    	
        assertEquals(USERNAME, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());
        
		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
	
		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
    }

    @Test
    public void successAndUpdatePassword(){
    	LoginUserIntegrator service = new LoginUserIntegrator(USERNAME,NEW_PASSWORD);
    	
    	User user = getUserFromUsername(USERNAME);
    	assertEquals(PASSWORD,user.getPassword());
    	changeUserPassword(USERNAME, NEW_PASSWORD);	
    	new Expectations(){
    		{
    			idRemote.loginUser(USERNAME, NEW_PASSWORD);
    		}
    	};
    	service.execute();
    	String token = service.getUserToken();
    	LocalTime currentTime = new LocalTime();
    	assertEquals(USERNAME, user.getUsername());
    	assertEquals(NEW_PASSWORD,user.getPassword());
    	
    	int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
    }
    
    /*
     *Testa o login local quando a password é válida
     */
    @Test
    public void successLoginLocally(){
    	LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
    	
    	 new Expectations(){
         	{
         		idRemote.loginUser(USERNAME,PASSWORD);
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
    /*
     *Testa o login local quando a password é inválida
     */
    @Test(expected = UnavailableServiceException.class)
    public void wrongPasswordLoginLocally(){
    	LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, "wrongpass");
    	
    	 new Expectations(){
         	{
         		idRemote.loginUser(USERNAME,"wrongpass");
         		result = new RemoteInvocationException();         	}
    	 };
    	 
        	 service.execute();
    }
    
    /*
     *Testa o login local quando a password é inválida
     */
    @Test(expected = UnavailableServiceException.class)
    public void invalidPasswordLoginLocally(){
    	LoginUserIntegrator service = new LoginUserIntegrator(USERNAME_INVALID_PASS, "pass");
    	
    	 new Expectations(){
         	{
         		idRemote.loginUser(USERNAME_INVALID_PASS,"pass");
         		result = new RemoteInvocationException();
         	}
         };
         
         service.execute();
    }
    
    /*
     *Testa o login local quando o username é desconhecido
     */
    @Test(expected = UnavailableServiceException.class)
    public void unknownUserLoginLocally(){
    	LoginUserIntegrator service = new LoginUserIntegrator(UNKNOWN_USERNAME, PASSWORD);
    	
    	 new Expectations(){
         	{
         		idRemote.loginUser(UNKNOWN_USERNAME,PASSWORD);
         		result = new RemoteInvocationException();
         	}
         };
  
         service.execute();
    }
    
    /*
     *Testa o login duas vezes
     */
    
    public void successLoginTwice() {
    	LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
        
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
    public void remoteLoginUnknownUser() {
    	LoginUserIntegrator service = new LoginUserIntegrator("jp2", PASSWORD);

    	new Expectations(){
    		{
    			idRemote.loginUser("jp2",anyString);
    			result = new LoginBubbleDocsException();
    		}
    	};

    		service.execute();
    	
    }


    @Test(expected = LoginBubbleDocsException.class)
    public void remoteLoginWrongPassword() {
    	LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, "jp2");
        
        new Expectations(){
        	{
        		idRemote.loginUser(USERNAME,"jp2");
        		result = new LoginBubbleDocsException();
        	}
        };
        
        service.execute();
    }
  
    
}
