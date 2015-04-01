package pt.ulisboa.tecnico.sdis.store.ws.impl;



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
	/**
	 * 
	 * @param docUserPair
	 * @throws DocAlreadyExists_Exception
	 */

	public void createDoc(DocUserPair docUserPair){
		//FIX ME
	}

	/**
	 * 
	 * @param userId
	 * @return
	 *     returns java.util.List<java.lang.String>
	 * @throws UserDoesNotExist_Exception
	 */

	public List<String> listDocs(String userId){
		// FIX ME
		return null;
	}

	/**
	 * 
	 * @param contents
	 * @param docUserPair
	 * @throws UserDoesNotExist_Exception
	 * @throws DocDoesNotExist_Exception
	 * @throws CapacityExceeded_Exception
	 */

	public void store(DocUserPair docUserPair, byte[] contents){
		//FIX ME
		
		
	}


	/**
	 * 
	 * @param docUserPair
	 * @return
	 *     returns byte[]
	 * @throws UserDoesNotExist_Exception
	 * @throws DocDoesNotExist_Exception
	 */

	public byte[] load(DocUserPair docUserPair){
		//FIX ME
		return null;
	}


}



