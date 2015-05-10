package pt.ulisboa.tecnico.sdis.store.ws.impl;


public class Document{
	private String docId;
	private byte[] content;
	private int cid=1;
	private int seq=1;
	
	
	

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
	
	public void setCid(int _cid){
		cid=_cid;				
	}
	
	public int getCid(){
		return cid;
	}
	
	public void setSeq(int _seq){
		seq=_seq;				
	}
	
	public int getSeq(){
		return seq;
	}
	
	
	
}

