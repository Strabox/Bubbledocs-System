package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.*;

import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.*;

import org.junit.Test;





public class listDocsIT extends SDStoreIT {

	public listDocsIT() throws Exception {
		super();
	}


	







	@Test(expected = UserDoesNotExist_Exception.class)
	public void testListDocsNullUser() throws Exception {
		try {
			uploadKerberosInfo(port, null);
		} catch (Exception e) {
			fail("Erro");
		}
		port.listDocs(null);
	}





}
