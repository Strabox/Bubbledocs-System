package util.kerberos.messages;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import util.kerberos.exception.KerberosException;

public class KerberosRequest extends KerberosMessage{

	private final static String XSD_FILE_WINDOWS_PATH = "\\..\\sd-util\\src\\main\\resources\\requestFormat.xsd";
	private final static String XSD_FILE_LINUX_PATH = "/../sd-util/src/main/resources/requestFormat.xsd";
	
	private String server;
	
	private String nounce;
	
	
	public KerberosRequest(String server,String nounce){
		this.server = server;
		this.nounce = nounce;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @return the nounce
	 */
	public String getNounce() {
		return nounce;
	}

	public byte[] serialize() throws UnsupportedEncodingException{
		String request, body;
		body = "<server>" + server + "</server>";
		body += "<nounce>" + nounce + "</nounce>";
		request = "<request>" + body +"</request>";
		return request.getBytes("UTF-8");
	}
	
	public static KerberosRequest deserialize(byte[] request) throws 
	ParserConfigurationException, SAXException, IOException, KerberosException{
		
		String s = "", n = "",dirFile = "";;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
		Document document;
		document = builder.parse(new ByteArrayInputStream(request));
		
        if(SystemUtils.IS_OS_WINDOWS)
        	dirFile = System.getProperty("user.dir") + XSD_FILE_WINDOWS_PATH;
        else if(SystemUtils.IS_OS_LINUX)
        	dirFile = System.getProperty("user.dir") + XSD_FILE_LINUX_PATH;
		validateXMLDocument(document,dirFile);
		
		for (Node node = document.getDocumentElement().getFirstChild();
	        node != null;
	        node = node.getNextSibling()) {
		
			if(node.getNodeName().equals("nounce")){
				n = node.getTextContent();
			}
			else if(node.getNodeName().equals("server")){
				s = node.getTextContent();
			}
		}
		
		return new KerberosRequest(s, n);
	}

}
