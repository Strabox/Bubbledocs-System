package pt.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BadSpreadSheetValuesException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.local.CreateSpreadSheet;


public class CreateSpreadSheetTest extends BubbleDocsServiceTest {

	private static final String USERNAME = "jpa";
	private static final String PASSWORD = "jp#";
	private static SpreadSheet SPSA;
	private static String TOKEN;

	@Override
	public void populate4Test() {
		createUser(USERNAME,"email@email.pt" ,PASSWORD, "João Pereira");
		TOKEN = addUserToSession(USERNAME);
		SPSA = new SpreadSheet("alpha", 10, 10);
	}


	@Test
	public void control() {
		User user = getUserFromSession(TOKEN);
		CreateSpreadSheet csps = new CreateSpreadSheet(TOKEN, SPSA.getName(), SPSA.getLines(), SPSA.getColumns());

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

	@Test(expected = BadSpreadSheetValuesException.class)
	public void TestBadValuesName() {
		new CreateSpreadSheet(TOKEN, null, SPSA.getLines(), SPSA.getColumns());
	}	

	@Test(expected = BadSpreadSheetValuesException.class)
	public void TestBadValuesLines() {
		new CreateSpreadSheet(TOKEN, SPSA.getName(), -1, SPSA.getColumns());
	}

	@Test(expected = BadSpreadSheetValuesException.class)
	public void TestBadValuesColumns() {
		new CreateSpreadSheet(TOKEN, SPSA.getName(), SPSA.getLines(), -1);
	}

	@Test(expected = UserNotInSessionException.class)
	public void TestNullToken() {
		CreateSpreadSheet csps = new CreateSpreadSheet("", SPSA.getName(), SPSA.getLines(), SPSA.getColumns());

		csps.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void TestInvalidToken() {
		CreateSpreadSheet csps = new CreateSpreadSheet("", SPSA.getName(), SPSA.getLines(), SPSA.getColumns());

		csps.execute();
	}
}
