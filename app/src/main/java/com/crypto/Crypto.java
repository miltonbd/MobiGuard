package com.crypto;

import android.content.Context;

import com.fs.lib.util.MyLogger;
import com.user.UserHelper;
import com.google.common.primitives.Bytes;

import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    private static final String ALGORITHM ="AES";
    private byte[] password;
    private static final int ITERATIONS = 5;
    private static final byte[] keyValue = new byte[]{'T', 'h', 'i', 's', 'I',
            's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    public Crypto(byte[] password) {
        this.password = password;
    }

    public static Crypto getCrypto(Context context) {
        Crypto crypto = null;
        try {
            UserHelper userHelper = UserHelper.getInstance(context);
            MessageDigest md = MessageDigest.getInstance("MD5");
            String password = userHelper.getCurrentUser().getPassword();
            MyLogger.debug("user password is " + password);
            byte[] thedigest = md.digest(password.getBytes());
            crypto = new Crypto(thedigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return crypto;

    }

    public byte[] encrypt(byte[] input) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(password, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input);
        } catch (Exception e) {
            MyLogger.debug(e.getMessage());
        }
        return Base64.encodeBase64(crypted);
    }
    public  byte[] encryptWithSalt(byte[] value, byte[] salt) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] valueToEnc;
        byte[] eValue = value;
        for (int i = 0; i < ITERATIONS; i++) {
            valueToEnc = Bytes.concat( salt , eValue);
            byte[] encValue = c.doFinal(valueToEnc);
            eValue = Base64.encodeBase64(encValue);
        }
        return eValue;
    }


    public byte[] decrypt(byte[] input) {
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(password, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decodeBase64(input));
        } catch (Exception e) {
            MyLogger.debug(e.getMessage());
        }
        return output;
    }

    public static byte[] MD5(String md5) {
        byte[] array = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            array = md.digest(md5.getBytes());
        } catch (NoSuchAlgorithmException e) {
        }
        return array;
    }


    public  byte[] decryptWithSalt(byte[] value, byte[] salt) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] dValue = null;
        byte[] valueToDecrypt = value;
        for (int i = 0; i < ITERATIONS; i++) {
            byte[] decordedValue = Base64.decodeBase64(valueToDecrypt);
            byte[] decValue = c.doFinal(decordedValue);
            dValue = new String(decValue).substring(new String(salt).length()).getBytes();
            valueToDecrypt = dValue;
        }
        return dValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }

}