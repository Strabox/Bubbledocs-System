package util.kerberos.messages;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import util.kerberos.exception.KerberosException;

/**
 * Represents the reply from Kerberos Authentication server
 * (Kerberos Saut) to the client.
 * @Author André
 * <pre>
 * {@code
 * <reply>
 *   <ticket>xs:base64Binary</ticket>
 *   <authentication>xs:base64Binary</authentication>
 * </reply>
 * }
 * </pre>
 */
public class KerberosReply extends KerberosNormalMessage{

	private final static String XSD_FILE_WINDOWS_PATH = "\\..\\sd-util\\src\\main\\resources\\replyFormat.xsd";
	private final static String XSD_FILE_LINUX_PATH = "/../sd-util/src/main/resources/replyFormat.xsd";
	
	/**
	 * Serialized ticker.
	 */
	private byte[] ticket;
	
	/**
	 * Serialized authentication message.
	 */
	private byte[] authentication;
	
	
	public KerberosReply(byte[] ticket,byte[] authentication){
		this.ticket = ticket;
		this.authentication = authentication;
	}
	
	/**
	 * @return the ticket
	 */
	public byte[] getTicket() {
		return ticket;
	}

	/**
	 * @return the authentication
	 */
	public byte[] getAuthentication() {
		return authentication;
	}

	@Override
	public byte[] serialize() throws KerberosException{
		String reply,body = "";
		String tick = DatatypeConverter.printBase64Binary(ticket);
		String auth = DatatypeConverter.printBase64Binary(authentication);
		
		body += "<ticket>"+ tick + "</ticket>";
		body += "<authentication>" + auth +"</authentication>";
		reply = "<reply>" + body + "</reply>";
		try{
			return reply.getBytes(UTF8);
		}catch(UnsupportedEncodingException e){
			throw new KerberosException();
		}
	}
	
	/**
	 * Given a kerberosReply serialized it returns the
	 * correct object.
	 * @param reply
	 * @return KerberosReply
	 * @throws KerberosException
	 */
	public static KerberosReply deserialize(byte[] reply) 
	throws KerberosException {
		
		String t = "",a = "",dirFile = "";
		Document document = getXMLDocumentFromBytes(reply);
		
        if(SystemUtils.IS_OS_WINDOWS)
        	dirFile = System.getProperty("user.dir") + XSD_FILE_WINDOWS_PATH;
        else if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC 
        		|| SystemUtils.IS_OS_MAC_OSX)
        	dirFile = System.getProperty("user.dir") + XSD_FILE_LINUX_PATH;
		validateXMLDocument(document,dirFile);
		
		for (Node node = document.getDocumentElement().getFirstChild();
	        node != null;
	        node = node.getNextSibling()) {
		
			if(node.getNodeName().equals("ticket")){
				t = node.getTextContent();
			}
			else if(node.getNodeName().equals("authentication")){
				a = node.getTextContent();
			}
		}
		byte[] tFinal = DatatypeConverter.parseBase64Binary(t);
		byte[] aFinal = DatatypeConverter.parseBase64Binary(a);
		return new KerberosReply(tFinal, aFinal);
	}
	
	
}
