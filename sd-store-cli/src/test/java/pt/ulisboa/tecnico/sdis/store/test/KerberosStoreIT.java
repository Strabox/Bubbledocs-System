package pt.ulisboa.tecnico.sdis.store.test;

import java.security.Key;

import javax.xml.soap.SOAPException;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.cli.SDStoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosCredential;
import util.kerberos.messages.KerberosTicket;

public class KerberosStoreIT extends SDStoreIT{

	public KerberosStoreIT() throws Exception {
		super();
	}
	
	
	public void uploadKerberoasdsInfo(SDStoreClient store, String username) 
	throws Exception{
		Key kcs = Kerberos.generateKerberosKey();
		KerberosTicket ticket = new KerberosTicket(username,"SD-STORE",8,kcs);
		Key ks = loadServerKey("SD-STORE");
		KerberosCredential cred = new KerberosCredential(ticket.serialize(ks), kcs);
		port.credentials = cred.serialize();
	}
	
	
	@Test(expected = RuntimeException.class)
	public void invalidCredentials() throws UserDoesNotExist_Exception,
		KerberosException{
		 port.credentials = Kerberos.generateRandomNumber().getBytes();
		 port.listDocs("bruno");
	}
	
	/*
	@Test(expected = SOAPException.class)
	public void invalidUsernameTicket() throws Exception{
		Key kcs = Kerberos.generateKerberosKey();
		Key ks = loadServerKey("SD-STORE");
		KerberosTicket ticket = new KerberosTicket("Angelo","SD-STORE",8,kcs);
		KerberosCredential cred = new KerberosCredential(ticket.serialize(ks),kcs);
		port.credentials = cred.serialize();
	    port.listDocs("bruno");
	} */

}
