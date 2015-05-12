package pt.tecnico.bubbledocs.service.local;

import java.util.ArrayList;

import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ExportDocument extends BubbleDocsService {
	private byte[] docXMLbytes;
	private String _token;
	private int _docId;
	private org.jdom2.Document docXML;
	private SpreadSheet sheet;
	private User user;

	public ExportDocument(String userToken, int docId) {
		_token=userToken;
		_docId=docId;
	}

	public final byte[] getDocXMLBytes() {
		return docXMLbytes;
	}
	public org.jdom2.Document getDocXML() {
		return docXML;
	}

	public String getUsername(){
		return user.getUsername();

	}

	public String getSheetname(){
		return sheet.getName();

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
		//IMPORTANT!! Resets the user session time.
		bubbled.resetsSessionTime(_token);
	}

	public void createXML() throws BubbleDocsException{
		Bubbledocs bubbled = Bubbledocs.getInstance();
		sheet = bubbled.getSpreadSheet(_docId);
		user = bubbled.getUserFromSession(_token);
		boolean hasReadPermissions = false;
		ArrayList<SpreadSheet> readable = user.listReadableSpreadSheets();
		for(SpreadSheet ss : readable){
			if (ss==sheet) hasReadPermissions = true;
		}
		if(!(sheet.getOwner()==user || hasReadPermissions)){
			throw new UnauthorizedOperationException();
		}
		serialize(sheet.exportToXML());
		docXML = sheet.exportToXML();		

	}



	@Override
	protected void dispatch() throws BubbleDocsException {
		createXML();		
	}




}
