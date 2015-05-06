package util;

import java.security.Key;

import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosRequest;
import util.kerberos.messages.KerberosTicket;


public class UtilMain {

	/* Main used to test and learn some features. */
	public static void main(String[] args) throws Exception {
		System.out.println("Welcome do SD util library!!");
		
		Key k = Kerberos.generateSymKey("DES", 56);
		KerberosTicket t = new KerberosTicket("a", 1, 1, k);
		KerberosTicket.deserialize(t.serialize(k), k);
		
		KerberosRequest req = new KerberosRequest(1, Kerberos.generateRandomNumber());
		KerberosRequest.deserialize(req.serialize());
	}
}

