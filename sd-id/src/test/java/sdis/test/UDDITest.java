package sdis.test;

import javax.xml.registry.JAXRException;

import mockit.*;
//import mockit.Mocked;

import org.junit.*;

import sdis.SDIDMain;
import sdis.uddi.UDDINaming;

public class UDDITest {
	
	@Mocked
	UDDINaming uddi;
	
	
	@Test
	public void success() throws Exception{
		
		new Expectations(){
			{
				new UDDINaming("http://localhost:8081");
				uddi.rebind("SD-ID", "http://localhost:8080/id-ws/endpoint");
				uddi.unbind("SD-ID");
			}
		};
		//SDIDMain.main(args);
		SDIDMain cli = new SDIDMain();
		UDDINaming realuddi = cli.bindUDDI("http://localhost:8081", "SD-ID", "http://localhost:8080/id-ws/endpoint");
		//UDDINaming realuddi = cli.bindUDDI("http://localhost:8081", "SD-ID", any);
		cli.unbind(realuddi,"SD-ID");
	}
	
	@Test(expected=JAXRException.class)
	public void jaxrexceptionTest() throws Exception{
		
		new Expectations(){
			{
				
				uddi.rebind("DNSgoogle", "http://8.8.8.8/dns");
				result = new JAXRException();
			}
		};
		SDIDMain cli = new SDIDMain();
		cli.bindUDDI("http://8.8.8.8", "DNSgoogle", "http://8.8.8.8/dns");
		
	}
}
