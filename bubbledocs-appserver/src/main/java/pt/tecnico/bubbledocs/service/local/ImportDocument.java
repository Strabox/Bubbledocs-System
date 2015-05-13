package pt.tecnico.bubbledocs.service.local;



import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
public class ImportDocument extends BubbleDocsService {
	private byte[] docXMLbytes;
	private String _token;
	private int _docId;
	private org.jdom2.Document docXML;
	private SpreadSheet sheet;
	private User user;
	private SpreadSheet importedSheet;
	private String username;
	private String sheetname;

	public ImportDocument(String userToken, int docId) {
		_token=userToken;
		_docId=docId;
	}

	public byte[] getDocXMLBytes() {
		return docXMLbytes;
	}
	public void setDocXMLBytes(byte[] doc) {
		docXMLbytes=doc;
	}
	public org.jdom2.Document getDocXML() {
		return docXML;
	}

	public String getUsername(){
		return this.username;

	}

	public String getSheetname(){
		return this.sheetname;

	}
	
	public SpreadSheet getImportedSpreadSheet(){
		return importedSheet;

	}
	



	public void serialize(Object obj){
		try{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(b);
			o.writeObject(obj);
			docXMLbytes = b.toByteArray();       
		}
		catch (IOException e){} 

	}
	public void deserialize(byte [] bytes){
		try{
			ByteArrayInputStream b = new ByteArrayInputStream(bytes);
			ObjectInputStream o = new ObjectInputStream(b);
			docXML = (org.jdom2.Document) o.readObject();
		}
		catch (IOException | ClassNotFoundException e){}
	}

	

	@Override
	public void accessControl(){
		Bubbledocs bubbled = Bubbledocs.getInstance();
		if(_token == null || bubbled.getUserFromSession(_token) == null)
			throw new UserNotInSessionException();
		sheet = bubbled.getSpreadSheet(_docId);
		user = bubbled.getUserFromSession(_token);
		this.username = user.getUsername();
		this.sheetname = sheet.getName();
		//IMPORTANT!! Resets the user session time.
		bubbled.resetsSessionTime(_token);
	}

	public void createSpreadSheet() throws BubbleDocsException{
		importedSheet= new SpreadSheet();
		deserialize(docXMLbytes);
		importedSheet.importFromXML(docXML, this.username);
			

	}



	@Override
	protected void dispatch() throws BubbleDocsException {
		createSpreadSheet();		
	}




}
