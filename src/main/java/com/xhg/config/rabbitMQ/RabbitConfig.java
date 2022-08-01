package com.xhg.config.rabbitMQ;

import io.swagger.annotations.SwaggerDefinition;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@SuppressWarnings("all")
public class RabbitConfig {
	
	//topic
//    public static final String TOPIC_QUEUE1 = "topic.queue1";
//    public static final String TOPIC_QUEUE2 = "topic.queue2";
//    public static final String TOPIC_EXCHANGE = "topic.exchange";
 
    //fanout
    public static final String FANOUT_QUEUE1 = "ququDemo";
    public static final String FANOUT_EXCHANGE = "exchangeDemo";

    //延时队列
    public static final String FANOUT_DELAYQUEUE = "delayQueue";
    public static final String FANOUT_DELAYEXCHANGE = "delayExchange";

    //redirect
//    public static final String DIRECT_QUEUE1 = "direct.queue1";
//    public static final String DIRECT_QUEUE2 = "direct.queue2" ;
//    public static final String DIRECT_EXCHANGE = "direct.exchange";

 

 
    /**
     * 创建队列
     * Fanout模式
     * Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。
     * @return
     */
    @Bean
    public Queue fanoutQueue1() {
        Map<String, Object> args = new HashMap<>();
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", "DeadExchange");
        //声明当前队列绑定死信的路由键
        args.put("x-dead-letter-routing-key", "DirectRouting");
        Queue queue = new Queue(FANOUT_QUEUE1, true, false, false, args);
        return queue;
    }

    /**
     * 创建交换机
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    /**
     * 绑定队列和交换机
     * @return
     */
    @Bean
    public Binding fanoutBinding1() { return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange()); }




    /**
     * 死信队列1
     *
     * @return
     */
    @Bean
    public Queue DeadQueue() { return new Queue("DeadQueue", true); }

    @Bean
    public DirectExchange DeadExchange() { return new DirectExchange("DeadExchange"); }

    @Bean
    public Binding bindingDead() { return BindingBuilder.bind(DeadQueue()).to(DeadExchange()).with("DirectRouting"); }


    //延时队列
    @Bean
    public Queue DelayQueue() {
        Map<String, Object> args = new HashMap<>();
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", "consumptionExchange");
        //声明当前队列绑定死信的路由键
        args.put("x-dead-letter-routing-key", "consumption_router_key");
        /**
         * 消息存活时间, 创建queue时设置该参数可指定消息在该queue中待多久， 过期后进入绑定的死信队列
         * 场景用于 订单超过x-message-ttl未支付 订单自动取消
         * 对于时间过期的消息会进入死信队列进行 订单取消动作消费
         *
         * 不需要此场景可以注释 (过期时间暂未实现成功)
         */
        args.put("x-message-ttl", 60000);
        Queue queue = new Queue(FANOUT_DELAYQUEUE, true, false, false, args);
        return queue;
    }

    @Bean
    public FanoutExchange fanoutDelayExchange() {
        return new FanoutExchange(FANOUT_DELAYEXCHANGE);
    }

    @Bean
    public Binding fanoutDelayBinding() { return BindingBuilder.bind(DelayQueue()).to(fanoutDelayExchange()); }


    /**
     * 死信队列2 start
     */
    @Bean
    public Queue DelayDeadQueue() { return new Queue("consumptionQueue", true); }

    @Bean
    public DirectExchange DelayDeadExchange() { return new DirectExchange("consumptionExchange"); }

    @Bean
    public Binding bindingDelayDead() { return BindingBuilder.bind(DelayDeadQueue()).to(DelayDeadExchange()).with("consumption_router_key"); }
    /**
     * end
     */


    //    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        // 消息发送失败返回到队列中, 配置文件需要配置 publisher-returns: true
//        rabbitTemplate.setMandatory(true);
//
//        return rabbitTemplate;
//    }

    /**
     * Topic模式
     *
     * @return
     */
//    @Bean
//    public Queue topicQueue1() {
//        return new Queue(TOPIC_QUEUE1);
//    }
//
//    @Bean
//    public Queue topicQueue2() {
//        return new Queue(TOPIC_QUEUE2);
//    }
//
//    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange(TOPIC_EXCHANGE);
//    }
//
//    @Bean
//    public Binding topicBinding1() {
//        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("lzc.message");
//    }
//
//    @Bean
//    public Binding topicBinding2() {
//        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("lzc.#");
//    }
//


    /**
     * direct模式
     * 消息中的路由键（routing key）如果和 Binding 中的 binding key 一致， 交换器就将消息发到对应的队列中。路由键与队列名完全匹配
     * @return
     */
//    @Bean
//    public Queue directQueue1() {
//        return new Queue(DIRECT_QUEUE1,true);
//    }
//
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange(DIRECT_EXCHANGE);
//    }
//
//    @Bean
//    public Binding directBinding1() { return BindingBuilder.bind(directQueue1()).to(directExchange()).with("direct.pwl"); }


}
