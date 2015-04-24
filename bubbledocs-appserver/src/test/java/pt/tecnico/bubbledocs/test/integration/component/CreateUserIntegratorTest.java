package pt.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.*;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class CreateUserIntegratorTest extends BubbleDocsServiceTest {

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
    private static final String EMAIL_DOES_NOT_EXIST = "not-exist@ESisAwesome.ist";
    private static final String INVALID_EMAIL = "invalid-email";
    
    @Override
    public void populate4Test() {
        createUser(USERNAME,EMAIL ,PASSWORD, "António Rito Silva");
        root = addUserToSession(ROOT_USERNAME);
        ars = addUserToSession("ars");
    }

    
    @Test
    public void success() {
        CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, 
        		EMAIL_DOES_NOT_EXIST,"José Ferreira");
        
        new Expectations(){
        	{
        		idRemote.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_DOES_NOT_EXIST);
        	}
        };
        service.execute();
        
        User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals(EMAIL_DOES_NOT_EXIST, user.getEmail());
        assertEquals("José Ferreira", user.getName());
    }
    
    /*-------------------- Local fail remote never executed ------------------ */
    
    @Test(expected = DuplicateUsernameException.class)
    public void usernameExists() {
    	CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME, 
        		EMAIL_DOES_NOT_EXIST,"José Ferreira");
        
        new Expectations(){
        	{
            String username = USERNAME;
        	idRemote.createUser(username,EMAIL_DOES_NOT_EXIST);
        	times = 0;
        	}
        };
        service.execute();
    }

    @Test(expected = InvalidUsernameException.class)
    public void emptyUsername() {
    	CreateUserIntegrator service = new CreateUserIntegrator(root, EMPTY_USERNAME, 
        		EMAIL_DOES_NOT_EXIST,"José Ferreira");
        
        new Expectations(){
        	{
        	idRemote.createUser(EMPTY_USERNAME, EMAIL_DOES_NOT_EXIST);
        	times = 0;
        	}
        };
        service.execute();
    }
     
    @Test(expected = InvalidUsernameException.class)
    public void smallUsername() {
    	CreateUserIntegrator service = new CreateUserIntegrator(root, SMALL_USERNAME, 
        		EMAIL_DOES_NOT_EXIST, "José Ferreira");
        
        new Expectations(){
        	{
        	idRemote.createUser(SMALL_USERNAME, EMAIL_DOES_NOT_EXIST);
        	times = 0;
        	}
        };
        service.execute();
    }
    
    @Test(expected = InvalidUsernameException.class)
    public void bigUsername() {
    	CreateUserIntegrator service = new CreateUserIntegrator(root, BIG_USERNAME, 
        		EMAIL_DOES_NOT_EXIST,"José Ferreira");
        
        new Expectations(){
        	{
        	idRemote.createUser(BIG_USERNAME, EMAIL_DOES_NOT_EXIST);
        	times = 0;
        	}
        };
        service.execute();
    }
    
    @Test
    public void duplicateEmail(){
    	CreateUserIntegrator service = new CreateUserIntegrator(root,USERNAME_DOES_NOT_EXIST,
    	EMAIL,"José Ferreira");
    	
    	new Expectations(){
    		{
    		idRemote.createUser(USERNAME_DOES_NOT_EXIST, EMAIL);	
    		result = new DuplicateEmailException();
    		}
    	};
    	
    	try{
    		service.execute();
    		fail("DuplicateEmailException expected");
    	}catch(DuplicateEmailException e){
    		assertNull(Bubbledocs.getInstance().getUserByName(USERNAME_DOES_NOT_EXIST));
    	}
    }

    @Test
    public void invalidEmail(){
    	CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST
    			,INVALID_EMAIL,"José Ferreira");
    	
    	new Expectations(){
    		{
    		idRemote.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL);
    		result = new InvalidEmailException();
    		}
    	};
    	try{
    		service.execute();
    		fail("DuplicateEmailException expected");
    	}catch(InvalidEmailException e){
    		assertNull(Bubbledocs.getInstance().getUserByName(USERNAME_DOES_NOT_EXIST));
    	}
    }
    
    /*-----------------------------------------------------------------------*/
    /*---------------------- Local Success Remote Fails ---------------------*/
    
    
    /*-----------------------------------------------------------------------*/
    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserCreation() {
    	CreateUserIntegrator service = new CreateUserIntegrator(ars, USERNAME_DOES_NOT_EXIST,
        		EMAIL_DOES_NOT_EXIST,"José Ferreira");
        
        new Expectations(){
    		{
    		idRemote.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_DOES_NOT_EXIST);	
    		times = 0;
    		}
    	};
        service.execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void accessUsernameNotExist() {
        removeUserFromSession(root);
        
        CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST,
        		EMAIL_DOES_NOT_EXIST,"José Ferreira");
        
        new Expectations(){
    		{
    		idRemote.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_DOES_NOT_EXIST);	
    		times = 0; 
    		}
    	};
        service.execute();
    }
    
    @Test
    public void unavailableIdRemoteServer(){
    	CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST,
    			EMAIL_DOES_NOT_EXIST,"José Ferreira");
    	
    	new Expectations(){
    		{
			idRemote.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_DOES_NOT_EXIST);
			result = new RemoteInvocationException();
    		}
    	};
    	
    	try{
	    	service.execute();
	    	fail("UnavailableServiceException expected.");
    	}catch(UnavailableServiceException e){
    		assertNull(Bubbledocs.getInstance().getUserByName(USERNAME_DOES_NOT_EXIST));
    	}
    }

}
