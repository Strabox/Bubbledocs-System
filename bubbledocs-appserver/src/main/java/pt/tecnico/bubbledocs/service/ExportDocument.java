package pt.tecnico.bubbledocs.service;
import org.jdom2.*;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.ArrayList;
import java.util.Arrays;

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
// add needed import declarations

public class ExportDocument extends BubbleDocsService {
	private byte[] docXMLbytes;
	private String token;
	private int docId;
	private org.jdom2.Document docXML;

	public byte[] getDocXMLBytes() {
		return docXMLbytes;
	}
	public org.jdom2.Document getDocXML() {
		return docXML;
	}

	@Override
	protected void accessControl(){
		Bubbledocs bubbled = Bubbledocs.getInstance();
		if(token == null || bubbled.getUserFromSession(token) == null)
			throw new UserNotInSessionException();

	}

	public ExportDocument(String _userToken, int _docId) {
		token=_userToken;
		docId=_docId;
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
	protected void dispatch() throws BubbleDocsException {
		Bubbledocs bubbled = Bubbledocs.getInstance();
		SpreadSheet sheet = bubbled.getSpreadSheet(docId);
		User user = bubbled.getUserFromSession(token);
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
}
