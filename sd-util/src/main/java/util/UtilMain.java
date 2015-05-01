package util;

import java.security.Key;
import java.util.Base64;

import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosClientAuthentication;
import util.kerberos.messages.KerberosTicket;

public class UtilMain {
	
	public static void generateServerKeysFile(){
		
	}

	/* Main used to test and learn some features. */
	public static void main(String[] args) throws Exception {
		System.out.println("Welcome do SD util library!!");
		
		KerberosTicket ticket = new KerberosTicket("client", 1, 2, Kerberos.generateSymKey(Kerberos.DES, 56));
		Key ks = Kerberos.generateSymKey(Kerberos.DES,56);
		byte[] b = ticket.serialize(ks);
		
		KerberosTicket ticket2 = KerberosTicket.deserialize(b, ks);
		
		System.out.println(Base64.getEncoder().encodeToString(ticket2.getKcs().getEncoded()));
		
		
		KerberosClientAuthentication a = new KerberosClientAuthentication("andre");
		byte[] lol = a.serialize(ks);
		a = KerberosClientAuthentication.deserialize(lol, ks);
		System.out.println(a.getRequestTime());
		
	}
}

