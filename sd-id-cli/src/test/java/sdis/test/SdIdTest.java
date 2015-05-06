package sdis.test;

import sdis.cli.SdIdClient;


public class SdIdTest {

	private static final String UDDI_URL = "http://localhost:8081";
	
	private static final String ID_NAME = "SD-ID";
	
	/* Proxy for idServer. */
	protected static SdIdClient idServer;
	
	public SdIdTest() throws Exception{
		idServer = new SdIdClient(UDDI_URL, ID_NAME);
	}
	
	
}
