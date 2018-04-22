package com.prasad.safeboxApp;

/**
 * Created by Prasad on 1/4/17.
 */

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


public class Crypto {

    public static final String algo="AES";
    private byte[] keyValue;


    protected Crypto(String ky)throws Exception{
        String key=ky;
        keyValue=key.getBytes();
    }

    protected String Encrypt(String Data)throws Exception{
        Key key=generateKey();
        Cipher c=Cipher.getInstance(algo);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal=c.doFinal(Data.getBytes());
        String encryptedValue=new BASE64Encoder().encode(encVal);
        return encryptedValue;

    }

    protected String Decrypt(String encData)throws Exception{
        Key key=generateKey();
        Cipher c=Cipher.getInstance(algo);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedValue=new BASE64Decoder().decodeBuffer(encData);
        byte[] decValue=c.doFinal(decodedValue);
        String decryptedValue=new String(decValue);
        return decryptedValue;

    }

    private Key generateKey()throws Exception{
        Key key=new SecretKeySpec(keyValue,algo);
        return key;
    }


}
