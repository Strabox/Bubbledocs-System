package pt.ulisboa.tecnico.sdis.store.cli;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Crypto{
	
	private final SecretKey secretKey;
	private byte[] message;
	
	private final Cipher cipher;
	
	
	public Crypto() throws NoSuchAlgorithmException, NoSuchPaddingException{
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
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
	
	public final byte[] makeMAC(byte[] bytes) throws Exception {

        // get a message digest object using the MD5 algorithm
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        // calculate the digest and print it out
        messageDigest.update(bytes);
        byte[] digest = messageDigest.digest();

        // encrypt the plaintext using the key
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherDigest = cipher.doFinal(digest);

        return cipherDigest;
    }


    public final boolean verifyMAC(byte[] cipherDigest, byte[] bytes) throws Exception {

        // get a message digest object using the MD5 algorithm
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        // calculate the digest and print it out
        messageDigest.update(bytes);
        byte[] digest = messageDigest.digest();

        // decrypt the ciphered digest using the public key
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decipheredDigest = cipher.doFinal(cipherDigest);

        // compare digests
        if (digest.length != decipheredDigest.length)
            return false;

        for (int i=0; i < digest.length; i++)
            if (digest[i] != decipheredDigest[i])
                return false;
        
        message = decipheredDigest;
        return true;

    }

	public SecretKey getSecretKey() {
		return secretKey;
	}

	public byte[] getMessage() {
		return message;
	}
}