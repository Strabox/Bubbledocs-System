package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.*;

import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.*;

import org.junit.Test;





public class listDocsTest extends SDStoreTest {
	/*
	@Test
	public void listDocsSuccess() throws UserDoesNotExist_Exception  {
		String user = "User2";
		
		DocUserPair pair1 = new DocUserPair();
		pair1.setDocumentId("document1");
		pair1.setUserId(user);
		port.createDoc(pair1);
		
		DocUserPair pair2 = new DocUserPair();
		pair2.setDocumentId("document2");
		pair2.setUserId(user);
		port.createDoc(pair2);
		
		DocUserPair pair3 = new DocUserPair();
		pair3.setDocumentId("document3");
		pair3.setUserId(user);
		port.createDoc(pair3);
		List<String> _docs= port.listDocs(user);
						
		assertEquals("index=0",_docs.get(0),"document1");
		assertEquals("index=1",_docs.get(0),"document2");
		assertEquals("index=2",_docs.get(0),"document3");
	}
	
		
	@Test (expected=UserDoesNotExist_Exception.class )
	public void userDoesNotExist () throws UserDoesNotExist_Exception {
		String user = "ghostUser";
		List<String> _docs= port.listDocs(user);
		assertEquals("...",true,true);
	}
	*/
	
}
