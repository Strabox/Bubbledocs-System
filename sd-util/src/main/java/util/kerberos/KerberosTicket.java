package util.kerberos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.XMLConstants;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/*
 * KerberosTicket - class used to create, write and handle
 * kerberos ticket.
 */
public class KerberosTicket {

	private final static String UTF8 = "UTF-8";

	private String client;
	private String server;
	private int hourDuration;
	private Date beginTime;
	private Date endTime;
	private String cliServKey;
	private String kcs;
	
	
	public KerberosTicket(String client,String server,int duration,
			Key kcs) {
		this.client = client;
		this.server = server;
		this.hourDuration = duration;
		this.kcs = Base64.getEncoder().encodeToString(kcs.getEncoded());
	}
	
	private KerberosTicket(String client,String server,int duration,
			String kcs,Date beginTime,Date endTime) {
		this.client = client;
		this.server = server;
		this.hourDuration = duration;
		this.kcs = kcs;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}
	
	/*
	 * serializeTicket -
	 */
	public byte[] serializeTicket(Key ks) throws DatatypeConfigurationException,
			UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, 
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		XMLGregorianCalendar createTime;
		XMLGregorianCalendar endTime;
	
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date()); 
		createTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		
		Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    cal.add(Calendar.HOUR_OF_DAY, hourDuration);
	    
	    gc.setTime(cal.getTime());
	    endTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		
		String ticketXML, ticketBody;
		ticketBody = "<client>" + client + "</client>";
		ticketBody += "<server>" + server + "</server>";
		ticketBody += "<beginTime>" + createTime.toString() + "</beginTime>";
		ticketBody += "<endTime>" + endTime.toString() + "</endTime>";
		ticketBody += "<cliServKey>" + kcs + "</cliServKey>";
		ticketXML = "<ticket>" + ticketBody +"</ticket>";
		System.out.println(ticketXML);
		return Kerberos.cipherText(ks, ticketXML.getBytes(UTF8));
	}
	
	/*
	 * deserializeTicket(byte[]) - 
	 */
	public static KerberosTicket deserializeTicket(byte[] ticket,Key k) 
			throws InvalidKeyException, NoSuchAlgorithmException, 
			NoSuchPaddingException, IllegalBlockSizeException, 
			BadPaddingException, SAXException, IOException, 
			ParserConfigurationException, DOMException, ParseException {
		
		byte[] plainTicket = Kerberos.decipherText(k, ticket);
		String strTicket = new String(plainTicket,UTF8);
		
		return parseTicket(strTicket);
	}

	
	private static KerberosTicket parseTicket(String strTicket) throws 
	SAXException, IOException, ParserConfigurationException, DOMException, ParseException {
		
		String client ="" ,server = "",kcs = "";
		
		Date beginTime = null ,endTime = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
		Document document;
		document = builder.parse(new ByteArrayInputStream(strTicket.getBytes()));
		validateTicket(document);
		
		for (Node node = document.getDocumentElement().getFirstChild();
	        node != null;
	        node = node.getNextSibling()) {
		
			if(node.getNodeName().equals("client")){
				client = node.getTextContent();
			}
			else if(node.getNodeName().equals("server")){
				server = node.getTextContent();
			}
			else if(node.getNodeName().equals("cliServKey")){
				kcs = node.getTextContent();
			}
			else if(node.getNodeName().equals("beginTime")){
				Calendar cal  = DatatypeConverter.parseDateTime(node.getTextContent());
				beginTime = cal.getTime();
			}
			else if(node.getNodeName().equals("endTime")){
				Calendar cal  = DatatypeConverter.parseDateTime(node.getTextContent());
				endTime = cal.getTime();
			}
		}
			
		return new KerberosTicket(client, server, -1, kcs,beginTime,endTime);
	}

	/*
	 * verify if ticket structure is cool.
	 */
	private static void validateTicket(Document doc) throws ParserConfigurationException,
			SAXException, IOException{
        
        File schemaFile = new File("/afs/.ist.utl.pt/users/4/6/ist176046/git/A_15_03_17-project/sd-util/src/main/resources/ticketFormat.xsd");
        Source schemaSource = new StreamSource(schemaFile);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaSource);
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(doc));

	}
	
	/**
	 * @return the beginTime
	 */
	public Date getBeginTime() {
		return beginTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @return the cliServKey
	 */
	public String getCliServKey() {
		return cliServKey;
	}
	
	/**
	 * 
	 * @return the client
	 */
	public String getClient(){
		return client;
	}
	
	/**
	 * 
	 * @return the kcs
	 */
	public String getKcs(){
		return kcs;
	}
	
	/**
	 * 
	 * @return the server
	 */
	public String getServer(){
		return server;
	}
	
}
