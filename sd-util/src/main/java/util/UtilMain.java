package util;

import java.security.Key;

import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosTicket;


public class UtilMain {

	/* Main used to test and learn some features. */
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		System.out.println("Welcome do SD util library!!");
		Key kcs = Kerberos.generateSymKey("DES", 56);
		KerberosTicket ticket = new KerberosTicket("cleiA0aa", 1, 2, kcs);
		byte[] l = ticket.serialize(kcs);
		ticket.deserialize(l, kcs);
	}
}

