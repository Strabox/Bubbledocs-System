package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.*;

import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.*;

import org.junit.Test;





public class loadTest extends SDStoreTest {
	
	@Test 
	public void loadSucess () throws DocDoesNotExist_Exception, UserDoesNotExist_Exception, DocAlreadyExists_Exception, CapacityExceeded_Exception  {
		String user ="user4";
		
		String expectedText = "abcdABCD1234";
		byte[] expectedContent = expectedText.getBytes();
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("document41");
		pair.setUserId(user);
		port.createDoc(pair);
		port.store(pair, expectedContent);		
		byte[] content=port.load(pair);
		String text = new String(content);
		assertEquals("Text",expectedText,text);
	}
	
	
	@Test (expected=DocDoesNotExist_Exception.class )
	public void documentDoesNotExist () throws DocDoesNotExist_Exception, UserDoesNotExist_Exception  {
		String user = "user4";
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("ghostDocument");
		pair.setUserId(user);		
		byte[] content=port.load(pair);
		assertEquals("...",true,true);
	}
	
	
	
	@Test (expected=UserDoesNotExist_Exception.class )
	public void userDoesNotExist () throws DocDoesNotExist_Exception, UserDoesNotExist_Exception  {
		String user = "ghostUser";
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("document42");
		pair.setUserId(user);		
		byte[] content=port.load(pair);
		assertEquals("...",true,true);
	}
	
	
}
