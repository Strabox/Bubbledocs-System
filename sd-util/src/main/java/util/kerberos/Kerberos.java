package util.kerberos;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.bind.DatatypeConverter;

import util.kerberos.exception.KerberosException;

public class Kerberos {
	
	public final static String MD5 = "MD5";
	
	public final static String DES = "DES";
	
	public final static String CYPHER_MODE = "DES/ECB/PKCS5Padding";
	
	
	/* digestPassword - used to get Simmetric key to talk
	 * with user in the authentication process. */
	public static byte[] digestPassword(byte[] password,String alg) 
	throws KerberosException{
		try{
			MessageDigest md = MessageDigest.getInstance(alg);
			md.update(password);
			return md.digest();
		}catch(NoSuchAlgorithmException e){
			throw new KerberosException();
		}
	}
	
	/**
	 * Transform byte[] in a Key.
	 * @param bytesKey
	 * @return
	 * @throws KerberosException
	 */
	public static Key getKeyFromBytes(byte[] bytesKey) 
	throws KerberosException{
		try{
			DESKeySpec keySpec = new DESKeySpec(bytesKey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			return keyFactory.generateSecret(keySpec);
		}catch(Exception e){
			throw new KerberosException();
		}
	}
	
	/**
	 * Generate a random symmetric key.
	 * @param algorithm used to generate key.
	 * @param keysize size of wanted key.
	 * @return Key return java.security.key
	 * @throws KerberosException
	 */
	public static Key generateSymKey(String algorithm,int keysize) 
	throws KerberosException{
		try{
			KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
			keyGen.init(keysize);
			return keyGen.generateKey();
		}catch(NoSuchAlgorithmException e){
			throw new KerberosException();
		}
	}
	
	/**
	 * Cypher a message given a key and bytes.
	 * @param key
	 * @param text
	 * @return
	 * @throws KerberosException
	 */
	public static byte[] cipherText(Key key,byte[] text) 
	throws KerberosException {
		try{
			Cipher cipher = Cipher.getInstance(CYPHER_MODE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(text);
		}catch(Exception e){
			throw new KerberosException();
		}
	}
	
	/**
	 * Decipher a message.
	 * @param key used to decipher message.
	 * @param text to be decyphered.
	 * @return return plain message.
	 * @throws KerberosException
	 */
	public static byte[] decipherText(Key key,byte[] text) 
	throws KerberosException {
		try{
			Cipher cipher = Cipher.getInstance(CYPHER_MODE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(text);
		}catch(Exception e){
			throw new KerberosException();
		}
	}
	
	/**
	 * Generate a random number.
	 * @return random 16 byte array in base64 to use in XML.
	 * @throws KerberosException
	 */
	public static String generateRandomNumber() 
	throws KerberosException {
		try{
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			final byte[] bytes = new byte[16];
			sr.nextBytes(bytes);
			return DatatypeConverter.printBase64Binary(bytes);
		}catch(Exception e){
			throw new KerberosException();
		}
	}
	
	/**
	 * 
	 * @param bytes
	 * @param key
	 * @return Cyphered MAC signature.
	 * @throws Exception
	 */
	public static byte[] makeMAC(byte[] bytes, SecretKey key) 
	throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(MD5);

        messageDigest.update(bytes);
        byte[] digest = messageDigest.digest();

        Cipher cipher = Cipher.getInstance(CYPHER_MODE);

        // encrypt the plaintext using the key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherDigest = cipher.doFinal(digest);

        return cipherDigest;
    }
	
	
	
}
