package pt.ulisboa.tecnico.sdis.store.ws.impl.handlers;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/* Kerberos Handler - Kerberos ID Client hanlder. */
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
			try{/*
				SOAPMessage msg = context.getMessage();
				SOAPPart part = msg.getSOAPPart();
				SOAPEnvelope env = part.getEnvelope();
				SOAPHeader hdr = env.getHeader();
				Iterator it = hdr.getChildElements();
                if (!it.hasNext()) {
                	//Header 404!
                    return true;
                }
                while(it.hasNext()){
                	SOAPElement ele = (SOAPElement) it.next();
                	String nodeName = ele.getNodeName();
                	if(nodeName.equals("ticket")){
                		System.out.println("Ticket here");
                		context.setScope("ticket", Scope.APPLICATION);
                	}
                	else if(nodeName.equals("auth")){
                		System.out.println("Auth here");
                		context.setScope("auth", Scope.APPLICATION);
                	}
                	else if(nodeName.equals("nonce")){
                		System.out.println("Nonce here");
                		context.setScope("nonce", Scope.APPLICATION);
                	}
                }*/
			}catch(Exception e){
				System.out.println("ERROR Arriving");
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
