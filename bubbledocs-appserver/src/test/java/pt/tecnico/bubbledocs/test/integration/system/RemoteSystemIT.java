package pt.tecnico.bubbledocs.test.integration.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.transaction.SystemException;


import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.integration.AssignBinaryFunctionToCellIntegrator;
import pt.tecnico.bubbledocs.integration.AssignLiteralCellIntegrator;
import pt.tecnico.bubbledocs.integration.AssignReferenceCellIntegrator;
import pt.tecnico.bubbledocs.integration.CreateSpreadSheetIntegrator;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.integration.GetSpreadSheetContentIntegrator;
import pt.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.integration.RenewPasswordIntegrator;

public class RemoteSystemIT {
	private String rootToken;
	private String userToken;
	private static final String ROOT_USERNAME = "root";
	private static final String ROOT_PASSWORD = "root";

	private static final String USERNAME_01 = "House";
	private static final String USERNAME_01_PASSWORD = "Vegas";
	private static final String USERNAME_01_EMAIL = "Mr_House@Lucky38.com";
	private static final String NAME_01 = "Mr. House";
	
	

	
	
	@Test
	public void SequencialTest(){
		
		/* Login with root to create a user and login with it to do stuff*/
		
		LoginUserIntegrator service = new LoginUserIntegrator(ROOT_USERNAME, ROOT_PASSWORD);
		service.execute();
		rootToken = service.getUserToken();
		
		CreateUserIntegrator service1 = new CreateUserIntegrator(rootToken, USERNAME_01, USERNAME_01_EMAIL, NAME_01);
		service1.execute();

		LoginUserIntegrator service2 = new LoginUserIntegrator("bruno", "Bbb2");
		service2.execute();
		userToken = service2.getUserToken();

		
		/* New user creates a spreadsheet*/
		
		CreateSpreadSheetIntegrator service3 = new CreateSpreadSheetIntegrator(userToken, "folha", 10, 10);
		service3.execute();


		AssignBinaryFunctionToCellIntegrator service4 = new AssignBinaryFunctionToCellIntegrator(userToken,service3.getSheetId(), "2;2", "=ADD(1,1)");
		service4.execute();
		assertEquals("Result of referred cell different from unexpected.", service4.getResult(), 2);


		AssignLiteralCellIntegrator service5 = new AssignLiteralCellIntegrator(userToken, service3.getSheetId(), "1;1", "11");
		service5.execute();
		assertEquals("Result of assigned cell different from unexpected.", service5.getResult(), 11);


		AssignReferenceCellIntegrator service6 = new AssignReferenceCellIntegrator(userToken, service3.getSheetId(),"0;0","2;2");
		service6.execute();
		assertEquals("Result of referred cell different from unexpected.", service6.getResult(), 2);

		GetSpreadSheetContentIntegrator service7 = new GetSpreadSheetContentIntegrator(userToken, service3.getSheetId());
		service7.execute();
		String[][] ssprint1 = service7.getResult();

		assertTrue("Spreadsheet cell output different from expected",ssprint1[2][2].equals("2"));
		assertTrue("Spreadsheet cell output different from expected",ssprint1[1][1].equals("11"));
		assertTrue("Spreadsheet cell output different from expected",ssprint1[0][0].equals("2"));
		
		ExportDocumentIntegrator service8 = new ExportDocumentIntegrator(userToken, service3.getSheetId());
		service8.execute();
		/*
		ImportDocumentIntegrator service9 = new ImportDocumentIntegrator(userToken, service3.getSheetId());
		service9.execute();
		assertTrue("spreadsheet owner's username different from expected",service9.getUsername().equals(USERNAME_01));
		assertTrue("Spreadsheet name different from expected",service9.getSheetname().equals("folha"));
		*/
		RenewPasswordIntegrator service10 = new RenewPasswordIntegrator(userToken);
		service10.execute();
		
		DeleteUserIntegrator integrator0 = new DeleteUserIntegrator(rootToken, USERNAME_01);
		integrator0.execute();
	}
	
	@After
	@Atomic
	public void tearDown(){
		Bubbledocs.getInstance().delete();
	}

}
