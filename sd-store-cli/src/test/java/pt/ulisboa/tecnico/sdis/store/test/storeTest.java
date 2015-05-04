package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;





public class storeTest extends SDStoreTest {
	
	@Test 
	public void storeSucess() throws DocAlreadyExists_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception   {
		String user = "User3";
		
		String text = "abcdABCD1234";
		byte[] content = text.getBytes();
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("document31");
		pair.setUserId(user);
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		port.createDoc(pair);
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
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
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		port.store(pair, content);
		assertEquals("...",true,true);	
	}
	
	
	
	
	
	
	@Test (expected=CapacityExceeded_Exception.class )
	public void capacityExceeded() throws DocAlreadyExists_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception   {
		String user = "User3";

		
	   	byte[] hugeContent = new byte[50000];
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("document32");
		pair.setUserId(user);
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		port.createDoc(pair);
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		port.store(pair, hugeContent);
		assertEquals("...",true,true);	
	}
	
	@Test (expected=UserDoesNotExist_Exception.class )
	public void userDoesNotExist () throws UserDoesNotExist_Exception, CapacityExceeded_Exception, DocDoesNotExist_Exception {
		String user = "ghostUser";

		String text = "abcdABCD1234";
		byte[] content = text.getBytes();;
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("document33");
		pair.setUserId(user);
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		port.store(pair, content);
		assertEquals("...",true,true);
	}
	

}
