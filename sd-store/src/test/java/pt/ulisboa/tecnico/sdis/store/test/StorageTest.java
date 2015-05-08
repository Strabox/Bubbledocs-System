package pt.ulisboa.tecnico.sdis.store.test;

import pt.ulisboa.tecnico.sdis.store.ws.impl.*;
import pt.ulisboa.tecnico.sdis.store.ws.*;
import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;


public class StorageTest {


	
	@Test
	public void success() throws Exception {
		String document1 ="document51";
		String document2 ="document52";
		String user = "User5";
		String content1 ="SomeTextAndSomeNumbers123456789";
		String content2 ="Another Text And Some Numbers987654321";
		Storage s = new Storage(user,document1);
		s.setContent(document1,content1.getBytes());
		s.addDoc(document2);
		s.setContent(document2,content2.getBytes());
		List <String> docList = s.getDocs();
		String newContent1 = new String (s.getContent(document1));
		String newContent2 = new String (s.getContent(document2));
		
		assertEquals("Doc1 content",content1,newContent1);
		assertEquals("Doc2 content",content2,newContent2);
		assertEquals("Doc1 name",document1,docList.get(0));
		assertEquals("Doc2 name",document2,docList.get(1));
		
	}
	
	@Test (expected=DocAlreadyExists_Exception.class)
	public void docAlreadyExists() throws Exception {
		String document1 ="document61";
		String document2 ="document61";
		String user = "User6";
		Storage s = new Storage(user,document1);
		s.addDoc(document2);
		assertEquals("...",true,true);
		
		
	}
	
	
	
	
	@Test (expected=DocDoesNotExist_Exception.class)
	public void docDoesNotExist() throws Exception {
		String document1 ="document81";		
		String user = "User8";
		String content1 ="Some picture";
		Storage s = new Storage(user,document1);
		s.setContent("ghostDocument", content1.getBytes());
		assertEquals("...",true,true);
		
		
	}
	
	
	}