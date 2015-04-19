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
import pt.tecnico.bubbledocs.exceptions.OutOfSpreadsheetBoundariesException;
import pt.tecnico.bubbledocs.exceptions.SpreadSheetNotFoundException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.InvalidLiteralException;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;

public class AssignLiteralCellTest extends BubbleDocsServiceTest {
	
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
		userNoPerm = createUser(USERNAMENOPERM,"user@user.pt", "pass", "no permissions");
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

	
	//CONTROL TEST.
	//User: Owner; Spreadsheet: exists; Cell Protection: unprotected;
	//Sheet boundaries: inside; Sheet coordinates: fine syntax; Cell content: empty.
    @Test
    public void userPermissionsOwnerTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignLiteralCell alc = new AssignLiteralCell(tokenOwner,sheet.getId(),"1;1","11");
    	alc.execute();
    	int result = alc.getResult();
		assertEquals("Result of assigned cell different from unexpected.", result, 11);
    }
    
    /*
     * User permissions
     */
    
    //Testing if an user with read permission can edit an empty cell
    @Test(expected = UnauthorizedOperationException.class)
    public void userPermissionsReadTest() {
    	String tokenOwner = addUserToSession(USERNAMEREAD);
    	AssignLiteralCell alc = new AssignLiteralCell(tokenOwner,sheet.getId(),"1;1","11");
    	alc.execute();
    	alc.getResult();
    }
    
    //Testing if an user with write permission can edit an empty cell
    @Test
    public void userPermissionsWriteTest() {
    	String tokenOwner = addUserToSession(USERNAMEWRITE);
    	AssignLiteralCell alc = new AssignLiteralCell(tokenOwner,sheet.getId(),"1;1","11");
    	alc.execute();
    	int result = alc.getResult();
		assertEquals("Result of assigned cell different from unexpected.", result, 11);
    }
    
    //Testing if an user with no permission can edit an empty cell
    @Test(expected = UnauthorizedOperationException.class)
    public void userPermissionsNoneTest() {
    	String tokenOwner = addUserToSession(USERNAMENOPERM);
    	AssignLiteralCell alc = new AssignLiteralCell(tokenOwner,sheet.getId(),"1;1","11");
    	alc.execute();
    	alc.getResult();
    }
    
    /*
     * Spreadsheet id
     */
    
    //Testing if an exception is thrown when using an invalid sheet
    @Test(expected = SpreadSheetNotFoundException.class)
    public void SpreadSheetDoesNotExist() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignLiteralCell alc = new AssignLiteralCell(tokenOwner,9999999,"1;1","11");
    	alc.execute();
    	alc.getResult();
    }

    /*
     * Cell protection
     */
    
    //Testing if an exception is thrown when editing a protected cell
    @Test(expected = UnauthorizedOperationException.class)
    public void cellProtectionProtectedTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignLiteralCell alc = new AssignLiteralCell(tokenOwner,sheet.getId(),"1;2","11");
    	alc.execute();
    	alc.getResult();
    }
    
    /*
     * Cell location
     */
    //Testing if an exception is thrown when editing a cell out of sheet bounds
    @Test(expected = OutOfSpreadsheetBoundariesException.class)
    public void cellLocationOutOfBoundariesTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignLiteralCell alc = new AssignLiteralCell(tokenOwner,sheet.getId(),"3;2","11");
    	alc.execute();
    	alc.getResult();
    }
    
    /*
     * Bad coordinates (unreadable)
     */
    
    //Testing with messy coordinates I
    @Test(expected = BadSpreadSheetValuesException.class)
    public void coordArgumentsFirstBadTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignLiteralCell alc = new AssignLiteralCell(tokenOwner,sheet.getId(),"22","11");
    	alc.execute();
    	alc.getResult();
    }
    
    //Testing with messy coordinates II
    @Test(expected = BadSpreadSheetValuesException.class)
    public void coordArgumentsSecondBadTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignLiteralCell alc = new AssignLiteralCell(tokenOwner,sheet.getId(),"2;a","11");
    	alc.execute();
    	alc.getResult();
    }

    /*
     * Literal content
     */
    
    //Testing if a literal can be a string of chars
    @Test(expected = InvalidLiteralException.class)
    public void AssignedCellHasContentTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignLiteralCell alc = new AssignLiteralCell(tokenOwner,sheet.getId(),"0;0","shouldIbeH3r3");
    	alc.execute();
    	alc.getResult();
    }
}


