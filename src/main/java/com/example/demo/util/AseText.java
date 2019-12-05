package com.example.demo.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;

public class AseText {

    /**
     * 预设Initialization Vector，为16 Bits的0
     */
    private static final IvParameterSpec DEFAULT_IV = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    private static final String ALGORITHM = "AES"; //加密算法ASE

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";//AES使用CBC模式与PKCS5Padding

    private Key key; //密钥

    private IvParameterSpec iv; //AES CBC模式使用的Initialization Vector

    private Cipher cipher; //Cipher 物件

    /**
     * 构造函数，使用256 Bits的AES密钥（计算任意长度密钥的MD5）和预设IV
     * @param key 传入任意长度的AES密钥
     */
    public AseText(final String key) {
        this(key, 256);
    }

    /**
     * 构造函数，使用128 Bits或是256 Bits的AES密钥（计算任意长度密钥的MD5或是SHA256）和预设IV
     *
     * @param key 传入任意长度的AES密钥
     * @param bit 传入AES密钥长度，数值可以是128、256（Bits）
     */
    public AseText(final String key, final int bit) {
        this(key, bit, null);
    }

    /**
     *构造函数，使用128 Bits或是256 Bits的AES密钥（计算任意长度密钥的MD5或是SHA256），用MD5计算IV值
     *
     * @param key 传入任意长度的AES密钥
     * @param bit 传入AES密钥长度，数值可以是128、256（Bits）
     * @param iv 传入任意长度的IV字串
     */
    public AseText(final String key, final int bit, final String iv) {
        if (bit == 128) {
            this.key = new SecretKeySpec(getHash("SHA-256", key), ALGORITHM);
        } else {
            this.key = new SecretKeySpec(getHash("MD5", key), ALGORITHM);
        }
        if (iv != null) {
            this.iv = new IvParameterSpec(getHash("MD5", iv));
        } else {
            this.iv = DEFAULT_IV;
        }

        init();
    }

    /**
     *取得字串的哈希值
     * @param algorithm 传入杂骤算法
     * @param text 传入要哈希的字串
     * @return 传回哈希后数据内容
     */
    private static byte[] getHash(final String algorithm, final String text) {
            try {
                return getHash(algorithm, text.getBytes("UTF-8"));
            } catch (final Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
    }

    /**
     *取得数据的哈希值
     ** @param algorithm 传入杂骤算法
     * @param data 传入要哈希的数据
     * @return 传回哈希后数据内容
     */
    private static byte[] getHash(final String algorithm, final byte[] data) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(data);
            return digest.digest();
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    //初始化
    private void init() {
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     *加密文字
     * @param str 传入要加密的文字
     * @return 传回加密后的文字
     */
    public String encrypt(final String str) {
        try {
            return encrypt(str.getBytes("UTF-8"));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     *加密数据
     * @param data 传入要加密的数据
     * @return 传回加密后的数据
     */
    public String encrypt(final byte[] data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            final byte[] encryptData = cipher.doFinal(data);
            return Base64.encode(encryptData);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     *解密文字
     * @param str 传入要解密的文字
     * @return 传回解密后的文字
     */
    public String decrypt(final String str) {
        try {
            return decrypt(Base64.decode(str));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     *解密文字
     * @param data 传入要解密的数据
     * @return 传回解密后的文字
     */
    public String decrypt(final byte[] data) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            final byte[] decryptData = cipher.doFinal(data);
            return new String(decryptData, "UTF-8");
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
