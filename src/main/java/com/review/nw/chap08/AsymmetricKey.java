package com.review.nw.chap08;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class AsymmetricKey {
    // 키 생성 및 저장
    public static void main(String[] args) {
        try {
            // 비대칭 암호화를 위해 RSA 알고리즘 사용 및 1024 비트를 사용하도록 지정
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            // 키 생성
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            // 키 저장
            savePrivateKey(privateKey);
            savePublicKey(publicKey);
            // 저장된 키 다시 가져오기
            privateKey = getPrivateKey();
            publicKey = getPublicKey();
            // 키가 올바르게 사용되고 있는지 확인
            String message = "The message";
            System.out.println("Message: " + message);
            byte[] encodeData = encrypt(publicKey, message);
            System.out.println("Decrypted Message: " + decrypt(privateKey, encodeData));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    // 개인키를 활용한 복호화
    private static String decrypt(PrivateKey privateKey, byte[] encodeData) {
        String message = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decodedData = Base64.getDecoder().decode(encodeData);
            message = new String(cipher.doFinal(decodedData));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return message;
    }
    // 공개키를 사용한 암호화
    private static byte[] encrypt(PublicKey publicKey, String message) {
        byte[] encodeData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            encodeData = Base64.getEncoder().withoutPadding().encode(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return encodeData;
    }
    // X590EncodedKeySpec 클래스를 사용하여 public.key 저장
    private static void savePublicKey(PublicKey publicKey) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
            FileOutputStream fos = new FileOutputStream("public.key");
            fos.write(x509EncodedKeySpec.getEncoded());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static PublicKey getPublicKey() {
        try {
            File publicKeyFile = new File("public.key");
            FileInputStream fis = new FileInputStream("public.key");
            byte[] encodedPublicKey = new byte[(int)publicKeyFile.length()];
            fis.read(encodedPublicKey);
            fis.close();

            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
    // PKCS8EncodedKeySpec 클래스(개인키의 인코딩 지원)을 사용하여 private.key 저장
    private static void savePrivateKey(PrivateKey privateKey) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            FileOutputStream fos = new FileOutputStream("private.key");
            fos.write(pkcs8EncodedKeySpec.getEncoded());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // KeyFactory 클래스의 generatePrivate 메소드는 PKCS8EncodedKeySpec 명세를 기반으로 키 생성
    private static PrivateKey getPrivateKey() {
        try {
            File privateKeyFile = new File("private.key");
            FileInputStream fis = new FileInputStream("private.key");

            byte[] encodedPrivateKey = new byte[(int)privateKeyFile.length()];
            fis.read(encodedPrivateKey);
            fis.close();

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
