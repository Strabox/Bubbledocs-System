package pt.ulisboa.tecnico.sdis.store.test;

import javax.xml.registry.JAXRException;

import mockit.*;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.store.ws.impl.*;
import util.uddi.UDDINaming;



public class UDDITest {
	
	/* Mocked UDDI naming server. 
	@Mocked
	UDDINaming uddi;

	private final String UDDI_URL = "http://localhost:8081";
	private final String ENDPOINT_URL = "http://localhost:8080/store-ws/endpoint";
	private final String NAME = "SD-STORE";
	
	@Test
	public void rebindSuccess() throws Exception{
		
		new Expectations(){
			{
				new UDDINaming(UDDI_URL);
				uddi.rebind(NAME, ENDPOINT_URL);
			}
		};	
		SDStoreMain.bindUDDI(UDDI_URL, NAME, ENDPOINT_URL);
	}
	
	@Test(expected = JAXRException.class)
	public void jaxrexceptionRebindTest() throws Exception{
		
		new Expectations(){
			{
				new UDDINaming(UDDI_URL);
				uddi.rebind(NAME, ENDPOINT_URL);
				result = new JAXRException();
			}
		};
		SDStoreMain.bindUDDI(UDDI_URL, NAME, ENDPOINT_URL);
	}
	
	@Test
	public void successUnbindTest() throws Exception{
		
		new Expectations(){
			{
				uddi.unbind(NAME);
			}
		};
		SDStoreMain.unbindUDDI(uddi,NAME);
	}
	
	@Test(expected = JAXRException.class)
	public void jaxrexceptionUnbindTest() throws Exception{
		
		new Expectations(){
			{
				uddi.unbind(NAME);
				result = new JAXRException();
			}
		};
		SDStoreMain.unbindUDDI(uddi,NAME);
	}
	*/
}