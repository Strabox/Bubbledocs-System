package pt.ulisboa.tecnico.sdis.store.cli.frontendb;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import util.uddi.UDDINaming;

public class FrontEndB {
	private HashMap<String, SDStore> clones;
	private String uddiURL;
	private String baseEndpointName;
	private int numberClones;
	
	public FrontEndB(String _urluddi, String _name, int _numberClones) throws Exception{
		this.uddiURL = _urluddi;
        this.baseEndpointName = _name;
        this.numberClones = _numberClones;
        
        for(int i = 1; i<= this.numberClones; i++){
        	System.out.printf("Contacting UDDI at %s%n", uddiURL);
	        UDDINaming uddiNaming = new UDDINaming(uddiURL);
	
	        System.out.printf("Looking for '%s'%n", this.baseEndpointName+i);
	        String endpointAddress = uddiNaming.lookup(this.baseEndpointName+i);
	      
	        if (endpointAddress == null) {
	            System.out.println("Not found: "+this.baseEndpointName+i);
	            return;
	        } else {
	            System.out.printf("Found %s%n", endpointAddress);
	        }
	
	        System.out.println("Creating stub ...");
	        SDStore_Service service = new SDStore_Service();
	        SDStore port = service.getSDStoreImplPort();
	
	        System.out.println("Setting endpoint address ...");
	        BindingProvider bindingProvider = (BindingProvider) port;
	        Map<String, Object> requestContext = bindingProvider.getRequestContext();
	        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	
	        addClone(this.baseEndpointName+i, port);
        }
	}
	
	public void addClone(String _name, SDStore _clone){
		if (!clones.containsKey(_name)) clones.put(_name, _clone);
	}
	
	public void createDoc(DocUserPair pair){
		for(String proxyName : clones.keySet()){
			try{
				clones.get(proxyName).createDoc(pair);
			}
			catch(Exception e){
				
			}
		}
		
	    		
	}
	
	public List<String> listDocs(String userId){
		for(String proxyName : clones.keySet()){
			try{
				//clones.get(proxyName).listDocs(userId);
			}
			catch(Exception e){
				
			}
		}
		return null;
	}
	
}
