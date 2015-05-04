package sdis.cli;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class Crypto{
	
	private final Key secretKey;
	private final Cipher cipher;
	
	
	public Crypto() throws NoSuchAlgorithmException, NoSuchPaddingException{
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(64);
        secretKey = keyGen.generateKey();
        cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	}
	
	
	public byte[] encrypt(byte[] file){
		byte[] cipherFile = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			cipherFile = cipher.doFinal(file);
		} catch (InvalidKeyException e) {
			System.out.println("Key is invalid");
		} catch (IllegalBlockSizeException e) {
			System.out.println("Illegal Block Size");
		} catch (BadPaddingException e) {
			System.out.println("Bad Padding Exception");
		}
        
		return cipherFile;
	}
	
	public byte[] decrypt(byte[] cipherFile){
		byte[] file = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
	        file = cipher.doFinal(cipherFile);
		} catch (InvalidKeyException e) {
			System.out.println("Key is invalid");
		} catch (IllegalBlockSizeException e) {
			System.out.println("Illegal Block Size");
		} catch (BadPaddingException e) {
			System.out.println("Bad Padding Exception");
		}
        
		return file;
	}
}