package pt.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class CreateUserTest extends BubbleDocsServiceTest {

	/* Mock RemoteServices to cover all test cases and avoid the need
	 * for put the real remote service working.*/
	@Mocked
	private IDRemoteServices idRemote;
	
    // The tokens
    private String root;
    private String ars;

    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME = "ars";
    private static final String EMPTY_USERNAME = "";
    private static final String SMALL_USERNAME = "lo";
    private static final String BIG_USERNAME = "Qu3Grand3";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    
    private static final String PASSWORD = "ars";
    private static final String EMAIL = "jp@ESisAwesome.ist";
 

    @Override
    public void populate4Test() {
        createUser(USERNAME,EMAIL ,PASSWORD, "António Rito Silva");
        root = addUserToSession(ROOT_USERNAME);
        ars = addUserToSession("ars");
    }

    
    @Test
    public void success() {
        CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, 
        		"jf1@ESisAwesome.ist","José Ferreira");
        
        new Expectations(){
        	{
        		idRemote.createUser(USERNAME_DOES_NOT_EXIST, "jf1@ESisAwesome.ist");
        	}
        };
        service.execute();
        
        User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals("jf1@ESisAwesome.ist", user.getEmail());
        assertEquals("José Ferreira", user.getName());
    }

    @Test(expected = DuplicateUsernameException.class)
    public void usernameExists() {
        CreateUser service = new CreateUser(root, USERNAME, 
        		"jf2@ESisAwesome.ist","José Ferreira");
        
        new Expectations(){
        	{
            String username = USERNAME;
        	idRemote.createUser(username,"jf2@ESisAwesome.ist");
        	result = new DuplicateUsernameException(username);
        	}
        };
        
        service.execute();
    }

    @Test(expected = EmptyUsernameException.class)
    public void emptyUsername() {
        CreateUser service = new CreateUser(root, EMPTY_USERNAME, "empty@ESisAwesome.ist"
        ,"José Ferreira");
        
        new Expectations(){
        	{
        	idRemote.createUser(EMPTY_USERNAME, "empty@ESisAwesome.ist");
        	result = new EmptyUsernameException();
        	}
        };
        service.execute();
    }
    
    
    
    @Test(expected = InvalidUsernameException.class)
    public void smallUsername() {
        CreateUser service = new CreateUser(root, SMALL_USERNAME, "small@ESisAwesome.ist"
        ,"José Ferreira");
        
        new Expectations(){
        	{
        	idRemote.createUser(SMALL_USERNAME, "small@ESisAwesome.ist");
        	result = new InvalidUsernameException();
        	}
        };
        service.execute();
    }
    
    @Test(expected = InvalidUsernameException.class)
    public void bigUsername() {
        CreateUser service = new CreateUser(root, BIG_USERNAME, "big@ESisAwesome.ist"
        ,"José Ferreira");
        
        new Expectations(){
        	{
        	idRemote.createUser(BIG_USERNAME, "big@ESisAwesome.ist");
        	result = new InvalidUsernameException();
        	}
        };
        service.execute();
    }
    
    @Test(expected = DuplicateEmailException.class)
    public void duplicateEmail(){
    	CreateUser service = new CreateUser(root,"otherUsername",EMAIL,"José Ferreira");
    	
    	new Expectations(){
    		{
    		idRemote.createUser("otherUsername", EMAIL);	
    		result = new DuplicateEmailException();
    		}
    	};
    	
    	service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserCreation() {
        CreateUser service = new CreateUser(ars, USERNAME_DOES_NOT_EXIST,
        		"jf@ESisAwesome.ist","José Ferreira");
        
        new Expectations(){
    		{
    		idRemote.createUser(USERNAME_DOES_NOT_EXIST, "jf@ESisAwesome.ist");	
    		times = 0;
    		}
    	};
        service.execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void accessUsernameNotExist() {
        removeUserFromSession(root);
        
        CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST,
        		"jf@ESisAwesome.ist","José Ferreira");
        
        new Expectations(){
    		{
    		idRemote.createUser(USERNAME_DOES_NOT_EXIST, "jf@ESisAwesome.ist");	
    		times = 0; 
    		}
    	};
        service.execute();
    }
    
    @Test(expected = UnavailableServiceException.class)
    public void unavailableIdRemoteServer(){
    	CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST,
        		EMAIL,"José Ferreira");
    	
    	new Expectations(){
    		{
			idRemote.createUser(USERNAME_DOES_NOT_EXIST, EMAIL);
			result = new RemoteInvocationException();
    		}
    	};
    	service.execute();
    }

}
