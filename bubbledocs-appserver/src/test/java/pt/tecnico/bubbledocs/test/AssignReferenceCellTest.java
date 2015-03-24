package pt.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.NumberInt;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;

public class AssignReferenceCellTest extends BubbleDocsServiceTest {
	
	private Bubbledocs bubbled;
	private static User userNoPerm;
	private static User userRead;
	private static User userWrite;
	private static User userOwner;
	private static final String USERNAME1 = "usernoperm";
    private static final String USERNAME2 = "userread";
    private static final String USERNAME3 = "userwrite";
    private static final String USERNAME4 = "userowner";
    private SpreadSheet sheet;
	private Cell withValue;
	private Cell noValue;
	private Cell holder;
	
	@Override
    public void populate4Test() {
        userNoPerm = createUser(USERNAME1, "pass", "no permissions");
        userRead = createUser(USERNAME2, "pass", "can read");
        userWrite = createUser(USERNAME3, "pass", "can write");
        userOwner = createUser(USERNAME4, "pass", "is owner");
        sheet = createSpreadSheet(userOwner,"sheet1", 3, 3);
        sheet.setOwner(userOwner);
        withValue = new Cell(0,0);	//should have a value
        noValue = new Cell(1,1);	//should have no value
        holder = new Cell(2,2);		//where content is placed
        NumberInt nint = new NumberInt(42);
        withValue.setContent(nint);
        sheet.addCel(withValue);
        sheet.addCel(noValue);
        sheet.addCel(holder);
        bubbled = Bubbledocs.getInstance();
        bubbled.addFolhaCalculo(sheet);
        bubbled.addUtilizador(userNoPerm);
        bubbled.addUtilizador(userRead);
        bubbled.addUtilizador(userWrite);
        bubbled.addUtilizador(userOwner);
    }


    @Test
    public void Control() {
    	String tokenOwner = addUserToSession(USERNAME4);
    	AssignReferenceCell arcs = new AssignReferenceCell(tokenOwner,sheet.getId(),"2;2","0;0");
        
    	arcs.execute();
    	int result = arcs.getResult();
		assertEquals("Result of referred cell different from unexpected.", result, 42);
		
		removeUserFromSession(tokenOwner);
    }
	

}
