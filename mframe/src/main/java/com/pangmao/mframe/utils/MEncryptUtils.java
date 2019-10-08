package com.pangmao.mframe.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.pangmao.mframe.utils.MStringUtils;

/**
 * 加密工具类（只实现部分）
 * MD5加密、AES
 * <p>
 * 对称加密常用算法:
 * AES、DES、3DES、TDEA、Blowfish、RC2、RC4、RC5、IDEA、SKIPJACK 等。
 * 非对称加密常用算法:
 * RSA、Elgamal、背包算法、Rabin、D-H、ECC（椭圆曲线加密算法）等，其中支付宝使用的就是RSA算法
 */

public class MEncryptUtils {

    private MEncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 转变
     * <p>法算法名称/加密模式/填充方式</p>
     * <p>加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB</p>
     * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
     */
    public static String AES_MODE = "AES/ECB/PKCS5Padding";
    private static final String AES = "AES";
    private static final String MD5 = "MD5";
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 描述：MD5加密.
     *
     * @param str 要加密的字符串
     * @return String 加密的字符串
     */
    public final static String MD5(String str) {
        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance(MD5);
            mdTemp.update(strTemp);
            byte tmp[] = mdTemp.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char strs[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                strs[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            return new String(strs).toLowerCase(); // 换后的结果转换为字符串
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * AES加密
     *
     * @param content 明文
     * @param pass  秘钥
     * @return 密文
     */
    public static String encryptAES(String content, String pass) throws Exception {
        Cipher aesECB = Cipher.getInstance(AES_MODE);
        SecretKeySpec key = getSecretKeySpec(pass);
        aesECB.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = aesECB.doFinal(content.getBytes());
        return MStringUtils.parseByte2HexStr(result);
    }

    /**
     * AES解密
     *
     * @param content 密文
     * @param pass  秘钥
     * @return 明文
     */
    public static String decryptAES(String content, String pass) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        SecretKeySpec key = getSecretKeySpec(pass);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = MStringUtils.parseHexStr2Byte(content);
        return new String(cipher.doFinal(result));
    }

    /**
     * RSA最大解密密文大小
     * RSA最大加密明文大小
     * 加密算法RSA
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * RSA加密
     *
     * @param encryptedData 明文
     * @param pass  秘钥
     * @return 密文
     */
    public static byte[] encryptRSA(byte[] encryptedData, String pass) throws Exception {

        byte[] keyBytes = Base64.decode(pass, Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * RSA解密
     *
     * @param decryptedData 密文
     * @param pass  秘钥
     * @return 明文
     */
    public static byte[] decryptRSA(byte[] decryptedData, String pass) throws Exception {
        byte[] keyBytes = Base64.decode(pass, Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = decryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(decryptedData, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(decryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }


    private static SecretKeySpec getSecretKeySpec(String key) throws Exception {
        byte[] arrBTmp = key.getBytes();
        byte[] arrB = new byte[16];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        return new SecretKeySpec(arrB, AES);
    }

}
