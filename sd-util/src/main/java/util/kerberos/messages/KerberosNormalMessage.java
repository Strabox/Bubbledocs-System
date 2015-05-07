package util.kerberos.messages;

import util.kerberos.exception.KerberosException;

/**
 * Superclass to kerberos message that doesnt need to be encrypted.
 */
public abstract class KerberosNormalMessage extends KerberosMessage{
	
	/**
	 * Serialize the message from XML to byte[].
	 * @return byte[] serialized message
	 * @throws KerberosException
	 */
	public abstract byte[] serialize() throws KerberosException;
	
	
}
