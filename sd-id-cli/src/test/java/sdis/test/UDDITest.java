package sdis.test;

import mockit.Mocked;

import org.junit.Test;

import util.uddi.UDDINaming;

public class UDDITest extends SdIdTest{

	public UDDITest() throws Exception {
		super();
	}

	@Mocked
	UDDINaming uddi;
	
	@Test
	public void successLookup(){
		/*
		new Expectations(){
			{
			new UDDINaming(anyString);	
			uddi.lookup(anyString);
			}
		};
		SdIdClient.UDDILookup("http://localhost:8081", "SD-ID");
		*/
	}
	

	
}
