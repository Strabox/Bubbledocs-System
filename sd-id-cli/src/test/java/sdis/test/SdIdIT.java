package sdis.test;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import sdis.cli.SdIdClient;
import example.ws.uddi.UDDINaming;


public class SdIdIT {

	private static final String UDDI_URL = "http://localhost:8081";
	
	private static final String ID_NAME = "SD-ID";
	
	/**
	 *  Client for idServer. 
	 */
	protected static SdIdClient idClient;
	
	/**
	 *  Proxy for idServer.
	 */
	protected static SDId idRemote;
	
	
	public SdIdIT() throws Exception{
		idClient = new SdIdClient(UDDI_URL, ID_NAME);
		connectUDDITestOnly();
	}
	
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
		SDId_Service service = new SDId_Service();
		idRemote = service.getSDIdImplPort();
		
		BindingProvider bindingProvider = (BindingProvider) idRemote;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	}
	
}
