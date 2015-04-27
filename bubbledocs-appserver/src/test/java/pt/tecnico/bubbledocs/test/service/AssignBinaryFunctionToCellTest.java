package pt.tecnico.bubbledocs.test.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.AccessMode;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Permission;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BadContentExpressionException;
import pt.tecnico.bubbledocs.exceptions.BadSpreadSheetValuesException;
import pt.tecnico.bubbledocs.exceptions.NoValueForReferenceException;
import pt.tecnico.bubbledocs.exceptions.SpreadSheetNotFoundException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.service.local.AssignBinaryFunctionToCell;
import pt.tecnico.bubbledocs.test.BubbleDocsServiceTest;

public class AssignBinaryFunctionToCellTest extends BubbleDocsServiceTest {

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
	private Cell withValue1;
	private Cell withValue2;
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
        withValue1 = new Cell(0,0);	//value is 20
        withValue2 = new Cell(0,1); //value is 5
        noValue = new Cell(1,1);	//should have no value
        holder = new Cell(2,2);		//where content is placed
        protect = new Cell(1,2);	//where content can't be placed
        protect.setProtect(true);
        Permission readable = new Permission(sheet, AccessMode.READ);
        userRead.addUsedBy(readable);
        Permission writable = new Permission(sheet, AccessMode.WRITE);
        userWrite.addUsedBy(writable);
        Literal nint1 = new Literal(20);
        withValue1.setContent(nint1);
        Literal nint2 = new Literal(5);
        withValue2.setContent(nint2);
        sheet.addCell(withValue1);
        sheet.addCell(withValue2);
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
    public void control() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=ADD(1,1)");
    	abfc.execute();
    	int result = abfc.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 2);
    }
    /*
     * User permissions
     */
    
    @Test(expected = UnauthorizedOperationException.class)
    public void userPermissionsReadTest() {
    	String tokenRead = addUserToSession(USERNAMEREAD);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenRead,sheet.getId(),"2;2","=ADD(1,1)");
		abfc.execute();
    }
    
    @Test
    public void userPermissionsWriteTest() {
    	String tokenWrite = addUserToSession(USERNAMEWRITE);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenWrite,sheet.getId(),"2;2","=ADD(1,1)");
    	abfc.execute();
    	int result = abfc.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 2);
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void userPermissionsNoneTest() {
    	String tokenNone = addUserToSession(USERNAMENOPERM);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenNone,sheet.getId(),"2;2","=ADD(1,1)");
		abfc.execute();
    }
    
    /*
     * Spreadsheet id
     */
    
    @Test(expected = SpreadSheetNotFoundException.class)
    public void SpreadSheetDoesNotExist() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,9999999,"2;2","=ADD(1,1)");
    	abfc.execute();
    }
    /*
     * Cell protection
     */
    
    @Test(expected = UnauthorizedOperationException.class)
    public void cellProtectionProtectedTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"1;2","=ADD(1,1)");
    	abfc.execute();
    }
    
    /*
     * Bad holder coordinates (unreadable)
     */
    
    @Test(expected = BadSpreadSheetValuesException.class)
    public void coordArgumentsHolderBadTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"22","=ADD(1,1)");
    	abfc.execute();
    }
    
    /*
     * Operands
     */
    
    @Test
    public void operandsADDTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=ADD(6,2)");
    	abfc.execute();
    	int result = abfc.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 8);
    }
    
    @Test
    public void operandsSUBTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=SUB(6,2)");
    	abfc.execute();
    	int result = abfc.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 4);
    }
    
    @Test
    public void operandsMULTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=MUL(6,2)");
    	abfc.execute();
    	int result = abfc.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 12);
    }
    
    @Test
    public void operandsDIVTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=DIV(6,2)");
    	abfc.execute();
    	int result = abfc.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 3);
    }

    @Test(expected = BadContentExpressionException.class)
    public void operandsBadTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=LOL(6,2)");
    	abfc.execute();
    }
    
    @Test(expected = BadContentExpressionException.class)
    public void operandsNoneTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=(6,2)");
    	abfc.execute();
    }
    
    /*
     * Argument combinations
     * literal + ref
     * ref + literal
     * ref + ref
     * literal + blank ref
     */
    
    @Test
    public void ArgLiteralRefTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=ADD(1,0;0)"); //1+20
    	abfc.execute();
    	int result = abfc.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 21);
    }

    @Test
    public void ArgRefLiteralTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=ADD(0;1,3)"); //5+3
    	abfc.execute();
    	int result = abfc.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 8);
    }

    @Test
    public void ArgRefRefTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=ADD(0;0,0;1)"); //20+5
    	abfc.execute();
    	int result = abfc.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 25);
    }

    @Test(expected = NoValueForReferenceException.class)
    public void ArgLiteralBlankRefTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=ADD(1,1;1)"); //1+blank
    	abfc.execute();
    	abfc.getResult();
    }
    
    /*
     * Messy expressions
     * 
     */
    
    @Test(expected = BadContentExpressionException.class)
    public void messyExpressions1() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=6,2");
    	abfc.execute();
    }

    @Test(expected = BadContentExpressionException.class)
    public void messyExpressions2() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=(6)");
    	abfc.execute();
    }

    @Test(expected = BadContentExpressionException.class)
    public void messyExpressions3() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=(2;3)");
    	abfc.execute();
    }

    @Test(expected = BadContentExpressionException.class)
    public void messyExpressions4() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=(1,1");
    	abfc.execute();
    }

    @Test(expected = BadContentExpressionException.class)
    public void messyExpressions5() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","=1,1)");
    	abfc.execute();
    }

    @Test(expected = BadContentExpressionException.class)
    public void messyExpressions6() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2","");
    	abfc.execute();
    }
    
    
    /*
     * No arguments
     */
    
    @Test(expected = BadSpreadSheetValuesException.class)
    public void nullArguments1() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),null,"=ADD(1;1)");
    	abfc.execute();
    }
    
    @Test(expected = BadSpreadSheetValuesException.class)
    public void nullArguments2() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	AssignBinaryFunctionToCell abfc = new AssignBinaryFunctionToCell(tokenOwner,sheet.getId(),"2;2",null);
    	abfc.execute();
    }
}
