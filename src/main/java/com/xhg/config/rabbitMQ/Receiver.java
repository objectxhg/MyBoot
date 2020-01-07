package com.xhg.config.rabbitMQ;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.xhg.pojo.User;

@Component
public class Receiver {
	
	
    @RabbitListener(queues = RabbitConfig.FANOUT_QUEUE1)
    public void receiveTopic1(User user) {
        System.out.println("【receiveFanout1监听到消息】" + user);
    }
    
}
