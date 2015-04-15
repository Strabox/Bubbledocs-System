package pt.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.AccessMode;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.ADD;
import pt.tecnico.bubbledocs.domain.DIV;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Permission;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;


public class ExportDocumentTest extends BubbleDocsServiceTest {
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
		userNoPerm = createUser(USERNAMENOPERM, "pass", "no permissions");
		userRead = createUser(USERNAMEREAD, "pass", "can read");
		userWrite = createUser(USERNAMEWRITE, "pass", "can write");
		userOwner = createUser(USERNAMEOWNER, "pass", "is owner");
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


	@Test
	public void owner() {
		String tokenOwner = addUserToSession(USERNAMEOWNER);
		ExportDocument expDoc = new ExportDocument(tokenOwner,sheet.getId());
		expDoc.createXML(); // create byte[] to be used as expectation
		new Expectations(){
        	{
        		storeRemote.storeDocument(userOwner.getUsername(),sheet.getName(),expDoc.getDocXMLBytes());
        		
        	}
        };
        
		expDoc.execute();
		removeUserFromSession(tokenOwner);
		// test
		expDoc.deserialize(expDoc.getDocXMLBytes());
		org.jdom2.Document tempDocXML = expDoc.getDocXML();
		importedSheet.importFromXML(tempDocXML, USERNAMEOWNER);
		assertEquals("Name", sheet.getName(),importedSheet.getName());
		assertEquals("Size: n.lines", sheet.getLines(),importedSheet.getLines());
		assertEquals("Size: n.columns", sheet.getColumns(),importedSheet.getColumns());
		assertEquals("N. cells", sheet.getCellSet().size(),importedSheet.getCellSet().size());
		for(Cell cell : sheet.getCellSet()){			
			assertEquals("Cell: content",cell.getContent().getClass(),importedSheet.getSingleCell(cell.getLine(),cell.getColumn()).getContent().getClass());
			assertEquals("Cell: content",cell.getResult(),importedSheet.getSingleCell(cell.getLine(),cell.getColumn()).getResult());

		}
	}

	
	
	@Test
	public void canWrite() {
		String token = addUserToSession(USERNAMEWRITE);
		ExportDocument expDoc = new ExportDocument(token,sheet.getId());
		expDoc.createXML(); // create byte[] to be used as expectation
		new Expectations(){
        	{
        		storeRemote.storeDocument(userWrite.getUsername(),sheet.getName(),expDoc.getDocXMLBytes());
        		
        	}
        };
		expDoc.execute();
		removeUserFromSession(token);
		// test
		expDoc.deserialize(expDoc.getDocXMLBytes());
		org.jdom2.Document tempDocXML = expDoc.getDocXML();
		importedSheet.importFromXML(tempDocXML, USERNAMEWRITE);
		assertEquals("Name", sheet.getName(),importedSheet.getName());
		assertEquals("Size: n.lines", sheet.getLines(),importedSheet.getLines());
		assertEquals("Size: n.columns", sheet.getColumns(),importedSheet.getColumns());
		assertEquals("N. cells", sheet.getCellSet().size(),importedSheet.getCellSet().size());
		for(Cell cell : sheet.getCellSet()){			
			assertEquals("Cell: content",cell.getContent().getClass(),importedSheet.getSingleCell(cell.getLine(),cell.getColumn()).getContent().getClass());
			assertEquals("Cell: content",cell.getResult(),importedSheet.getSingleCell(cell.getLine(),cell.getColumn()).getResult());

		}
	}
	@Test
	public void canRead() {
		String token = addUserToSession(USERNAMEREAD);
		ExportDocument expDoc = new ExportDocument(token,sheet.getId());
		expDoc.createXML(); // create byte[] to be used as expectation
		new Expectations(){
        	{
        		storeRemote.storeDocument(userRead.getUsername(),sheet.getName(),expDoc.getDocXMLBytes());
        		
        	}
        };
		expDoc.execute();
		removeUserFromSession(token);
		// test
		expDoc.deserialize(expDoc.getDocXMLBytes());
		org.jdom2.Document tempDocXML = expDoc.getDocXML();
		importedSheet.importFromXML(tempDocXML, USERNAMEREAD);
		assertEquals("Name", sheet.getName(),importedSheet.getName());
		assertEquals("Size: n.lines", sheet.getLines(),importedSheet.getLines());
		assertEquals("Size: n.columns", sheet.getColumns(),importedSheet.getColumns());
		assertEquals("N. cells", sheet.getCellSet().size(),importedSheet.getCellSet().size());
		for(Cell cell : sheet.getCellSet()){			
			assertEquals("Cell: content",cell.getContent().getClass(),importedSheet.getSingleCell(cell.getLine(),cell.getColumn()).getContent().getClass());
			assertEquals("Cell: content",cell.getResult(),importedSheet.getSingleCell(cell.getLine(),cell.getColumn()).getResult());

		}
	}
	
	@Test (expected=UnavailableServiceException.class)
	public void unavailableService() {
		String token = addUserToSession(USERNAMEREAD);
		ExportDocument expDoc = new ExportDocument(token,sheet.getId());
		expDoc.createXML(); // create byte[] to be used as expectation
		new Expectations(){
			{
				storeRemote.storeDocument(userRead.getUsername(),sheet.getName(),expDoc.getDocXMLBytes());
				result = new RemoteInvocationException();

			}
		};
		expDoc.execute();
		removeUserFromSession(token);
		// test
		expDoc.deserialize(expDoc.getDocXMLBytes());
		org.jdom2.Document tempDocXML = expDoc.getDocXML();
		importedSheet.importFromXML(tempDocXML, USERNAMEREAD);
		assertEquals("Name", sheet.getName(),importedSheet.getName());
		assertEquals("Size: n.lines", sheet.getLines(),importedSheet.getLines());
		assertEquals("Size: n.columns", sheet.getColumns(),importedSheet.getColumns());
		assertEquals("N. cells", sheet.getCellSet().size(),importedSheet.getCellSet().size());
		for(Cell cell : sheet.getCellSet()){			
			assertEquals("Cell: content",cell.getContent().getClass(),importedSheet.getSingleCell(cell.getLine(),cell.getColumn()).getContent().getClass());
			assertEquals("Cell: content",cell.getResult(),importedSheet.getSingleCell(cell.getLine(),cell.getColumn()).getResult());

		}
	}
	
	@Test (expected=UnauthorizedOperationException.class)
	public void cantRead() {
		String token = addUserToSession(USERNAMENOPERM);
		ExportDocument expDoc = new ExportDocument(token,sheet.getId());
		expDoc.createXML(); // create byte[] to be used as expectation
		new Expectations(){
        	{
        		storeRemote.storeDocument(userNoPerm.getUsername(),sheet.getName(),expDoc.getDocXMLBytes());
        		
        	}
        };
		expDoc.execute();
		removeUserFromSession(token);    
		//this assert wont execute
		assertEquals("...", 0, 0);
	}
	
	
	

}


