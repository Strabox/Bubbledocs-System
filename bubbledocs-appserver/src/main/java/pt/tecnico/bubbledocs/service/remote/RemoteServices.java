package pt.tecnico.bubbledocs.service.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.xml.registry.JAXRException;

import util.uddi.UDDINaming;

public abstract class RemoteServices {

	protected final String UDDILookup(String uddiUrl,String name) 
		throws JAXRException {
        UDDINaming uddiNaming = new UDDINaming(uddiUrl);
        return uddiNaming.lookup(name);
    }
	
	protected byte[] objectToBytes(Object obj) throws IOException{
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		ObjectOutputStream oOut = new ObjectOutputStream(bOut);
		oOut.writeObject(obj);
		return bOut.toByteArray();
	}
	
}
