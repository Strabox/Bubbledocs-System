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

	public loadTest() throws Exception {
		super();
	}


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
		try {
			uploadKerberosInfo(port, user);
		} catch (Exception e) {
			fail("Erro");
		}
		byte[] content=port.load(pair);
		String text = new String(content);
		assertEquals("Text",expectedText,text);
	}

	@Test
	public void loadSetupSucess () throws DocDoesNotExist_Exception, UserDoesNotExist_Exception{
/*
		DocUserPair pair1 = new DocUserPair();
		pair1.setDocumentId("a1");
		pair1.setUserId("alice");
		DocUserPair pair2 = new DocUserPair();
		pair2.setDocumentId("a2");
		pair2.setUserId("alice");
		DocUserPair pair3 = new DocUserPair();
		pair3.setDocumentId("b1");
		pair3.setUserId("bruno");
		
		
		try {
			uploadKerberosInfo(port, "alice");
		} catch (Exception e) {
			fail("Erro");
		}
		byte[] content1=port.load(pair1);
		String text1 = new String(content1);
		try {
			uploadKerberosInfo(port, "alice");
		} catch (Exception e) {
			fail("Erro");
		}
		byte[] content2=port.load(pair2);
		String text2 = new String(content2);
		try {
			uploadKerberosInfo(port, "bruno");
		} catch (Exception e) {
			fail("Erro");
		}
		byte[] content3=port.load(pair3);
		String text3 = new String(content3);

		assertEquals("1","AAAAAAAAAA",text1);
		assertEquals("2","aaaaaaaaaa",text2);
		assertEquals("3","BBBBBBBBBBBBBBBBBBBB",text3);

*/
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
