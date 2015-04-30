package util;

import java.security.Key;
import util.kerberos.Kerberos;
import util.kerberos.KerberosTicket;

public class UtilMain {

	/* Main used to test and learn some features. */
	public static void main(String[] args) throws Exception {
		System.out.println("Welcome do SD util library!!");
		
		KerberosTicket kt = new KerberosTicket("client", "server", 2, Kerberos.generateSymKey("DES", 56));
		
		Key k = Kerberos.generateSymKey("DES", 56);
		
		byte[] ticket = kt.serializeTicket(k);
		
		KerberosTicket.deserializeTicket(ticket, k);
		
	
	
	}
}

