package util.kerberos.messages;

import util.kerberos.exception.KerberosException;

/**
 * Superclass to kerberos message doesnt need to be encrypted.
 */
public abstract class KerberosNormalMessage extends KerberosMessage{
	
	public abstract byte[] serialize() throws KerberosException;
	
	
}
