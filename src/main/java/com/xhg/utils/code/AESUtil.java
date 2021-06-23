package com.xhg.utils.code;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;

public class AESUtil {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static final String CHARSET = "utf-8";
    //密钥算法
    private static final String KEY_ALGORITHM = "AES";
    //填充模式
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES";

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static byte[] encrypt(String content, String password) {
        try {
            // 生成加密秘钥
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), KEY_ALGORITHM);
            // 创建密码器加密方式为AES加密
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 设置字符集
            byte[] byteContent = content.getBytes(CHARSET);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] result = cipher.doFinal(byteContent);

            return result;

        } catch (Exception ex) {
            logger.info("AES:加密失败");
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static byte[] decrypt(String content, String password) {

        try {
            // 生成解密秘钥
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), KEY_ALGORITHM);
            // 实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 解密
            byte[] result = cipher.doFinal(parseHexStr2Byte(content));

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("AES:解密失败");
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            //AES 要求密钥长度为 128
            kg.init(128, random);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            logger.info("AES密钥生成失败");
        }
        return null;
    }

    /**
     * 将2进制转为16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]){

        StringBuffer stringBuffer = new StringBuffer();

        for(int i = 0; i < buf.length; i++){
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if(hex.length() == 1){
                hex = '0' + hex;
            }
            stringBuffer.append(hex.toUpperCase());
        }

        return stringBuffer.toString();
    }

    /**
     * 将16进制转为2进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr){

        if(hexStr.length() < 1) return new byte[]{};
        byte[] result = new byte[hexStr.length()/2];
        for(int i = 0; i < hexStr.length()/2; i++){
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low  = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
