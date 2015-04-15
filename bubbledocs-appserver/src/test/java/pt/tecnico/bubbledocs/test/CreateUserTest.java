package pt.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class CreateUserTest extends BubbleDocsServiceTest {

	/* Mock RemoteServices to cover all test cases and avoid the need
	 * for put the real remote service working.*/
	@Mocked
	private IDRemoteServices idRemote;
	
    // the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String EMAIL = "jp@ESisAwesome.ist";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";

    @Override
    public void populate4Test() {
        createUser(USERNAME,EMAIL ,PASSWORD, "António Rito Silva");
        root = addUserToSession(ROOT_USERNAME);
        ars = addUserToSession("ars");
    }

    @Test
    public void success() {
        CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, 
        		"jf@ESisAwesome.ist","José Ferreira");
        
        new Expectations(){
        	{
        		idRemote.createUser(USERNAME_DOES_NOT_EXIST, "jf@ESisAwesome.ist");
        	}
        };
        service.execute();
        
        User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals("jf@ESisAwesome.ist", user.getEmail());
        assertEquals("José Ferreira", user.getName());
    }

    @Test(expected = DuplicateUsernameException.class)
    public void usernameExists() {
        CreateUser service = new CreateUser(root, USERNAME, 
        		"jf@ESisAwesome.ist","José Ferreira");
        
        new Expectations(){
        	{
            String username = USERNAME;
        	idRemote.createUser(username,"jf@ESisAwesome.ist");
        	result = new DuplicateUsernameException(username);
        	}
        };
        
        service.execute();
    }

    @Test(expected = EmptyUsernameException.class)
    public void emptyUsername() {
        CreateUser service = new CreateUser(root, "", "jf@ESisAwesome.ist"
        ,"José Ferreira");
        
        new Expectations(){
        	{
        	idRemote.createUser("", "jf@ESisAwesome.ist");
        	result = new EmptyUsernameException();
        	}
        };
        service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserCreation() {
        CreateUser service = new CreateUser(ars, USERNAME_DOES_NOT_EXIST,
        		"jf@ESisAwesome.ist","José Ferreira");

        service.execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void accessUsernameNotExist() {
        removeUserFromSession(root);
        
        CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST,
        		"jf@ESisAwesome.ist","José Ferreira");

        service.execute();
    }

}
