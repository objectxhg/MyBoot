package com.xhg.config.rabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import com.xhg.service.OrderService;
import com.xhg.service.UserService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.xhg.pojo.sysUser;

import javax.annotation.Resource;
import java.io.IOException;


@Component
public class Receiver {

    /**
     * 队列名称
     * @param user
     */

    @Resource
    private UserService userService;


    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.FANOUT_QUEUE1)
    public void receiveTopic1(sysUser user, Channel channel, Message message) throws IOException {
        System.out.println("【receiveFanout1监听到消息,开始消费......】-----> " + user);

        try {
            Integer state = userService.addUserIntegral(user.getId());
            if(state == 1){
                System.out.println("---------->消费成功 购物积分已增加");
            }
        }catch (Exception e){
            channel.basicAck(message.getMessageProperties().getDelay(), false);
            System.out.println("---------->消费失败");
        }
    }
    
}
