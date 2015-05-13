package pt.ulisboa.tecnico.sdis.store.ws.impl.handlers;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ulisboa.tecnico.sdis.store.ws.impl.exceptions.InvalidRequest;

/**
 *  Kerberos Handler - Kerberos Server handler. 
 */
public class KerberosHandler implements SOAPHandler<SOAPMessageContext> {
	
	//Inbound Properties
	public static final String TICKET_PROPERTY = "ticket";
	
	public static final String AUTH_PROPERTY = "auth";
	
	public static final String MAC_PROPERTY = "mac";
	
	public static final String MSG_PROPERTY = "msg";
	
	//Outbound Properties
	public static final String TIMESTAMP_PROPERTY = "time";
	public static final String TIMESTAMP_PREFIX = "t";
	public static final String TIMESTAMP_NAMESPACE = "urn:time";
	
	
	private String soapMessageToString(SOAPMessage soapMsg) 
	throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapMsg.writeTo(out);
		String soapMsgStr = new String(out.toByteArray());
		return soapMsgStr;
	}
	
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		if(out.booleanValue()){		//If message is LEAVING!!.
			try{
				byte[] timeByte = (byte[]) context.get(TIMESTAMP_PROPERTY);
				String timeBase64 = DatatypeConverter.printBase64Binary(timeByte);
				SOAPMessage msg = context.getMessage();
				SOAPPart part = msg.getSOAPPart();
				SOAPEnvelope env = part.getEnvelope();
				SOAPHeader hdr = env.getHeader();
				if(hdr == null){
					hdr = env.addHeader();
				}
				Name name = env.createName(TIMESTAMP_PROPERTY, 
				TIMESTAMP_PREFIX, TIMESTAMP_NAMESPACE);
				SOAPElement n = hdr.addHeaderElement(name);
				n.setTextContent(timeBase64);
			}
			catch(Exception e){
				System.out.println("ERROR Leaving " + e);
			}
		}
		else{						//If message is ARRIVING!!.
			try{
				String ticket = "",auth ="",mac ="";
				SOAPMessage msg = context.getMessage();
				SOAPPart part = msg.getSOAPPart();
				SOAPEnvelope env = part.getEnvelope();
				SOAPHeader hdr = env.getHeader();
				@SuppressWarnings("rawtypes")
				Iterator it = hdr.getChildElements();
                if (!it.hasNext()) 
                	throw new InvalidRequest();	//Header Missing
                while(it.hasNext()){
                	SOAPHeaderElement ele = (SOAPHeaderElement) it.next();
                	String nodeName = ele.getLocalName();
                	if(nodeName.equals(TICKET_PROPERTY)){
                		ticket = ele.getTextContent();
                	}
                	else if(nodeName.equals(AUTH_PROPERTY)){
                		auth = ele.getTextContent();
                	}
                	else if(nodeName.equals(MAC_PROPERTY)){
                		mac = ele.getTextContent();
                		hdr.removeChild(ele);
                	}
                }
                msg.saveChanges();
                byte[] msgByte = soapMessageToString(msg).getBytes();
                byte[] macByte = DatatypeConverter.parseBase64Binary(mac);
                byte[] authByte = DatatypeConverter.parseBase64Binary(auth);
                byte[] ticketByte = DatatypeConverter.parseBase64Binary(ticket);
                context.put(TICKET_PROPERTY, ticketByte);
                context.put(MAC_PROPERTY, macByte);
                context.put(AUTH_PROPERTY, authByte);
                context.put(MSG_PROPERTY, msgByte);
                context.setScope(AUTH_PROPERTY, Scope.APPLICATION);
                context.setScope(TICKET_PROPERTY, Scope.APPLICATION);
                context.setScope(MSG_PROPERTY, Scope.APPLICATION);
                context.setScope(MAC_PROPERTY, Scope.APPLICATION);
			}catch(Exception e){
				System.out.println("ERROR Processing Request Message!!");
				throw new InvalidRequest();
			}
		}
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(out.booleanValue()){		//If message is LEAVING!!.
			try{
				byte[] timeByte;
				timeByte = (byte[]) context.get(TIMESTAMP_PROPERTY);
				if(timeByte != null){
					String timeBase64 = DatatypeConverter.printBase64Binary(timeByte);
					SOAPMessage msg = context.getMessage();
					SOAPPart part = msg.getSOAPPart();
					SOAPEnvelope env = part.getEnvelope();
					SOAPHeader hdr = env.getHeader();
					if(hdr == null){
						hdr = env.addHeader();
					}
					Name name = env.createName(TIMESTAMP_PROPERTY, 
					TIMESTAMP_PREFIX, TIMESTAMP_NAMESPACE);
					SOAPElement n = hdr.addHeaderElement(name);
					n.setTextContent(timeBase64);
				}
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("ERROR Leaving " + e);
			}
		}
		else
			return false;
		return true;
	}

	public void close(MessageContext context) {
	}

	public Set<QName> getHeaders() {
		return null;
	}

}
