package com.xhg.utils.code;

import org.apache.commons.lang3.StringUtils;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * 加签
 */

public class SignUtil {

    public static String signWithRSA2(Map<String, String> paramsMap, String privateKey)throws Exception {
        Map sortedMap = sortMap(paramsMap);
        StringBuilder content =new StringBuilder();
        int index = 0;
        for (Object key :sortedMap.keySet()) {
            Object value = sortedMap.get(key.toString());
            if (value !=null && StringUtils.isNotBlank(value.toString())) {
                content.append(index == 0 ?"" :"&").append(key).append("=").append(value);
                index++;
            }
        }
        Signature signature = Signature.getInstance("SHA1WithRSA");
        byte[]keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec =new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        keyFactory.generatePrivate(keySpec);
        signature.initSign(keyFactory.generatePrivate(keySpec));
        signature.update(content.toString().getBytes("utf-8"));
        byte[]signed =signature.sign();
        return Base64.getEncoder().encodeToString(signed);
    }

    public static Map sortMap(Map<String, String> parameMap){
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.putAll(parameMap);
        return treeMap;
    }



}
