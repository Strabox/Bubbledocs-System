package pt.ulisboa.tecnico.sdis.store.cli.frontendb;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;

import pt.ulisboa.tecnico.sdis.store.ws.CreateDocResponse;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.ListDocsResponse;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import util.uddi.UDDINaming;

public class FrontEndB {
	private HashMap<String, SDStore> clones;
	private String uddiURL;
	private String baseEndpointName;
	private int quorumN;
	private int quorumRT;
	private int quorumWT;
	
	public FrontEndB(String _urluddi, String _name, int _N, int _RT, int _WT) throws Exception{
		this.uddiURL = _urluddi;
        this.baseEndpointName = _name;
        this.quorumN = _N;
        this.quorumRT = _RT;
        this.quorumWT = _WT;
        
        System.out.printf("Contacting UDDI at %s%n", uddiURL);
        UDDINaming uddiNaming = new UDDINaming(uddiURL);

        
        for(int i = 1; i<= this.quorumN; i++){
        	
	        System.out.printf("Looking for '%s'%n", this.baseEndpointName+"-"+i);
	        String endpointAddress = uddiNaming.lookup(this.baseEndpointName+"-"+i);
	      
	        if (endpointAddress == null) {
	            System.out.println("Not found: "+this.baseEndpointName+"-"+i);
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
	
	        addClone(this.baseEndpointName+"-"+i, port);
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
		Response<ListDocsResponse>[] responses = new Response[quorumN];
		int responsesIndex = 0;
		int numberOfResponses = 0;
		
		for(String proxyName : clones.keySet()){
			try{
				//responses[responsesIndex] = clones.get(proxyName).listDocs(userId);
			}
			catch(Exception e){
			}
			responsesIndex++;
		}
		while(numberOfResponses <= quorumRT){
	    	/*for(Response responseUnknown : responses){
	    		if(responseUnknown.isDone()){
	    			numberOfResponses++;
	    		}
	    	}*/
	    	try{
	    		Thread.sleep(100 /* milliseconds */);
	    	}
	    	catch(InterruptedException e){
	    		System.out.println("interrupted sleep");
	    	}
		}
		for(Response response : responses){
    		response.cancel(false);
		}
		//mergeLists(makeStringsFromResponses(responses));
		return null;
	}
	
	private ArrayList<ArrayList<String>> makeStringsFromResponses(Response<ListDocsResponse> responsesraw){
		ArrayList<ArrayList<String>> arrays = new ArrayList<ArrayList<String>>();
		
		//for(Response response : responses){
    		try{
    			//arrays.add(response.get().getReturn());
    		}
    		catch(Exception e){
    			//IGNORES BECAUSE IT'S UNNECESSARY
    		}
    	return arrays;
	}
	
	private ArrayList<String> mergeLists(ArrayList<ArrayList<String>> lists){
		ArrayList<String> result = new ArrayList<String>();
		for(ArrayList<String> array : lists){
			for(String string : array){
				if(!result.contains(string)) result.add(string);
			}
		}
		return result;
	}
	
}
