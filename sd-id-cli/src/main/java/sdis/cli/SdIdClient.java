package sdis.cli;

import java.util.*;
import javax.xml.ws.*;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import pt.ulisboa.tecnico.sdis.id.ws.*; // classes generated from WSDL


public class SdIdClient {

    public static void main(String[] args) throws Exception {
        SDId_Service service = new SDId_Service();
        SDId id = service.getSDIdImplPort();
        // get endpoint address
        BindingProvider bindingProvider = (BindingProvider) id;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();

        System.out.println("Endpoint address:");
        System.out.println(requestContext.get(ENDPOINT_ADDRESS_PROPERTY));
        
        try{
        	id.createUser("User", "adas@a.a");
        }catch(Exception e){
        	System.out.println(e);
        }
    }

}
