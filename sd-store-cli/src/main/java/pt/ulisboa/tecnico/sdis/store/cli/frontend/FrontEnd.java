package pt.ulisboa.tecnico.sdis.store.cli.frontend;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;

import org.apache.commons.lang3.mutable.MutableInt;

import pt.ulisboa.tecnico.sdis.store.cli.Crypto;
import pt.ulisboa.tecnico.sdis.store.cli.handlers.KerberosHandler;
import pt.ulisboa.tecnico.sdis.store.cli.handlers.RelayClientHandler;
import pt.ulisboa.tecnico.sdis.store.ws.*;
import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosClientAuthentication;
import util.kerberos.messages.KerberosCredential;
import example.ws.uddi.UDDINaming;

public class FrontEnd {
	private SDStore[] clones;
	private String uddiURL;
	private String baseEndpointName;
	private int numberClones;
	private int quorumRT;
	private int quorumWT;
	private int mycid;
	int maxcid = -2;
	int maxseq = -2;
	private int [] rt ={1,1,1};
	private int [] wt ={1,1,1};
	private double rq=0;
	private double wq=0;
	private Crypto crypto;


	public FrontEnd(String _urluddi, String _name, int _numberClones, int _RT, int _WT) throws Exception{
		this.uddiURL = _urluddi;
		this.baseEndpointName = _name;
		this.numberClones = _numberClones;
		this.quorumRT = _RT;
		this.quorumWT = _WT;
		this.crypto = new Crypto();
		for (int i : rt)
		    rq += i;
		rq=rq/2;
		for (int i : wt)
		    wq += i;
		wq=wq/2;
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
		Random rand = new Random();
		mycid = rand.nextInt(500);
	}

	/**
	 * Used to pass kerberos structures to handlers.
	 * @param credentials
	 * @return Date used as nonce, to verify in server response.
	 */
	private Date processRequest(byte[] credentials,String userId){
		try{
			Date requestDate = new Date();
			for(int i = 1; i<= numberClones; i++){
				SDStore aux = clones [i-1];
				if (aux==null)
						continue;
				BindingProvider bp = (BindingProvider) aux;		
				Map<String, Object> requestContext = bp.getRequestContext();
				KerberosCredential cred = KerberosCredential.deserialize(credentials);
				KerberosClientAuthentication auth = new KerberosClientAuthentication(userId,requestDate);
				requestContext.put("auth",DatatypeConverter.printBase64Binary(auth.serialize(cred.getKcs())));
				requestContext.put("ticket", DatatypeConverter.printBase64Binary(cred.getTicket()));
				requestContext.put("kcs", DatatypeConverter.printBase64Binary(cred.getKcs().getEncoded()));
			}
			return requestDate;
		}catch(KerberosException e){
			throw new RuntimeException();
		}
	}

	/**
	 * Used to verify if the server response is correct.
	 * @param response
	 * @param cred
	 * @param d
	 * @return true if the response is legit false otherwise.
	 */
	private boolean processReply(byte[] timeStamp,byte[] cred,Date d){
		try{
			KerberosCredential credential = KerberosCredential.deserialize(cred);
			String date = new String(Kerberos.decipherText(credential.getKcs(), timeStamp));
			Calendar cal  = DatatypeConverter.parseDateTime(date);
			Date requestTime = cal.getTime();
			if(requestTime.equals(d))
				return true;
			return false;
		}catch(KerberosException e){
			throw new RuntimeException();
		}
	}
	//(byte[]) response.getContext().get(KerberosHandler.TIMESTAMP_PROPERTY);
	/*===================== REMOTE CALLS ==========================*/

	public void createDoc(DocUserPair pair,final byte[] credential) 
			throws DocAlreadyExists_Exception{
		final Date requestTime;
		requestTime = processRequest(credential,pair.getUserId());

		final MutableInt numberOfResponses = new MutableInt(0);
		final MutableInt numberOfFailures = new MutableInt(0);
		ArrayList<Future<?>> responses = new ArrayList<Future<?>>(numberClones);
		for(int i=1;i<=numberClones;i++){
			try{
				//System.out.println("\n\n\n\ngenerating an async call "+pair.getDocumentId());
				responses.add(clones[i-1].createDocAsync(pair, new AsyncHandler<CreateDocResponse>() {
					@Override
					public void handleResponse(Response<CreateDocResponse> response) {
						try {
							//System.out.println("entered handler");
							numberOfResponses.increment();
							byte[] time = (byte[]) response.getContext().get(KerberosHandler.TIMESTAMP_PROPERTY);
							if(!processReply(time, credential,requestTime)){		// this "if" won't complete if there are problems with the handlers, check runtime exception
								//System.out.println("GOT KERBEROS FAILURE");				// this happens if there is no exception from processreply, but there was a hack
								throw new ExecutionException(null);
							}
							//System.out.println("Asynchronous call result arrived. checking if exception ");
							response.get();
							//System.out.println("not exception - success");
						} catch (InterruptedException e) {
							//System.out.println("Caught interrupted exception.");
							//System.out.println(e.getCause());
						} catch (ExecutionException e) {
							//System.out.println("Caught execution exception.  Incrementing!");
							numberOfFailures.increment();
							//System.out.println(e.getCause());
						}
						catch (RuntimeException e) {									//this catches an exception from the handler
							//System.out.println("GOT KERBEROS EXCEPTION");
							numberOfFailures.increment();
							//System.out.println(e.getCause());
						}
					}
				}));
			}
			catch(Exception e){

			}
		}
		int numberOfChecks = 0;
		int maxChecks = 10;
		int necessaryResponses = numberClones/2;
		//System.out.println("Waiting for answers");
		while (numberOfResponses.intValue()<=necessaryResponses) {
			//System.out.println("responses received before sleeping: "+numberOfResponses.intValue());
			numberOfChecks++;
			if(numberOfChecks > maxChecks){
				//System.out.println("took too long to get responses");
				break;
			}

			if(numberOfFailures.intValue()>0){
				//System.out.println("got a failure that stopped responses");
				break;
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//System.out.println("waiting...");
			}
			//System.out.println(".");
			//System.out.flush();
		}
		//System.out.println("Stopping requests");
		for(Future<?> response : responses){
			response.cancel(false);
		}

		//System.out.println("number of failures: "+numberOfFailures.intValue());
		if(numberOfFailures.intValue()>0){
			DocAlreadyExists E = new DocAlreadyExists();
			throw new DocAlreadyExists_Exception("Failed to create doc on all servers", E);
		}

		return;
	}
	public List<String> listDocs(String userId,final byte[] credential) 
			throws UserDoesNotExist_Exception{
		final Date requestTime;
		requestTime = processRequest(credential,userId);


		final MutableInt numberOfResponses = new MutableInt(0);
		final MutableInt numberOfSuccesses = new MutableInt(0);
		final ArrayList<ArrayList<String>> arrays = new ArrayList<ArrayList<String>>();
		ArrayList<Future<?>> responses = new ArrayList<Future<?>>(numberClones);
		for(int i=1;i<=numberClones;i++){
			try{
				//System.out.println("generating an async call");
				responses.add(clones[i-1].listDocsAsync(userId, new AsyncHandler<ListDocsResponse>() {
					@Override
					public void handleResponse(Response<ListDocsResponse> response) {
						try {
							//System.out.println("entered handler");
							byte[] time = (byte[]) response.getContext().get(KerberosHandler.TIMESTAMP_PROPERTY);
							if(!processReply(time, credential,requestTime)){
								//System.out.println("GOT KERBEROS FAILURE");
								throw new ExecutionException(null);
							}
							numberOfResponses.increment();
							//System.out.println("Asynchronous call result arrived: ");
							ArrayList<String> aListFromAServer = (ArrayList<String>) response.get().getDocumentId();
							//System.out.println("valid list from a server");
							//System.out.println("not exception - success");
							numberOfSuccesses.increment();
							arrays.add(aListFromAServer);
							//System.out.println("added list to TO-MERGE list");
						} catch (InterruptedException e) {
							//System.out.println("Caught interrupted exception.");
							//System.out.println(e.getCause());
						} catch (ExecutionException e) {
							//System.out.println("Caught execution exception.");
							//System.out.println(e.getCause());
						}
						catch (RuntimeException e) {
							//System.out.println("Caught kerberos exception.");
							//System.out.println(e.getCause());
						}
					}
				}));
			}
			catch(Exception e){

			}
		}
		int numberOfChecks = 0;
		int maxChecks = 10;
		//System.out.println("Waiting for answers");
		int necessaryResponses = numberClones/2;
		while (numberOfResponses.intValue()<=necessaryResponses) {
			//System.out.println("responses received before sleeping: "+numberOfResponses.intValue());
			numberOfChecks++;
			if(numberOfChecks>maxChecks) break;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//System.out.println("waiting...");
			}
			//System.out.println(".");
			//System.out.flush();
		}
		//System.out.println("Stopping requests");
		for(Future<?> response : responses){
			response.cancel(false);
		}

		//System.out.println("number of successes: "+numberOfSuccesses.intValue());
		if(numberOfSuccesses.intValue()==0){
			UserDoesNotExist E = new UserDoesNotExist();
			throw new UserDoesNotExist_Exception("Failed to fetch list from servers", E);
		}

		Collections.sort(mergeLists(arrays));
		return mergeLists(arrays);
	}



	public void store(DocUserPair docUserPair, byte[] contents,byte[] credential) 
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception, 
			UserDoesNotExist_Exception {
		Date requestTime = processRequest(credential,docUserPair.getUserId());
		try{
			load(docUserPair,credential,false);
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
		requestTime = processRequest(credential,docUserPair.getUserId());
		String newtag=(maxseq+1)+";"+mycid;
		maxcid=-2;
		maxseq=-2;
		int wsum=0;
		for(int i=1;i<=numberClones;i++){
			try{
				SDStore aux = clones [i-1];
				BindingProvider bp = (BindingProvider) aux;		
				Map<String, Object> requestContext = bp.getRequestContext();
				requestContext.put(RelayClientHandler.REQUEST_PROPERTY, newtag);
				clones[i-1].store(docUserPair,contents);
				Map<String, Object> responseContext = bp.getResponseContext();
				byte[] time = (byte[]) responseContext.get(KerberosHandler.TIMESTAMP_PROPERTY);
				if(!processReply(time, credential,requestTime)){
					throw new Exception();	//Server response was compromised.
				}
				String finalValue = (String)responseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
				if (finalValue.equals("0;0")==false)
					throw new Exception (); //ack not received
				wsum+=wt[i-1];
				if (wsum>wq)
					break;  //quorum reached

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


	public byte[] load(DocUserPair docUserPair,byte[] credential,boolean reset) 
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		Date requestTime = processRequest(credential,docUserPair.getUserId());


		byte[] maxresult=null;
		byte [] result = null;
		int rsum=0;
		for(int i=1;i<=numberClones;i++){
			try{
				SDStore aux = clones [i-1];
				BindingProvider bp = (BindingProvider) aux;		
				Map<String, Object> requestContext = bp.getRequestContext();
				String client="0;0";
				requestContext.put(RelayClientHandler.REQUEST_PROPERTY, client);
				result = clones[i-1].load(docUserPair);
				Map<String, Object> responseContext = bp.getResponseContext();
				byte[] time = (byte[]) responseContext.get(KerberosHandler.TIMESTAMP_PROPERTY);
				if(!processReply(time, credential,requestTime)){
					throw new Exception();	//Server response was compromised.
				}
				String finalValue = (String)responseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
				System.out.printf("OUT:%s\n",finalValue);
				if (finalValue==null)
					continue;
				int seq = parseTag(finalValue)[0];
				int cid = parseTag(finalValue)[1];
				System.out.printf("Seq:%d   Cid:%d MAXS %d   MAXC %d Result: %s\n",seq,cid ,maxseq,maxcid,new String(result));
				if(seq>maxseq ||(seq==maxseq && cid>maxcid)){
					maxseq=seq;
					maxcid=cid;
					maxresult=result;
					System.out.printf("INSIDE IF\n");
				}
				rsum+=rt[i-1];
				if (rsum>rq){
					System.out.printf ("REACHED QUORUM!!!!!!!!!!!!!!!!!!!\n");
					break;
					
				}
					  //quorum reached
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
		if(reset){
			maxcid=-2;
			maxseq=-2;
			System.out.printf("reset done!");

		}
		System.out.printf("FINAL%s  \n",new String(maxresult));
		return maxresult;
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

	private int[] parseTag (String s){
		String[] parts = s.split(",");
		String[] tags = parts[0].split(";");
		int [] result = {Integer.parseInt(tags[0]),Integer.parseInt(tags[1])};
		return result;
	}

}
