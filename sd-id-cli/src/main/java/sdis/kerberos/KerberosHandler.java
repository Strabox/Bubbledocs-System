package sdis.kerberos;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/* Kerberos Hadler - ID Client hanlder. */
public class KerberosHandler implements SOAPHandler<SOAPMessageContext> {

	
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(out.booleanValue()){		//If message is LEAVING!!.
			try{
				/*
				SOAPMessage msg = context.getMessage();
				SOAPPart part = msg.getSOAPPart();
				SOAPEnvelope env = part.getEnvelope();
				SOAPHeader hdr = env.getHeader();
				if(hdr == null){
					hdr = env.addHeader();
				}
				Name server = env.createName("server");
				Name nounce = env.createName("nounce");
                SOAPHeaderElement servEle = hdr.addHeaderElement(server);
                SOAPHeaderElement nounEle = hdr.addHeaderElement(nounce);
                servEle.addTextNode((String)context.get(Kerberos.SERVER_ENDPOINT));
                nounEle.addTextNode((String)context.get(Kerberos.NOUNCE_ENDPOINT));
                */
			}
			catch(Exception e){
				System.out.println("ERROR Leaving");
				return false;
			}
		}
		else{						//If message is ARRIVING!!.
			try{
				// PROBABLY EMPTY in this handler.
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
