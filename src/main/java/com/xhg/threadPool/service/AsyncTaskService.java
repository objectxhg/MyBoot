package com.xhg.threadPool.service;

import com.xhg.config.rabbitMQ.Sender;
import com.xhg.pojo.sysUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AsyncTaskService {

    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource
    private Sender sender;

    /**
     * mq线程任务：给mq发送消息
     * @param user
     */
	@Async
    public void sendMQAsyncTask(sysUser user) {
        logger.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + user.getId());
        sender.send(user);
    }

}
