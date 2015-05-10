package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;

import pt.ulisboa.tecnico.sdis.store.cli.frontend.FrontEnd;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.CreateDocResponse;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.ListDocsResponse;
import pt.ulisboa.tecnico.sdis.store.ws.LoadResponse;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.StoreResponse;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import util.kerberos.exception.KerberosException;
import util.uddi.UDDIClient;


public class SDStoreClient extends UDDIClient implements SDStore{
	
	/**
	 * Proxy for webService.
	 */
	SDStore storeRemote;
	FrontEnd frontend;
	public SDStoreClient(String uddiURL, String idName) throws Exception  {
		super(uddiURL, idName);
		//connectUDDI();
		int rt = 2, wt = 2;
		frontend = new FrontEnd(uddiURL,idName,3,rt,wt);
	}

	@Override
	protected void getSpecificProxy(String endpoint) throws Exception {
		SDStore_Service service = new SDStore_Service();
		storeRemote = service.getSDStoreImplPort();
		
		BindingProvider bindingProvider = (BindingProvider) storeRemote;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpoint);
	}
	
	/*=================== SD STORE WEB METHODS ================== */
	
	@Override
	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {
		frontend.createDoc(docUserPair);
	}

	@Override
	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		return frontend.listDocs(userId);
	}

	@Override
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		frontend.store(docUserPair, contents);
		
	}

	@Override
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		return frontend.load(docUserPair);
	}
	
    
	/**
	 * Pass client credentials to the request, and convert it
	 * to request structures.
	 * @param credentials
	 * @throws KerberosException 
	 */
	public void processRequest(byte[] credentials) throws KerberosException{
		frontend.processRequest(credentials);
	}
	
	/*===================== ASYNC METHODS NEED TO COMPILE =================== */
	@Override
	public Response<CreateDocResponse> createDocAsync(DocUserPair docUserPair) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> createDocAsync(DocUserPair docUserPair,
			AsyncHandler<CreateDocResponse> asyncHandler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<ListDocsResponse> listDocsAsync(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> listDocsAsync(String userId,
			AsyncHandler<ListDocsResponse> asyncHandler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<StoreResponse> storeAsync(DocUserPair docUserPair,
			byte[] contents) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> storeAsync(DocUserPair docUserPair, byte[] contents,
			AsyncHandler<StoreResponse> asyncHandler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<LoadResponse> loadAsync(DocUserPair docUserPair) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> loadAsync(DocUserPair docUserPair,
			AsyncHandler<LoadResponse> asyncHandler) {
		// TODO Auto-generated method stub
		return null;
	}
}
