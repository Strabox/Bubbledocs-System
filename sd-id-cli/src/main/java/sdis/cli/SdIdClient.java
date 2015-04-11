package sdis.cli;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

import javax.xml.ws.*;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import pt.ulisboa.tecnico.sdis.id.ws.*; // classes generated from WSDL


public class SdIdClient {
	
	/*
	 * stringToBytes(Serializable) - Transforms a (Serializable) object
	 * in byte of arrays.
	 */
	private static byte[] objectToBytes(Serializable obj){
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
	private static Object bytesToObject(byte[] bytes){
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
	
    public static void main(String[] args) throws Exception {
        SDId_Service service = new SDId_Service();
        SDId id = service.getSDIdImplPort();
        // get endpoint address
        BindingProvider bindingProvider = (BindingProvider) id;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();

        System.out.print("Endpoint address:");
        System.out.println(requestContext.get(ENDPOINT_ADDRESS_PROPERTY));
        
        try{
        	id.createUser("User", "adas@a.a");
        	
        	Boolean b = (Boolean) bytesToObject(id.requestAuthentication("User", objectToBytes("pass")));
        	if(b.booleanValue())
        		System.out.println("Login Successful.");
        	
        }catch(Exception e){
        	System.out.println(e);
        }
    }

}
