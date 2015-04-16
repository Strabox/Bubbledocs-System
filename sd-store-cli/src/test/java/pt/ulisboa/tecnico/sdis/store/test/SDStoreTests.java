package pt.ulisboa.tecnico.sdis.store.test;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;

import pt.ulisboa.tecnico.sdis.store.ws.uddi.UDDINaming;


public class SDStoreTests {

	
	protected static SDStore port;
	
	
	@BeforeClass
	public static void setUpClient() throws Exception{
		 String uddiURL = "http://localhost:8081";
	        String name = "SDStore";	       
	        UDDINaming uddiNaming = new UDDINaming(uddiURL);	        
	        String endpointAddress = uddiNaming.lookup(name);
	        if (endpointAddress == null) {	            
	            return;
	        } else {	           
	        }	      
	        SDStore_Service service = new SDStore_Service();
	        port = service.getSDStoreImplPort();

	      
	        BindingProvider bindingProvider = (BindingProvider) port;
	        Map<String, Object> requestContext = bindingProvider.getRequestContext();
	        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

	        	
	}
	
	@AfterClass
	/* cleanClient() - delete proxy in end of tests. */
	public static void cleanClient(){
		port = null;
	}
	
	
}
