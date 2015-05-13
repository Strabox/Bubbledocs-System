package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.*;

import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.*;

import org.junit.Test;





public class listDocsIT extends SDStoreIT {

	public listDocsIT() throws Exception {
		super();
	}


	@Test
	public void listDocsSuccess() throws UserDoesNotExist_Exception, DocAlreadyExists_Exception  {
		String user = "User2";
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		DocUserPair pair1 = new DocUserPair();
		pair1.setDocumentId("document21");
		pair1.setUserId(user);
		port.createDoc(pair1);

		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		DocUserPair pair2 = new DocUserPair();
		pair2.setDocumentId("document22");
		pair2.setUserId(user);
		port.createDoc(pair2);

		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		DocUserPair pair3 = new DocUserPair();
		pair3.setDocumentId("document23");
		pair3.setUserId(user);
		port.createDoc(pair3);
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		List<String> _docs= port.listDocs(user);

		assertTrue(_docs.contains("document21"));
		assertTrue(_docs.contains("document22"));
		assertTrue(_docs.contains("document23"));
	}


	@Test (expected=UserDoesNotExist_Exception.class )
	public void userDoesNotExist () throws UserDoesNotExist_Exception {
		String user = "ghostUser";
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		port.listDocs(user);
		assertEquals("...",true,true);
	}

	//TEACHER TESTS
	@Test(expected = UserDoesNotExist_Exception.class)
	public void testListDocsNoUser() throws Exception {
		final String user = "userthatdoesnotexist";
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		port.listDocs(user);
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

	@Test(expected = UserDoesNotExist_Exception.class)
	public void testListDocsEmptyUser() throws Exception {
		final String user = "";
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		port.listDocs(user);
	}

	@Test
	public void testEmptyListDocs() throws Exception {
		final String user = "eduardo";
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		List<String> list = port.listDocs(user);
		assertNotNull(list);
		assertEquals(0, list.size());
	}

	@Test
	public void testListDocs() throws Exception {
		final String user = "bruno2";

		{
			final DocUserPair docUser = new DocUserPair();
			docUser.setDocumentId("b11");
			docUser.setUserId(user);
			try {
				uploadKerberosInfo(port, user);
			} catch (Exception e) {
				fail("Erro");
			}
			port.createDoc(docUser);
		}
		{
			final DocUserPair docUser = new DocUserPair();
			docUser.setDocumentId("b21");
			docUser.setUserId(user);
			try {
				uploadKerberosInfo(port, user);
			} catch (Exception e) {
				fail("Erro");
			}
			port.createDoc(docUser);
		}
		{
			final DocUserPair docUser = new DocUserPair();
			docUser.setDocumentId("b31");
			docUser.setUserId(user);
			try {
				uploadKerberosInfo(port, user);
			} catch (Exception e) {
				fail("Erro");
			}
			port.createDoc(docUser);
		}

		List<String> list = port.listDocs(user);
		assertNotNull(list);
		assertEquals(3, list.size());
		assertTrue(list.contains("b11"));
		assertTrue(list.contains("b21"));
		assertTrue(list.contains("b31"));
	}



}
