package com.xhg.config.rabbitMQ;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xhg.pojo.Message;
import com.xhg.pojo.User;

@Component
public class Sender {
	
	
	@Autowired
    private AmqpTemplate rabbitTemplate;
 
    public void send(User user) {
        this.rabbitTemplate.convertAndSend("F-exchange", "Routingkey_demo", user);
    }
}
