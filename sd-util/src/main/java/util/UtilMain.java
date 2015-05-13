package util;

import java.security.Key;

import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosTicket;


public class UtilMain {

	/* Main used to test and learn some features. */
	public static void main(String[] args) throws Exception {
		System.out.println("Welcome do SD util library!!");
		Key kcs = Kerberos.generateKerberosKey();
		Key ks = Kerberos.generateKerberosKey();
		KerberosTicket ticket = new KerberosTicket(null, "serv", 1, kcs);
		byte[] ser = ticket.serialize(ks);
		KerberosTicket ticket2 = KerberosTicket.deserialize(ser, ks);
		System.out.println(ticket2.isValidTicket("serv", "null"));
	}
}

