package sdis.test;

import javax.xml.registry.JAXRException;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import sdis.cli.SdIdClient;
import sdis.uddi.UDDINaming;

public class UDDITest extends SdIdTest{

	@Mocked
	UDDINaming uddi;
	
	@Test
	public void successLookup() throws Exception{
		
		new Expectations(){
			{
			new UDDINaming(anyString);	
			uddi.lookup(anyString);
			}
		};
		SdIdClient.UDDILookup("http://localhost:8081", "SD-ID");
	}
	
	@Test(expected = JAXRException.class)
	public void failedLookup() throws Exception{
		
		new Expectations(){
			{
			new UDDINaming(anyString);	
			uddi.lookup(anyString);
			result =  new JAXRException();
			}
		};
		SdIdClient.UDDILookup("http://localhost:8081", "SD-ID");
	}
	
}
