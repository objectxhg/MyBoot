# MyBoot
    SpringBoot2.0 + redis5.0 + rabbitMQ  + Sentinel

# Redis
    使用SpringBoot 集成redis进行缓存热点数据 主要有redis主从复制、哨兵模式进行读写分离和高可用 
    在一台服务器上起了三个redis服务 1主2从（服务器redis主从，哨兵配置没贴出来）以及开启reids事务
    使用lua脚本，来保证一些业务逻辑和分布式事物

# RabbitMQ
使用SpringBoot 集成rabbitMQ 的一个小的demo 还不是很完整。目前包含 消息发送到mq的回调判断，消息是否被mq成功接收防止数据还没到mq就丢失、 死信队列、为了确保消息被正常消费，多加一层防护<br/>

&emsp;&emsp;模拟用户下单后 添加完订单 直接返回成功后 给队列发送一条消息 （使用的是fanout广播模式），然后监听队列进行消费信息 <br/>
&emsp;&emsp;这里有个问题如果监听失败了，如果你没有处理这条报错的消息也就是没有被正常消费，mq会把它重新放入队列头部下一次在取出来，由于一直在监听队列，所以又重新读到了上一次消费失败的消息 ，可能会出现死循环一直在重复消费上一条消息然后一直失败一直重复消费

解决方法1：<br/>
&emsp;&emsp;加上try catch mq开启手动确认消费消息  在catch里面告诉mq这条消息我已经消费了<br/>
&emsp;&emsp;channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);<br/>
&emsp;&emsp;因为消费的时候报错了 我并不想重新消费这条消息<br/>
&emsp;&emsp;所以在catch里面手动去确认这条消息 假装告诉mq我已经成功消费了不需要管了 手动确认消息会照成消息数据的丢失 所以我们这里不采用这个方案 当然也可能又有一些场景能用到<br/>
&emsp;&emsp;channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);<br/>
  
解决方法2：<br/>
&emsp;&emsp;失败的消息加入死信队列（使用的是direct模式） 让死信队列去消费这条消息，达到异步的效果 一边业务队列消费正常的消息，另一边死信队列同时在消费业务队列失败的消息<br/>
&emsp;&emsp;同样的加上try catch 然后在catch里面手动去拒收这条消息, 拒收消息后mq会自动将这条消息发送到你配置好的死信队列进行再次消费 <br/>
&emsp;&emsp;channel.basicNack(message.getMessageProperties().getDeliveryTag(),false, false);
  
死信队列介绍：<br/>
   &emsp;&emsp;其实死信队列并不是一个什么其他类型的队列，就只是一个专门用来装消费失败的正常的队列而已 我们习惯性的称这种用来装死信消息的队列叫死信队列 死信交换机也一样<br/>
   &emsp;&emsp;就是设置了一个队列中的消息死亡后的去处，就等于消息死亡后给它不把它删掉而是做一次转发，发到其他Exchange去。<br/>
   
   &emsp;&emsp;首先我们先创建死信队列和交换机，跟正常一样创建的创建模式,再给它们绑定路由键<br/>
   
   &emsp;&emsp;然后我们再创建正常的业务队列，业务队列交换机一样的正常创建方式，<br/>
   &emsp;&emsp;业务队列这里要注意了： 业务队列比正常创建要多设置2个参数，也就是配置消费失败的消息的去除 转发给另一个交换机去给到队列消费<br/>
   &emsp;&emsp;x-dead-letter-exchange:	DeadExchange（这里填上面死信队列的名称)<br/>
   &emsp;&emsp;x-dead-letter-routing-key:	DeadRoutingKey（这里填上面死信队列和交换机绑定的路由键）<br/>
   &emsp;&emsp;注意：正常队列不设置这两个参数 业务队列拒收后，死信队列是收不到消息的<br/>
   
   &emsp;&emsp;准备工作的做完了 接下来我们就启动试一下模拟正常队列报错后在catch里面拒收消息 死信队列能否消费到消息<br/>
 
# Sentinel
    使用Sentinel对指定接口进行流控和服务降级
    
   原文链接：<a href="https://paogg.cn" target="_blank"> https://paogg.cn  </a>
   希望能帮到大家,有不对的地方欢迎大家来指导 0.0 , 邮箱：xiaohuagang@outlook.com

  
