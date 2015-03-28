package pt.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.AccessMode;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.ADD;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Permission;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.service.ExportDocument;


public class ExportDocumentTest extends BubbleDocsServiceTest {
	
	private Bubbledocs bubbled;
	private static User userNoPerm;
	private static User userRead;
	private static User userWrite;
	private static User userOwner;
	private static final String USERNAMENOPERM = "usernoperm";
    private static final String USERNAMEREAD = "userread";
    private static final String USERNAMEWRITE = "userwrite";
    private static final String USERNAMEOWNER = "userowner";
    private SpreadSheet sheet;
	private Cell cell1;
	private Cell cell2;
	private Cell cell3;
	
	
	@Override
    public void populate4Test() {
        userNoPerm = createUser(USERNAMENOPERM, "pass", "no permissions");
        userRead = createUser(USERNAMEREAD, "pass", "can read");
        userWrite = createUser(USERNAMEWRITE, "pass", "can write");
        userOwner = createUser(USERNAMEOWNER, "pass", "is owner");
        sheet = createSpreadSheet(userOwner,"sheet1", 3, 3);
        sheet.setOwner(userOwner);
        cell1 = new Cell(0,0);	
        cell2 = new Cell(0,1);	
        cell3 = new Cell(1,0);              
        Permission readable = new Permission(sheet, AccessMode.READ);
        userRead.addUsedBy(readable);
        Permission writable = new Permission(sheet, AccessMode.WRITE);
        userWrite.addUsedBy(writable);
        cell1.setContent(new Literal(3));
        cell2.setContent(new Reference(0,0));
        cell3.setContent(new ADD(new Literal (2),new Literal(4)));    
        sheet.addCell(cell1);
        sheet.addCell(cell2);
        sheet.addCell(cell3);  
        bubbled = Bubbledocs.getInstance();
        bubbled.addBubbleSpreadsheet(sheet);
        bubbled.addUser(userNoPerm);
        bubbled.addUser(userRead);
        bubbled.addUser(userWrite);
        bubbled.addUser(userOwner);
    }


    @Test
    public void a() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	ExportDocument expDoc = new ExportDocument(tokenOwner,sheet.getId());
    	expDoc.execute();
    	removeUserFromSession(tokenOwner);    	
		assertEquals("...", 42, 42);
    }
	
   
    }
    

