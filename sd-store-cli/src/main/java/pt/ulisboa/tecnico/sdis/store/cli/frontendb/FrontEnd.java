package pt.ulisboa.tecnico.sdis.store.cli.frontendb;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;







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
	//private int [] rt = new int[nservers];
	//private int [] wt = new int[nservers];


	public FrontEnd(String _urluddi, String _name, int _numberClones) throws Exception{
		this.uddiURL = _urluddi;
		this.baseEndpointName = _name;
		this.numberClones = _numberClones;
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
			KerberosClientAuthentication auth = new KerberosClientAuthentication(cred.getClient());
			requestContext.put("auth", Base64.getEncoder().encodeToString(auth.serialize(cred.getKcs())));
			requestContext.put("ticket", Base64.getEncoder().encodeToString(cred.getTicket()));	
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



}
