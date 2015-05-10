package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.cli.SDStoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
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
			storeClient.processRequest(IDRemoteServices.credentials);
			storeClient.store(d, document);
		}catch(CapacityExceeded_Exception e){
			throw new CannotStoreDocumentException();
		}catch(DocDoesNotExist_Exception e){
			throw new CannotStoreDocumentException();
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
			storeClient.processRequest(IDRemoteServices.credentials);
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

