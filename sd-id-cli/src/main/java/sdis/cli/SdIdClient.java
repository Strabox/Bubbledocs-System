package sdis.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.security.Key;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service; // classes generated from WSDL
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import sdis.kerberos.KerberosClientUtil;
import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosClientAuthentication;
import util.kerberos.messages.KerberosReply;
import util.kerberos.messages.KerberosRequest;
import util.kerberos.messages.KerberosServerAuthentication;
import util.uddi.UDDINaming;

public class SdIdClient {
	
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
        
        //----------- Get SD-STORE proxies ----------------
        endpointAddress = UDDILookup("http://localhost:8082/store-ws/endpoint", "SD-STORE");	
        SDStore_Service serv = new SDStore_Service();
        SDStore store = serv.getSDStoreImplPort();
        bindingProvider = (BindingProvider) store;
        requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
        //------------ Some test Code ---------------------
        try{
        	String nonce = Kerberos.generateRandomNumber();
        	byte[] r= id.requestAuthentication("bruno", new KerberosRequest(1, nonce).serialize());
        	KerberosReply reply = KerberosReply.deSerializeReply(r);
        	Key kc = Kerberos.getKeyFromBytes(Kerberos.digestPassword("Bbb2", Kerberos.MD5));
        	KerberosServerAuthentication l = KerberosServerAuthentication.deserialize(reply.getAuthentication(), kc);
        	
        	KerberosClientAuthentication hmm = new KerberosClientAuthentication("bruno");
        	KerberosClientUtil.request(store,reply.getTicket(), hmm.serialize(l.getKcs()), Kerberos.generateRandomNumber().getBytes());
    
        	store.listDocs("bruno");
        	
       
        }catch(Exception e){
        	System.out.println(e);
        }
    }
    
    public static String UDDILookup(String uddiUrl,String name) throws Exception{
        UDDINaming uddiNaming = new UDDINaming(uddiUrl);
        return uddiNaming.lookup(name);
    }

}
