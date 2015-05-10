package pt.ulisboa.tecnico.sdis.store.cli.frontendb;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;







import javax.xml.ws.Response;

import pt.ulisboa.tecnico.sdis.store.ws.*;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosClientAuthentication;
import util.kerberos.messages.KerberosCredential;
import util.uddi.UDDINaming;

public class FrontEnd {
	private SDStore[] clones;
	private String uddiURL;
	private String baseEndpointName;
	private int numberClones;
	private int quorumRT;
	private int quorumWT;
	//private int [] rt = new int[nservers];
	//private int [] wt = new int[nservers];


	public FrontEnd(String _urluddi, String _name, int _numberClones, int _RT, int _WT) throws Exception{
		this.uddiURL = _urluddi;
		this.baseEndpointName = _name;
		this.numberClones = _numberClones;
		this.quorumRT = _RT;
        this.quorumWT = _WT;
		clones = new SDStore[_numberClones];
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		for(int i = 1; i<= _numberClones; i++){
			String endpointAddress = uddiNaming.lookup(this.baseEndpointName+"-"+i);	      
			if (endpointAddress == null) 
				continue;
			SDStore_Service service = new SDStore_Service();
			SDStore port = service.getSDStoreImplPort();
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			clones[i-1]=port;

		}
	}

	public void processRequest(byte[] credentials) throws KerberosException{
		for(int i = 1; i<= numberClones; i++){
			SDStore aux = clones [i-1];
			BindingProvider bp = (BindingProvider) aux;		
			Map<String, Object> requestContext = bp.getRequestContext();
			KerberosCredential cred = KerberosCredential.deserialize(credentials);
			Date requestDate = new Date();
			KerberosClientAuthentication auth = new KerberosClientAuthentication(cred.getClient(),requestDate);
			requestContext.put("auth", Base64.getEncoder().encodeToString(auth.serialize(cred.getKcs())));
			requestContext.put("ticket", Base64.getEncoder().encodeToString(cred.getTicket()));
			requestContext.put("kcs", Base64.getEncoder().encodeToString(cred.getKcs().getEncoded()));
			requestContext.put("date", requestDate);
		}
	}

	public void createDoc(DocUserPair pair) throws DocAlreadyExists_Exception{
		for(int i=1;i<=numberClones;i++){
			try{
				clones[i-1].createDoc(pair);
			}
			catch(DocAlreadyExists_Exception e){
				DocAlreadyExists E = new DocAlreadyExists();
				throw new DocAlreadyExists_Exception("Doc already exists", E);
			}
			catch(Exception e){
				//SERVER DOWN
			}
		}		

	}
	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception{
		List<String> result = null;
		for(int i=1;i<=numberClones;i++){
			try{
				result = clones[i-1].listDocs(userId);
			}
			catch(UserDoesNotExist_Exception e){
				UserDoesNotExist E = new UserDoesNotExist();
				throw new UserDoesNotExist_Exception("User does not exists", E);
			}
			catch(Exception e){
				//SERVER DOWN
			}
		}
		return result;
		/*
		Response<ListDocsResponse>[] responses = new Response[numberClones];
		int numberOfResponses = 0;
		
		for(int i=1;i<=numberClones;){
			try{
				//responses[i-1] = clones[i-1].listDocsAsync(userId);
			}
			catch(Exception e){
				//SERVER DOWN
			}
		}
		while(numberOfResponses <= quorumRT){
	    	/*for(Response responseUnknown : responses){
	    		if(responseUnknown.isDone()){
	    			numberOfResponses++;
	    		}
	    	}*/
	    	//try{
				//System.out.println("sleeping");
	    		//Thread.sleep(100 /* milliseconds */);
	    	//}
	    	//catch(InterruptedException e){
	    	//	System.out.println("interrupted sleep");
	    	//}
		//}
		/*for(Response response : responses){
    		response.cancel(false);
		}*/
		//mergeLists(makeStringsFromResponses(responses));
		//return null;
	}



	public void store(DocUserPair docUserPair, byte[] contents) throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		for(int i=1;i<=numberClones;i++){
			try{
				clones[i-1].store(docUserPair,contents);
			}
			catch(DocDoesNotExist_Exception e){
				DocDoesNotExist E = new DocDoesNotExist();
				throw new DocDoesNotExist_Exception("Doc does not exists", E);
			}
			catch(UserDoesNotExist_Exception e){
				UserDoesNotExist E = new UserDoesNotExist();
				throw new UserDoesNotExist_Exception("User does not exists", E);
			}
			catch(Exception e){
				//SERVER DOWN
			}
		}	
	}


	public byte[] load(DocUserPair docUserPair) throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		byte [] result = null;
		for(int i=1;i<=numberClones;i++){
			try{
				result = clones[i-1].load(docUserPair);
			}
			catch(DocDoesNotExist_Exception e){
				DocDoesNotExist E = new DocDoesNotExist();
				throw new DocDoesNotExist_Exception("Doc does not exists", E);
			}
			catch(UserDoesNotExist_Exception e){
				UserDoesNotExist E = new UserDoesNotExist();
				throw new UserDoesNotExist_Exception("User does not exists", E);
			}
			catch(Exception e){
				//SERVER DOWN
			}
		}	
		return result;
	}

	private ArrayList<ArrayList<String>> makeStringsFromResponses(Response<ListDocsResponse> responsesraw){
		ArrayList<ArrayList<String>> arrays = new ArrayList<ArrayList<String>>();
		
		//for(Response response : responsesraw){
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
