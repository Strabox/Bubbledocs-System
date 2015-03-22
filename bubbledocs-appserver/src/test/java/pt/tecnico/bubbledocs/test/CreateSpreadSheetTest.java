package pt.tecnico.bubbledocs.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.LoginUser;


public class CreateSpreadSheetTest extends BubbleDocsServiceTest {

    private static final String USERNAME = "jp";
    private static final String PASSWORD = "jp#";
    private LoginUser log;
    private static SpreadSheet SPSA;
    private SpreadSheet[] SPSV;

    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, "João Pereira");
        log = new LoginUser(USERNAME, PASSWORD);
        log.execute();
        SPSA = new SpreadSheet("alpha", 10, 10);
        
        SPSV = new SpreadSheet[10];
        for (int i = 0; i < SPSV.length; i++) {
			SPSV[i] = new SpreadSheet("aaaa"+i, 1*i, 1*i);
		}
    }

	
	@Test
    public void Control() {
        User user = getUserFromSession(log.getUserToken());
    	CreateSpreadSheet csps = new CreateSpreadSheet(log.getUserToken(), SPSA.getName(), SPSA.getLines(), SPSA.getColumns());
    	
    	csps.execute();
        
    	int new_size = user.getOwnedSet().size();
        assertTrue("user has wrong number of spreadsheets", new_size==1);
        
        String test_name = null;
        int test_lines = 0, test_Columns = 0, test_id = 0;
        for (SpreadSheet s : user.getOwnedSet()) {
			test_name = s.getName(); test_lines = s.getLines(); 
			test_Columns = s.getColumns(); test_id = s.getId();
		}
        
		boolean condition = (
				test_name == SPSA.getName()
				&& test_lines == SPSA.getLines()
				&& test_Columns == SPSA.getColumns()
				&& test_id == csps.getSheetId());
		
		assertTrue("user spreadsheet atributes differ from SPSA's atributes", condition);
    }
	
	@Test
	public void TestNegativeValues() {
		User user = getUserFromSession(log.getUserToken());
    	
    	try {
    		CreateSpreadSheet csps = new CreateSpreadSheet(log.getUserToken(), null, SPSA.getLines(), SPSA.getColumns());
		} catch (UnauthorizedOperationException e) {
			System.out.println("UnauthorizedOperation Exception caught: " + e.getMessage());
		}
    	
    	try {
    		CreateSpreadSheet csps = new CreateSpreadSheet(log.getUserToken(), null, SPSA.getLines(), SPSA.getColumns());
		} catch (UnauthorizedOperationException e) {
			System.out.println("UnauthorizedOperation Exception caught: " + e.getMessage());
		}
    	
    	try {
    		CreateSpreadSheet csps = new CreateSpreadSheet(log.getUserToken(), null, SPSA.getLines(), SPSA.getColumns());
		} catch (UnauthorizedOperationException e) {
			System.out.println("UnauthorizedOperation Exception caught: " + e.getMessage());
		}
	}
	
	@Test
	public void TestInvalidToken() {
		CreateSpreadSheet csps = new CreateSpreadSheet("", SPSA.getName(), SPSA.getLines(), SPSA.getColumns());
		
		try {
			csps.execute();
		} catch (UserNotInSessionException e) {
			System.out.println("UserNotInSession Exception caught: " + e.getMessage());
		}
	}
	
	
    
}
