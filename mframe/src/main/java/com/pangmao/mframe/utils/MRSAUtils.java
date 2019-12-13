package com.pangmao.mframe.utils;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import cn.hutool.core.codec.Base64;

/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 * 
 * @author IceWee
 * @date 2012-4-26
 * @version 1.0
 */
public class MRSAUtils {

    /**
     * 加密算法RSA
     */
    private static String KEY_ALGORITHM = "RSA";

    public static String getKeyAlgorithm() {
        return KEY_ALGORITHM;
    }

    public static void setKeyAlgorithm(String keyAlgorithm) {
        KEY_ALGORITHM = keyAlgorithm;
    }

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     * 
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }



    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     * 
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * 
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encode(signature.sign());
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     * 
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     * 
     * @return
     * @throws Exception
     * 
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decode(sign));
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     * 
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
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

//    public static byte[] decryptByPrivateKey(byte[] encryptedData) throws Exception {
//        String pr="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI4ceEjvVYk+ThqZ1FNYztB6ux7y"+
//                "5kVqkcZ8UJHgf2N+G9bXfT+WL8ChzOYQkDlhqmW5Bb1iBqEc9f9h08CxWH8V+FHc5DQYRp33bDyO"+
//                "DoVkGtFpk6+hShWgB3Rl9aiMC7wPfQde+61gi9iQD4wXMIyYK/qQEP+nkc8maLP/VL7FAgMBAAEC"+
//                "gYAK1ebiAMWv2j1QT+xdkobqZIFCAdlG77I/xRjLlqd/6Yyr2/Bj2cq4UF30KM0mYukAKpRidUf2"+
//                "HuQu3cGsjpklsgVNOOTEthgHaBcOaeueGwUbWKPep4kpGtcR0Fl54WtzhZT0Q8XAs3sR3aKsXXcW"+
//                "O9w3XJNxOqOStA1q/+J/oQJBAOEmLSUI9Pz0Sc2GkHSFithYY3JGkI/r34GruZD5GeThvHl8ATfR"+
//                "10BeNqUd63ovlIuWP3SJ1gyh74v2TeXSOg0CQQChlXlKRjyNX+EX5DZFLps5Xp0CQOnRfiWFESfU"+
//                "W1+g5Zu8TNp3mEQA8HH7GutaEsLs89SUcUwHMpwMlS6MzgGZAkBxkbI/9i6t0gPQWkpPnHUXAC7Z"+
//                "m+Kb1l0dqaOnDyASphNOLFDo+T5cx6lEzLzVG18Qsi8797MzGBQfQ5SzksGZAkEAlLQz+HflGBJp"+
//                "3AvpKgf3UHfTNRS6WlKmguzD4/nnavKEPWnZEIEv7FFcBETE3ZMwktLfC3GtD16zIB1Wxrx72QJA"+
//                "AOxpsf1duy/jTLTIEYXLE8a+b0e5DXUmd8FDMB9JOFL6yBVEsIrV6M1BZj/3dCYXwwzP5OQa4p7B"+
//                "JIbgybgxCg==";
//        return decryptByPrivateKey(encryptedData, "");
//    }
    /**
     * <p>
     * 公钥解密
     * </p>
     * 
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
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
     * <p>
     * 公钥加密
     * </p>
     * 
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

//    public static byte[] encryptByPublicKey(byte[] data) throws Exception {
//        String pu="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOHHhI71WJPk4amdRTWM7Qerse8uZFapHGfFCR"+
//                "4H9jfhvW130/li/AoczmEJA5YapluQW9YgahHPX/YdPAsVh/FfhR3OQ0GEad92w8jg6FZBrRaZOv"+
//                "oUoVoAd0ZfWojAu8D30HXvutYIvYkA+MFzCMmCv6kBD/p5HPJmiz/1S+xQIDAQAB";
//        return encryptByPublicKey(data, pu);
//    }

    /**
     * <p>
     * 私钥加密
     * </p>
     * 
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     * 
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encode(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     * 
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encode(key.getEncoded());
    }
    
    public static void main(String[] args) {
		String s = "10000000000000592001";
		String pu="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOHHhI71WJPk4amdRTWM7Qerse8uZFapHGfFCR"+
"4H9jfhvW130/li/AoczmEJA5YapluQW9YgahHPX/YdPAsVh/FfhR3OQ0GEad92w8jg6FZBrRaZOv"+
"oUoVoAd0ZfWojAu8D30HXvutYIvYkA+MFzCMmCv6kBD/p5HPJmiz/1S+xQIDAQAB";
		String pr="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI4ceEjvVYk+ThqZ1FNYztB6ux7y"+
"5kVqkcZ8UJHgf2N+G9bXfT+WL8ChzOYQkDlhqmW5Bb1iBqEc9f9h08CxWH8V+FHc5DQYRp33bDyO"+
"DoVkGtFpk6+hShWgB3Rl9aiMC7wPfQde+61gi9iQD4wXMIyYK/qQEP+nkc8maLP/VL7FAgMBAAEC"+
"gYAK1ebiAMWv2j1QT+xdkobqZIFCAdlG77I/xRjLlqd/6Yyr2/Bj2cq4UF30KM0mYukAKpRidUf2"+
"HuQu3cGsjpklsgVNOOTEthgHaBcOaeueGwUbWKPep4kpGtcR0Fl54WtzhZT0Q8XAs3sR3aKsXXcW"+
"O9w3XJNxOqOStA1q/+J/oQJBAOEmLSUI9Pz0Sc2GkHSFithYY3JGkI/r34GruZD5GeThvHl8ATfR"+
"10BeNqUd63ovlIuWP3SJ1gyh74v2TeXSOg0CQQChlXlKRjyNX+EX5DZFLps5Xp0CQOnRfiWFESfU"+
"W1+g5Zu8TNp3mEQA8HH7GutaEsLs89SUcUwHMpwMlS6MzgGZAkBxkbI/9i6t0gPQWkpPnHUXAC7Z"+
"m+Kb1l0dqaOnDyASphNOLFDo+T5cx6lEzLzVG18Qsi8797MzGBQfQ5SzksGZAkEAlLQz+HflGBJp"+
"3AvpKgf3UHfTNRS6WlKmguzD4/nnavKEPWnZEIEv7FFcBETE3ZMwktLfC3GtD16zIB1Wxrx72QJA"+
"AOxpsf1duy/jTLTIEYXLE8a+b0e5DXUmd8FDMB9JOFL6yBVEsIrV6M1BZj/3dCYXwwzP5OQa4p7B"+
"JIbgybgxCg==";
		try {
			String s2 = Base64.encode(encryptByPublicKey(s.getBytes(), pu));
			s2 = replaceBlank(s2);
            System.out.println(""+s2);
			MFileUtils.write("APP_ID=" + s2, "c:\\", "icbc.properties");
//			System.out.println(s2);
//			s2 = "XGJkyJWJVT4faVjGdV2v3qAif6smbXIIjE5+FmeVCUAU2akaL1umBKyO9o1AdmmCnYPqKPzr1xuHjy/CqqRLPbs/+o1ReuOKc7Px0M3H309qx5pmfJrUjr9NTr9HSJJscttC9r8H4hM3rkeNZPAIVcZ4isod1uozyt3UNTEHCFM=";
//			String s2 = "WSMokUTjSuE5wmdY01n8zsMQGpdUgJKFAMAkWypQRSKRQEFkZIlVUTsLWKEOPK6CiemfJXKJV9Sgj8D2XDS73M4O1eru9a9meCh6ixETFuR0a2GWXDMm2wW2adgaYmrFLC6f6vEiFTOn8MBQCppgB7KRPJZIO1gNw+UDZbotcBM=";
//            System.out.println(s2);
			System.out.println(new String(decryptByPrivateKey(Base64.decode(s2), pr)));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
