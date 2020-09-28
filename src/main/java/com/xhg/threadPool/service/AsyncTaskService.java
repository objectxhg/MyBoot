package com.xhg.threadPool.service;

import com.xhg.config.rabbitMQ.Sender;
import com.xhg.pojo.sysUser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AsyncTaskService {


    @Resource
    private Sender sender;

	@Async
    public void sendMQAsyncTask(sysUser user) {
        System.out.println("线程" + Thread.currentThread().getName() + " 执行异步任务：" + user.getId());

        sender.send(user);

    }

}
