package com.nfsq.framework.encrypt;

import org.springframework.beans.factory.config.PropertyOverrideConfigurer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 用于加密数据库密码的工具类
 * Created by guoyongzheng on 14/12/23.
 */
public class EncryptPropertyOverrideConfigurer extends PropertyOverrideConfigurer {

    //加密key
    private final Map<String, String> keyMap = new HashMap<>();

    // prefix
    private static final String PREFIX = "nfsq";

    /**
     * Convert the given property from the properties source to the value
     * which should be applied.
     * <p>The default implementation calls {@link #convertPropertyValue(String)}.
     *
     * @param propertyName  the name of the property that the value is defined for
     * @param propertyValue the original value from the properties source
     * @return the converted value, to be used for processing
     * @see #convertPropertyValue(String)
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {

//        if (propertyName.endsWith("DataSource.username")) {
//            //如果propertyName是以xxxDataSource.username结尾的，那么将前半部分塞到keymap中去
//            try {
//                fillKeyMap(propertyName, propertyValue);
//            } catch (NoSuchAlgorithmException e) {
//                logger.error(e.getMessage(), e);
//                throw new IllegalStateException(e);
//            }
//            return propertyValue;
//        } else
        if (propertyName.endsWith("DataSource.password")) {
            //如果propertyName是以xxxDataSource.password结尾的，那么进行解密
            return decrypt(propertyName, propertyValue);
        }

        return super.convertProperty(propertyName, propertyValue);
    }

    @Override
    protected void convertProperties(Properties props) {
        Enumeration<?> propertyNames = props.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = (String)propertyNames.nextElement();
            if (key.endsWith("DataSource.username")) {
                //如果propertyName是以xxxDataSource.username结尾的，那么将前半部分塞到keymap中去
                try {
                    fillKeyMap(key, props.getProperty(key));
                } catch (NoSuchAlgorithmException e) {
                    logger.error(e.getMessage(), e);
                    throw new IllegalStateException(e);
                }
            }
        }

        //不能忘了调用super方法
        super.convertProperties(props);
    }

    /**
     * 生成加密字符串，填到keyMap中
     *
     * @param placeholder
     * @param value
     */
    private void fillKeyMap(String placeholder, String value) throws NoSuchAlgorithmException {
        String passwordsPlaceholder = placeholder.substring(0, placeholder.indexOf("."));
        String key = buildKey(passwordsPlaceholder, value);
        this.keyMap.put(passwordsPlaceholder, key);
    }

    /**
     * 生成加密解密字符串
     *
     * @param placeholder
     * @param value
     * @return
     */
    private String buildKey(String placeholder, String value) throws NoSuchAlgorithmException {
        String keyStr = PREFIX + Math.PI + placeholder + Math.E + value;
        return keyStr;
    }

    /**
     * 解密
     *
     * @param a
     * @param b
     * @return
     */
    private String decrypt(String a, String b) {
        String c = "";
        String d = this.keyMap.get(a.substring(0, a.indexOf(".")));
        if (d != null) {
            String[] e = b.split("\\|");
            if (e.length != 3) {
                throw new IllegalStateException("cipherPassword 长度不对！ length = " + e.length);
            }
            String f = e[0];
            String g = e[1];
            String h = e[2];
            try {
                byte[] i = Base64.getDecoder().decode(g);
                byte[] j = Base64.getDecoder().decode(h);
                byte[] k = Base64.getDecoder().decode(f);
                SecretKeyFactory l = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                KeySpec m = new PBEKeySpec(d.toCharArray(), i, 65536, 128);
                SecretKey n = l.generateSecret(m);
                SecretKey o = new SecretKeySpec(n.getEncoded(), "AES");
                Cipher p = Cipher.getInstance("AES/CBC/PKCS5Padding");
                p.init(Cipher.DECRYPT_MODE, o, new IvParameterSpec(j));
                String q = new String(p.doFinal(k), "UTF-8");
                c = q;
            } catch (NoSuchPaddingException | BadPaddingException | InvalidAlgorithmParameterException |
                    NoSuchAlgorithmException | IllegalBlockSizeException | UnsupportedEncodingException |
                    InvalidKeyException | InvalidKeySpecException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return c;
    }

    //placeholder, user, origin >> reverse
    public static String encrypt(String a, String b, String c) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, InvalidParameterSpecException, UnsupportedEncodingException {
        String e = PREFIX + Math.PI + c + Math.E + b;
        char[] f = e.toCharArray();
        SecureRandom g = new SecureRandom();
        byte[] h = g.generateSeed(8);
        SecretKeyFactory i = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec j = new PBEKeySpec(f, h, 65536, 128);
        SecretKey k = i.generateSecret(j);
        SecretKey l = new SecretKeySpec(k.getEncoded(), "AES");
        Cipher m = Cipher.getInstance("AES/CBC/PKCS5Padding");
        m.init(Cipher.ENCRYPT_MODE, l);
        AlgorithmParameters n = m.getParameters();
        byte[] o = n.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] p = m.doFinal(a.getBytes("UTF-8"));
        String q = Base64.getEncoder().encodeToString(o);
        String r = Base64.getEncoder().encodeToString(p);
        String s = Base64.getEncoder().encodeToString(h);
        return r + "|" + s + "|" + q;
    }

}

