package util.test.kerberos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosRequest;

public class KerberosRequestTest {
	
	private static final String SERVICE = "Service";
	
	private static String nonce;

	
	@BeforeClass
	public static void create() throws KerberosException{
		nonce = Kerberos.generateRandomNumber();
	}
	
	/**
	 * Success serializing a request and deserializing it.
	 */
	@Test
	public void successSerializeDeserialize() throws KerberosException{
		KerberosRequest req = new KerberosRequest(SERVICE, nonce);
		byte[] request =  req.serialize();
		KerberosRequest req2 = KerberosRequest.deserialize(request);
		assertEquals(req2.getNonce(),req.getNonce());
		assertEquals(req2.getServer(),req.getServer());
	}
	
	/**
	 * Try deserialize a null.
	 */
	@Test(expected = KerberosException.class)
	public void nullDeserializing() throws KerberosException{
		KerberosRequest.deserialize(null);
	}
	
	/**
	 * Try deserialize a random byte[].
	 */
	@Test(expected = KerberosException.class)
	public void deserializingWrong() throws KerberosException{
		byte[] rand = Kerberos.generateRandomNumber().getBytes();
		KerberosRequest.deserialize(rand);
	}
	
	/**
	 * Serialize with null arguments.
	 */
	@Test
	public void serializeNull() throws KerberosException{
		KerberosRequest re = new KerberosRequest(null, null);
		byte[] req = re.serialize();
		assertNotNull(req);
	}
	
	/**
	 * Deserialize a incorrect serialized request.
	 * @throws KerberosException 
	 */
	 @Test(expected = KerberosException.class)
	 public void deserializeWrongRequest() throws KerberosException{
		 String body = "",request = "";
		 body = "<se>" + "Server" + "</server>";
		 body += "<nonce>" + Kerberos.generateRandomNumber() + "</nonce>";
		 request = "<request>" + body +"</request>";
		 byte[] reqSerialized = request.getBytes();
		 KerberosRequest.deserialize(reqSerialized);
	 }
}
