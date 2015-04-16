package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.*;
import pt.ulisboa.tecnico.sdis.store.ws.*;

import org.junit.Test;





public class CreateDocTest extends SDStoreTest {
	
	@Test
	public void createDocSuccess() throws DocAlreadyExists_Exception {
		String document ="document11";
		String user = "User1";
		
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
		port.createDoc(pair);
		DocUserPair pair1 = new DocUserPair();
		pair1.setDocumentId(document);
		pair1.setUserId(user);
		port.createDoc(pair1);
		
		
		assertEquals("...",1,1);
		
	}
	
	
}
