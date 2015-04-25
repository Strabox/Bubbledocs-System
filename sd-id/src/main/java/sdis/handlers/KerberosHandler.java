package sdis.handlers;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class KerberosHandler implements SOAPHandler<SOAPMessageContext> {

	public boolean handleMessage(SOAPMessageContext context) {
		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(!out.booleanValue()){
			try{
			SOAPMessage message = context.getMessage();
			SOAPPart sp = message.getSOAPPart();
			SOAPEnvelope sh = sp.getEnvelope();
			
			SOAPHeader header = sh.getHeader();
			
			NodeList list = header.getChildNodes();
			
			Node n = list.item(0);
			
			System.out.println(n.getTextContent());
			
			}catch(Exception e){
				System.out.println(e);
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
