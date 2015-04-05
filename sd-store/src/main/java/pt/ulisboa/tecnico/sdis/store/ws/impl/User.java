package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.util.List;

public class User{
	private List<SpreadSheet> docs;
	private String userId;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<SpreadSheet> getDocs() {
		return docs;
	}

	public void addDocs(SpreadSheet docs) {
		this.docs.add(docs);
	}

	public User(String userId) {
		this.userId = userId;
	}

}