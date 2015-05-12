package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.io.IOException;

import javax.xml.registry.JAXRException;
import javax.xml.ws.Endpoint;

import example.ws.uddi.UDDINaming;

public class SDStoreMain {

	public static void main(String[] args) throws IOException {
		int nservers = 3;  //CHANGE ME

		// Check arguments
		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s url%n", SDStoreMain.class.getName());
			return;
		}
		try{
			String uddiURL = args[0];
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
			Endpoint[] endpoints= new Endpoint[nservers];
			for (int i=1;i<=nservers;i++){
				endpoints[i-1]=Endpoint.create(new SDStoreImpl());
				startup(uddiURL, "SD-STORE-"+i+"", "http://localhost:80"+(81+i)+""+"/store-ws/endpoint",uddiNaming,endpoints[i-1]);
			}
			System.out.println("All servers up. Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();
			for (int i=1;i<=nservers;i++){
				endpoints[i-1].stop();
				uddiNaming.unbind("SD-STORE-"+i+"");
			}

		}
		catch (Exception e){
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();
		}
	}
	public static void startup(String uddiURL, String name, String url, UDDINaming uddi, Endpoint endpoint) throws JAXRException {
		uddi.rebind(name, url);			
		endpoint.publish(url);					
	}
}



