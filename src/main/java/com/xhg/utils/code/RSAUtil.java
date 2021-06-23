package com.xhg.utils.code;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RSAUtil {

    private static String KEY_ALGORITHM = "RSA";
    private static String PUBLIC_KEY = "RSAPublicKey";
    private static String PRIVATE_KEY = "RSAPrivateKey";

    private static  final String PRIVATE_KEY_USE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALxaMLYStjmaRg3m3ZLQqvJYC/CxZyB7w9XuHqwHQiR7Yn1ftXfQeaYME/e1/syUt2fTB3Yh60mP0LPWFZdlU6DVVyFmQ8agPe0ooMtCn/Otuocp4Y2HxB/pYpY70iCKUMYMGOAaW57ichFuIDj9CR9PaLEcLxDFgizZU6WMK5JxAgMBAAECgYABlt08XBIPkF6w9Va/S2V14ApRwJ13J7QyVO0LVJBZUHU3S5xzY13zabytZWq9/S9DAAPbGlQq7by8F71c5qz69bqKpGMzx5sWMeEwFHx8fZFUgDax36THxl/SkrLIiWAown29PYOTxjyBVJ2sU/VMSX9xBES8Fw54SzdA703llQJBAOysrNPwVzxnEkfX4V4/vch+MR+/jwSJ9W7sC9WjN4RnxJAxbcR/rXcU8RFc087HjTu0gtm4sWdubUuOALLRqjMCQQDLu2l+ffJ85jlUzY/Fj3CpmdDtVzDQslV1jqMn/FeW8qom9P7y4HBZLhutT7BYjk2obgpJDvkoHjvDrm2Ai/TLAkBu0CaXkRd3S5culjCKLXQRlKwxfkJbULDev5yG6cXLs74/+TS45UL115NLmtf9IEfLZahCgoxlrjl0P8ep8isrAkBaKEOVEJNgplk0qAs5uDJ5O3JztaQKlwCul0KojUkNqbGWr9CrFpthO8BPv/YgGklPgfLXReMI1+hGvkgDHOxlAkEA1f5PqmQMGmdrRh4klk2ThLOpentoA9iYwo82h8oRJ/Eu1MggH7iG+pf5Zkcx1EDEk+QPrXIWjFPwPurc1K8YdA==";
    private static  final String PUBLIC_KEY_USE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8WjC2ErY5mkYN5t2S0KryWAvwsWcge8PV7h6sB0Ike2J9X7V30HmmDBP3tf7MlLdn0wd2IetJj9Cz1hWXZVOg1VchZkPGoD3tKKDLQp/zrbqHKeGNh8Qf6WKWO9IgilDGDBjgGlue4nIRbiA4/QkfT2ixHC8QxYIs2VOljCuScQIDAQAB";

    /**
     * 根据keyMap获取公钥字符串
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 根据keyMap获取私钥字符串
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化秘钥
     */
    public static Map<String, Object> initKey() throws NoSuchAlgorithmException {
        //获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, rsaPublicKey);
        keyMap.put(PRIVATE_KEY, rsaPrivateKey);
        return keyMap;
    }

    /**
     * 将base64编码后的公钥字符串转成PublicKey实例
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将base64编码后的私钥字符串转成PrivateKey实例
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 公钥加密
     */
    private static String encrypt(String content, String public_key) throws Exception {
        PublicKey publicKey = getPublicKey(public_key);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String CHARSET_NAME = "UTF-8";
        byte[] result = cipher.doFinal(content.getBytes(CHARSET_NAME));
        return org.apache.commons.codec.binary.Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     */
    private static String decrypt(String content, String private_key) throws Exception {
        byte[] decodeContent = org.apache.commons.codec.binary.Base64.decodeBase64(content);
        PrivateKey privateKey = getPrivateKey(private_key);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(decodeContent);
        return new String(result);
    }


    /**
     * 编码返回字符串
     */
    private static String encryptBASE64(byte[] key) throws Exception {
        return Base64.getEncoder().encodeToString(key);
    }


    /**
     * 使用RSA签名
     */
    public static String signWithRSA(Map<String, String> paramsMap, String privateKey) throws Exception {
        String content = formatSignContent(paramsMap);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(getPrivateKey(privateKey));
        signature.update(content.getBytes("utf-8"));
        byte[] signed = signature.sign();
        return encryptBASE64(signed);
    }

    /**
     * 使用RSA验签
     */
    public static boolean checkSignWithRSA(Map<String, String> paramsMap, String publicKey, String sign) throws Exception {
        String content = formatSignContent(paramsMap);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(getPublicKey(publicKey));
        signature.update(content.getBytes("utf-8"));
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * 格式化map
     */
    private static String formatSignContent(Map<String, String> params) {
        Map sortedMap = sortMap(params);
        StringBuilder content = new StringBuilder();
        int index = 0;
        for (Object key : sortedMap.keySet()) {
            Object value = sortedMap.get(key.toString());
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                content.append(index == 0 ? "" : "&").append(key).append("=").append(value);
                index++;
            }
        }
        return content.toString();
    }

    /**
     * map 排序
     */
    private static Map sortMap(Map<String, String> map) {
        Map<String, String> sortedParams = new TreeMap<String, String>();
        sortedParams.putAll(map);
        return sortedParams;
    }


    public static void main(String[] args) throws Exception {
//        Map<String, Object> keyMap = initKey();
//        String publicKey = getPublicKey(keyMap);
//        String privateKey = getPrivateKey(keyMap);
//        String content = "hello world";
//        String encryptContent = encrypt(content, publicKey);
//        String decryptContent = decrypt(encryptContent, privateKey);
//        System.out.println("生成的公钥：" + publicKey);
//        System.out.println("生成的私钥：" + privateKey);
//        System.out.println("加密后的数据：" + encryptContent);
//        System.out.println("解密后的数据：" + decryptContent);

        Map<String, String> signMap = new HashMap<>();
        signMap.put("version", "1.0");
        signMap.put("apiId", "10086");
        signMap.put("sid", "6423a1ef-a163-4769-aca8-a5fcd238342f");
        signMap.put("bizParams", "\\\"uniscid\\\":\\\"911000001000013428\\\",\\\"authBookUrl\\\":\\\"http://36.7.144.246:28080/group1/M00/10/35/ooYBAGC_WCOAWczEAAQrTT8V_p4342.pdf\\\"}");

        String USER_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALxaMLYStjmaRg3m3ZLQqvJYC/CxZyB7w9XuHqwHQiR7Yn1ftXfQeaYME/e1/syUt2fTB3Yh60mP0LPWFZdlU6DVVyFmQ8agPe0ooMtCn/Otuocp4Y2HxB/pYpY70iCKUMYMGOAaW57ichFuIDj9CR9PaLEcLxDFgizZU6WMK5JxAgMBAAECgYABlt08XBIPkF6w9Va/S2V14ApRwJ13J7QyVO0LVJBZUHU3S5xzY13zabytZWq9/S9DAAPbGlQq7by8F71c5qz69bqKpGMzx5sWMeEwFHx8fZFUgDax36THxl/SkrLIiWAown29PYOTxjyBVJ2sU/VMSX9xBES8Fw54SzdA703llQJBAOysrNPwVzxnEkfX4V4/vch+MR+/jwSJ9W7sC9WjN4RnxJAxbcR/rXcU8RFc087HjTu0gtm4sWdubUuOALLRqjMCQQDLu2l+ffJ85jlUzY/Fj3CpmdDtVzDQslV1jqMn/FeW8qom9P7y4HBZLhutT7BYjk2obgpJDvkoHjvDrm2Ai/TLAkBu0CaXkRd3S5culjCKLXQRlKwxfkJbULDev5yG6cXLs74/+TS45UL115NLmtf9IEfLZahCgoxlrjl0P8ep8isrAkBaKEOVEJNgplk0qAs5uDJ5O3JztaQKlwCul0KojUkNqbGWr9CrFpthO8BPv/YgGklPgfLXReMI1+hGvkgDHOxlAkEA1f5PqmQMGmdrRh4klk2ThLOpentoA9iYwo82h8oRJ/Eu1MggH7iG+pf5Zkcx1EDEk+QPrXIWjFPwPurc1K8YdA==";
        String USER_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8WjC2ErY5mkYN5t2S0KryWAvwsWcge8PV7h6sB0Ike2J9X7V30HmmDBP3tf7MlLdn0wd2IetJj9Cz1hWXZVOg1VchZkPGoD3tKKDLQp/zrbqHKeGNh8Qf6WKWO9IgilDGDBjgGlue4nIRbiA4/QkfT2ixHC8QxYIs2VOljCuScQIDAQAB";

        String sign = signWithRSA(signMap, USER_PRIVATE_KEY);

        boolean check = checkSignWithRSA(signMap, USER_PUBLIC_KEY, sign);
        System.out.println("签名结果，sign:" + sign);
        System.out.println("验签结果，result:" + check);
    }

}
