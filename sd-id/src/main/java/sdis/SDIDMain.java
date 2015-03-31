package sdis;

import javax.xml.ws.Endpoint;

import sdis.uddi.UDDINaming;


@SuppressWarnings("restriction")
public class SDIDMain {

    public static void main(String[] args) {
        // Check arguments
        if (args.length < 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s url%n", SDIDMain.class.getName());
            return;
        }
        
        String uddiUTL = args[0];
        String name = args[1];
        
        String url = args[2];
        
        Endpoint endpoint = null;
        UDDINaming uddiNaming = null;
        try {
            endpoint = Endpoint.create(new SDImpl());

            // publish endpoint
            System.out.printf("Starting %s%n", url);
            endpoint.publish(url);

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
                    endpoint.stop();
                    System.out.printf("Stopped %s%n", url);
                }
            } catch(Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
        }

    }

}
