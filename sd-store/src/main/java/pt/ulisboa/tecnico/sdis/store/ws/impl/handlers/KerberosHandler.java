package pt.ulisboa.tecnico.sdis.store.ws.impl.handlers;

import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/* Kerberos Handler - Kerberos Store Server hanlder. */
public class KerberosHandler implements SOAPHandler<SOAPMessageContext> {

	
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		if(out.booleanValue()){		//If message is LEAVING!!.
			try{
				
			}
			catch(Exception e){
				System.out.println("ERROR Leaving");
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
                while(it.hasNext()){
                	SOAPHeaderElement ele = (SOAPHeaderElement) it.next();
                	String nodeName = ele.getLocalName();
                	if(nodeName.equals("ticket")){
                		context.put("ticket",ele.getTextContent());
                		context.setScope("ticket", Scope.APPLICATION);
                	}
                	else if(nodeName.equals("auth")){
                		context.put("auth",ele.getTextContent());
                		context.setScope("auth", Scope.APPLICATION);
                	}
                	else if(nodeName.equals("nonce")){
                		context.put("nonce",ele.getTextContent());
                		context.setScope("nonce", Scope.APPLICATION);
                	}
                }
			}catch(Exception e){
				System.out.println("ERROR Arriving" + e);
				return false;
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
