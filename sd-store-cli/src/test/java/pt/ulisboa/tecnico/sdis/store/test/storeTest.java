package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.*;

import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.*;

import org.junit.Test;





public class storeTest extends SDStoreTest {
	/*
	@Test 
	public void storeSucess() throws DocAlreadyExists_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception   {
		String user = "User3";
		
		String text = "abcdABCD1234";
		byte[] content = text.getBytes();
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("document1");
		pair.setUserId(user);
		port.createDoc(pair);
		port.store(pair, content);
		assertEquals("...",true,true);	
	}
	
	
	@Test (expected=DocDoesNotExist_Exception.class )
	public void docDoesNotExist() throws DocAlreadyExists_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception   {
		String user = "User3";

		String text = "abcdABCD1234";
		byte[] content = text.getBytes();;
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("ghostDocument");
		pair.setUserId(user);
		port.createDoc(pair);
		port.store(pair, content);
		assertEquals("...",true,true);	
	}
	
	
	
	
	
	
	@Test (expected=CapacityExceeded_Exception.class )
	public void capacityExceeded() throws DocAlreadyExists_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception   {
		String user = "User3";

		int size = Integer.MAX_VALUE;
	   	byte[] hugeContent = new byte[size];
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("document1");
		pair.setUserId(user);
		port.createDoc(pair);
		port.store(pair, hugeContent);
		assertEquals("...",true,true);	
	}
	
	@Test (expected=UserDoesNotExist_Exception.class )
	public void userDoesNotExist () throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception {
		String user = "ghostUser";

		String text = "abcdABCD1234";
		byte[] content = text.getBytes();;
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("document1");
		pair.setUserId(user);		
		port.store(pair, content);
		assertEquals("...",true,true);
	}
	*/
	
}
