package pt.ulisboa.tecnico.sdis.store.cli.handlers;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/* Kerberos Handler - Kerberos Store Client hanlder. */
public class KerberosHandler implements SOAPHandler<SOAPMessageContext> {
	
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
				
				Name ticket = env.createName("ticket","t","urn:req");
				Name auth = env.createName("auth","a","urn:req");
				Name nonce = env.createName("nonce","n","urn:req");
                SOAPHeaderElement authEle = hdr.addHeaderElement(auth);
                SOAPHeaderElement noncEle = hdr.addHeaderElement(nonce);
                SOAPHeaderElement tickEle = hdr.addHeaderElement(ticket);
                authEle.addTextNode((String)context.get("auth"));
                noncEle.addTextNode((String)context.get("nonce"));
                tickEle.addTextNode((String)context.get("ticket"));
				}
			catch(Exception e){
				System.out.println("ERROR Leaving " + e);
				return false;
			}
		}
		else{						//If message is ARRIVING!!.
			try{
				
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
