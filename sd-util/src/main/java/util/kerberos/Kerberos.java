package util.kerberos;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Kerberos {
	
	public final static String MD5 = "MD5";
	
	public final static String DES = "DES";
	
	public final static String CYPHER_MODE = "DES/ECB/PKCS5Padding";
	
	
	/* digestPassword - used to get Simmetric key to talk
	 * with user in the authentication process. */
	public static byte[] digestPassword(String password,String alg) 
	throws NoSuchAlgorithmException{
		
		byte[] passInBytes = password.getBytes();
		MessageDigest md = MessageDigest.getInstance(alg);
		md.update(passInBytes);
		return md.digest();
	}
	
	/*
	 * getKeyFromBytes - transform some bytes in a Key;
	 */
	public static Key getKeyFromBytes(byte[] bytesKey) 
	throws InvalidKeySpecException, 
	NoSuchAlgorithmException, InvalidKeyException {
		
		DESKeySpec keySpec = new DESKeySpec(bytesKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		return keyFactory.generateSecret(keySpec);
	}
	
	/*
	 * generateSymKey - generate random symmetric key with keysize.
	 */
	public static Key generateSymKey(String algorithm,int keysize) 
	throws NoSuchAlgorithmException{
		
		KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
		keyGen.init(keysize);
		return keyGen.generateKey();
	}
	
	/*
	 * cipherText - Cipher bytes with given key.
	 */
	public static byte[] cipherText(Key key,byte[] text) 
	throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, 
	IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		
		Cipher cipher = Cipher.getInstance(CYPHER_MODE);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(text);
	}
	
	/*
	 * decipherText - 
	 */
	public static byte[] decipherText(Key key,byte[] text) 
	throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, 
	IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		
		Cipher cipher = Cipher.getInstance(CYPHER_MODE);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(text);
	}
	
	/*
	 * generateRandomNumber() -
	 */
	public static String generateRandomNumber() throws NoSuchAlgorithmException{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		final byte[] bytes = new byte[16];
		sr.nextBytes(bytes);
		return DatatypeConverter.printBase64Binary(bytes);
	}
	
	
	
}
