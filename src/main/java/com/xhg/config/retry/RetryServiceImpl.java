package com.xhg.config.retry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xhg.excepetion.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class RetryServiceImpl implements RetryServie{

    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Recover
    public void deRecover(Exception e, String message){

        logger.info("重试机制用尽，执行deRecover, 入参：{}", message);
    }

    /**
     * @Recover注释的方法产返回值和参数要与@Retryable对应
     *
     * Exception 两者抛出和获取的异常类型一致
     *
     * maxAttempts 重试次数
     *
     * backoff 重试补偿机制：{
     *                  delay：指定延迟2000毫秒后重试
     *                  multiplier：每次延迟的倍数，首次直接执行，第二次延迟 2000， 第三次 4000毫秒
     *              }
     */
    //@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    @Override
    public void sendApi(String message) throws Exception {

        logger.info("开始调用接口。。。 {}", new Date(System.currentTimeMillis()));

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://172.26.9.102:8090/fins-pre-gateway/gateway/unifiedService/cxqyshxydm";

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("apiId", "110106");
        jsonObject.put("bizParams", "4D6ABAC988D9D60AF278F500AD8EF45F0831B870F6BE992ED2BF93A31039734B");
        jsonObject.put("method", "cxqyshxydm");
        jsonObject.put("sid", "6423a1ef-a163-4769-aca8-a5fcd238342f");
        jsonObject.put("sign", "GH1JmGAN1WzRlAMbgPfbx2YayKeRAKqaMBfmseg3W+gZrMBlIFVfkqdkOqn0mRk4Xt9bHXEhiWHacSe/WdTDTNeILybsUkJdUoQnqLCrRVxQM6LHG+L0SqsUJ3DXSuWJWhAVeogvek1k81atfVwZemSzoIoCvD5giXVY1t0WWlU=");
        jsonObject.put("signType", "RSA");
        jsonObject.put("timestamp", "1620466042781");
        jsonObject.put("version", "1.0");

        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        String json =  restTemplate.postForObject(url, jsonObject, String.class);

        Map<String, String> map = JSON.parseObject(json, Map.class);

        if(!StringUtils.isEmpty(map.get("errCode"))){
            int code = Integer.parseInt(map.get("errCode"));
            if(code == 500){
                logger.info("errMsg: {}", map.get("errMsg").toString());
                throw new BaseException(500, map.get("errMsg").toString());
            }
        }
        logger.info("接口请求完毕。。。");
    }



}
