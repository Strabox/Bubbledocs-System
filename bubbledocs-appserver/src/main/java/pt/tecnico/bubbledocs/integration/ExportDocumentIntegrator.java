package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;

import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.tecnico.bubbledocs.service.local.ExportDocument;



public class ExportDocumentIntegrator extends BubbleDocsIntegrator {

	/* Delegate do CreateUser service. */
	private ExportDocument expDoc;

	/* Used to interact with remote id service. */
	private StoreRemoteServices storeRemote;

	


	public ExportDocumentIntegrator(String userToken, int docId) {
		expDoc = new ExportDocument(userToken,docId);
		storeRemote = new StoreRemoteServices();
		
	}
	
	public void createXML(){
		expDoc.createXML();		
	}
	public String getUsername(){
		return expDoc.getUsername();
	}
	
	public org.jdom2.Document getDocXML() {
		return expDoc.getDocXML();
	}
	
	public String getSheetname(){
		return expDoc.getSheetname();
	}
	
	public byte[] getDocXMLBytes(){
		return expDoc.getDocXMLBytes();
	}
	

	private void exportDocumentRemote(String username,String sheetname, byte[]  docXMLbytes)
			throws BubbleDocsException {
		try{
			storeRemote.storeDocument(username,sheetname,docXMLbytes);
		}catch(RemoteInvocationException rie){
			//Problems contacting remote service.
			throw new UnavailableServiceException();
		}
	}
	
	
	
	public void deserialize(byte [] bytes){
		expDoc.deserialize(bytes);
	}

	public void execute() throws BubbleDocsException{
		expDoc.execute();
		String username = expDoc.getUsername();
		String sheetname = expDoc.getSheetname();
		byte[] docXMLbytes = expDoc.getDocXMLBytes();
		try{
			exportDocumentRemote(username,sheetname,docXMLbytes);
		}catch(Exception e){
			/* Compensation if the remote call fails. */

			throw e;
		}
	}

}
