package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.cli.SDStoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreRemoteServices extends RemoteServices{
	
	private static final String STORE_NAME = "SD-STORE";
	
	private SDStoreClient storeClient;
	
	public void storeDocument(String username, String docName, byte[] document)
		throws CannotStoreDocumentException, RemoteInvocationException {
		try {
			storeClient = new SDStoreClient(UDDI_URL, STORE_NAME);
			DocUserPair d = new DocUserPair();
			d.setDocumentId(docName);
			d.setUserId(username);
			storeClient.credentials = IDRemoteServices.credentials.get(username);
			if(storeClient.credentials == null)
				throw new RemoteInvocationException();
			storeClient.store(d, document);
		}catch(CapacityExceeded_Exception e){
			throw new CannotStoreDocumentException();
		}catch(DocDoesNotExist_Exception e){
			try{
				DocUserPair dup = new DocUserPair();
				dup.setDocumentId(docName);
				dup.setUserId(username);
				storeClient.createDoc(dup);
				storeClient.store(dup, document);
			}
			catch(DocAlreadyExists_Exception dae){
				
			}
			catch(UserDoesNotExist_Exception udn){
				
			}
			catch (CapacityExceeded_Exception e1) {
			
			}
			catch (DocDoesNotExist_Exception e1) {
				
			}
			//throw new CannotStoreDocumentException();
		}catch(UserDoesNotExist_Exception e){
			throw new CannotStoreDocumentException();
		}catch (Exception e) {
			throw new RemoteInvocationException();
		}
	}
	
	public byte[] loadDocument(String username, String docName)
		throws CannotLoadDocumentException, RemoteInvocationException {
		try {
			storeClient = new SDStoreClient(UDDI_URL, STORE_NAME);
			DocUserPair d = new DocUserPair();
			d.setDocumentId(docName);
			d.setUserId(username);
			storeClient.credentials = IDRemoteServices.credentials.get(username);
			if(storeClient.credentials == null)
				throw new RemoteInvocationException();
			return storeClient.load(d);
		}catch(DocDoesNotExist_Exception e){
			throw new CannotLoadDocumentException();
		}catch(UserDoesNotExist_Exception e){
			throw new CannotLoadDocumentException();
		}catch (Exception e) {
			throw new RemoteInvocationException();
		}
	}
}

