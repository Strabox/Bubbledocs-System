package util.kerberos;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import util.kerberos.exception.KerberosException;

public class Kerberos {
	
	public final static String MD5 = "MD5";
	
	public final static String AES = "AES";
	
	public final static String DES = "DES";
	
	public final static String DES_MODE = "DES/ECB/PKCS5Padding";
	
	public final static String AES_MODE = "AES/ECB/PKCS5Padding";
	
	public final static String RNG = "SHA1PRNG";
	
	/**
	 * Digest password, make a hash from a password, using
	 * a non reversible function.
	 * @param password
	 * @param alg
	 * @return
	 * @throws KerberosException
	 */
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
	 * Transform byte[] in a Key to use.
	 * @param bytesKey
	 * @return Key
	 * @throws KerberosException
	 */
	public static Key getKeyFromBytes(byte[] bytesKey) 
	throws KerberosException{
		try{
			return new SecretKeySpec(bytesKey, AES);
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
	 * Generate an AES (128 bits) Key, for kerberos.
	 * @return Key
	 * @throws KerberosException
	 */
	public static Key generateKerberosKey() 
	throws KerberosException{
		try{
			KeyGenerator keyGen = KeyGenerator.getInstance(AES);
			keyGen.init(128);
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
			Cipher cipher = Cipher.getInstance(AES_MODE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(text);
		}catch(Exception e){
			throw new KerberosException();
		}
	}
	
	/**
	 * Decipher a text using the given key.
	 * @param key used to decipher message.
	 * @param text to be decyphered.
	 * @return return plain message.
	 * @throws KerberosException
	 */
	public static byte[] decipherText(Key key,byte[] text) 
	throws KerberosException {
		try{
			Cipher cipher = Cipher.getInstance(AES_MODE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(text);
		}catch(Exception e){
			throw new KerberosException();
		}
	}
	
	/**
	 * Generate a random number/string.
	 * @return random 16 byte array in base64 to use in XML.
	 * @throws KerberosException
	 */
	public static String generateRandomNumber() 
	throws KerberosException {
		try{
			SecureRandom sr = SecureRandom.getInstance(RNG);
			final byte[] bytes = new byte[16];
			sr.nextBytes(bytes);
			return DatatypeConverter.printBase64Binary(bytes);
		}catch(Exception e){
			throw new KerberosException();
		}
	}
	
	/**
	 * Make MAC (Message Authentication Code).
	 * @param bytes plain text
	 * @param key used to cypher
	 * @return Cyphered MAC signature.
	 * @throws KerberosException
	 */
	public static byte[] makeMAC(byte[] bytes, SecretKey key) 
	throws KerberosException {
		try{
	        Mac cipher = Mac.getInstance("HmacMD5");
	    	cipher.init(key);
	        return cipher.doFinal(bytes);
		}catch(Exception e){
			throw new KerberosException();
		}
    }
	
	
	
}
