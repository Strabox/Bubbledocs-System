package util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.parsers.ParserConfigurationException;

public class Kerberos {

	public final static String MD5 = "MD5";
	
	public final static String DES = "DES";
	
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
	 * 
	 */
	public static Key getKeyFromBytes(byte[] bytesKey) throws InvalidKeySpecException, 
			NoSuchAlgorithmException, InvalidKeyException {
		DESKeySpec keySpec = new DESKeySpec(bytesKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		return keyFactory.generateSecret(keySpec);
	}
	
	/*
	 * 
	 */
	public static Key generateSymKey(String algorithm,int keysize) 
			throws NoSuchAlgorithmException{
		KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
		keyGen.init(keysize);
		return keyGen.generateKey();
	}
	
	/*
	 * 
	 */
	public static byte[] cipherText(Key key,String text) 
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, 
			IllegalBlockSizeException, BadPaddingException{
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(text.getBytes());
	}
	
	/*
	 * 
	 */
	public static String createTicket(String client,String server,
		String kcs,String ks) throws ParserConfigurationException{
		String ticketXML, ticketBody;
		ticketBody = "<client>" + client + "</client>";
		ticketXML = "<ticket>" + ticketBody +"</ticket>";
		return ticketXML;
	}
	
}
