package util.kerberos;

import java.io.UnsupportedEncodingException;
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
	 * getKeyFromBytes -
	 */
	public static Key getKeyFromBytes(byte[] bytesKey) throws InvalidKeySpecException, 
			NoSuchAlgorithmException, InvalidKeyException {
		
		DESKeySpec keySpec = new DESKeySpec(bytesKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		return keyFactory.generateSecret(keySpec);
	}
	
	/*
	 * generateSymKey -
	 */
	public static Key generateSymKey(String algorithm,int keysize) 
			throws NoSuchAlgorithmException{
		
		KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
		keyGen.init(keysize);
		return keyGen.generateKey();
	}
	
	/*
	 * cipherText - 
	 */
	public static byte[] cipherText(Key key,byte[] text) 
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, 
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(text);
	}
	
	/*
	 * 
	 */
	public static byte[] decipherText(Key key,byte[] text) 
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, 
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(text);
	}
	
	public static void createAuthPackage(){
		
	}
	
	
}
