package sdis.handlers;

import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/* Kerberos Hadler - ID Client hanlder. */
public class KerberosHandler implements SOAPHandler<SOAPMessageContext> {

	
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		if(out.booleanValue()){		//If message is leaving.
			try{
			Date d = (Date) context.get("nounce");
			SOAPMessage msg = context.getMessage();
			SOAPPart sp = msg.getSOAPPart();
			SOAPEnvelope se = sp.getEnvelope();
			SOAPHeader sh = se.getHeader();

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
