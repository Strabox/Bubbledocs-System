package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;


public class Storage{
	private String userId;
	private final int capacity = 10240;
	private List<Document> docs;
	private static final int DEFAULT_SIZE = 5;
	private int currentsize = 0;

	private class Document{
		private String docId;
		private byte[] content;


		public Document(String docId) {
			super();
			this.docId = docId;
		}
		public String getDocId() {
			return docId;
		}
		public byte[] getContent() {
			return content;
		}
		public void setContent(byte[] content) {
			this.content = content;
		}
	}

	public Storage(String userId) {
		super();
		this.userId = userId;
	}
	public Storage(String userId, String docId) throws DocAlreadyExists_Exception {
		super();
		this.userId = userId;
		docs = new ArrayList<Storage.Document>(DEFAULT_SIZE);
		Document newdoc = new Document(docId);
		docs.add(newdoc);
	}
	public void setContent(String docId, byte[] content) throws CapacityExceeded_Exception, DocDoesNotExist_Exception{
		for (Document document : docs) {
			if (docId.equals(document.getDocId())) {				
				document.setContent(content);
				return;
			}
		}
		DocDoesNotExist E = new DocDoesNotExist();
		throw new DocDoesNotExist_Exception("Document: "+docId+" not found", E);
	}




	public byte[] getContent(String docId) throws DocDoesNotExist_Exception{
		for (Document document : docs) {
			if (docId.equals(document.getDocId())) {
				return document.getContent();
			}
		}
		DocDoesNotExist E = new DocDoesNotExist();
		throw new DocDoesNotExist_Exception("Document: "+docId+" not found", E);
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<String> getDocs() {
		List<String> namelist = new ArrayList<String>();
		for (Document doc : docs) {
			namelist.add(doc.getDocId());
		}
		return namelist;
	}
	public void setDocs(List<Document> docs) {
		this.docs = docs;
	}
	public void addDoc(String doc) throws DocAlreadyExists_Exception{

		for (Document document : docs) {
			if (doc.equals(document.getDocId())) {

				DocAlreadyExists E = new DocAlreadyExists();				
				throw new DocAlreadyExists_Exception(doc+" already exists", E);

			}
		}
		Document newdoc = new Document(doc);
		docs.add(newdoc);
	}
}