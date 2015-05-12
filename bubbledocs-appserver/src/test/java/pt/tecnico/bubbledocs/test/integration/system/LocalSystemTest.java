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
import pt.tecnico.bubbledocs.domain.ADD;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.service.local.AssignBinaryFunctionToCell;
import pt.tecnico.bubbledocs.service.local.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.local.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.local.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;


public class LocalSystemTest{

	@Mocked
	private IDRemoteServices idRemote;
	@Mocked
	private StoreRemoteServices storeRemote;

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
		int id = service3.getSheetId();

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


		

		final ExportDocumentIntegrator expdoc= new ExportDocumentIntegrator(userToken,id);
		new Expectations(){
			{
				storeRemote.storeDocument(USERNAME_01,"folha",(byte[]) withNotNull());			
			}
		};
		expdoc.execute();
		
		final ImportDocumentIntegrator impdoc= new ImportDocumentIntegrator(userToken,id);
		new Expectations(){
			{
				storeRemote.loadDocument(USERNAME_01,"folha");
				result=expdoc.getDocXMLBytes();
			}
		};
		impdoc.execute();
		
		SpreadSheet importedSheet=new SpreadSheet();
		importedSheet.importFromXML(impdoc.getDocXML(), USERNAME_01);
		assertEquals("folha",importedSheet.getName());
		assertEquals("Size: n.lines", 10,importedSheet.getLines());
		assertEquals("Size: n.columns",10,importedSheet.getColumns());
		assertEquals("N. cells", 3,importedSheet.getCellSet().size());
		assertEquals("Cell: class",new ADD().getClass(),importedSheet.getSingleCell(2,2).getContent().getClass());
		assertEquals("Cell: class",new Literal().getClass(),importedSheet.getSingleCell(1,1).getContent().getClass());
		assertEquals("Cell: class",new Reference().getClass(),importedSheet.getSingleCell(0,0).getContent().getClass());
		assertEquals("Cell: result",2,importedSheet.getSingleCell(2,2).getContent().getResult());
		assertEquals("Cell: result",11,importedSheet.getSingleCell(1,1).getContent().getResult());
		assertEquals("Cell: result",2,importedSheet.getSingleCell(0,0).getContent().getResult());
		
		
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
