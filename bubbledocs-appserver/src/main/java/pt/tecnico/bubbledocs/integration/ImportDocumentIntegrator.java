package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.tecnico.bubbledocs.service.local.GetUserInfoService;
import pt.tecnico.bubbledocs.service.local.ImportDocument;



public class ImportDocumentIntegrator extends BubbleDocsIntegrator {

	
	private ImportDocument impDoc;

	private StoreRemoteServices storeRemote;

	


	public ImportDocumentIntegrator(String userToken, int docId) {
		impDoc = new ImportDocument(userToken,docId);
		storeRemote = new StoreRemoteServices();
				
	}
	
	public void createSpreadSheet(){
		impDoc.createSpreadSheet();		
	}
	public String getUsername(){
		return impDoc.getUsername();
	}
	
	public org.jdom2.Document getDocXML() {
		return impDoc.getDocXML();
	}
	
	public String getSheetname(){
		return impDoc.getSheetname();
	}
	
	public byte[] getDocXMLBytes(){
		return impDoc.getDocXMLBytes();
	}
	

	private byte[] importDocumentRemote(String username,String sheetname)
			throws BubbleDocsException {
		try{
			return storeRemote.loadDocument(username,sheetname);
		}catch(RemoteInvocationException rie){
			//Problems contacting remote service.
			throw new UnavailableServiceException();
		}
	}
	
	
	
	public void deserialize(byte [] bytes){
		impDoc.deserialize(bytes);
	}

	public void execute() throws BubbleDocsException{
		impDoc.accessControl();
		String username =impDoc.getUsername();
		String sheetname = impDoc.getSheetname();
		byte[] docXMLbytes = importDocumentRemote(username,sheetname);
		impDoc.setDocXMLBytes(docXMLbytes);
		impDoc.execute();
				
		
	}

}
