package sdis;

import javax.xml.ws.Endpoint;

import sdis.uddi.UDDINaming;


public class SDIDMain {

    public static void main(String[] args) {
        // Check arguments
        if (args.length < 3) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s url%n", SDIDMain.class.getName());
            return;
        }
        
        String uddiURL = args[0];
        String name = args[1];
        String url = args[2];
        
        Endpoint endpoint = null;
        UDDINaming uddiNaming = null;
        try {
            endpoint = Endpoint.create(new SDImpl());

            // publish endpoint
            System.out.printf("Starting %s%n", url);
            endpoint.publish(url);
            
            // publish to UDDI
            System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
            uddiNaming = bindUDDI(uddiURL,name,url);
            
            // Wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();

        } catch(Exception e) {
            System.out.printf("Caught exception: %s%n", e);
        }finally {
            try {
                if(endpoint != null) {
                    // Stop endpoint
                    endpoint.stop();
                    System.out.printf("Stopped %s%n", url);
                }
            } catch(Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
            try{
            	if(uddiNaming != null){
            		// Delete from UDDI
                    unbindUDDI(uddiNaming,name);
                    System.out.printf("Deleted '%s' from UDDI%n", name);
            	}
            }catch(Exception e){
            	System.out.printf("Caught exception when deleting: %s%n", e);
            }
        }

    }
      SDIDMain(){ }
    
    public static UDDINaming bindUDDI(String uddiURL, String name, String url) throws Exception {
    	// Publish to UDDI
        UDDINaming uddiNaming = new UDDINaming(uddiURL);
        uddiNaming.rebind(name, url);
        return uddiNaming;
    }
    
    public static void unbindUDDI(UDDINaming uddi, String name) throws Exception {
    	uddi.unbind(name);
    }
    
}
