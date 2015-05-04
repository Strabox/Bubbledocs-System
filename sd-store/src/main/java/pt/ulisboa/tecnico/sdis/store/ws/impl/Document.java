package pt.ulisboa.tecnico.sdis.store.ws.impl;


public class Document{
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

