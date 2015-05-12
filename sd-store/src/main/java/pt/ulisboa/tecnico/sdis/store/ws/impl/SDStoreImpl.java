package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.*;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;



import pt.ulisboa.tecnico.sdis.store.ws.impl.handlers.RelayServerHandler;
import pt.ulisboa.tecnico.sdis.store.ws.*;
import pt.ulisboa.tecnico.sdis.store.ws.impl.exceptions.InvalidRequest;
import pt.ulisboa.tecnico.sdis.store.ws.impl.handlers.KerberosHandler;
import pt.ulisboa.tecnico.sdis.store.ws.impl.kerberos.KerberosManager;

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

	public static final String SERVICE_ID = "SD-STORE";
	
	@Resource
	WebServiceContext webServiceContext;
	/**
	 * Used to manage business logic.
	 */
	private List<Storage> storage;

	/**
	 * kerberos manager - Used to manage kerberos protocol in server side.
	 */
	private KerberosManager kerberosManager;
	
	private ServerCrypto crypto;

	@Resource
	WebServiceContext wsc;


	public SDStoreImpl() throws Exception{
		super();
		this.kerberosManager = new KerberosManager(SERVICE_ID);
		this.storage = new ArrayList<Storage>();
		this.crypto = new ServerCrypto();
		testSetup();
	}

	private void testSetup() {
		try{			
			//alice
			Storage newstor = new Storage("alice","a1");
			newstor.setContent("a1","AAAAAAAAAA".getBytes());
			newstor.addDoc("a2");
			newstor.setContent("a2","aaaaaaaaaa".getBytes());
			storage.add(newstor);
			//bruno

			newstor = new Storage("bruno","b1");
			newstor.setContent("b1","BBBBBBBBBBBBBBBBBBBB".getBytes());
			storage.add(newstor);
			//carla
			newstor = new Storage("carla");
			storage.add(newstor);
			//duarte
			newstor = new Storage("duarte");
			storage.add(newstor);
			//eduardo
			newstor = new Storage("eduardo");
			storage.add(newstor);


		}
		catch(Exception e){
		}

	}

	/**
	 * Used to trade arguments between server and handlers.
	 * @throws Exception
	 */
	private void kerberosProcessRequest(){
		try{
			MessageContext msg = wsc.getMessageContext();
			byte[] mac = (byte[]) msg.get(KerberosHandler.MAC_PROPERTY);
			byte[] ms = (byte[]) msg.get(KerberosHandler.MSG_PROPERTY);
			byte[] ticket = (byte[]) msg.get(KerberosHandler.TICKET_PROPERTY);
			byte[] auth = (byte[]) msg.get(KerberosHandler.AUTH_PROPERTY);
			byte[] time = kerberosManager.processRequest(ticket, auth, ms, mac);
			msg.put(KerberosHandler.TIMESTAMP_PROPERTY, time);
		}catch(Exception e){
			throw new InvalidRequest();
		}
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
		kerberosProcessRequest();
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
	 * @returns java.util.List<java.lang.String>
	 * @throws UserDoesNotExist_Exception
	 */

	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception {
		kerberosProcessRequest();
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
		kerberosProcessRequest();
		if (docUserPair.getUserId()==null || docUserPair.getUserId()==""){
			UserDoesNotExist E = new UserDoesNotExist();
			throw new UserDoesNotExist_Exception("User does not exist", E); 

		}
		if (docUserPair.getDocumentId()==null || docUserPair.getDocumentId()==""){
			DocDoesNotExist E = new DocDoesNotExist();
			throw new DocDoesNotExist_Exception("Doc does not exist", E); 

		}
		/*
		if(!crypto.verifyMAC(cipherDigest, contents, key)){
			DocDoesNotExist E = new DocDoesNotExist();
			throw new DocDoesNotExist_Exception("Cryptografy does not match!", E);
		}
		*/
		if (contents!=null){
			checkUserExistence(docUserPair.getUserId());
			for (Storage s : storage) {
				if(s.getUserId().equals(docUserPair.getUserId())){
					MessageContext messageContext = webServiceContext.getMessageContext();
					String propertyValue = (String) messageContext.get(RelayServerHandler.REQUEST_PROPERTY);
			        System.out.printf("HANDLER ANSWER:%s\n", propertyValue);
			        
			        
			        s.setTemp_seq(parseTag(propertyValue)[0]);
					s.setTemp_cid(parseTag(propertyValue)[1]); //GET INFO FROM HANDLERS
					
					//GET INFO FROM HANDLERS
					s.setContent(docUserPair.getDocumentId(), contents);
			        String returnValue ="0;0";
			       messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, returnValue);
				}					
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
		kerberosProcessRequest();
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
			if(s.getUserId().equals(docUserPair.getUserId())){
				
				MessageContext messageContext = webServiceContext.getMessageContext();
				String propertyValue = (String) messageContext.get(RelayServerHandler.REQUEST_PROPERTY);
		        System.out.printf("HANDLER ANSWER:%s\n", propertyValue);
		        String returnValue =s.getTemp_seq()+";"+ s.getTemp_cid();
		        messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, returnValue);

				return s.getContent(docUserPair.getDocumentId());
			}
		}
		return null;
	}
	private int[] parseTag (String s){
		String[] parts = s.split(",");
		String[] tags = parts[0].split(";");
		int [] result = {Integer.parseInt(tags[0]),Integer.parseInt(tags[1])};
		return result;
	}
	
}



