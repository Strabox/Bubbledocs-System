package pt.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.AccessMode;
import pt.tecnico.bubbledocs.domain.AccessType;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.NumberInt;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BadSpreadSheetValuesException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.DeleteUser;

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
        AccessType readable = new AccessType(sheet, AccessMode.READ);
        userRead.addUsedBy(readable);
        AccessType writable = new AccessType(sheet, AccessMode.WRITE);
        userWrite.addUsedBy(writable);
        NumberInt nint = new NumberInt(42);
        withValue.setContent(nint);
        sheet.addCel(withValue);
        sheet.addCel(noValue);
        sheet.addCel(holder);
        sheet.addCel(protect);
        bubbled = Bubbledocs.getInstance();
        bubbled.addFolhaCalculo(sheet);
        bubbled.addUtilizador(userNoPerm);
        bubbled.addUtilizador(userRead);
        bubbled.addUtilizador(userWrite);
        bubbled.addUtilizador(userOwner);
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
    	removeUserFromSession(tokenOwner);
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }
    
    @Test
    public void userPermissionsReadTest() {
    	String tokenRead = addUserToSession(USERNAMEREAD);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenRead,sheet.getId(),"2;2","0;0");
    	try{
    		arcs.execute();
    		arcs.getResult();
    		fail();
    	}
    	catch(UnauthorizedOperationException e){
    		System.out.println("userPermissionsReadTest: caught expected exception");
    	}
    	finally{
    		removeUserFromSession(tokenRead);
    	}
    }
    
    @Test
    public void userPermissionsWriteTest() {
    	String tokenWrite = addUserToSession(USERNAMEWRITE);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenWrite,sheet.getId(),"2;2","0;0");
    	arcs.execute();
    	removeUserFromSession(tokenWrite);
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
    }

    @Test
    public void userPermissionsNoneTest() {
    	String tokenNone = addUserToSession(USERNAMENOPERM);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenNone,sheet.getId(),"2;2","0;0");
    	try{
    		arcs.execute();
    		arcs.getResult();
    		fail();
    	}
    	catch(UnauthorizedOperationException e){
    		System.out.println("userPermissionsNoneTest: caught expected exception");
    	}
		finally{
			removeUserFromSession(tokenNone);
		}
    }
    
}
