package com.xhg.config.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.xhg.pojo.sysUser;

@Component
public class Receiver {
	
	//队列名称
    @RabbitListener(queues = RabbitConfig.FANOUT_QUEUE1)
    public void receiveTopic1(sysUser user) {
        System.out.println("【receiveFanout1监听到消息】-----> " + user);
    }
    
}
