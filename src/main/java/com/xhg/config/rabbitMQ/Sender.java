package com.xhg.config.rabbitMQ;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xhg.pojo.sysUser;

@Component
public class Sender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{


	@Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(sysUser user, CorrelationData correlationData) {
        amqpTemplate.convertAndSend("exchangeDemo", "Routingkey-xhg", user);
        /**
         * 回调监听
         */
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * mq回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        if(ack){
            System.out.println("【发送MQ，等待消费】");
        }else{
            System.out.println("【发送MQ失败】"+correlationData+", 出现异常："+cause);
            rabbitTemplate.setReturnCallback(this);
        }
    }

    /**
     *
     *  发送MQ失败 重新发送
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("【被退回的消息为】" + message + "-----> 开始重新发送...");
        amqpTemplate.convertAndSend("exchangeDemo", "Routingkey-xhg", message.getBody());
    }
}
