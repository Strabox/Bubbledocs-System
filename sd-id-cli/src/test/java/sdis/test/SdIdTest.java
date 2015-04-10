package sdis.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;


public class SdIdTest {

	/* Proxy for idServer. */
	protected static SDId idServer;
	
	/* Used to setup the client before all tests.
	 * (Get Proxy, fix endpoints etc). */
	@BeforeClass
	public static void setUpClient(){
		SDId_Service service = new SDId_Service();
        idServer = service.getSDIdImplPort();		//GET the PROXY!!!!
        //idServer = (BindingProvider) id;
   
	}
	
	@AfterClass
	public static void cleanClient(){
		idServer = null;
	}
	
	public void populate4Test() {}
	
}
