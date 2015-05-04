package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;





public class loadTest extends SDStoreTest {
	
	@Test 
	public void loadSucess () throws DocDoesNotExist_Exception, UserDoesNotExist_Exception, DocAlreadyExists_Exception, CapacityExceeded_Exception  {
		String user ="user4";
		
		String expectedText = "abcdABCD1234";
		byte[] expectedContent = expectedText.getBytes();
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("document41");
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
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		// FIXME isto está estranho digo eu (andré)
		port.load(pair);
		assertEquals("...",true,true);
	}
	
	
	
	@Test (expected=UserDoesNotExist_Exception.class )
	public void userDoesNotExist () throws DocDoesNotExist_Exception, UserDoesNotExist_Exception  {
		String user = "ghostUser";
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId("document42");
		pair.setUserId(user);		
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		port.load(pair);
		assertEquals("...",true,true);
	}
	
	
}
