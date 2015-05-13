package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class ServerCrypto{
	
	public final boolean verifyMAC(byte[] cipherDigest, byte[] bytes, SecretKey secretKey) throws Exception {

        // get a message digest object using the MD5 algorithm
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

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
        
        return true;

    }
}