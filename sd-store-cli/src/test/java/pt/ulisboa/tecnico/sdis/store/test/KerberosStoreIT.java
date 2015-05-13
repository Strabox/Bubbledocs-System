package pt.ulisboa.tecnico.sdis.store.test;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import static org.junit.Assert.fail;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import example.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.store.cli.exception.KerberosInvalidRequestException;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosClientAuthentication;
import util.kerberos.messages.KerberosCredential;
import util.kerberos.messages.KerberosTicket;

public class KerberosStoreIT extends SDStoreIT{
	
	private static final String UDDI_URL = "http://localhost:8081";
	
	private static final String ID_NAME = "SD-STORE-1";
	
	private static Key kcs;
	
	private static Key ks;
	
	/* Direct proxy to test Security Attacks */
	private SDStore proxy;
	
	public KerberosStoreIT() throws Exception {
		super();
		connectUDDITestOnly();
	}
	
	@BeforeClass
	public static void create() throws Exception{
		kcs = Kerberos.generateKerberosKey();
		ks = loadServerKey("SD-STORE");

	}
	
	/* Used only to test direct call tos server interface. */
	public void connectUDDITestOnly() throws Exception{
		String endpointAddress;
		try {
		 UDDINaming uddiNaming = new UDDINaming(UDDI_URL);
	     endpointAddress = uddiNaming.lookup(ID_NAME);
		} catch (JAXRException e) {
			throw new Exception();
		}
		if (endpointAddress == null)
			throw new Exception();
		SDStore_Service service = new SDStore_Service();
		proxy = service.getSDStoreImplPort();
		
		BindingProvider bindingProvider = (BindingProvider) proxy;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	}
	
	/* Auxiliary method do pass structures to handlers. */
	private void processRequest(byte[] ticket,byte[] auth,Key kcs) 
	throws KerberosException{
		BindingProvider bp = (BindingProvider) proxy;		
		Map<String, Object> requestContext = bp.getRequestContext();
		requestContext.put("auth",DatatypeConverter.printBase64Binary(auth));
		requestContext.put("ticket", DatatypeConverter.printBase64Binary(ticket));
		requestContext.put("kcs", DatatypeConverter.printBase64Binary(kcs.getEncoded()));
	}
	
	
	/* =================== TESTS WITH STORE CLIENT ====================== */
	
	@Test(expected = RuntimeException.class)
	public void invalidCredentials() throws UserDoesNotExist_Exception,
		KerberosException{
		 port.credentials = Kerberos.generateRandomNumber().getBytes();
		 port.listDocs("bruno");
	}
	
	
	@Test(expected = RuntimeException.class)
	public void invalidUsernameTicket() throws Exception{
		Key kcs = Kerberos.generateKerberosKey();
		Key ks = loadServerKey("SD-STORE");
		KerberosTicket ticket = new KerberosTicket("Angelo","SD-STORE",8,kcs);
		KerberosCredential cred = new KerberosCredential(ticket.serialize(ks),kcs);
		port.credentials = cred.serialize();
	    port.listDocs("bruno");
	} 
	
	@Test(expected = RuntimeException.class)
	public void wrongServiceTicket() throws Exception{
		Key kcs = Kerberos.generateKerberosKey();
		Key ks = loadServerKey("SD-STORE");
		KerberosTicket ticket = new KerberosTicket("bruno","SD-SSTORE",8,kcs);
		KerberosCredential cred = new KerberosCredential(ticket.serialize(ks),kcs);
		port.credentials = cred.serialize();
	    port.listDocs("bruno");
	}
	
	@Test(expected = RuntimeException.class)
	public void ticketWithWrongKS() throws Exception{
		Key kcs = Kerberos.generateKerberosKey();
		Key ks = Kerberos.generateKerberosKey();
		KerberosTicket ticket = new KerberosTicket("bruno","SD-STORE",8,kcs);
		KerberosCredential cred = new KerberosCredential(ticket.serialize(ks),kcs);
		port.credentials = cred.serialize();
	    port.listDocs("bruno");
	}
	
	/* ======== TESTS WITHOUT CLIENTS DIRECT TO SERVER INTERFACE =========== */
	
	@Test(expected = RuntimeException.class)
	public void repeatedRequest() throws UserDoesNotExist_Exception, KerberosException{
		Date reqDate = new Date();
		byte[] ticket = new KerberosTicket("bruno", "SD-STORE", 8, kcs).serialize(ks);
		byte[] auth = new KerberosClientAuthentication("bruno", reqDate).serialize(kcs);
		try{
			processRequest(ticket, auth, kcs);
			port.listDocs("bruno");
		}
		catch(KerberosInvalidRequestException e){
			fail("Error");
		}
		processRequest(ticket, auth, kcs);
		port.listDocs("bruno");
	}
	
}
