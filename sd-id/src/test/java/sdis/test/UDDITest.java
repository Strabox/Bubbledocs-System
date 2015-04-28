package sdis.test;

import javax.xml.registry.JAXRException;

import mockit.Expectations;
import mockit.Mocked;
//import mockit.Mocked;

import org.junit.Test;

import sdis.SDIDMain;
import util.uddi.UDDINaming;


public class UDDITest {
	
	/* Mocked UDDI naming server. */
	@Mocked
	UDDINaming uddi;

	private final String UDDI_URL = "http://localhost:8081";
	private final String ENDPOINT_URL = "http://localhost:8080/id-ws/endpoint";
	private final String NAME = "SD-ID";
	
	@Test
	public void rebindSuccess() throws Exception{
		
		new Expectations(){
			{
				new UDDINaming(UDDI_URL);
				uddi.rebind(NAME, ENDPOINT_URL);
			}
		};	
		SDIDMain.bindUDDI(UDDI_URL, NAME, ENDPOINT_URL);
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
		SDIDMain.bindUDDI(UDDI_URL, NAME, ENDPOINT_URL);
	}
	
	@Test
	public void successUnbindTest() throws Exception{
		
		new Expectations(){
			{
				uddi.unbind(NAME);
			}
		};
		SDIDMain.unbindUDDI(uddi,NAME);
	}
	
	@Test(expected = JAXRException.class)
	public void jaxrexceptionUnbindTest() throws Exception{
		
		new Expectations(){
			{
				uddi.unbind(NAME);
				result = new JAXRException();
			}
		};
		SDIDMain.unbindUDDI(uddi,NAME);
	}
	
}
