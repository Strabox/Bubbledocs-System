package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.*;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import pt.ulisboa.tecnico.sdis.store.ws.*;

@WebService(
	endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore", 
	wsdlLocation="SD-STORE.1_1.wsdl",
	name="SDStore",
	portName="SDStoreImplPort",
	targetNamespace="urn:pt:ulisboa:tecnico:sdis:store:ws",
	serviceName="SDStore"
)
@HandlerChain(file = "/handler-chain.xml")
public class SDStoreImpl implements SDStore {

	private List<Storage> storage;
	
	@Resource
	WebServiceContext wsc;
	
	public SDStoreImpl(){
		super();
		this.storage = new ArrayList<Storage>();
	}
	
	private void passUser(String userId){
		MessageContext msg = wsc.getMessageContext();
		msg.put("userId", userId);
	}
	
	private void checkUserExistence(String UserId) throws UserDoesNotExist_Exception{
		for (Storage storage2 : storage) {
			if (storage2.getUserId().equals(UserId)) {
				return;
			}
		}
		UserDoesNotExist E = new UserDoesNotExist();
		throw new UserDoesNotExist_Exception(UserId+" does not exist", E);
	}
	
	/* ========================= WEB SERVICES METHODS =============================== */
	
	/**
	 * 
	 * @param docUserPair
	 * @throws DocAlreadyExists_Exception 
	 */
	public void createDoc(DocUserPair docUserPair) throws DocAlreadyExists_Exception {
		passUser(docUserPair.getUserId());
		if(docUserPair.getUserId() != null && docUserPair.getUserId() != "" 
			&& docUserPair.getDocumentId() != null && docUserPair.getDocumentId() != ""){
			for (Storage storage2 : storage) {
				
				if (storage2.getUserId().equals(docUserPair.getUserId())) {
					
					storage2.addDoc(docUserPair.getDocumentId());				
					return;
				}
			}
			Storage newstor = new Storage(docUserPair.getUserId(), docUserPair.getDocumentId());
			storage.add(newstor);
	
		}
	}

	/**
	 * 
	 * @param userId
	 * @return
	 *     returns java.util.List<java.lang.String>
	 * @throws UserDoesNotExist_Exception
	 */

	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception {
		passUser(userId);
		if (userId==null || userId.equals("")==true){
			UserDoesNotExist E = new UserDoesNotExist();
			throw new UserDoesNotExist_Exception ("User does not exist", E); 
			
		}
		checkUserExistence(userId);
		List<String> doclist = new ArrayList<String>();
		
		for (Storage store : storage) {
			if (userId.equals(store.getUserId())) {
				doclist = store.getDocs();
			}
		}
		passUser(userId);
		return doclist;
	}

	/**
	 * 
	 * @param contents
	 * @param docUserPair
	 * @throws UserDoesNotExist_Exception
	 * @throws DocDoesNotExist_Exception
	 * @throws CapacityExceeded_Exception
	 */

	public void store(DocUserPair docUserPair, byte[] contents) throws UserDoesNotExist_Exception, 
	DocDoesNotExist_Exception, CapacityExceeded_Exception {
		passUser(docUserPair.getUserId());
		if (docUserPair.getUserId()==null || docUserPair.getUserId()==""){
			UserDoesNotExist E = new UserDoesNotExist();
			throw new UserDoesNotExist_Exception("User does not exist", E); 
			
		}
		if (docUserPair.getDocumentId()==null || docUserPair.getDocumentId()==""){
			DocDoesNotExist E = new DocDoesNotExist();
			throw new DocDoesNotExist_Exception("Doc does not exist", E); 
			
		}
		if (contents!=null){
			checkUserExistence(docUserPair.getUserId());
			for (Storage s : storage) {
				if(s.getUserId().equals(docUserPair.getUserId()))
					s.setContent(docUserPair.getDocumentId(), contents);
			}
		}
	}


	/**
	 * 
	 * @param docUserPair
	 * @return
	 *     returns byte[]
	 * @throws UserDoesNotExist_Exception
	 * @throws DocDoesNotExist_Exception
	 */

	public byte[] load(DocUserPair docUserPair) throws UserDoesNotExist_Exception, DocDoesNotExist_Exception{
		passUser(docUserPair.getUserId());
		if (docUserPair.getUserId()==null || docUserPair.getUserId()==""){
			UserDoesNotExist E = new UserDoesNotExist();
			throw new UserDoesNotExist_Exception("User does not exist", E); 
			
		}
		if (docUserPair.getDocumentId()==null || docUserPair.getDocumentId()==""){
			DocDoesNotExist E = new DocDoesNotExist();
			throw new DocDoesNotExist_Exception("Doc does not exist", E); 
			
		}
		checkUserExistence(docUserPair.getUserId());
		for (Storage s : storage) {
			if(s.getUserId().equals(docUserPair.getUserId()))
				return s.getContent(docUserPair.getDocumentId());
		}
		return null;
	}

}



