package com.xhg.config.rabbitMQ;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xhg.pojo.sysUser;

@Component
@Slf4j
public class Sender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{


	@Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 死信队列
     * 模拟秒杀场景,使用队列进行平滑消费，消息发送失败重试机制。以及消费失败的消息，通过绑定死信队列重新进行消费，保证消息的可靠性和完整性
     */

    public void send(String jsonStr) {
        /**
         * 发送
         */
        this.rabbitTemplate.convertAndSend("exchangeDemo", "Routingkey-xhg", jsonStr);
        /**
         * 回调监听,发送失败重新发送
         */
        this.rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 延时队列
     * 模拟订单30分钟有效，设置队列消息有效期，过期的消息将放入其他特定的队列进行取消订单
     */
    public void sendDelay(String jsonStr) {
        /**
         * 发送
         */
        this.rabbitTemplate.convertAndSend("delayExchange", "delay_routing_key", jsonStr);
        /**
         * 回调监听,发送失败重新发送
         */
        this.rabbitTemplate.setConfirmCallback(this);
    }


    /**
     * mq发送消息结果回调监控，发送失败的消息重写returnedMessage重新发送
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        if(ack){
            System.out.println("【消息发送成功，等待消费...】");
        }else{
            System.out.println("【消息发送失败】"+correlationData+", 出现异常："+cause);
            rabbitTemplate.setReturnCallback(this);
        }
    }

    /**
     *
     *  发送MQ失败 重新发送
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

        //System.out.println("【被退回的消息为】" + message + "-----> 开始重新发送...");

        log.info("【被退回的消息为】交换机：{}, 队列：{}, 消息内容：{}", exchange, routingKey, message.getBody());

        amqpTemplate.convertAndSend(exchange, routingKey, message.getBody());
    }




}
