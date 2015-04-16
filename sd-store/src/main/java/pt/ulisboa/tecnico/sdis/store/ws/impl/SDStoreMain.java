package pt.ulisboa.tecnico.sdis.store.ws.impl;

import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.store.ws.impl.uddi.UDDINaming;

public class SDStoreMain {

    public static void main(String[] args) {
        // Check arguments
        if (args.length < 3) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s url%n", SDStoreMain.class.getName());
            return;
        }

        String uddiURL = args[0];
        String name = args[1];
        String url = args[2];
        
        Endpoint endpoint = null;
        UDDINaming uddiNaming = null;
        
        try {
            endpoint = Endpoint.create(new SDStoreImpl());

            // publish endpoint
            System.out.printf("Starting %s%n", url);
            endpoint.publish(url);
            
         // publish to UDDI
            System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
            uddiNaming = bindUDDI(uddiURL,name,url);
            

            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();

        } catch(Exception e) {
            System.out.printf("Caught exception: %s%n", e);
            e.printStackTrace();

        } finally {
            try {
                if (endpoint != null) {
                    // stop endpoint
                    unbindUDDI(uddiNaming,name);
                    endpoint.stop();
                    System.out.printf("Stopped %s%n", url);
                }
            } catch(Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
        }

    }
    public static UDDINaming bindUDDI(String uddiURL, String name, String url) throws Exception {
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
        uddiNaming.rebind(name, url);
        return uddiNaming;
    }
    
    public static void unbindUDDI(UDDINaming uddi, String name) throws Exception {
    	uddi.unbind(name);
    }

}



  
