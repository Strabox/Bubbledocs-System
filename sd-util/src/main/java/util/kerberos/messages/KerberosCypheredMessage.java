package util.kerberos.messages;

import java.security.Key;

import util.kerberos.exception.KerberosException;

public abstract class KerberosCypheredMessage extends KerberosMessage{

	public abstract byte[] serialize(Key k) throws KerberosException;
	
}
