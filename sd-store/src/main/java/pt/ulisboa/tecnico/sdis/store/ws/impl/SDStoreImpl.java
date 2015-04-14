package pt.ulisboa.tecnico.sdis.store.ws.impl;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jws.*;
import javax.print.Doc;
import javax.swing.text.Document;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import pt.ulisboa.tecnico.sdis.store.ws.*;



@WebService(
		endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore", 
		wsdlLocation="SD-STORE.1_1.wsdl",
		name="SDStore",
		portName="SDStoreImplPort",
		targetNamespace="urn:pt:ulisboa:tecnico:sdis:store:ws",
		serviceName="SDStore"
		)

public class SDStoreImpl implements SDStore {

	private List<Storage> storage;

	public SDStoreImpl() {
		super();
		this.storage = new ArrayList<Storage>();
	}

	private void checkUserExistence(String UserId) throws UserDoesNotExist_Exception{
		for (Storage storage2 : storage) {
			if (storage2.getUserId()==UserId) {
				return;
			}
		}
		UserDoesNotExist E = new UserDoesNotExist();
		throw new UserDoesNotExist_Exception(UserId+" does not exist", E);
	}

	/**
	 * 
	 * @param docUserPair
	 * @throws DocAlreadyExists_Exception 
	 */
	public void createDoc(DocUserPair docUserPair) throws DocAlreadyExists_Exception {
		Storage newstor = new Storage(docUserPair.getUserId(), docUserPair.getDocumentId());
		storage.add(newstor);
	}

	/**
	 * 
	 * @param userId
	 * @return
	 *     returns java.util.List<java.lang.String>
	 * @throws UserDoesNotExist_Exception
	 */

	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception {
		checkUserExistence(userId);
		List<String> doclist = new ArrayList<String>();
		
		for (Storage store : storage) {
			if (userId==store.getUserId()) {
				doclist = store.getDocs();
			}
		}
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

		checkUserExistence(docUserPair.getUserId());
		for (Storage s : storage) {
			if(s.getUserId()==docUserPair.getUserId())
				s.setContent(docUserPair.getDocumentId(), contents);
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

		checkUserExistence(docUserPair.getUserId());
		for (Storage s : storage) {
			if(s.getUserId()==docUserPair.getUserId())
				return s.getContent(docUserPair.getDocumentId());
		}
		return null;
	}

}



