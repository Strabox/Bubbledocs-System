package pt.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.ADD;
import pt.tecnico.bubbledocs.domain.AccessMode;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.DIV;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Permission;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.local.ExportDocument;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.tecnico.bubbledocs.test.BubbleDocsServiceTest;


public class ImportDocumentIntegratorTest extends BubbleDocsServiceTest {
	@Mocked
	private StoreRemoteServices storeRemote;
	private Bubbledocs bubbled;
	private static User userNoPerm;
	private static User userRead;
	private static User userWrite;
	private static User userOwner;
	private static final String USERNAMENOPERM = "usernope";
	private static final String USERNAMEREAD = "userread";
	private static final String USERNAMEWRITE = "userwrit";
	private static final String USERNAMEOWNER = "userowne";
	private SpreadSheet sheet;	
	private Cell cell1;
	private Cell cell2;
	private Cell cell3;
	private Cell cell4;
	private Cell cell5;
	SpreadSheet importedSheet;


	@Override
	public void populate4Test() {
		userNoPerm = createUser(USERNAMENOPERM, "user1@user.pt","pass" ,"no permissions");
		userRead = createUser(USERNAMEREAD, "user2@user.pt", "pass" ,"can read");
		userWrite = createUser(USERNAMEWRITE, "user3@user.pt","pass", "can write");
		userOwner = createUser(USERNAMEOWNER, "user4@user.pt","pass", "is owner");
		sheet = createSpreadSheet(userOwner,"sheet1", 300, 200);
		sheet.setOwner(userOwner);
		cell1 = new Cell(0,0);	
		cell2 = new Cell(0,1);	
		cell3 = new Cell(1,0); 
		cell4 = new Cell(1,1);
		cell5 = new Cell(2,0);  
		Permission readable = new Permission(sheet, AccessMode.READ);
		userRead.addUsedBy(readable);
		Permission writable = new Permission(sheet, AccessMode.WRITE);
		userWrite.addUsedBy(writable);
		cell1.setContent(new Literal(2)); 
		cell2.setContent(new Reference(0,0)); 
		cell3.setContent(new Reference(0,1)); 
		cell4.setContent(new ADD(new Literal(7),new Literal(3))); 
		cell5.setContent(new DIV(new Reference (1,1), new Reference (1,0)));
		sheet.addCell(cell1);
		sheet.addCell(cell2);
		sheet.addCell(cell3);
		sheet.addCell(cell4);
		sheet.addCell(cell5);  
		cell2.getContent().mountReference(cell2);
		cell3.getContent().mountReference(cell3);
		cell5.getContent().mountReference(cell5);
		bubbled = Bubbledocs.getInstance();
		bubbled.addBubbleSpreadsheet(sheet);
		bubbled.addUser(userNoPerm);
		bubbled.addUser(userRead);
		bubbled.addUser(userWrite);
		bubbled.addUser(userOwner);
		importedSheet = new SpreadSheet();
		
	}

	public void compareSpreadsheet(SpreadSheet expected, SpreadSheet result){
		assertEquals("Name", expected.getName(),result.getName());
		assertEquals("Size: n.lines", expected.getLines(),result.getLines());
		assertEquals("Size: n.columns", expected.getColumns(),result.getColumns());
		assertEquals("N. cells", expected.getCellSet().size(),result.getCellSet().size());
		for(Cell cell : expected.getCellSet()){			
			assertEquals("Cell: content",cell.getContent().getClass(),result.getSingleCell(cell.getLine(),cell.getColumn()).getContent().getClass());
			assertEquals("Cell: content",cell.getResult(),result.getSingleCell(cell.getLine(),cell.getColumn()).getResult());

		}

	}
	
																
	@Test
	public void owner() {
		String tokenOwner = addUserToSession(USERNAMEOWNER);
		ImportDocumentIntegrator impDoc = new ImportDocumentIntegrator(tokenOwner,sheet.getId());
		final ExportDocument localexport = new ExportDocument (tokenOwner,sheet.getId());
		localexport.execute();
		new Expectations(){
			{
				storeRemote.loadDocument(userOwner.getUsername(),sheet.getName());
				result = localexport.getDocXMLBytes();

			}
		};

		impDoc.execute();
		removeUserFromSession(tokenOwner);		
		importedSheet.importFromXML(impDoc.getDocXML(), USERNAMEOWNER);
		compareSpreadsheet(sheet,importedSheet);
	}
	
	
	@Test (expected=CannotLoadDocumentException.class)
	public void cannotLoadDocument() {
		String tokenOwner = addUserToSession(USERNAMEOWNER);
		ImportDocumentIntegrator impDoc = new ImportDocumentIntegrator(tokenOwner,sheet.getId());
		ExportDocument localexport = new ExportDocument (tokenOwner,sheet.getId());
		localexport.execute();
		new Expectations(){
			{
				storeRemote.loadDocument(userOwner.getUsername(),sheet.getName());
				result = new CannotLoadDocumentException();

			}
		};

		impDoc.execute();
		removeUserFromSession(tokenOwner);		
		
	}
	
	
	@Test (expected=UnavailableServiceException.class)
	public void UnavailableServiceException() {
		String tokenOwner = addUserToSession(USERNAMEOWNER);
		ImportDocumentIntegrator impDoc = new ImportDocumentIntegrator(tokenOwner,sheet.getId());
		ExportDocument localexport = new ExportDocument (tokenOwner,sheet.getId());
		localexport.execute();
		new Expectations(){
			{
				storeRemote.loadDocument(userOwner.getUsername(),sheet.getName());
				result = new RemoteInvocationException();

			}
		};		
		impDoc.execute();
		removeUserFromSession(tokenOwner);
		importedSheet.importFromXML(impDoc.getDocXML(), USERNAMEOWNER);
		compareSpreadsheet(sheet,importedSheet);
	}
	
														
	@Test (expected=UserNotInSessionException.class)
	public void userNotInSession() {
		String tokenOwner = addUserToSession(USERNAMEOWNER);
		ImportDocumentIntegrator impDoc = new ImportDocumentIntegrator(tokenOwner,sheet.getId());
		final ExportDocument localexport = new ExportDocument (tokenOwner,sheet.getId());
		localexport.execute();
		new Expectations(){
			{
				storeRemote.loadDocument(userOwner.getUsername(),sheet.getName());
				result = localexport.getDocXMLBytes();
				times = 0;

			}
		};
		removeUserFromSession(tokenOwner);	
		impDoc.execute();			
		importedSheet.importFromXML(impDoc.getDocXML(), USERNAMEOWNER);
		compareSpreadsheet(sheet,importedSheet);
	}
	

}


