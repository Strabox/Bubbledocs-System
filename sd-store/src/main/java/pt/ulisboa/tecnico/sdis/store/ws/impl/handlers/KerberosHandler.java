package pt.ulisboa.tecnico.sdis.store.ws.impl.handlers;

import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ulisboa.tecnico.sdis.store.ws.impl.exceptions.InvalidRequest;
import pt.ulisboa.tecnico.sdis.store.ws.impl.kerberos.KerberosManager;

/**
 *  Kerberos Handler - Kerberos Store Server handler. 
 */
public class KerberosHandler implements SOAPHandler<SOAPMessageContext> {
	
	public static final String SERVICE_ID = "SD-STORE";
	
	public static final String TICKET_PROPERTY = "ticket";
	
	public static final String AUTH_PROPERTY = "auth";
	
	public static final String TIMESTAMP_PROPERTY = "time";
	public static final String TIMESTAMP_PREFIX = "t";
	public static final String TIMESTAMP_NAMESPACE = "urn:time";
	/**
	 * kerberos manager - Used to manage kerberos protocol in server side.
	 */
	private KerberosManager kerberosManager;
	
	
	public KerberosHandler() throws Exception{
		kerberosManager = new KerberosManager(SERVICE_ID);
	}
	
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		if(out.booleanValue()){		//If message is LEAVING!!.
			try{
				String nonceBase64,client;
				client = (String) context.get("userId");
				nonceBase64 = kerberosManager.processReply(client);
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
				n.setTextContent(nonceBase64);
			}
			catch(Exception e){
				System.out.println("ERROR Leaving " + e);
			}
		}
		else{						//If message is ARRIVING!!.
			try{
				String ticket = "",auth ="";
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
                while(it.hasNext()){
                	SOAPHeaderElement ele = (SOAPHeaderElement) it.next();
                	String nodeName = ele.getLocalName();
                	if(nodeName.equals(TICKET_PROPERTY)){
                		ticket = ele.getTextContent();
                	}
                	else if(nodeName.equals(AUTH_PROPERTY)){
                		auth = ele.getTextContent();
                	}
                }
                kerberosManager.processRequest(ticket, auth);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("ERROR Processing Request Message!!");
				throw new InvalidRequest();
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
