package sdis.handlers;

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

public class KerberosHandler implements SOAPHandler<SOAPMessageContext> {

	public boolean handleMessage(SOAPMessageContext context) {
		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(out.booleanValue()){
			try{
				
			SOAPMessage msg = context.getMessage();
			SOAPPart sp = msg.getSOAPPart();
			SOAPEnvelope se = sp.getEnvelope();
			
			SOAPHeader header = se.getHeader();
			if(header == null)
				header = se.addHeader();
			
			Name name = se.createName("lo", "k", "http://strabox");
			SOAPHeaderElement ele = header.addHeaderElement(name);
			ele.addTextNode(Integer.toString(1000));
			}
			catch(Exception e){
				System.out.println("ERRO");
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
