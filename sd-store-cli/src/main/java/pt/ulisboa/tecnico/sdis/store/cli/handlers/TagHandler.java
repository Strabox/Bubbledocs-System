package pt.ulisboa.tecnico.sdis.store.cli.handlers;

import java.util.Iterator;
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


public class TagHandler implements SOAPHandler<SOAPMessageContext> {
	private int clientid=-1, version=-1;
	
	public int getClientid() {
		return clientid;
	}

	public int getVersion() {
		return version;
	}

	private boolean splitTag(String tag){
		//assuming <Tag>clientid, version</Tag>
		String[] tagsplit;
		tagsplit=tag.split(",");
		clientid = Integer.parseInt(tagsplit[0]);
		version = Integer.parseInt(tagsplit[1]);
		System.out.println("Output for me: "+clientid+";"+version);
		return true;
	}

	public void close(MessageContext context) {
		//Do nothing
	}

	public boolean handleFault(SOAPMessageContext context) {
		//Do nothing
		return false;
	}
	
	

	public boolean handleMessage(SOAPMessageContext context) {
		boolean b = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (b) {	//leaving
			
			try{
				SOAPMessage msg = context.getMessage();
				SOAPPart part = msg.getSOAPPart();
				SOAPEnvelope env = part.getEnvelope();
				SOAPHeader header = env.getHeader();
				if (header == null) {
					header = env.addHeader();
				}
				
				Name myTag = env.createName("Tag");
				SOAPHeaderElement tag = header.addHeaderElement(myTag);
				clientid++; version++;
				tag.addTextNode(clientid+","+version);
				 
			}catch(Exception e){
				System.out.println("Exception Caught:" + e);
			}
			
		}
		else {	//receiving
			
			try{
				String tag = null;
				
				SOAPMessage msg = context.getMessage();
				SOAPPart part = msg.getSOAPPart();
				SOAPEnvelope env = part.getEnvelope();
				SOAPHeader header = env.getHeader();
				
				Iterator<?> it = header.getChildElements();
                if (!it.hasNext()) {
                    return false;
                }
                while(it.hasNext()){
					SOAPHeaderElement ele = (SOAPHeaderElement) it.next();
                	String nodeName = ele.getLocalName();
                	if(nodeName.equals("Tag")){
                		tag = ele.getTextContent();
                		break;
                	}
                }
                if (tag == null) {
                	System.out.println("Error: Tag not found");
					return false;
				}
                return splitTag(tag);
			}catch(Exception e){
				System.out.println("Exception Caught:" + e);
			}
			
		}
		
		return true;
	}

	public Set<QName> getHeaders() {
		//Do nothing
		return null;
	}
	
}