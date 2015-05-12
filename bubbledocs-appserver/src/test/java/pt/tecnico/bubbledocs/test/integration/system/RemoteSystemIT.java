package pt.tecnico.bubbledocs.test.integration.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.transaction.SystemException;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.integration.GetSpreadSheetContentIntegrator;
import pt.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.integration.RenewPasswordIntegrator;
import pt.tecnico.bubbledocs.service.local.AssignBinaryFunctionToCell;
import pt.tecnico.bubbledocs.service.local.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.local.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.local.CreateSpreadSheet;

public class RemoteSystemIT {
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
		
		/* Login with root to create a user and login with it to do stuff*/
		
		LoginUserIntegrator service = new LoginUserIntegrator(ROOT_USERNAME, ROOT_PASSWORD);
		service.execute();
		rootToken = service.getUserToken();
		System.out.println("\n\n\n\n\n\n\n\n"+rootToken);

		CreateUserIntegrator service1 = new CreateUserIntegrator(rootToken, USERNAME_01, USERNAME_01_EMAIL, NAME_01);
		service1.execute();

		LoginUserIntegrator service2 = new LoginUserIntegrator(USERNAME_01, USERNAME_01_PASSWORD);
		service2.execute();
		userToken = service2.getUserToken();

		/* New user creates a spreadsheet*/
		
		CreateSpreadSheet service3 = new CreateSpreadSheet(userToken, "folha", 10, 10);
		service3.execute();


		AssignBinaryFunctionToCell service4 = new AssignBinaryFunctionToCell(userToken,service3.getSheetId(), "2;2", "=ADD(1,1)");
		service4.execute();
		assertEquals("Result of referred cell different from unexpected.", service4.getResult(), 2);


		AssignLiteralCell service5 = new AssignLiteralCell(userToken, service3.getSheetId(), "1;1", "11");
		service5.execute();
		assertEquals("Result of assigned cell different from unexpected.", service5.getResult(), 11);


		AssignReferenceCell service6 = new AssignReferenceCell(userToken, service3.getSheetId(),"0;0","2;2");
		service6.execute();
		assertEquals("Result of referred cell different from unexpected.", service6.getResult(), 2);

		GetSpreadSheetContentIntegrator service7 = new GetSpreadSheetContentIntegrator(userToken, service3.getSheetId());
		service7.execute();
		String[][] ssprint1 = service7.getResult();
		assertTrue("Spreadsheet cell output different from expected",ssprint1[2][2].equals(11));
		assertTrue("Spreadsheet cell output different from expected",ssprint1[0][0].equals(11));
		
		ExportDocumentIntegrator service8 = new ExportDocumentIntegrator(userToken, service3.getSheetId());
		service8.execute();
		
		ImportDocumentIntegrator service9 = new ImportDocumentIntegrator(userToken, service3.getSheetId());
		service9.execute();
		assertTrue("spreadsheet owner's username different from expected",service9.getUsername().equals(USERNAME_01));
		assertTrue("Spreadsheet name different from expected",service9.getSheetname().equals("folha"));
		
		RenewPasswordIntegrator service10 = new RenewPasswordIntegrator(userToken);
		service10.execute();
		
		DeleteUserIntegrator integrator0 = new DeleteUserIntegrator(rootToken, USERNAME_01);
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
