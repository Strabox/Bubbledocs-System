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

	private List<User> users = null;
	

	public void createDoc(DocUserPair docUserPair) {
		String userId = docUserPair.getUserId();
		String docId = docUserPair.getDocumentId();
		
		SpreadSheet sp = new SpreadSheet(docId);
		User user = new User(userId);
		user.addDocs(sp);
		users.add(user);
	}

	/**
	 * 
	 * @param userId
	 * @return
	 *     returns java.util.List<java.lang.String>
	 * @throws UserDoesNotExist_Exception
	 */

	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception {
		User aux = null;
		for (User user : users) {
			if(user.getUserId()==userId){
				aux = user;
				break;
			}
		}
		if (aux==null) {
			UserDoesNotExist E = new UserDoesNotExist();
			E.setUserId(userId);
			throw new UserDoesNotExist_Exception("userId wasn't matched", E);
		}
		
		List<String> docnames = new ArrayList<String>(aux.getDocs().size());
		for (SpreadSheet sp : aux.getDocs()) {
			docnames.add(sp.getDocid());
		}
		
		return docnames;
	}

	/**
	 * 
	 * @param contents
	 * @param docUserPair
	 * @throws UserDoesNotExist_Exception
	 * @throws DocDoesNotExist_Exception
	 * @throws CapacityExceeded_Exception
	 */

	public void store(DocUserPair docUserPair, byte[] contents) 
			throws UserDoesNotExist_Exception, DocDoesNotExist_Exception {
		String userId = docUserPair.getUserId();
		String docId = docUserPair.getDocumentId();
		
		User aux = null;
		for (User user : users) {
			if(user.getUserId()==userId){
				aux = user;
				break;
			}
		}
		if (aux==null) {
			UserDoesNotExist E = new UserDoesNotExist();
			E.setUserId(userId);
			throw new UserDoesNotExist_Exception("userId wasn't matched", E);
		}
		
		boolean exists = false;
		for (SpreadSheet sp : aux.getDocs()) {
			if (sp.getDocid()==docId) {
				exists = true;
				sp.setContent(contents);
				break;
			}
		}
		if (!exists) {
			DocDoesNotExist E = new DocDoesNotExist();
			throw new DocDoesNotExist_Exception("docId wasn't matched", E);
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

	public byte[] load(DocUserPair docUserPair) throws UserDoesNotExist_Exception{
		String userId = docUserPair.getUserId();
		String docId = docUserPair.getDocumentId();
		
		User aux = null;
		for (User user : users) {
			if(user.getUserId()==userId){
				aux = user;
				break;
			}
		}
		if (aux==null) {
			UserDoesNotExist E = new UserDoesNotExist();
			E.setUserId(userId);
			throw new UserDoesNotExist_Exception("userId wasn't matched", E);
		}
		
		for (SpreadSheet sp : aux.getDocs()) {
			if (sp.getDocid()==docId) {
				return sp.getContent();
			}
		}
		return null;
	}

}



