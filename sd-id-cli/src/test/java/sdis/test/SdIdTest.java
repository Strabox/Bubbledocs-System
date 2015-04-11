package sdis.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
	/* cleanClient() - delete proxy in end of tests. */
	public static void cleanClient(){
		idServer = null;
	}
	
	/*
	 * stringToBytes(Serializable) - Transforms a (Serializable) object
	 * in byte of arrays.
	 */
	protected static byte[] objectToBytes(Serializable obj){
		try{
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			ObjectOutputStream oOut = new ObjectOutputStream(bOut);
			oOut.writeObject(obj);
			return bOut.toByteArray();
		}
		catch(IOException e){
			System.err.println(e);
			return null;
		}
	}
	
	/*
	 * bytesToObject(bytes) - Transforms bytes in an (Serializable) object.
	 */
	protected static Object bytesToObject(byte[] bytes){
		try{
			ByteArrayInputStream b = new ByteArrayInputStream(bytes);
			ObjectInputStream o = new ObjectInputStream(b);
			return o.readObject();
		}
		catch(IOException e){
			System.err.println(e);
			return null;
		}
		catch(ClassNotFoundException e){
			System.err.println(e);
			return null;
		}
	}
	
	public void populate4Test(){}
}
