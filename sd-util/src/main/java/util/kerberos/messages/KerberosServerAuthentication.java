package util.kerberos.messages;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;

/**
 * Authentication - Used to authenticate server in
 * first kerberos round trip.
 */
public class KerberosServerAuthentication extends KerberosCypheredMessage{

	private final static String XSD_FILE_WINDOWS_PATH = "\\..\\sd-util\\src\\main\\resources\\authenticationFormat.xsd";
	private final static String XSD_FILE_LINUX_PATH = "/../sd-util/src/main/resources/authenticationFormat.xsd";
	
	private Key kcs;
	
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
	
	@Override
	public byte[] serialize(Key kc) throws KerberosException {
		String authentication,body = "";
		body += "<nounce>"+ nonce + "</nounce>";
		body += "<cliServKey>" + DatatypeConverter.printBase64Binary(kcs.getEncoded()) +"</cliServKey>";
		authentication = "<authentication>" + body + "</authentication>";
		
		try{
			return Kerberos.cipherText(kc, authentication.getBytes(UTF8));
		}
		catch(Exception e){
			throw new KerberosException();
		}
	}
	
	
	public static KerberosServerAuthentication deserialize(byte[] auth,Key k) 
			throws KerberosException{
		try{
			byte[] plainAuth = Kerberos.decipherText(k, auth);
			return parseTicket(plainAuth);
		}catch(Exception e){
			throw new KerberosException();
		}
	}
	
	private static KerberosServerAuthentication parseTicket(byte[] auth) 
	throws KerberosException {
		
		String n = "",k = "",dirFile = "";
		try{
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
			
				if(node.getNodeName().equals("nounce")){
					n = node.getTextContent();
				}
				else if(node.getNodeName().equals("cliServKey")){
					k = node.getTextContent();
				}
			}
			byte[] decodedKcs =  Base64.getDecoder().decode(k);
			Key key = Kerberos.getKeyFromBytes(decodedKcs);
			return new KerberosServerAuthentication(key, n);
		}catch (InvalidKeyException e) {
			throw new KerberosException();
		} catch (InvalidKeySpecException e) {
			throw new KerberosException();
		} catch (NoSuchAlgorithmException e) {
			throw new KerberosException();
		}
	}

}
