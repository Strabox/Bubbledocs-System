package util.kerberos.messages;

import java.security.Key;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;

/**
 * Used by clients to make authenticated remote calls.
 * @author André
 * <pre>
 * {@code
 * <credential>
 *   <client>xs:string</client>
 * 	 <ticket>xs:base64Binary</ticket>
 * 	 <kcs>xs:base64Binary</kcs>
 * </credential>
 * }
 * </pre>
 */
public class KerberosCredential extends KerberosNormalMessage{
	
	private final static String XSD_FILE_WINDOWS_PATH = "\\..\\sd-util\\src\\main\\resources\\credentialFormat.xsd";
	private final static String XSD_FILE_LINUX_PATH = "/../sd-util/src/main/resources/credentialFormat.xsd";
	
	/**
	 * Cyphered ticket in raw bytes.
	 */
	private byte[] ticket;
	
	/**
	 * Kcs key.
	 */
	private Key kcs;
	
	/**
	 * Client ID.
	 */
	private String client;
	
	/**
	 * 
	 * @return
	 */
	public byte[] getTicket(){
		return ticket;
	}
	
	/**
	 * 
	 * @return kcs
	 */
	public Key getKcs(){
		return kcs;
	}
	
	public String getClient(){
		return client;
	}
	
	public KerberosCredential(String client,byte[] ticket,Key kcs) {
		this.client = client;
		this.ticket = ticket;
		this.kcs = kcs;
	}
	
	@Override
	public byte[] serialize() throws KerberosException {
		String credential = "",body ="";
		body += "<ticket>" + DatatypeConverter.printBase64Binary(ticket) +"</ticket>";
		body += "<client>" + client +"</client>";
		body += "<kcs>" + DatatypeConverter.printBase64Binary(kcs.getEncoded()) + "</kcs>";
		credential += "<credential>" + body + "</credential>";
		return credential.getBytes();
	}
	
	/**
	 * Given a serialized KerberosCredential it return
	 * the correct Object
	 * @param credential
	 * @return
	 * @throws KerberosException
	 */
	public static KerberosCredential deserialize(byte[] credential) 
	throws KerberosException{
		if(credential == null) throw new KerberosException();
		String dirFile = "",c = "";
		byte[] k = null,t = null;
		Document document = getXMLDocumentFromBytes(credential);
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
				t = DatatypeConverter.parseBase64Binary(node.getTextContent());
			}
			else if(node.getNodeName().equals("kcs")){
				k = DatatypeConverter.parseBase64Binary(node.getTextContent());
			}
			else if(node.getNodeName().equals("client")){
				c = node.getTextContent();
			}
		}
		Key key = Kerberos.getKeyFromBytes(k);
		return new KerberosCredential(c,t,key);
	}
	

}
