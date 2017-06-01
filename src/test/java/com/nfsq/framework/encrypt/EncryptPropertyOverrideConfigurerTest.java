package com.nfsq.framework.encrypt;

import org.junit.Test;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by guoyongzheng on 14/12/26.
 */
public class EncryptPropertyOverrideConfigurerTest {

    @Test
    public void testDecrypt() throws Exception {
        String encryptPwd = EncryptPropertyOverrideConfigurer.encrypt("testDataSource", "testDataSource", "testDataSource");

        EncryptPropertyOverrideConfigurer c = new EncryptPropertyOverrideConfigurer();

        Properties p = new Properties();
        p.setProperty("testDataSource.username", "testDataSource");
        p.setProperty("testDataSource.password", encryptPwd);

        c.convertProperties(p);

        String user = c.convertProperty("testDataSource.username", "testDataSource");
        assertEquals("testDataSource", user);

        String pwd = c.convertProperty("testDataSource.password", encryptPwd);
        assertEquals("testDataSource", pwd);
    }

    @Test
    public void testPig() throws Exception {
        String s = "别干坏事:)";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append("\\u");
            sb.append(s.codePointAt(i));
        }

        StringBuilder sb1 = new StringBuilder();
        String[] arr = sb.toString().split("\\\\u");
        for (String a : arr) {
            if (a != null && !a.equals("")) {
                char[] cArray = Character.toChars(Integer.valueOf(a));
                if (cArray.length > 0) {
                    sb1.append(cArray[0]);
                }
            }
        }

        assertEquals(s, sb1.toString());
    }

    @Test
    public void testEAndD() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        char[] password = "\\u21035\\u24178\\u22351\\u20107\\u58\\u41".toCharArray();
        SecureRandom r = new SecureRandom();
        byte[] salt = r.generateSeed(8);

        /* Derive the key, given password and salt. */
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        /* Encrypt the message. */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] ciphertext = cipher.doFinal("test".getBytes("UTF-8"));

        String ivs = Base64.getEncoder().encodeToString(iv);
        String ciphert = Base64.getEncoder().encodeToString(ciphertext);
        String salts = Base64.getEncoder().encodeToString(salt);

        byte[] salt1 = Base64.getDecoder().decode(salts);
        byte[] iv1 = Base64.getDecoder().decode(ivs);
        byte[] ciphert1 = Base64.getDecoder().decode(ciphert);
        /* Derive the key, given password and salt. */
        SecretKeyFactory factory1 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec1 = new PBEKeySpec(password, salt1, 65536, 128);
        SecretKey tmp1 = factory1.generateSecret(spec1);
        SecretKey secret1 = new SecretKeySpec(tmp1.getEncoded(), "AES");
        /* Decrypt the message, given derived key and initialization vector. */
        Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher1.init(Cipher.DECRYPT_MODE, secret1, new IvParameterSpec(iv1));
        String plaintext = new String(cipher1.doFinal(ciphert1), "UTF-8");

        assertEquals("test", plaintext);
    }

    @Test
    public void make() throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidParameterSpecException, InvalidKeyException, InvalidKeySpecException {
        //String s = EncryptPropertyOverrideConfigurer.encrypt("xpscontract", "xpscontract", "nfxsDataSource");
        //数据库密码,用户名,数据源名
        String s = EncryptPropertyOverrideConfigurer.encrypt("ystims2016test", "osap", "imsDataSource");
        System.out.println(s);
        assertNotNull(s);
    }
}
