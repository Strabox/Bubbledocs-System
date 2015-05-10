package pt.tecnico.bubbledocs.test.integration.system;

import static org.junit.Assert.assertEquals;

import javax.transaction.SystemException;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.service.local.AssignBinaryFunctionToCell;
import pt.tecnico.bubbledocs.service.local.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.local.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.local.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class LocalSystemTest{

	@Mocked
	private IDRemoteServices idRemote;

	private String rootToken;
	private String userToken;
	private static final String ROOT_USERNAME = "root";
	private static final String ROOT_PASSWORD = "root";

	private static final String USERNAME_01 = "House";
	private static final String USERNAME_01_PASSWORD = "Vegas";
	private static final String USERNAME_01_EMAIL = "Mr_House@Lucky38";
	private static final String NAME_01 = "Mr. House";
	
	
	@Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
        }  catch (WriteOnReadError | SystemException e) {
        	System.out.println(e);
        }
    }


	@Test
	public void SequencialTest(){
		
		LoginUserIntegrator service = new LoginUserIntegrator(ROOT_USERNAME, ROOT_PASSWORD);
		
		new Expectations(){
			{
				idRemote.loginUser(ROOT_USERNAME,ROOT_PASSWORD);
			}
		}; 
		service.execute();

		rootToken = service.getUserToken();

		CreateUserIntegrator service1 = new CreateUserIntegrator(rootToken, USERNAME_01, 
				USERNAME_01_EMAIL, NAME_01);

		new Expectations(){
			{
				idRemote.createUser(USERNAME_01, USERNAME_01_EMAIL);
			}
		};
		service1.execute();


		/*
        GetUserInfoService gis = new GetUserInfoService(USERNAME_01);
		gis.execute();
		UserDTO dto = gis.getUserData();

		assertTrue(dto.getEmail() == USERNAME_01_EMAIL);
		assertTrue(dto.getName() == NAME_01);
		assertTrue(dto.getPassword() == USERNAME_01_PASSWORD);
		assertTrue(dto.getUsername() == USERNAME_01);
		 */


		LoginUserIntegrator service2 = new LoginUserIntegrator(USERNAME_01, USERNAME_01_PASSWORD);
		new Expectations(){
			{
				idRemote.loginUser(USERNAME_01, USERNAME_01_PASSWORD);
			}
		}; 
		service2.execute();
		userToken = service2.getUserToken();


		CreateSpreadSheet service3 = new CreateSpreadSheet(userToken, "folha", 10, 10);
		service3.execute();


		AssignBinaryFunctionToCell service4 = new AssignBinaryFunctionToCell(userToken,service3.getSheetId(), "2;2", "=ADD(1,1)");
		service4.execute();
		int result = service4.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 2);


		AssignLiteralCell service5 = new AssignLiteralCell(userToken, service3.getSheetId(), "1;1", "11");
		service5.execute();
		result = service5.getResult();
		assertEquals("Result of assigned cell different from unexpected.", result, 11);


		AssignReferenceCell service6 = new AssignReferenceCell(userToken, service3.getSheetId(),"0;0","2;2");
		service6.execute();
		result = service6.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 2);


		DeleteUserIntegrator integrator0 = new DeleteUserIntegrator(rootToken, USERNAME_01);
		new Expectations(){
			{
				idRemote.removeUser(USERNAME_01);
			}
		};
		integrator0.execute();
	}
	
	@After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
        } catch (SystemException | IllegalStateException | SecurityException e) {
            System.out.println(e);
        }
    }

}
