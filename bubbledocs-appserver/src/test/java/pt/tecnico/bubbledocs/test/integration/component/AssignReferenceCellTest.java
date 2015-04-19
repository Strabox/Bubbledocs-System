package pt.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.AccessMode;
import pt.tecnico.bubbledocs.domain.Permission;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BadSpreadSheetValuesException;
import pt.tecnico.bubbledocs.exceptions.NoValueForReferenceException;
import pt.tecnico.bubbledocs.exceptions.OutOfSpreadsheetBoundariesException;
import pt.tecnico.bubbledocs.exceptions.SpreadSheetNotFoundException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;

public class AssignReferenceCellTest extends BubbleDocsServiceTest {
	
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
	private Cell withValue;
	private Cell noValue;
	private Cell holder;
	private Cell protect;
	
	@Override
    public void populate4Test() {
        userNoPerm = createUser(USERNAMENOPERM,"user1@user.pt", "pass", "no permissions");
        userRead = createUser(USERNAMEREAD,"user2@user.pt", "pass", "can read");
        userWrite = createUser(USERNAMEWRITE,"user3@user.pt", "pass", "can write");
        userOwner = createUser(USERNAMEOWNER,"user4@user.pt", "pass", "is owner");
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

	/*
	 * This first test involves two cells in the boundaries of the sheet, with the referred
	 * cell existing and having a literal as content, and the holder cell being unprotected.
	 * The user is the owner of the sheet, being able to write on it, and will enter valid
	 * and intelligible coordinates as arguments. This could replace the first test of each
	 * test-suite, since they're all based on this.
	 */
    @Test
    public void control() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","0;0");
    	arcs.execute();
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }
	
    /*
     * User permissions
     */
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
    
    /*
     * Spreadsheet id
     */
    @Test
    public void SpreadSheetExistsTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","0;0");
    	arcs.execute();
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }
    
    @Test(expected = SpreadSheetNotFoundException.class)
    public void SpreadSheetDoesNotExist() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,9999999,"2;2","0;0");
    	arcs.execute();
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }
    /*
     * Cell protection
     */
    @Test
    public void cellProtectionUnprotectedTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","0;0");
    	arcs.execute();
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }
    
    @Test(expected = UnauthorizedOperationException.class)
    public void cellProtectionProtectedTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"1;2","0;0");
    	arcs.execute();
    	arcs.getResult();
    }
    
    /*
     * Cell location
     */
    @Test
    public void cellLocationInBoundariesTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","0;0");
    	arcs.execute();
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }
    
    @Test(expected = OutOfSpreadsheetBoundariesException.class)
    public void cellLocationOutOfBoundariesTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"3;2","0;0");
    	arcs.execute();
    	arcs.getResult();
    }
    
    /*
     * Bad coordinates (unreadable)
     */
    @Test
    public void coordArgumentsGoodTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","0;0");
    	arcs.execute();
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }
    
    @Test(expected = BadSpreadSheetValuesException.class)
    public void coordArgumentsFirstBadTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"22","0;0");
    	arcs.execute();
    	arcs.getResult();
    }
    
    @Test(expected = BadSpreadSheetValuesException.class)
    public void coordArgumentsSecondBadTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","a;0");
    	arcs.execute();
    	arcs.getResult();
    }
    
    /*
     * Referred cell content
     */
    @Test
    public void referredCellHasContentTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","0;0");
    	arcs.execute();
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }
    
    @Test(expected = NoValueForReferenceException.class)
    public void referredCellHasNoContentTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","1;1");
    	arcs.execute();
    	arcs.getResult();
    }
    
}
