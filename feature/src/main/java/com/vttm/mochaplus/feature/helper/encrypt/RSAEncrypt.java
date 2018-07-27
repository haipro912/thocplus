package com.vttm.mochaplus.feature.helper.encrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.vttm.mochaplus.feature.utils.AppConstants;
import com.vttm.mochaplus.feature.utils.Config;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


/**
 * Created by toanvk2 on 01/07/2015.
 */
public class RSAEncrypt {
    private static final String TAG = AESCrypt.class.getSimpleName();
    protected static final String ALGORITHM = "RSA";
    private static RSAEncrypt mInstance;
    private SharedPreferences mPref;
    private PublicKey mPublicKey;

    private RSAEncrypt(Context app) {
        try {
            mPref = app.getSharedPreferences(AppConstants.PREFERENCE.PREF_DIR_NAME, Context.MODE_PRIVATE);
            String key = mPref.getString(AppConstants.PREFERENCE.PREF_PUBLIC_RSA_KEY, Config.RSA_KEY);
            this.mPublicKey = getPublicKeyFromString(key);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    public static synchronized RSAEncrypt getInStance(Context app) {
        if (mInstance == null) {
            mInstance = new RSAEncrypt(app);
        }
        return mInstance;
    }


    private synchronized byte[]  encrypt(Context app, byte[] text) throws Exception {
        if (mPublicKey == null) {
            mInstance = new RSAEncrypt(app);
        }
        byte[] cipherText;
        // get an RSA cipher object and print the provider
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // encrypt the plaintext using the public key
        cipher.init(Cipher.ENCRYPT_MODE, mPublicKey);
        cipherText = cipher.doFinal(text);
        return cipherText;
    }

    /**
     * Encrypt a text using public key. The result is enctypted BASE64 encoded
     * text
     *
     * @param text The original unencrypted text
     * @return Encrypted text encoded as BASE64
     * @throws Exception
     */
    public String encrypt(Context app, String text) {
        try {
            String encryptedText;
            byte[] cipherText = encrypt(app, text.getBytes("UTF8"));
            encryptedText = base64Encode(cipherText);
            return encryptedText;
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public String encryptRFC2045(Context app, String text) {
        try {
            String encryptedText;
            byte[] cipherText = encrypt(app, text.getBytes("UTF8"));
            encryptedText = base64EncodeRFC2045(cipherText);
            return encryptedText;
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    private PublicKey getPublicKeyFromString(String key) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(base64Decode(key));
        return keyFactory.generatePublic(publicKeySpec);
    }

    private String base64Encode(byte[] input) {
        //Log.d(getClass().getSimpleName(), "Base64-encode: " + encoded);
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    private String base64EncodeRFC2045(byte[] input) {
        //Log.d(getClass().getSimpleName(), "Base64-encode: " + encoded);
        return Base64.encodeToString(input, Base64.DEFAULT);
    }

    private byte[] base64Decode(String input, String coding) throws UnsupportedEncodingException {
        //Log.d(getClass().getSimpleName(), "Base64-decode: " + input);
        return Base64.decode(input.getBytes(coding), Base64.NO_WRAP);
    }

    private byte[] base64Decode(String input) {
        //Log.d(getClass().getSimpleName(), "Base64-decode: " + input);
        return Base64.decode(input.getBytes(), Base64.NO_WRAP);
    }
}