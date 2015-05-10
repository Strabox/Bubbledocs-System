package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.*;
import pt.ulisboa.tecnico.sdis.store.ws.*;

import org.junit.Test;





public class CreateDocTest extends SDStoreTest {
	
	public CreateDocTest() throws Exception {
		super();
	}

	@Test
	public void createDocSuccess() throws DocAlreadyExists_Exception {
		String document ="document11";
		String user = "User1";
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(document);
		pair.setUserId(user);
		port.createDoc(pair);
		assertEquals("...",1,1);
	}
	
	@Test
	public void createOtherDocSuccess() throws DocAlreadyExists_Exception {
		String document ="otherdocument12";
		String user = "User1";
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro");
		}
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(document);
		pair.setUserId(user);
		port.createDoc(pair);
		assertEquals("...",1,1);
	}
	
	
	@Test (expected=DocAlreadyExists_Exception.class)
	public void docAlreadyExists () throws DocAlreadyExists_Exception {
		String document ="document13";
		String user = "User1";
	
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(document);
		pair.setUserId(user);
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		} 
		port.createDoc(pair);
		DocUserPair pair1 = new DocUserPair();
		pair1.setDocumentId(document);
		pair1.setUserId(user);
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		} 
		port.createDoc(pair1);
		assertEquals("...",1,1);
		
	}
	

    @Test
    public void testCreateDocNullUser() throws Exception {
        final DocUserPair docUser = new DocUserPair();
        docUser.setDocumentId("a1");
        docUser.setUserId(null);
        try {
			uploadKerberosInfo(port, null);
		} catch (Exception e) {
			fail("Erro");
		}
        port.createDoc(docUser);
    }

    @Test
    public void testCreateDocEmptyUser() throws Exception {
        final DocUserPair docUser = new DocUserPair();
        docUser.setDocumentId("a1");
        docUser.setUserId("");
        try {
			uploadKerberosInfo(port, "");
		} catch (Exception e) {
			fail("Erro");
		}
        port.createDoc(docUser);
    }

    @Test
    public void testCreateNullDoc() throws Exception {
        final DocUserPair docUser = new DocUserPair();
        docUser.setDocumentId(null);
        docUser.setUserId("alice");
        try {
			uploadKerberosInfo(port, "alice");
		} catch (Exception e) {
			fail("Erro");
		}
        port.createDoc(docUser);
    }

    @Test
    public void testCreateEmptyDoc() throws Exception {
        final DocUserPair docUser = new DocUserPair();
        docUser.setDocumentId("");
        docUser.setUserId("alice");
        try {
			uploadKerberosInfo(port, "alice");
		} catch (Exception e) {
			fail("Erro");
		}
        port.createDoc(docUser);
    }

   
	
	
}
