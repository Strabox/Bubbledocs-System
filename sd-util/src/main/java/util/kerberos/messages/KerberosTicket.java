package util.kerberos.messages;

import java.io.ByteArrayInputStream;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;

/*
 * KerberosTicket - class used to create, write and handle
 * kerberos ticket.
 */
public class KerberosTicket extends KerberosCypheredMessage{

	private final static String XSD_FILE_WINDOWS_PATH = "\\..\\sd-util\\src\\main\\resources\\ticketFormat.xsd";
	private final static String XSD_FILE_LINUX_PATH = "/../sd-util/src/main/resources/ticketFormat.xsd";

	private String client;
	private Integer server;
	private int hourDuration;
	private Date beginTime;
	private Date endTime;
	private Key kcs;
	
	
	public KerberosTicket(String client,int server,int duration,
			Key kcs) {
		this.client = client;
		this.server = server;
		this.hourDuration = duration;
		this.kcs = kcs;
	}
	
	private KerberosTicket(String client,int server,
			Key kcs,Date beginTime,Date endTime) {
		this.client = client;
		this.server = server;
		this.kcs = kcs;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}
	
	@Override
	public byte[] serialize(Key serverKey) throws KerberosException{
		XMLGregorianCalendar createTime;
		XMLGregorianCalendar endTime;
		try{
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
			ticketBody += "<server>" + server.toString() + "</server>";
			ticketBody += "<beginTime>" + createTime.toString() + "</beginTime>";
			ticketBody += "<endTime>" + endTime.toString() + "</endTime>";
			ticketBody += "<cliServKey>" + Base64.getEncoder().encodeToString(kcs.getEncoded()) + "</cliServKey>";
			ticketXML = "<ticket>" + ticketBody +"</ticket>";
			System.out.println(ticketXML);
			System.out.println(Base64.getEncoder().encodeToString(kcs.getEncoded()));
			return Kerberos.cipherText(serverKey, ticketXML.getBytes(UTF8));
		}catch(Exception e){
			throw new KerberosException();
		}
	}
	
	/**
	 * @param ticket
	 * @param k
	 * @return
	 * @throws KerberosException
	 */
	public static KerberosTicket deserialize(byte[] ticket,Key k) 
	throws KerberosException {
		try{
			byte[] plainTicket = Kerberos.decipherText(k, ticket);
			String strTicket = new String(plainTicket,UTF8);
			
			return parseTicket(strTicket);
		}catch(Exception e){
			throw new KerberosException();
		}
	}

	/**
	 * @param strTicket
	 * @return
	 * @throws KerberosException
	 */
	private static KerberosTicket parseTicket(String strTicket) 
	throws KerberosException {
		int server = 0;
		String client ="" ,kcs = "",dirFile = "";
		Date beginTime = null ,endTime = null;
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
			Document document;
			document = builder.parse(new ByteArrayInputStream(strTicket.getBytes()));
			
			if(SystemUtils.IS_OS_WINDOWS)
	        	dirFile = System.getProperty("user.dir") + XSD_FILE_WINDOWS_PATH;
	        else if(SystemUtils.IS_OS_LINUX)
	        	dirFile = System.getProperty("user.dir") + XSD_FILE_LINUX_PATH;
			validateXMLDocument(document,dirFile);
			
			for (Node node = document.getDocumentElement().getFirstChild();
		        node != null;
		        node = node.getNextSibling()) {
			
				if(node.getNodeName().equals("client")){
					client = node.getTextContent();
				}
				else if(node.getNodeName().equals("server")){
					server = Integer.parseInt(node.getTextContent());
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
			byte[] keyInBytes = Base64.getDecoder().decode(kcs);
			Key key = Kerberos.getKeyFromBytes(keyInBytes);
			return new KerberosTicket(client, server, key,beginTime,endTime);
		}catch(Exception e){
			throw new KerberosException();
		}
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
	public Key getKcs(){
		return kcs;
	}
	
	/**
	 * 
	 * @return the server
	 */
	public Integer getServer(){
		return server;
	}
	
}
