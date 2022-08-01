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

        Integer state = userService.incrUserIntegral(user.getId());
        if(state == 1){
            logger.info("【DeadQueue死信队列】-----> 重新消费成功 购物积分已增加");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }else{
            logger.error("【DeadQueue死信队列】-----> 用户id:" + user.getId() + " 消费失败 死信队列开始丢弃....");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

    }

    @RabbitHandler
    @RabbitListener(queues = "consumptionQueue")
    public void consumptionQueue(String jsonStr, Channel channel, Message message) throws Exception {

        logger.info("【consumptionQueue 死信队列 监听到消息】-----> 开始消费....");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        logger.info("【consumptionQueue 死信队列】-----> 消费成功");
    }

}

