package util.kerberos.messages;

import java.security.Key;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;

/**
 * Authentication - Used to authenticate server in
 * first kerberos round trip.
 * <pre>
 * {@code
 * <authentication>
 *   <nonce>base64Binary</nonce>
 *   <cliServKey>base64Binary</cliServKey>
 * </authentication>
 * }
 * </pre>
 */
public class KerberosServerAuthentication extends KerberosCypheredMessage{

	private final static String XSD_FILE_WINDOWS_PATH = "\\..\\sd-util\\src\\main\\resources\\authenticationFormat.xsd";
	private final static String XSD_FILE_LINUX_PATH = "/../sd-util/src/main/resources/authenticationFormat.xsd";
	
	/**
	 * Kcs key.
	 */
	private Key kcs;
	
	/**
	 * nonce in base64
	 */
	private String nonce;
	
	
	public KerberosServerAuthentication(Key kcs,String nonce){
		this.kcs = kcs;
		this.nonce = nonce;
	}
	
	/**
	 * @return the kcs
	 */
	public Key getKcs() {
		return kcs;
	}

	/**
	 * @return the nouce
	 */
	public String getNonce() {
		return nonce;
	}
	
	/**
	 * Used to validate Server reply (authenticator).
	 * @param nonce
	 * @return true if valid false otherwise.
	 * @throws KerberosException 
	 */
	public boolean isValid(String nonce64) throws KerberosException{
		if(nonce == null) throw new KerberosException();
		if(nonce.equals(nonce64))
			return true;
		return false;
	}
	
	@Override
	public byte[] serialize(Key kc) throws KerberosException {
		if(kc == null) throw new KerberosException();
		try{
		String authentication,body = "";
		body += "<nonce>"+ nonce + "</nonce>";
		body += "<cliServKey>" + DatatypeConverter.printBase64Binary(kcs.getEncoded()) +"</cliServKey>";
		authentication = "<authentication>" + body + "</authentication>";
		return Kerberos.cipherText(kc, authentication.getBytes(UTF8));
		} catch (Exception e) {
			throw new KerberosException();
		}
	}
	
	
	public static KerberosServerAuthentication deserialize(byte[] auth,Key k) 
	throws KerberosException {
		if(auth == null || k == null) throw new KerberosException();
		byte[] plainAuth = Kerberos.decipherText(k, auth);
		return parseTicket(plainAuth);
	}
	
	private static KerberosServerAuthentication parseTicket(byte[] auth) 
	throws KerberosException {
		String n = "",k = "",dirFile = "";
		Document document = getXMLDocumentFromBytes(auth);
		
		if(SystemUtils.IS_OS_WINDOWS)
        	dirFile = System.getProperty("user.dir") + XSD_FILE_WINDOWS_PATH;
        else if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC 
        		||SystemUtils.IS_OS_MAC_OSX )
        	dirFile = System.getProperty("user.dir") + XSD_FILE_LINUX_PATH;
		validateXMLDocument(document,dirFile);
		
		for (Node node = document.getDocumentElement().getFirstChild();
	        node != null;
	        node = node.getNextSibling()) {
		
			if(node.getNodeName().equals("nonce")){
				n = node.getTextContent();
			}
			else if(node.getNodeName().equals("cliServKey")){
				k = node.getTextContent();
			}
		}
		byte[] decodedKcs =  DatatypeConverter.parseBase64Binary(k);
		Key key = Kerberos.getKeyFromBytes(decodedKcs);
		return new KerberosServerAuthentication(key, n);
	}

}
