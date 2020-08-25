package com.xhg.config.rabbitMQ;

import com.rabbitmq.client.Channel;
import com.xhg.pojo.sysUser;
import com.xhg.service.UserService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author xiaoh
 * @create 2020/8/25 16:15
 */
@Component
public class DeadReceiver {

    @Resource
    private UserService userService;

    @RabbitHandler
    @RabbitListener(queues = "DeadQueue")
    public void receiveTopic1(sysUser user, Channel channel, Message message) throws Exception {

        System.out.println("【DeadQueue死信队列 监听到消息】-----> 开始消费....");
        Integer state = userService.addUserIntegral(user.getId());
        if(state == 1){
            System.out.println("【DeadQueue死信队列】-----> 消费成功 购物积分已增加");
        }

    }

}

