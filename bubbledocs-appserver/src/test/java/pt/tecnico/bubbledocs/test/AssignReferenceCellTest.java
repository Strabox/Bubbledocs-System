package pt.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.AccessMode;
import pt.tecnico.bubbledocs.domain.Permission;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BadSpreadSheetValuesException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;

public class AssignReferenceCellTest extends BubbleDocsServiceTest {
	
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
	private Cell withValue;
	private Cell noValue;
	private Cell holder;
	private Cell protect;
	
	@Override
    public void populate4Test() {
        userNoPerm = createUser(USERNAMENOPERM, "pass", "no permissions");
        userRead = createUser(USERNAMEREAD, "pass", "can read");
        userWrite = createUser(USERNAMEWRITE, "pass", "can write");
        userOwner = createUser(USERNAMEOWNER, "pass", "is owner");
        sheet = createSpreadSheet(userOwner,"sheet1", 3, 3);
        sheet.setOwner(userOwner);
        withValue = new Cell(0,0);	//should have a value
        noValue = new Cell(1,1);	//should have no value
        holder = new Cell(2,2);		//where content is placed
        protect = new Cell(1,2);	//where content can't be placed
        protect.setProtect(true);
        Permission readable = new Permission(sheet, AccessMode.READ);
        userRead.addUsedBy(readable);
        Permission writable = new Permission(sheet, AccessMode.WRITE);
        userWrite.addUsedBy(writable);
        Literal nint = new Literal(42);
        withValue.setContent(nint);
        sheet.addCell(withValue);
        sheet.addCell(noValue);
        sheet.addCell(holder);
        sheet.addCell(protect);
        bubbled = Bubbledocs.getInstance();
        bubbled.addBubbleSpreadsheet(sheet);
        bubbled.addUser(userNoPerm);
        bubbled.addUser(userRead);
        bubbled.addUser(userWrite);
        bubbled.addUser(userOwner);
    }


    @Test
    public void Control() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","0;0");
    	arcs.execute();
    	removeUserFromSession(tokenOwner);
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }
	
    @Test
    public void userPermissionsOwnerTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","0;0");
    	arcs.execute();
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }
    
    @Test(expected = UnauthorizedOperationException.class)
    public void userPermissionsReadTest() {
    	String tokenRead = addUserToSession(USERNAMEREAD);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenRead,sheet.getId(),"2;2","0;0");
		arcs.execute();
		arcs.getResult();
    }
    
    @Test
    public void userPermissionsWriteTest() {
    	String tokenWrite = addUserToSession(USERNAMEWRITE);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenWrite,sheet.getId(),"2;2","0;0");
    	arcs.execute();
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void userPermissionsNoneTest() {
    	String tokenNone = addUserToSession(USERNAMENOPERM);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenNone,sheet.getId(),"2;2","0;0");
		arcs.execute();
		arcs.getResult();
    }
    
}
