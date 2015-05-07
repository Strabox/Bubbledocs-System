package util.kerberos.messages;

import java.security.Key;

import util.kerberos.exception.KerberosException;

public abstract class KerberosCypheredMessage extends KerberosMessage{
	
	/**
	 * Method use key to cypher XML message and return
	 * it cyphered in bytes.
	 * @param k
	 * @return
	 * @throws KerberosException
	 */
	public abstract byte[] serialize(Key k) throws KerberosException;
	
}
