package pt.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.RenewPasswordIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.test.BubbleDocsServiceTest;

public class RenewPasswordIntegratorTest extends BubbleDocsServiceTest {
	@Mocked
	private IDRemoteServices remoteID;
	
	private final String USERNAME_EXISTS = "renewEX";
	private final String USERNAME_DOES_NOT_EXIST = "renewDNX";
    private final String PASSWORD = "hunter2";
    private final String EMAIL = "renew@able.energy";
    private final String EMAIL2 = "renew2@able.energy";
    
    private String tokenExists;
    private String tokenDoesNotExist;

    @Override
    public void populate4Test() {
        createUser(USERNAME_EXISTS, EMAIL, PASSWORD, "Mr Renew");
        tokenExists = addUserToSession(USERNAME_EXISTS);
        createUser(USERNAME_DOES_NOT_EXIST, EMAIL2, PASSWORD, "static electricity");
        tokenDoesNotExist = addUserToSession(USERNAME_DOES_NOT_EXIST);
    }
	
    /*
     * Tests:
     * Everything works (success)
     * No session
     * Can't reach server
     * Login failure
     * Username doesn't exist on the server
     * 
     */
    
    /*
     * Passes if the service executes and the password is null'd.
     */
    @Test
    public void success() {
    	RenewPasswordIntegrator service = new RenewPasswordIntegrator(tokenExists);
        
        new Expectations(){
        	{
        		remoteID.renewPassword(USERNAME_EXISTS);
        	}
        };
        
        service.execute();

        User user = getUserFromUsername(USERNAME_EXISTS);
        assertNull(user.getPassword());
    }

    /*
     * 
     */
    @Test(expected=UserNotInSessionException.class)
    public void testRenewUserNotInSession(){
    	removeUserFromSession(tokenExists);
    	RenewPasswordIntegrator service = new RenewPasswordIntegrator(tokenExists);
        service.execute();
    }
    
    /*
     * 
     */
   @Test
   public void testRenewCantReachServer(){
   	RenewPasswordIntegrator service = new RenewPasswordIntegrator(tokenExists);
   	
   	new Expectations(){
       	{
       		remoteID.renewPassword(USERNAME_EXISTS);
       		result = new RemoteInvocationException();
       	}
       };
       try{
       		service.execute();
       		fail();
       }
       catch(UnavailableServiceException e){
    	   User user = getUserFromUsername(USERNAME_EXISTS);
           assertNull(user.getPassword());
       }
       
   }
    
    @Test
    public void testRenewWrongServerLogin(){
    	RenewPasswordIntegrator service = new RenewPasswordIntegrator(tokenExists);
    	
    	new Expectations(){
        	{
        		remoteID.renewPassword(USERNAME_EXISTS);
        		result = new LoginBubbleDocsException();
        	}
        };
        try{
        	service.execute();
        	fail();
        }
        catch(LoginBubbleDocsException e){
        	User user = getUserFromUsername(USERNAME_EXISTS);
            assertNull(user.getPassword());
        }
      
    }
    
    @Test
    public void testRenewUserNotInServer(){
    	RenewPasswordIntegrator service = new RenewPasswordIntegrator(tokenDoesNotExist);
    	
    	new Expectations(){
        	{
        		remoteID.renewPassword(USERNAME_DOES_NOT_EXIST);
        		result = new LoginBubbleDocsException();
        	}
        };
        try{
        	service.execute();
        	fail();
        }
        catch(LoginBubbleDocsException e){
        	User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
            assertNull(user.getPassword());
        }
    }
}
