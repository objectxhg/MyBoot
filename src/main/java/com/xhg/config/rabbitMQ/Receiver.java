package com.xhg.config.rabbitMQ;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import com.xhg.service.OrderService;
import com.xhg.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.xhg.pojo.sysUser;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Component
public class Receiver {

    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource
    private UserService userService;


    @RabbitHandler
    @RabbitListener(queues = "ququDemo")
    public void receiveTopic1(String jsonStr, Channel channel, Message message) throws Exception {
        logger.info("【ququDemo正常队列 监听到消息,开始消费......】-----> " + jsonStr);

        sysUser user = JSON.parseObject(jsonStr, sysUser.class);

        // 获取消息Id，用消息ID做业务判断
        String messageId = message.getMessageProperties().getMessageId();
        String content = new String(message.getBody(), "UTF-8");
        logger.info("messageId：{}, content：{}", messageId, content);
        try {

            //模拟处理消息产生异常后进入死信队列进行消费
            //int i = 1/0;
            Integer state = userService.incrUserIntegral(user.getId());
            if(state == 1){
                logger.info("【ququDemo正常队列】-----> 消费成功 购物积分已增加");
                // 手动签收消息已消费
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }else{
                logger.error("【ququDemo正常队列】-----> 用户id:" + user.getId() + " 消费失败 正常队列开始丢弃....");
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false, false);
            }

        }catch (Exception e){
            logger.info("【ququDemo正常队列】-----> 捕获到异常，消费失败：拒收消息");
            //拒收消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false, false);

        }

    }



    @RabbitHandler
    @RabbitListener(queues = "delayQueue")
    public void receiveDelay(String jsonStr, Channel channel, Message message) throws Exception {

        logger.info("【延时队列】 监听到消息,开始消费......】-----> " + jsonStr);

    }
}
