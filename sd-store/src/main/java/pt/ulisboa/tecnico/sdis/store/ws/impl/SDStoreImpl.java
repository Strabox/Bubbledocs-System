package pt.ulisboa.tecnico.sdis.store.ws.impl;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jws.*;
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

	private List<CreateDoc> docs;
	private List<ListDocs> listdocs;
	private List<Load> load;
	private List<Store> store;
	private final int CAPACITY = 10000;


	public void init(){
		docs = new ArrayList<CreateDoc>();
		listdocs = new ArrayList<ListDocs>();
		load = new ArrayList<Load>();
		store = new ArrayList<Store>();
	}
	
	private void checkUserExistance(String userId) throws UserDoesNotExist_Exception{
		ListDocs aux = new ListDocs();
		aux.setUserId(userId);
		
		if (!listdocs.contains(aux)) {
			UserDoesNotExist E = new UserDoesNotExist();
			E.setUserId(userId);
			throw new UserDoesNotExist_Exception("Id not found: "+ userId, E);
		}
	}
	
	
	/**
     * 
     * @param docUserPair
     * @throws DocAlreadyExists_Exception
     * @throws UserDoesNotExist_Exception 
     */

	public void createDoc(DocUserPair docUserPair) throws DocAlreadyExists_Exception {
		CreateDoc newdoc = new CreateDoc();
		newdoc.setDocUserPair(docUserPair);
		
		if (docs.contains(docUserPair)) {
			DocAlreadyExists E = new DocAlreadyExists();
			E.setDocId(docUserPair.getDocumentId());
			throw new DocAlreadyExists_Exception("Doc found: "+ E.getDocId(), E);
		}
		
		docs.add(newdoc);

		ListDocs doc = new ListDocs();
		doc.setUserId(docUserPair.getUserId());

		listdocs.add(doc);
	}

	/**
	 * 
	 * @param userId
	 * @return
	 *     returns java.util.List<java.lang.String>
	 * @throws UserDoesNotExist_Exception
	 */

	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception {
		List<String> doclist = new ArrayList<String>();
		
		checkUserExistance(userId);
		for (CreateDoc doc : docs) {
			if(doc.getDocUserPair().getUserId() == userId){
				doclist.add(doc.getDocUserPair().getDocumentId());
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

		checkUserExistance(docUserPair.getUserId());
		if (!docs.contains(docUserPair)) {
			DocDoesNotExist E = new DocDoesNotExist();
			E.setDocId(docUserPair.getDocumentId());
			throw new DocDoesNotExist_Exception("Id not found: "+ E.getDocId(), E);
		}

		int capacity = 0;
		for (Store s : store) {
			if(s.getDocUserPair() == docUserPair){
				capacity += s.getContents().length;
			}
		}
		if (capacity + contents.length > CAPACITY) {
			CapacityExceeded E = new CapacityExceeded();
			E.setAllowedCapacity(CAPACITY);
			E.setCurrentSize(capacity+contents.length);
			throw new CapacityExceeded_Exception("Capacity Exceeded by: " + (CAPACITY-capacity+contents.length), E);
		}
		
		Store s = new Store();
		s.setDocUserPair(docUserPair);
		s.setContents(contents);
		store.add(s);

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

		checkUserExistance(docUserPair.getUserId());
		for (Store s : store) {
			if(s.getDocUserPair() == docUserPair){
				s.getContents();
			}
		}

		DocDoesNotExist E = new DocDoesNotExist();
		E.setDocId(docUserPair.getDocumentId());
		throw new DocDoesNotExist_Exception("Id not found: "+ E.getDocId(), E);
	}

}



