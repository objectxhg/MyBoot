package com.xhg.threadPool.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import com.xhg.config.rabbitMQ.Sender;
import com.xhg.config.retry.RetryServiceImpl;
import com.xhg.excepetion.BaseException;
import com.xhg.pojo.sysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class AsyncTaskService {

    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource
    private Sender sender;

    @Resource
    private RetryServiceImpl retryServiceImpl;

    /**
     * mq线程任务：给mq发送消息
     * @param jsonStr
     */
	@Async
    public void sendMQAsyncTask(String jsonStr) {

        logger.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + jsonStr);
        sender.send(jsonStr);
    }


    /**
     * 10秒后执行第一次
     *
     * fixedDelay：每次执行完毕后x秒后再执行
     *
     * fixedRate: 每次执行后x秒后再执行
     */
    //@Scheduled(initialDelay=10000, fixedDelay = 10000)
    public void sendGateway() {

        logger.info("开始执行定时任务--------> {}", new Date(System.currentTimeMillis()));
        try {
            retryServiceImpl.sendApi("message");
        } catch (Exception e) {
            logger.info("重试");
        }
        logger.info("定时任务结束--------> {}", new Date(System.currentTimeMillis()));
    }

}
