package pt.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.test.BubbleDocsServiceTest;


public class DeleteUserIntegratorTest extends BubbleDocsServiceTest {

	@Mocked
	private IDRemoteServices idRemote;

	private String root;
	private String wrongToken;

	private static final String ROOT_USERNAME = "root";
	private static final String USERNAME_00 = "Wesker";
	private static final String USERNAME_01 = "Michael";
	private static final String USERNAME_02 = "Gordon";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";

	private static final String PASSWORD = "boot";
	private static final String EMAIL = "gaben@valve.com";
	@Override
	public void populate4Test() {
		createUser(USERNAME_00, EMAIL, PASSWORD, "Albert");
		createUser(USERNAME_01, EMAIL, PASSWORD, "Trevor");
		createUser(USERNAME_02, EMAIL, PASSWORD, "Gabe Newell");
		root = addUserToSession(ROOT_USERNAME);
		wrongToken = addUserToSession(USERNAME_02);
	}

	@Test
	public void success() {
		DeleteUserIntegrator integrator0 = new DeleteUserIntegrator(root, "Wesker");
		DeleteUserIntegrator integrator1 = new DeleteUserIntegrator(root, "Gordon");

		new Expectations(){
			{
				idRemote.removeUser(USERNAME_00);  //Not in session
				idRemote.removeUser(USERNAME_02);  //In session
			}
		};
		integrator0.execute();
		integrator1.execute();

		User user = getUserFromUsername(USERNAME_00);
		assertNull(user);

		user = getUserFromUsername(USERNAME_02);
		assertNull(user);

		user = getUserFromUsername(USERNAME_01);
		assertNotNull(user);

	}

	@Test(expected = LoginBubbleDocsException.class)
	public void userDoesNotExist(){
		DeleteUserIntegrator integrator0 = new DeleteUserIntegrator(root, USERNAME_DOES_NOT_EXIST);

		new Expectations(){
			{
				idRemote.removeUser(USERNAME_DOES_NOT_EXIST);
			}
		};

		integrator0.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedUserDeletion() {
		DeleteUserIntegrator integrator0 = new DeleteUserIntegrator(wrongToken, USERNAME_01);

		new Expectations(){
			{
				idRemote.removeUser(USERNAME_01);
				times = 0;
			}
		};
		
		integrator0.execute();
	}

	@Test
	public void unavailableIdRemoteServer(){
		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_01);

		new Expectations(){
			{
				idRemote.removeUser(USERNAME_01);
				result = new RemoteInvocationException();
			}
		};

		try{
			service.execute();
			fail("UnavailableServiceException expected.");
		}catch(UnavailableServiceException e){
			assertNotNull(Bubbledocs.getInstance().getUserByName(USERNAME_01));
		}
	}

}