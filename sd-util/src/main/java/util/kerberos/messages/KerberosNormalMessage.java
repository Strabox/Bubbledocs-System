package util.kerberos.messages;

import util.kerberos.exception.KerberosException;

public abstract class KerberosNormalMessage extends KerberosMessage{

	public abstract byte[] serialize() throws KerberosException;
	
}
