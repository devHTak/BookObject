package com.review.nw.chap08;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SymmetricKey {

    public static String encrypt(String plainText, SecretKey secretKey) {
        String encryptText = null;
        try {
            Cipher cipher = Cipher.getInstance("AES"); // Cipher 클래스는 암호화 과정을 위한 프레임워크 제공하며 ENCRYPT_MODE 사용
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptBytes = cipher.doFinal(plainText.getBytes());

            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(encryptBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return encryptText;
    }
    public static String decrypt(String encryptedText, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            Base64.Decoder decoder = Base64.getDecoder();
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(decoder.decode(encryptedText));
            return new String(decryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
