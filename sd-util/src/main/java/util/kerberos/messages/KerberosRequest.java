package util.kerberos.messages;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import util.kerberos.exception.KerberosException;

/**
 * 
 * @author André
 *
 *	<request>
 *	  <server>integer</server>
 *    <nonce>base64</nonce>
 *	</request>
 */
public class KerberosRequest extends KerberosNormalMessage{

	private final static String XSD_FILE_WINDOWS_PATH = "\\..\\sd-util\\src\\main\\resources\\requestFormat.xsd";
	private final static String XSD_FILE_LINUX_PATH = "/../sd-util/src/main/resources/requestFormat.xsd";
	
	private Integer server;
	
	private String nonce;
	
	
	public KerberosRequest(Integer server,String nonce){
		this.server = server;
		this.nonce = nonce;
	}

	/**
	 * @return the server
	 */
	public Integer getServer() {
		return server;
	}

	/**
	 * @return the nounce
	 */
	public String getNonce() {
		return nonce;
	}
	
	@Override
	public byte[] serialize() throws KerberosException{
		try{
			String request, body;
			body = "<server>" + server.toString() + "</server>";
			body += "<nonce>" + nonce + "</nonce>";
			request = "<request>" + body +"</request>";
			return request.getBytes(UTF8);
		}catch(UnsupportedEncodingException a){
			throw new KerberosException();
		}
	}
	
	public static KerberosRequest deserialize(byte[] request)
	throws KerberosException{
		Integer s = 0;
		String n = "",dirFile = "";
		Document document = getXMLDocumentFromBytes(request);
		
        if(SystemUtils.IS_OS_WINDOWS)
        	dirFile = System.getProperty("user.dir") + XSD_FILE_WINDOWS_PATH;
        else if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC 
        		|| SystemUtils.IS_OS_MAC_OSX)
        	dirFile = System.getProperty("user.dir") + XSD_FILE_LINUX_PATH;
		validateXMLDocument(document,dirFile);
		
		for (Node node = document.getDocumentElement().getFirstChild();
	        node != null;
	        node = node.getNextSibling()) {
		
			if(node.getNodeName().equals("nonce")){
				n = node.getTextContent();
			}
			else if(node.getNodeName().equals("server")){
				s = Integer.parseInt(node.getTextContent());
			}
		}
		return new KerberosRequest(s, n);
	}

}
