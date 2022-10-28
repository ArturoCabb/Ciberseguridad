package com.example.myapplication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class CifradoDes {

    public CifradoDes() {

    }

    public String encryptForDES(String souce, String key) throws UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {

        SecureRandom sr = new SecureRandom();
        DESKeySpec dks;
        //try {
        dks = new DESKeySpec(key.getBytes("UTF-8"));
        //}catch(InvalidKeyException e){
        //	throw new RuntimeException(e);
        //}

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key1 = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key1, sr);
        byte encryptedData[] = cipher.doFinal(souce.getBytes("UTF-8"));
        String base64Str = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            base64Str = Base64.getEncoder().encodeToString(encryptedData);
        }

        return base64Str;
    }


    public String decryptForDES(String souce, String key) throws UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,InvalidKeyException,NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IOException,IllegalBlockSizeException, BadPaddingException
    {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key1 = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key1, sr);
        byte[] encryptedData = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encryptedData = Base64.getDecoder().decode(souce);
        }
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return new String(decryptedData,"UTF-8");
    }
}
