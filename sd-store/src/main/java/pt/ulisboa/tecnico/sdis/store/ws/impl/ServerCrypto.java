package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class ServerCrypto{
	
	public final byte[] makeMAC(byte[] bytes, SecretKey key) throws Exception {

        // get a message digest object using the MD5 algorithm
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        // calculate the digest and print it out
        messageDigest.update(bytes);
        byte[] digest = messageDigest.digest();

        // get a DES cipher object
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

        // encrypt the plaintext using the key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherDigest = cipher.doFinal(digest);

        return cipherDigest;
    }


    /* auxiliary method to calculate new digest from text and compare it to the
         to deciphered digest */
    public final boolean verifyMAC(byte[] cipherDigest,
    								byte[] bytes,
                                    SecretKey key) throws Exception {

        // get a message digest object using the MD5 algorithm
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        // calculate the digest and print it out
        messageDigest.update(bytes);
        byte[] digest = messageDigest.digest();

        // get a DES cipher object
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

        // decrypt the ciphered digest using the public key
        cipher.init(Cipher.DECRYPT_MODE, key);
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