package sdis.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service; // classes generated from WSDL
import util.kerberos.messages.KerberosRequest;
import util.uddi.UDDINaming;

public class SdIdClient {
	
	/*
	 * stringToBytes(Object) - Transforms a (Serializable) object
	 * in byte of arrays.
	 */
	@SuppressWarnings("unused")
	private static byte[] objectToBytes(Object obj){
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
	
	
    public static void main(String[] args) throws Exception {
    	if(args.length < 2){
    		System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL name%n", SdIdClient.class.getName());
            return;
    	}
    	
    	String uddiURL = args[0];		//URL where is running UDDI.
    	String name = args[1];			//Service name published in UDDI.
    	
    	System.out.printf("Contacting UDDI at %s%n", uddiURL);
    	
        System.out.printf("Looking for '%s'%n", name);
        String endpointAddress = UDDILookup(uddiURL, name);
        if (endpointAddress == null) {
            System.out.println("Not found!");
            return;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }
        System.out.println("Creating stub ...");
        SDId_Service service = new SDId_Service();
        SDId id = service.getSDIdImplPort();
        
        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) id;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
        
        System.out.println("Endpoint address:" + requestContext.get(ENDPOINT_ADDRESS_PROPERTY));
        
        //------------ Some test Code ---------------------
        try{
        	id.requestAuthentication("bruno", new KerberosRequest("1", "400").serialize());
        }catch(Exception e){
        	System.out.println(e);
        }
    }
    
    public static String UDDILookup(String uddiUrl,String name) throws Exception{
        UDDINaming uddiNaming = new UDDINaming(uddiUrl);
        return uddiNaming.lookup(name);
    }

}
