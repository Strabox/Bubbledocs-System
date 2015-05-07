package util.kerberos.messages;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;

/**
 * Message used to validate client request.
 * @author André
 * <pre>
 * {@code
 * <clientAuth>
 * 	 <client>xs:dateTime</client>
 *   <requestTime>xs:dateTime</requestTime>
 * </clientAuth>
 * }
 * </pre>
 */
public class KerberosClientAuthentication extends KerberosCypheredMessage {
	
	private final static String XSD_FILE_WINDOWS_PATH = "\\..\\sd-util\\src\\main\\resources\\clientAuthenticationFormat.xsd";
	private final static String XSD_FILE_LINUX_PATH = "/../sd-util/src/main/resources/clientAuthenticationFormat.xsd";
	
	/**
	 * Client making the request. 
	 */
	private String client;
	
	/**
	 * Request timestamp.
	 */
	private Date requestTime;
	
	
	public KerberosClientAuthentication(String client) {
		this.client = client;
	}
	
	private KerberosClientAuthentication(String client,Date requestTime){
		this.client = client;
		this.requestTime = requestTime;
	}
	
	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @return the requestTime
	 */
	public Date getRequestTime() {
		return requestTime;
	}
	
	
	/**
	 * Used to validate authenticator.
	 * @param username
	 * @param lastRequest
	 * @return true if valid, false otherwise
	 */
	public boolean isValid(String username,Date lastRequest){
		if(!client.equals(username))
			return false;
		Date currentTime = new Date();
		if(lastRequest == null|| (currentTime.getTime() > getRequestTime().getTime() &&
			getRequestTime().getTime() > lastRequest.getTime()))
			return true;
		return false;
	}
	
	
	@Override
	public byte[] serialize(Key kcs) throws KerberosException {
		String auth = "", body ="";
		XMLGregorianCalendar requestTime;
		try{
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date()); 
			requestTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
			
			body += "<client>" + client + "</client>";
			body += "<requestTime>" + requestTime.toXMLFormat() + "</requestTime>";
			auth ="<clientAuth>"+ body + "</clientAuth>";
		
			return Kerberos.cipherText(kcs, auth.getBytes(UTF8));
		}catch(Exception e){
			throw new KerberosException();
		}
	}
	
	/**
	 * 
	 * @param auth
	 * @param kcs
	 * @return
	 * @throws KerberosException
	 */
	public static KerberosClientAuthentication deserialize(byte[] auth,Key kcs)
	throws KerberosException{
		byte[] plainAuth = Kerberos.decipherText(kcs,auth);
		return parse(plainAuth);
	}
	
	/**
	 * 
	 * @param auth
	 * @return
	 * @throws KerberosException
	 */
	private static KerberosClientAuthentication parse(byte[] auth)
	throws KerberosException{
		String dirFile ="",c ="";
		Date requestTime = null;
		
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
		
			if(node.getNodeName().equals("client")){
				c = node.getTextContent();
			}
			else if(node.getNodeName().equals("requestTime")){
				Calendar cal  = DatatypeConverter.parseDateTime(node.getTextContent());
				requestTime = cal.getTime();
			}
		}
		return new KerberosClientAuthentication(c,requestTime);
	}


}
