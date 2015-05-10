package pt.tecnico.bubbledocs.test.integration.system;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

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
				idRemote.createUser(USERNAME_01, USERNAME_01_PASSWORD);
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


		AssignReferenceCell service6 = new AssignReferenceCell(userToken, service3.getSheetId(),"2;2","0;0");
		service6.execute();
		result = service6.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);


		DeleteUserIntegrator integrator0 = new DeleteUserIntegrator(rootToken, USERNAME_01);
		new Expectations(){
			{
				idRemote.removeUser(USERNAME_01);
			}
		};
		integrator0.execute();
	}

}
