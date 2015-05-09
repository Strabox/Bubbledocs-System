package pt.ulisboa.tecnico.sdis.store.cli.handlers;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import util.kerberos.Kerberos;

/**
 * Kerberos Handler - Kerberos Client hanlder. 
 */
public class KerberosHandler implements SOAPHandler<SOAPMessageContext> {
	
	public static final String TIMESTAMP_PROPERTY = "time";
	
	public static final String AUTH_PROPERTY = "auth";
	public static final String AUTH_PREFIX = "a";
	public static final String AUTH_NAMESPACE = "urn:auth";
	
	public static final String TICKET_PROPERTY = "ticket";
	public static final String TICKET_PREFIX = "t";
	public static final String TICKET_NAMESPACE = "urn:tick";

	public static final String MAC_PROPERTY = "mac";
	public static final String MAC_PREFIX = "m";
	public static final String MAC_NAMESPACE = "urn:mac";
	
	private String soapMessageToString(SOAPMessage soapMsg) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapMsg.writeTo(out);
		String soapMsgStr = new String(out.toByteArray());
		return soapMsgStr;
	}
	
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		if(out.booleanValue()){		//If message is LEAVING!!.
			try{
			SOAPMessage msg = context.getMessage();
			SOAPPart part = msg.getSOAPPart();
			SOAPEnvelope env = part.getEnvelope();
			SOAPHeader hdr = env.getHeader();
			if(hdr == null){
				hdr = env.addHeader();
			}
			Name ticketName = env.createName(TICKET_PROPERTY,
			TICKET_PREFIX,TICKET_NAMESPACE);
			Name authName = env.createName(AUTH_PROPERTY,AUTH_PREFIX,
					AUTH_NAMESPACE);
			Name macName = env.createName(MAC_PROPERTY,MAC_PREFIX,
					MAC_NAMESPACE);
            SOAPHeaderElement authEle = hdr.addHeaderElement(authName);
            SOAPHeaderElement tickEle = hdr.addHeaderElement(ticketName);
            authEle.addTextNode((String)context.get(AUTH_PROPERTY));
            tickEle.addTextNode((String)context.get(TICKET_PROPERTY));
            msg.saveChanges();
            /*==================== Building MAC =================*/
            String mac64;
            SecretKey kcs;
            byte[] byteKcs;
            byteKcs = DatatypeConverter.parseBase64Binary((String)context.get("kcs"));
            kcs = (SecretKey) Kerberos.getKeyFromBytes(byteKcs);
            byte[] macByte = Kerberos.makeMAC(soapMessageToString(msg).getBytes(),kcs);
            mac64 = DatatypeConverter.printBase64Binary(macByte);
            SOAPHeaderElement macEle = hdr.addHeaderElement(macName);
            macEle.addTextNode(mac64);
			}catch(SOAPException e){
				System.out.println("ERROR Leaving " + e);
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		else{						//If message is ARRIVING!!.
			try{
			SOAPMessage msg = context.getMessage();
			SOAPPart part = msg.getSOAPPart();
			SOAPEnvelope env = part.getEnvelope();
			SOAPHeader hdr = env.getHeader();
			
			@SuppressWarnings("rawtypes")
			Iterator it = hdr.getChildElements();
            if (!it.hasNext()) {
            	//Header 404!
                return false;
            }
            SOAPHeaderElement ele = (SOAPHeaderElement) it.next();
        	String nodeName = ele.getLocalName();
        	if(nodeName.equals(TIMESTAMP_PROPERTY)){
        		// TODO
        	}
        	else
        		return true;
			}catch(Exception e){
				System.out.println("ERROR Arriving");
				return true;
			}
		}
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	public void close(MessageContext context) {
	}

	public Set<QName> getHeaders() {
		return null;
	}

}
