package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.*;
import util.kerberos.exception.KerberosException;

import org.junit.Test;





public class listDocsTest extends SDStoreTest {
	
	@Test
	public void listDocsSuccess() throws UserDoesNotExist_Exception, DocAlreadyExists_Exception  {
		String user = "User2";
		try {
			uploadKerberosInfo(port, user);
		} catch (NoSuchAlgorithmException | KerberosException e) {
			fail("Erro");
		}
		DocUserPair pair1 = new DocUserPair();
		pair1.setDocumentId("document21");
		pair1.setUserId(user);
		port.createDoc(pair1);
		
		try {
			uploadKerberosInfo(port, user);
		} catch (NoSuchAlgorithmException | KerberosException e) {
			fail("Erro");
		}
		DocUserPair pair2 = new DocUserPair();
		pair2.setDocumentId("document22");
		pair2.setUserId(user);
		port.createDoc(pair2);
		
		try {
			uploadKerberosInfo(port, user);
		} catch (NoSuchAlgorithmException | KerberosException e) {
			fail("Erro");
		}
		DocUserPair pair3 = new DocUserPair();
		pair3.setDocumentId("document23");
		pair3.setUserId(user);
		port.createDoc(pair3);
		List<String> _docs= port.listDocs(user);
						
		assertEquals("index=0",_docs.get(0),"document21");
		assertEquals("index=1",_docs.get(1),"document22");
		assertEquals("index=2",_docs.get(2),"document23");
	}
	
		
	@Test (expected=UserDoesNotExist_Exception.class )
	public void userDoesNotExist () throws UserDoesNotExist_Exception {
		String user = "ghostUser";
		try {
			uploadKerberosInfo(port, user);
		} catch (NoSuchAlgorithmException | KerberosException e) {
			fail("Erro");
		}
		port.listDocs(user);
		assertEquals("...",true,true);
	}
	
	
}
