package com.xhg.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xhg.pojo.Order;
import com.xhg.pojo.sysUser;
import com.xhg.service.OrderService;
import com.xhg.utils.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * redis
 */
@Service
public class RedisServiceImpl {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private OrderService orderService;


    /**
     * 下单前使用lua脚本去redis获取库存 如果库存大于0 返回 1 可以进行下单， 如果等于0 返回 0 表示商品已经卖光了
     */
    public Integer redisIncrBy(String key, Integer userId, String orderDescribe, Integer testTime){

        /**
         * Long keyValue = Long.parseLong(JSON.toJSONString(redisUtil.get(key))) ;
         * 需要秒杀的库存 key
         * 调用redis脚本（lua脚本） 多行reids命令 进行原子操作
         */
        List<String> keys = new ArrayList<>();
        keys.add(key);

        boolean state = redisUtil.decrLuaScript(keys, 0);

        if(state){
            // redis库存 减完创建订单
            return orderService.addOrder(new Order(userId, orderDescribe));
        }

        return 0;


    }
}
