package pt.tecnico.bubbledocs.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.ADD;
import pt.tecnico.bubbledocs.domain.AccessMode;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Permission;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.SpreadSheetNotFoundException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.service.local.GetSpreadSheetContent;
import pt.tecnico.bubbledocs.test.BubbleDocsServiceTest;

public class GetSpreadSheetContentTest extends BubbleDocsServiceTest {
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
    private int nlines = 3, ncolumns = 4;
	private Cell empty;
	private Cell withLiteral;
	private Cell withReferenceToLiteral;
	private Cell withReferenceToEmpty;
	private Cell withSumOfLiteral;
	private Cell withSumOfReferenceToEmpty;
	
	
	@Override
    public void populate4Test() {
        userNoPerm = createUser(USERNAMENOPERM,"user1@user.pt", "pass", "no permissions");
        userRead = createUser(USERNAMEREAD,"user2@user.pt", "pass", "can read");
        userWrite = createUser(USERNAMEWRITE,"user3@user.pt", "pass", "can write");
        userOwner = createUser(USERNAMEOWNER,"user4@user.pt", "pass", "is owner");
        sheet = createSpreadSheet(userOwner,"sheet1", nlines, ncolumns);
        sheet.setOwner(userOwner);
        Permission readable = new Permission(sheet, AccessMode.READ);
        userRead.addUsedBy(readable);
        Permission writable = new Permission(sheet, AccessMode.WRITE);
        userWrite.addUsedBy(writable);
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
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenOwner, sheet.getId());
    	service.execute();
    	String[][] result = service.getResult();
		boolean allAreEmpty = true;
		for(int i = 0; i<nlines; i++){
			for(int j = 0; j<ncolumns; j++){
				if(!result[i][j].equals("()")) allAreEmpty=false;
			}
		}
		assertEquals("number of lines is different", result.length, nlines);
		assertEquals("number of columns is different", result[0].length, ncolumns);
		assertTrue(allAreEmpty);
    }
	
	/*
     * User permissions
     */
    
    @Test
    public void userPermissionsReadTest() {
    	String tokenRead = addUserToSession(USERNAMEREAD);
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenRead, sheet.getId());
    	service.execute();
    }
    
    @Test
    public void userPermissionsWriteTest() {
    	String tokenWrite = addUserToSession(USERNAMEWRITE);
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenWrite, sheet.getId());
    	service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void userPermissionsNoneTest() {
    	String tokenNone = addUserToSession(USERNAMENOPERM);
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenNone, sheet.getId());
    	service.execute();
    }
    
	/*
	 * Spreadsheet id
	 */
    
    @Test(expected = SpreadSheetNotFoundException.class)
    public void spreadsheetIDInvalidTest() {
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenOwner, 999999999);
    	service.execute();
    }
    
    /*
     * Content production tests
     * 
     */
    
    @Test
    public void contentEmptyCell() {
    	empty = new Cell(1,1);
    	sheet.addCell(empty);
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenOwner, sheet.getId());
    	service.execute();
    	String[][] result = service.getResult();
		assertEquals("number of lines is different", result[1][1],"()");
    }
    
    @Test
    public void contentLiteralCell() {
    	withLiteral = new Cell(1,2);
        withLiteral.setContent(new Literal(3));
        sheet.addCell(withLiteral);
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenOwner, sheet.getId());
    	service.execute();
    	String[][] result = service.getResult();
		assertEquals("number of lines is different", result[1][2],"3");
    }
    
    @Test
    public void contentReferenceToEmpty() {
    	empty = new Cell(1,1);
    	sheet.addCell(empty);
    	withReferenceToEmpty = new Cell(2,2);
        withReferenceToEmpty.setContent(new Reference(empty));
        sheet.addCell(withReferenceToLiteral);
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenOwner, sheet.getId());
    	service.execute();
    	String[][] result = service.getResult();
		assertEquals("number of lines is different", result[2][2],"()");
    }
    
    @Test
    public void contentReferenceToLiteralCell() {
    	withLiteral = new Cell(1,2);
        withLiteral.setContent(new Literal(3));
        sheet.addCell(withLiteral);
    	withReferenceToLiteral = new Cell(2,1);
        withReferenceToLiteral.setContent(new Reference(withLiteral));
        sheet.addCell(withReferenceToLiteral);
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenOwner, sheet.getId());
    	service.execute();
    	String[][] result = service.getResult();
		assertEquals("number of lines is different", result[2][1],"3");
		assertEquals("number of lines is different", result[1][2],"3");
    }
    
    @Test
    public void contentSumOfLiteral() {
    	withSumOfLiteral = new Cell(1,3);
    	withSumOfLiteral.setContent(new ADD(new Literal(3),new Literal(2)));
        sheet.addCell(withSumOfLiteral);
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenOwner, sheet.getId());
    	service.execute();
    	String[][] result = service.getResult();
		assertEquals("number of lines is different", result[1][3],"5");
    }
    
    @Test
    public void contentSumOfReferenceToEmpty() {
    	withLiteral = new Cell(1,2);
        withLiteral.setContent(new Literal(3));
        sheet.addCell(withLiteral);
    	withSumOfReferenceToEmpty = new Cell(1,3);
    	withSumOfReferenceToEmpty.setContent(new ADD(new Literal(4),new Reference(withLiteral)));
        sheet.addCell(withSumOfReferenceToEmpty);
    	String tokenOwner = addUserToSession(USERNAMEOWNER);
    	GetSpreadSheetContent service = new GetSpreadSheetContent(tokenOwner, sheet.getId());
    	service.execute();
    	String[][] result = service.getResult();
		assertEquals("number of lines is different", result[1][3],"7");
		assertEquals("number of lines is different", result[1][2],"3");
    }
    
}
