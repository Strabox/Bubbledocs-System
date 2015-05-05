package pt.ulisboa.tecnico.sdis.store.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import util.uddi.UDDIClient;


public class SDStoreClient extends UDDIClient implements SDStore{
	
	/**
	 * Proxy for webService.
	 */
	SDStore storeRemote;
	
	public SDStoreClient(String uddiURL, String idName) throws Exception {
		super(uddiURL, idName);
		connectUDDI();
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
		storeRemote.createDoc(docUserPair);
	}

	@Override
	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		return storeRemote.listDocs(userId);
	}

	@Override
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		storeRemote.store(docUserPair, contents);
		
	}

	@Override
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		return storeRemote.load(docUserPair);
	}
    
	/**
	 * Pass kerberos Strucures to the handler.
	 * @param ticket
	 * @param auth
	 * @param nonce
	 */
	public void processRequest(byte[] ticket,byte[] auth,byte[] nonce){
		BindingProvider bp = (BindingProvider) storeRemote;
		Map<String, Object> requestContext = bp.getRequestContext();
        requestContext.put("ticket", Base64.getEncoder().encodeToString(ticket));
        requestContext.put("auth", Base64.getEncoder().encodeToString(auth));
        requestContext.put("nonce", Base64.getEncoder().encodeToString(nonce));
	}

}
