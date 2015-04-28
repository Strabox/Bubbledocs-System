package sdis.handlers;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/* Kerberos Hadler - ID Client hanlder. */
public class KerberosHandler implements SOAPHandler<SOAPMessageContext> {

	
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		if(out.booleanValue()){		//If message is leaving.
			try{
			//Date d = (Date) context.get("nounce");
			//SOAPMessage msg = context.getMessage();
			//SOAPPart sp = msg.getSOAPPart();
			//SOAPEnvelope se = sp.getEnvelope();
			//SOAPHeader sh = se.getHeader();

			}
			catch(Exception e){
				System.out.println("ERRO Leaving");
			}
		}
		else{						//If message is arriving.
			try{
				
			}catch(Exception e){
				System.out.println("ERRO Arriving");
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
