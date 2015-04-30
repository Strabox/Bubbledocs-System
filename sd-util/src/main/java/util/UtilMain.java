package util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import util.kerberos.Kerberos;
import util.kerberos.KerberosTicket;

public class UtilMain {

	/* Main used to test and learn some features. */
	public static void main(String[] args) throws NoSuchAlgorithmException, DatatypeConfigurationException, IOException,
	InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SAXException, ParserConfigurationException, DOMException, ParseException {
		System.out.println("Welcome do SD util library!!");
		
		KerberosTicket kt = new KerberosTicket("client", "server", 2, Kerberos.generateSymKey("DES", 56));
		
		Key k = Kerberos.generateSymKey("DES", 56);
		
		byte[] ticket = kt.serializeTicket(k);
		
		KerberosTicket.deserializeTicket(ticket, k);
	
	
	}
}

