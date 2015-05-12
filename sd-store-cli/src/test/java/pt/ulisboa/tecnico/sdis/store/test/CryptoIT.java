package pt.ulisboa.tecnico.sdis.store.test;

import static org.junit.Assert.*;
import pt.ulisboa.tecnico.sdis.store.cli.Crypto;

import org.junit.Test;






public class CryptoIT extends SDStoreIT {
	
	public CryptoIT() throws Exception {
		super();
	}
/*
	@Test
	public void cypherSuccess() throws Exception {
		String expectedString ="ABCDabcd123456";
		Crypto crypto = new Crypto();
		byte[] content= expectedString.getBytes();
		byte[] encryptedContent=crypto.encrypt(content);
		String resultString=new String(crypto.decrypt(encryptedContent));
		assertEquals("crypto1",expectedString,resultString);
	}
	

	*/
	
}
