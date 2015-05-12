package pt.tecnico.bubbledocs.test.integration.system;

import javax.transaction.SystemException;

import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;

public class RemoteSystemIT {
	private String rootToken;
	private String userToken;
	private static final String ROOT_USERNAME = "root";
	private static final String ROOT_PASSWORD = "root";

	private static final String USERNAME_01 = "House";
	private static final String USERNAME_01_PASSWORD = "Vegas";
	private static final String USERNAME_01_EMAIL = "Mr_House@Lucky38";
	private static final String NAME_01 = "Mr. House";
	
	
	@Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
        }  catch (WriteOnReadError | SystemException e) {
        	System.out.println(e);
        }
    }
}
