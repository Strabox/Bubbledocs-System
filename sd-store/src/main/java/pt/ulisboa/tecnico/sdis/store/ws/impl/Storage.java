package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;


public class Storage{
	private String userId;
	private List<Document> docs;
	private static final int DEFAULT_SIZE = 100;	
	private int temp_cid;
	private int temp_seq;
	
	
	
	public int getTemp_cid() {
		return temp_cid;
	}
	public void setTemp_cid(int temp_cid) {
		this.temp_cid = temp_cid;
	}
	public int getTemp_seq() {
		return temp_seq;
	}
	public void setTemp_seq(int temp_seq) {
		this.temp_seq = temp_seq;
	}
	

	

	public Storage(String userId) {
		super();
		this.userId = userId;
		docs = new ArrayList<Document>(DEFAULT_SIZE);
	}
	public Storage(String userId, String docId) throws DocAlreadyExists_Exception {
		super();
		this.userId = userId;
		docs = new ArrayList<Document>(DEFAULT_SIZE);
		Document newdoc = new Document(docId);
		docs.add(newdoc);
	}
	public void setContent(String docId, byte[] content) throws CapacityExceeded_Exception, DocDoesNotExist_Exception{
		for (Document document : docs) {
			if (docId.equals(document.getDocId())) {				
				document.setContent(content);
				document.setCid(temp_cid);
				document.setSeq(temp_seq);
				return;
			}
		}
		DocDoesNotExist E = new DocDoesNotExist();
		throw new DocDoesNotExist_Exception("Document: "+docId+" not found", E);
	}




	public byte[] getContent(String docId) throws DocDoesNotExist_Exception{
		for (Document document : docs) {
			if (docId.equals(document.getDocId())) {
				temp_cid =document.getCid();
				temp_seq =document.getSeq();
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