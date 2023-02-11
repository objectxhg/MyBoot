package com.xhg.config.rabbitMQ;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xhg.pojo.sysUser;
import com.xhg.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource
    private UserService userService;

    @RabbitHandler
    @RabbitListener(queues = "DeadQueue")
    public void receiveTopic1(String jsonStr, Channel channel, Message message) throws Exception {

        logger.info("【DeadQueue死信队列 监听到消息】-----> 开始重新消费....");

        sysUser user = JSON.parseObject(jsonStr, sysUser.class);
        userService.incrUserIntegral(user.getId());
        logger.info("【DeadQueue死信队列】-----> 重新消费成功 购物积分已增加");
        //手动签收
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }

    @RabbitHandler
    @RabbitListener(queues = "consumptionQueue")
    public void consumptionQueue(String jsonStr, Channel channel, Message message) throws Exception {

        logger.info("【consumptionQueue 延时死信队列 监听到消息】-----> 开始消费....", message.getBody());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        logger.info("【consumptionQueue 延时死信队列】-----> 消费成功");
    }

}

