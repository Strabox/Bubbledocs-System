package pt.tecnico.bubbledocs.service.remote;

import javax.xml.registry.JAXRException;
import pt.tecnico.bubbledocs.service.uddi.UDDINaming;

public abstract class RemoteServices {

	public final String UDDILookup(String uddiUrl,String name) 
		throws JAXRException {
        UDDINaming uddiNaming = new UDDINaming(uddiUrl);
        return uddiNaming.lookup(name);
    }
	
}
