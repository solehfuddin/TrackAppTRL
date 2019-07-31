package com.sofudev.trackapptrl.Util;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESencUtil {
    private static final String ALGO = "AES";
    private static final String key = "39814f7f8763acea97a1ed010ac99e0c";

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(String data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        //return Base64.getUrlEncoder().encodeToString(encVal);
        String output = android.util.Base64.encodeToString(encVal, 1);
        return output;
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
//    public static String decrypt(String encryptedData) throws Exception {
//        Key key = generateKey();
//        Cipher c = Cipher.getInstance(ALGO);
//        c.init(Cipher.DECRYPT_MODE, key);
//        byte[] decordedValue = Base64.getUrlDecoder().decode(encryptedData);
//        byte[] decValue = c.doFinal(decordedValue);
//        return new String(decValue);
//    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() throws Exception {
        byte[] keyValue = key.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyValue = sha.digest(keyValue);
        keyValue = Arrays.copyOf(keyValue, 16);
        return new SecretKeySpec(keyValue, ALGO);
    }
}
