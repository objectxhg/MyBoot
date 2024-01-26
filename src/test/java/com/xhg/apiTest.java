package com.xhg;


import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xhg.excepetion.BaseException;
import com.xhg.pojo.ApiRequest;
import com.xhg.pojo.ApiResponse;
import com.xhg.utils.code.AESUtil;
import com.xhg.utils.code.RSAUtil;
import com.xhg.utils.code.SignUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyBootApplication.class})
public class apiTest {

    /**
     * 加密
     * @throws Exception
     */

    public void RSAencrypt(String cerno, String authBookUid) throws Exception {

        String appSecrt = "Ng130i56fWq9jth2";

        String appPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2HNuUd6auepKZx2vVbXVBRO9o0Nd0iuXpPmZxVsC4tZ5eVbFgtpbsoPhd2Hf70bKyvKUV8BZSnuZSSTwNxFBpf/1ta0608r0NVExqfzcYHqj24AsBW+ZxtXp86aI1AzMzFD2q517Zf1A4UqkKV5vw4/tZg2+kcHgP5/Kcp4F6yQIDAQAB";

        String appPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIeYPczo3EPiA34PMq1AmRHA5ILSAO8XdiqjvtapBLDqMP11DsTGbkBu7MZzgIqrpNCfdiBVNlpFNSmI33nFcC0tSvEj615x9TKWiq7SPEWGHQErcB4Rg7QAZ6Pxc6uxnAv58jrcY2/ZS/lTMPS2QiJilO1IEFxeT6uuYkWgSJmBAgMBAAECgYBiryFjTaJpYUerakI2p4n/ysRElbSKTif5Nn1A23gHjhojjRs3iTdae6ClehB2XB+ymiutsnbBUhcz6GLEzDYsB47eJWzOduwIabxVr7qbWflkl/OBKgEccyOEXZq0CRtivPdNQ5+haKXBH8Qzp2wEA8InAe/pXstYVFilzyf1wQJBAN53797XASzcfxchLVL8brlsqLsg83vuYNpPFLYRsOfG8ER+5m/oZe+jDJnY4Pf4NL893PWTik/5cLnEuby6VHkCQQCcCDuzfR9JEMhgnc09JSs/4wDoiMhjbjpCe7oPyjO/DYYbRu5S2k0dN2jLiuLjOOwvS28rz9bFHmvc/TM5O9tJAkEAxj+XULvE3ld4IgJ8w3EUssSz8F5C3HPnd2P1jvJg9YsA3elALQWqoUxplEgC4rcbOjlEyMs7/FzLKaC37AkX8QJAPanTTj/omUuUpPo4Hi4ORZVEyqEj8IejZprXEV5rzNlfYnBJx1yWlTFMrQQaXookbRx2tu3Qht5a6l72W2wYYQJBAJ94uhXEZ2uX4Hnioaacc1pCPkNFYxsYBEPfGr/stQgqGcBUQMHwecThOzvIkGghAEWiWO0hV9mj66DhdmdzisM=";

        ApiRequest apiRequest =new ApiRequest();
        apiRequest.setApiId(10029+"");
        apiRequest.setSid("efa7c878-8431-4339-a70e-d7d5995c7f27");
        apiRequest.setMethod("qthf");
        apiRequest.setVersion("1.0");
        String timestamp = System.currentTimeMillis()+"";
        apiRequest.setTimestamp(timestamp);
        apiRequest.setSignType("RSA");

        Map<String, String> map = new HashMap<>();
        map.put("cerno", cerno);
        map.put("applyCode", "applyCode");
        map.put("memberType", "1");
        map.put("groupCode", "gtgsh");
        map.put("authBookUid", authBookUid);

        String bizParams = JSON.toJSONString(map);

        //入参加密
        String newBizParams =  AESUtil.parseByte2HexStr(AESUtil.encrypt(bizParams, appSecrt));

        apiRequest.setBizParams(newBizParams);

        Map<String, String> signMap = new HashMap<>();
        signMap.put("version", apiRequest.getVersion());
        signMap.put("apiId", apiRequest.getApiId());
        signMap.put("sid", apiRequest.getSid());
        signMap.put("bizParams", apiRequest.getBizParams());

        String sign = RSAUtil.signWithRSA(signMap, appPrivateKey);

        apiRequest.setSign(sign);

        String requestJson = JSON.toJSONString(apiRequest);

        System.out.println("【加密参数】:" + requestJson);

        //验签
        boolean flag = RSAUtil.checkSignWithRSA(signMap, appPublicKey, apiRequest.getSign());

        System.out.println("flag:" + flag);
    }



//    public  void proSignXJXQ() throws Exception {
//        String appsecrt="Ng130i56fWq9jth2";
//        String appPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIeYPczo3EPiA34PMq1AmRHA5ILSAO8XdiqjvtapBLDqMP11DsTGbkBu7MZzgIqrpNCfdiBVNlpFNSmI33nFcC0tSvEj615x9TKWiq7SPEWGHQErcB4Rg7QAZ6Pxc6uxnAv58jrcY2/ZS/lTMPS2QiJilO1IEFxeT6uuYkWgSJmBAgMBAAECgYBiryFjTaJpYUerakI2p4n/ysRElbSKTif5Nn1A23gHjhojjRs3iTdae6ClehB2XB+ymiutsnbBUhcz6GLEzDYsB47eJWzOduwIabxVr7qbWflkl/OBKgEccyOEXZq0CRtivPdNQ5+haKXBH8Qzp2wEA8InAe/pXstYVFilzyf1wQJBAN53797XASzcfxchLVL8brlsqLsg83vuYNpPFLYRsOfG8ER+5m/oZe+jDJnY4Pf4NL893PWTik/5cLnEuby6VHkCQQCcCDuzfR9JEMhgnc09JSs/4wDoiMhjbjpCe7oPyjO/DYYbRu5S2k0dN2jLiuLjOOwvS28rz9bFHmvc/TM5O9tJAkEAxj+XULvE3ld4IgJ8w3EUssSz8F5C3HPnd2P1jvJg9YsA3elALQWqoUxplEgC4rcbOjlEyMs7/FzLKaC37AkX8QJAPanTTj/omUuUpPo4Hi4ORZVEyqEj8IejZprXEV5rzNlfYnBJx1yWlTFMrQQaXookbRx2tu3Qht5a6l72W2wYYQJBAJ94uhXEZ2uX4Hnioaacc1pCPkNFYxsYBEPfGr/stQgqGcBUQMHwecThOzvIkGghAEWiWO0hV9mj66DhdmdzisM=";
//        String jsonStr="{\n" +
//                "    \"apiId\": \"10129\", \n" +
//                "    \"bizParams\": \"{" +
////                "                       \\\"uniscid\\\":\\\"430122197608266217\\\"," +
//                "                       \\\"cerno\\\":\\\"430122198205306995\\\"," +
//                "                       \\\"applyCode\\\":\\\"54122\\\"," +
//                "                       \\\"memberType\\\":\\\"1\\\"," +
//                "                       \\\"groupCode\\\":\\\"gtgsh\\\"," +
//                "                       \\\"authBookUid\\\":\\\"0a760983-1206-4f85-a94e-4ee764a34c6e\\\"" +
//                "                      }\", \n" +
//                "    \"method\": \"hjmx\", \n" +
//                "    \"signType\": \"RSA\", \n" +
//                "    \"timestamp\": \"1647506972260\", \n" +
//                "    \"version\": \"1.0\", \n" +
//                "    \"sid\": \"efa7c878-8431-4339-a70e-d7d5995c7f27\"\n" +
//                "}";
//        ApiRequest apiRequest = JSONObject.parseObject(jsonStr, ApiRequest.class);
//        String requestStr = ApiClient.sign2Encrypt(apiRequest,appsecrt,appPrivateKey);
//        System.out.println("加签参数"+requestStr);
//
//    }
    /**
     * 解密
     * @throws Exception
     */
    @Test
    public void RSAdecrypt() throws Exception {

        String str = "{\n" +
                "  \"errCode\": \"-1\",\n" +
                "  \"errMsg\": \"success\",\n" +
                "  \"data\": {\n" +
                "    \"retParams\": \"20A13041148912C29D7EA8821C6F63D8660B2F0AC42B553622E5ED122BC047AC381ABDE2FB1C9738B97018588D8281610A4E6F8B41C2127F37FCB18194B404A732301F05EBA3FC0BE79AEA5F2B16A57E\",\n" +
                "    \"sign\": \"VT1ITbrURsMoJlphf10qebX6CQGRS8jHiiSJ7OT0oZ3YbvjyDfP9ESCAI79rgDW6HDrflLlbW4oNVDZRWPvHP36vAKvk6TkcU0ENnHTqx1HRtksNKJCyl7+U5sdIPA3yQk1rHdqwmPeLzYFLLSdF6dj0kkp8+uwhA1itK11DaZg=\"\n" +
                "  },\n" +
                "  \"flag\": true\n" +
                "}";

        String appSecrt = "43c01Ia5BQK6i68i";

        String PlatPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdmqDv2732H3JPdzE9OU6t+G5T6M/J4eIMIn714XOgJVv4QcIqakCII0L42/nReSW4Yt+fPdCGRa18BrOj6/hyruFxzPqVAGftWdSE0sQtvmcovgNxq3t+FzlIAZv0yqEo2m/Ax9ZyXP5nYiIJQFeJn6oR1HB1glKJo4vpb4A4TQIDAQAB";

        ApiResponse apiResponse = JSON.parseObject(str, ApiResponse.class);
        String signType = "RSA";

        Map<String, String> verif = new HashMap<>();

        Map<String, String> retParamesMap = (Map<String, String>) apiResponse.getData();

        verif.put("retParams", retParamesMap.get("retParams"));

        boolean check =  false;
        //验签
        check = RSAUtil.checkSignWithRSA(verif, PlatPublicKey, retParamesMap.get("sign"));
        if(!check) System.out.println("验签失败");

        //解密
        String decrypt = new String(AESUtil.decrypt(verif.get("retParams"), appSecrt));

        retParamesMap.put("retParams", decrypt);

        apiResponse.setData(retParamesMap);

        System.out.println(apiResponse);

    }

    @Test
    public void testdemo4(){

        System.out.println(decrypt("4DlwQ/NZeA482RXBa7S93Q=="));
    }

    public static String decrypt(String data) {
        String KEY = "ahhfkxjf";
        String DES = "DES/CBC/PKCS5Padding";
        String decryptStr = null;
        try {
            Base64 base64 = new Base64();
            byte[] src = base64.decode(data);
            Cipher cipher = Cipher.getInstance(DES);
            System.out.println("11");
            cipher.init(Cipher.DECRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(KEY.getBytes("UTF-8"))), new IvParameterSpec(KEY.getBytes("UTF-8")));
            decryptStr = new String(cipher.doFinal(src),"UTF-8");
            System.out.println("22");
            return decryptStr;
        } catch (Exception e) {
            System.out.println("Exception");
        } finally{
            System.out.println("finally");
            if(decryptStr != null){
                decryptStr = null;
            }
        }
        System.out.println("return");
        return null;
    }

}
