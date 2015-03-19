package pt.tecnico.bubbledocs.test;


import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;

public class BubbleDocsServiceTest {

    @Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
            populate4Test();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    // should redefine this method in the subclasses if it is needed to specify
    // some initial state
    public void populate4Test() {}

    // auxiliary methods that access the domain layer and are needed in the test classes
    // for defining the iniital state and checking that the service has the expected behavior
    User createUser(String username, String password, String name) {
    	User user = new User(name,username,password);
    	Bubbledocs.getInstance().addUtilizador(user);
	    return user;
    }

    public SpreadSheet createSpreadSheet(User user, String name, int row,int column) {
    	SpreadSheet s = new SpreadSheet(name,row,column);
    	user.addOwned(s);
    	return s;
    }

    // returns a spreadsheet whose name is equal to name
    public SpreadSheet getSpreadSheet(String name) {
    	return Bubbledocs.getInstance().getSpreadSheet(name);
    }

    // returns the user registered in the application whose username is equal to username
    User getUserFromUsername(String username) {
    	return FenixFramework.getDomainRoot().getBubbledocs().getUserByName(username);
    }

    // put a user into session and returns the token associated to it
    String addUserToSession(String username) {
    	String token = Bubbledocs.getInstance().generateToken(username);
		Bubbledocs.getInstance().addSession(new Session(new LocalTime(),username,token));
		return token;
    }

    // remove a user from session given its token
    void removeUserFromSession(String token) {
    	FenixFramework.getDomainRoot().getBubbledocs().removeUserFromSession(token);
    }

    // return the user registered in session whose token is equal to token
    User getUserFromSession(String token) {
    	return FenixFramework.getDomainRoot().getBubbledocs().getUserFromSession(token);
    }

}
