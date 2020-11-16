package com.xhg.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xhg.pojo.Order;
import com.xhg.pojo.sysUser;
import com.xhg.service.OrderService;
import com.xhg.utils.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * redis
 */
@Service
public class RedisServiceImpl {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private OrderService orderService;

    public Integer redisIncrBy(String key, Integer userId, String orderDescribe, Integer testTime){
        /**
         * 下单前去获取库存 如果库存大于0 去购买 库存-1
         */
        Long number = Long.parseLong(JSON.toJSONString(redisUtil.get(key)));
        if(number == 0){
            return -1;
        }
        boolean flag = redisUtil.decr(key, testTime);
        if(flag){
            // redis库存 减完创建订单
            return  orderService.addOrder(new Order(userId, orderDescribe));
        }else{
            return 0;
        }

    }
}
