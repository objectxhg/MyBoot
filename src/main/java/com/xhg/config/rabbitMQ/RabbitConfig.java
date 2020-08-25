package com.xhg.config.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {
	
	//topic
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topic.exchange";
 
    //fanout
    //Queue 1和2
    public static final String FANOUT_QUEUE1 = "ququDemo";
    public static final String FANOUT_QUEUE2 = "fanout.queue2";
    //Exchange
    public static final String FANOUT_EXCHANGE = "exchangeDemo";
 
    //redirect模式
    public static final String DIRECT_QUEUE1 = "direct.queue1";
    public static final String DIRECT_EXCHANGE = "direct.exchange";
    public static final String DIRECT_QUEUE2 ="direct.queue2" ;
 
    /**
     * Topic模式
     *
     * @return
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1);
    }
 
    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2);
    }
 
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }
 
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("lzc.message");
    }
 
    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("lzc.#");
    }
 
 
    /**
     * Fanout模式
     * Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。
     * @return
     */
    @Bean
    public Queue fanoutQueue1() {
        Map<String, Object> args = new HashMap<>(2);
        //交换机标识符
        args.put("x-dead-letter-exchange", "DeadExchange");
        //绑定键标识符
        args.put("x-dead-letter-routing-key", "DirectRouting");
        Queue queue = new Queue(FANOUT_QUEUE1, true, false, false, args);
        return queue;
        //return new Queue(FANOUT_QUEUE1);
    }

    @Bean
    public Queue fanoutQueue2() { return new Queue(FANOUT_QUEUE2); }
 
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
 
    @Bean
    public Binding fanoutBinding1() { return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange()); }

    @Bean
    public Binding fanoutBinding2() { return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange()); }



    /**
     * direct模式
     * 消息中的路由键（routing key）如果和 Binding 中的 binding key 一致， 交换器就将消息发到对应的队列中。路由键与队列名完全匹配
     * @return
     */
    @Bean
    public Queue directQueue1() {
        return new Queue(DIRECT_QUEUE1);
    }
 
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }
 
    @Bean
    public Binding directBinding1() { return BindingBuilder.bind(directQueue1()).to(directExchange()).with("direct.pwl"); }

    /**
     * 死信队列
     *
     * @return
     */
    @Bean
    public Queue DeadQueue() {

        return new Queue("DeadQueue", true);
    }

    @Bean
    public DirectExchange DeadExchange() { return new DirectExchange("DeadExchange"); }

    @Bean
    public Binding bindingDead() { return BindingBuilder.bind(DeadQueue()).to(DeadExchange()).with("DirectRouting"); }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        // 消息发送失败返回到队列中, 配置文件需要配置 publisher-returns: true
//        rabbitTemplate.setMandatory(true);
//
//        return rabbitTemplate;
//    }
}
