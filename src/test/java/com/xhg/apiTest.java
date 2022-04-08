package com.xhg;


import com.alibaba.fastjson.JSON;
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

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyBootApplication.class})
public class apiTest {

    /**
     * 加密
     * @throws Exception
     */
    @Test
    public void RSAencrypt() throws Exception {

        String appSecrt = "43c01Ia5BQK6i68i";

        String appPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8WjC2ErY5mkYN5t2S0KryWAvwsWcge8PV7h6sB0Ike2J9X7V30HmmDBP3tf7MlLdn0wd2IetJj9Cz1hWXZVOg1VchZkPGoD3tKKDLQp/zrbqHKeGNh8Qf6WKWO9IgilDGDBjgGlue4nIRbiA4/QkfT2ixHC8QxYIs2VOljCuScQIDAQAB";

        String appPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALxaMLYStjmaRg3m3ZLQqvJYC/CxZyB7w9XuHqwHQiR7Yn1ftXfQeaYME/e1/syUt2fTB3Yh60mP0LPWFZdlU6DVVyFmQ8agPe0ooMtCn/Otuocp4Y2HxB/pYpY70iCKUMYMGOAaW57ichFuIDj9CR9PaLEcLxDFgizZU6WMK5JxAgMBAAECgYABlt08XBIPkF6w9Va/S2V14ApRwJ13J7QyVO0LVJBZUHU3S5xzY13zabytZWq9/S9DAAPbGlQq7by8F71c5qz69bqKpGMzx5sWMeEwFHx8fZFUgDax36THxl/SkrLIiWAown29PYOTxjyBVJ2sU/VMSX9xBES8Fw54SzdA703llQJBAOysrNPwVzxnEkfX4V4/vch+MR+/jwSJ9W7sC9WjN4RnxJAxbcR/rXcU8RFc087HjTu0gtm4sWdubUuOALLRqjMCQQDLu2l+ffJ85jlUzY/Fj3CpmdDtVzDQslV1jqMn/FeW8qom9P7y4HBZLhutT7BYjk2obgpJDvkoHjvDrm2Ai/TLAkBu0CaXkRd3S5culjCKLXQRlKwxfkJbULDev5yG6cXLs74/+TS45UL115NLmtf9IEfLZahCgoxlrjl0P8ep8isrAkBaKEOVEJNgplk0qAs5uDJ5O3JztaQKlwCul0KojUkNqbGWr9CrFpthO8BPv/YgGklPgfLXReMI1+hGvkgDHOxlAkEA1f5PqmQMGmdrRh4klk2ThLOpentoA9iYwo82h8oRJ/Eu1MggH7iG+pf5Zkcx1EDEk+QPrXIWjFPwPurc1K8YdA==";

        ApiRequest apiRequest =new ApiRequest();
        apiRequest.setApiId(10080+"");
        apiRequest.setSid("6423a1ef-a163-4769-aca8-a5fcd238342f");
        apiRequest.setMethod("dfqf");
        apiRequest.setVersion("1.0");
        String timestamp = System.currentTimeMillis()+"";
        apiRequest.setTimestamp(timestamp);
        apiRequest.setSignType("RSA");

        Map<String, String> map = new HashMap<>();
        map.put("uniscid", "911000001000013428");
        map.put("authBookUrl","http://172.26.9.102:8090/group1/M00/00/00/oYYBAGDRh0qAf_npAAD2xqf2_m0212.pdf");

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

        String str = "3";

        if(str != null && (str.equals("1") ||  str.equals("2"))){
            System.out.println("ok");
        }else {
            System.out.println("no");
        }

    }

}
