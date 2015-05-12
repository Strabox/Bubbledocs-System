package example.ws.uddi;

import javax.xml.registry.JAXRException;

/**
 * Abstract class for a classical UDDI client.
 * @author André
 *
 */
public abstract class UDDIClient {
	
	/**
	 * Uddi Universal Resource Locator
	 */
	protected String uddiUrl;
	
	/**
	 * Server name in name's service (UDDI).
	 */
	protected String idName;
	
	
	public UDDIClient(String uddiURL,String idName){
		this.uddiUrl = uddiURL;
		this.idName = idName;;
	}
	
	public void connectUDDI() throws Exception{
		String endpointAddress;
		try {
			endpointAddress = UDDILookup(uddiUrl,idName);
		} catch (JAXRException e) {
			throw new Exception();
		}
		if (endpointAddress == null)
			throw new Exception();
		getSpecificProxy(endpointAddress);
	}
	
	protected abstract void getSpecificProxy(String endpoint) throws Exception;
	
	private String UDDILookup(String uddiUrl,String name) throws Exception{
        UDDINaming uddiNaming = new UDDINaming(uddiUrl);
        return uddiNaming.lookup(name);
    }
}
