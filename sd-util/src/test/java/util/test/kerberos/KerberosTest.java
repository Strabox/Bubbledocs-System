package util.test.kerberos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.Key;
import javax.crypto.Cipher;
import org.junit.BeforeClass;
import org.junit.Test;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;

/**
 * Class used to test kerberos auxiliary functions.
 * @author André
 *
 */
public class KerberosTest {

	private static Key key;
	
	private static final String TEXT = "as dasd asd asd\121 2";
	
	@BeforeClass
	public static void create() throws KerberosException{
		key = Kerberos.generateKerberosKey();
	}
	
	@Test
	public void generateRandomString() throws KerberosException{
		String gen = Kerberos.generateRandomNumber();
		assertNotNull(gen);
	}
	
	@Test
	public void generateKerberosKey() throws KerberosException{
		assertNotNull(Kerberos.generateKerberosKey());
	}
	
	@Test
	public void keyFromBytesSuccess() throws KerberosException{
		byte[] keyByte = key.getEncoded();
		Key k = Kerberos.getKeyFromBytes(keyByte);
		assertEquals(k,key);
	}

	
	@Test(expected = KerberosException.class)
	public void keyFromNull() throws KerberosException{
		Kerberos.getKeyFromBytes(null);
	}
	
	@Test
	public void cypherSuccess() throws Exception{
		byte[] cyphered = Kerberos.cipherText(key, TEXT.getBytes());
		Cipher cipher = Cipher.getInstance(Kerberos.AES_MODE);
		cipher.init(Cipher.DECRYPT_MODE, key);
		String text =  new String(cipher.doFinal(cyphered));
		assertEquals(text,TEXT);
	}
	
	@Test(expected = KerberosException.class)
	public void cypherKeyNull() throws KerberosException{
		Kerberos.cipherText(null, TEXT.getBytes());
	}
	
	@Test(expected = KerberosException.class)
	public void cypherTextNull() throws KerberosException{
		Kerberos.cipherText(key, null);
	}
	
	@Test
	public void decypherSuccess() throws Exception{
		Cipher cipher = Cipher.getInstance(Kerberos.AES_MODE);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] enc =  cipher.doFinal(TEXT.getBytes());
		String text = new String(Kerberos.decipherText(key, enc));
		assertEquals(text,TEXT);
	}
	
	
	@Test(expected = KerberosException.class)
	public void decypherKeyNull() throws KerberosException{
		Kerberos.cipherText(null, TEXT.getBytes());
	}
	
	@Test(expected = KerberosException.class)
	public void decypherTextNull() throws KerberosException{
		Kerberos.cipherText(key, null);
	}
}
