package pt.ulisboa.tecnico.sdis.store.ws.impl;

public class SpreadSheet{
	
	private String docid;
	private byte[] content;
	
	
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public SpreadSheet(String docid) {
		this.docid = docid;
	}
	
}