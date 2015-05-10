package pt.ulisboa.tecnico.sdis.store.cli.frontend;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;







import javax.xml.ws.Response;

import org.apache.commons.lang3.mutable.MutableInt;

import pt.ulisboa.tecnico.sdis.store.cli.handlers.KerberosHandler;
import pt.ulisboa.tecnico.sdis.store.ws.*;
import util.kerberos.Kerberos;
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

	private Date processRequest(byte[] credentials) throws KerberosException{
		Date requestDate = new Date();
		for(int i = 1; i<= numberClones; i++){
			SDStore aux = clones [i-1];
			BindingProvider bp = (BindingProvider) aux;		
			Map<String, Object> requestContext = bp.getRequestContext();
			KerberosCredential cred = KerberosCredential.deserialize(credentials);
			KerberosClientAuthentication auth = new KerberosClientAuthentication(cred.getClient(),requestDate);
			requestContext.put("auth", Base64.getEncoder().encodeToString(auth.serialize(cred.getKcs())));
			requestContext.put("ticket", Base64.getEncoder().encodeToString(cred.getTicket()));
			requestContext.put("kcs", Base64.getEncoder().encodeToString(cred.getKcs().getEncoded()));
		}
		return requestDate;
	}

	
	private boolean processReply(@SuppressWarnings("rawtypes") Response response,byte[] cred,Date d){
		try{
			KerberosCredential credential = KerberosCredential.deserialize(cred);
			byte[] b = (byte[]) response.getContext().get(KerberosHandler.TIMESTAMP_PROPERTY);
			String date = new String(Kerberos.decipherText(credential.getKcs(), b));
			Calendar cal  = DatatypeConverter.parseDateTime(date);
			Date requestTime = cal.getTime();
			if(requestTime.equals(d))
				return true;
			return false;
		}catch(KerberosException e){
			throw new RuntimeException();
		}
	}
	
	/*===========================================================================*/
	public void createDoc(DocUserPair pair,final byte[] credential) 
	throws DocAlreadyExists_Exception{
		final Date requestTime;
		try {
			requestTime = processRequest(credential);
		} catch (KerberosException e1) {
			throw new RuntimeException();
		}

		final MutableInt numberOfResponses = new MutableInt(0);
		final MutableInt numberOfFailures = new MutableInt(0);
		ArrayList<Future<?>> responses = new ArrayList<Future<?>>(numberClones);
		for(int i=1;i<=numberClones;i++){
			try{
				System.out.println("\n\n\n\ngenerating an async call "+pair.getDocumentId());
				responses.add(clones[i-1].createDocAsync(pair, new AsyncHandler<CreateDocResponse>() {
			        @Override
			        public void handleResponse(Response<CreateDocResponse> response) {
			            try {
			                System.out.println("entered handler");
			                numberOfResponses.increment();
			                if(!processReply(response, credential,requestTime)){		// this "if" won't complete if there are problems with the handlers, check runtime exception
			                	System.out.println("GOT KERBEROS FAILURE");				// this happens if there is no exception from processreply, but there was a hack
			                	throw new ExecutionException(null);
			                }
			                System.out.println("Asynchronous call result arrived. checking if exception ");
			                response.get();
			                System.out.println("not exception - success");
			            } catch (InterruptedException e) {
			                System.out.println("Caught interrupted exception.");
			                System.out.println(e.getCause());
			            } catch (ExecutionException e) {
			                System.out.println("Caught execution exception.  Incrementing!");
			                numberOfFailures.increment();
			                System.out.println(e.getCause());
			            }
			            catch (RuntimeException e) {									//this catches an exception from the handler
			                System.out.println("GOT KERBEROS EXCEPTION");
			                numberOfFailures.increment();
			                System.out.println(e.getCause());
			        	}
			        }
				}));
			}
			catch(Exception e){
				
			}
		}
		int numberOfChecks = 0;
		int maxChecks = 10;
		System.out.println("Waiting for answers");
		while (numberOfResponses.intValue()<=quorumRT) {
			System.out.println("responses received before sleeping: "+numberOfResponses.intValue());
			numberOfChecks++;
	    	if(numberOfChecks > maxChecks){
	    		System.out.println("took too long to get responses");
	    		break;
	    	}
	    		
	    	if(numberOfFailures.intValue()>0){
	    		System.out.println("got a failure that stopped responses");
	    		break;
	    	}
	    	
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("waiting...");
			}
	    	System.out.println(".");
	    	System.out.flush();
	    }
	    System.out.println("Stopping requests");
	    for(Future<?> response : responses){
    		response.cancel(false);
		}
	    
	    System.out.println("number of failures: "+numberOfFailures.intValue());
	    if(numberOfFailures.intValue()>0){
	    	DocAlreadyExists E = new DocAlreadyExists();
			throw new DocAlreadyExists_Exception("Failed to create doc on all servers", E);
	    }
	    
		return;
	}
	public List<String> listDocs(String userId,final byte[] credential) 
	throws UserDoesNotExist_Exception{
		final Date requestTime;
		try {
			requestTime = processRequest(credential);
		} catch (KerberosException e1) {
			throw new RuntimeException();
		}
		
		final MutableInt numberOfResponses = new MutableInt(0);
		final MutableInt numberOfSuccesses = new MutableInt(0);
		final ArrayList<ArrayList<String>> arrays = new ArrayList<ArrayList<String>>();
		ArrayList<Future<?>> responses = new ArrayList<Future<?>>(numberClones);
		for(int i=1;i<=numberClones;i++){
			try{
				System.out.println("generating an async call");
				responses.add(clones[i-1].listDocsAsync(userId, new AsyncHandler<ListDocsResponse>() {
			        @Override
			        public void handleResponse(Response<ListDocsResponse> response) {
			            try {
			                System.out.println("entered handler");
			                if(!processReply(response, credential,requestTime)){
			                	System.out.println("GOT KERBEROS FAILURE");
			                	throw new ExecutionException(null);
			                }
			                numberOfResponses.increment();
			                System.out.println("Asynchronous call result arrived: ");
			                ArrayList<String> aListFromAServer = (ArrayList<String>) response.get().getDocumentId();
			                System.out.println("valid list from a server");
			                System.out.println("not exception - success");
			                numberOfSuccesses.increment();
			                arrays.add(aListFromAServer);
			                System.out.println("added list to TO-MERGE list");
			            } catch (InterruptedException e) {
			                System.out.println("Caught interrupted exception.");
			                System.out.println(e.getCause());
			            } catch (ExecutionException e) {
			                System.out.println("Caught execution exception.");
			                System.out.println(e.getCause());
			            }
			        	catch (RuntimeException e) {
		                	System.out.println("Caught kerberos exception.");
		                	System.out.println(e.getCause());
		            	}
			        }
				}));
			}
			catch(Exception e){
				
			}
		}
		int numberOfChecks = 0;
		int maxChecks = 10;
		System.out.println("Waiting for answers");
		while (numberOfResponses.intValue()<=quorumRT) {
			System.out.println("responses received before sleeping: "+numberOfResponses.intValue());
			numberOfChecks++;
	    	if(numberOfChecks>maxChecks) break;
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("waiting...");
			}
	    	System.out.println(".");
	    	System.out.flush();
	    }
	    System.out.println("Stopping requests");
	    for(Future<?> response : responses){
    		response.cancel(false);
		}
	    
	    System.out.println("number of successes: "+numberOfSuccesses.intValue());
	    if(numberOfSuccesses.intValue()==0){
	    	UserDoesNotExist E = new UserDoesNotExist();
			throw new UserDoesNotExist_Exception("Failed to fetch list from servers", E);
	    }
	    
		return mergeLists(arrays);
	}



	public void store(DocUserPair docUserPair, byte[] contents,byte[] credential) 
	throws CapacityExceeded_Exception, DocDoesNotExist_Exception, 
	UserDoesNotExist_Exception {
		try {
			Date requestRime = processRequest(credential);
		} catch (KerberosException e1) {
			throw new RuntimeException();
		}
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


	public byte[] load(DocUserPair docUserPair,byte[] credential) 
	throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		try {
			Date requestRime = processRequest(credential);
		} catch (KerberosException e1) {
			throw new RuntimeException();
		}
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

	private ArrayList<ArrayList<String>> makeStringsFromResponses(ArrayList<Response<ListDocsResponse>> responsesraw){
		ArrayList<ArrayList<String>> arrays = new ArrayList<ArrayList<String>>();
		
		for(Response<ListDocsResponse> response : responsesraw){
    		try{
    			arrays.add((ArrayList<String>) response.get().getDocumentId());
    		}
    		catch(Exception e){
    			//IGNORES BECAUSE IT'S UNNECESSARY
    		}
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
