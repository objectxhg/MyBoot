package com.xhg.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xhg.utils.RedisUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * redis
 */
@Service
@Transactional
public class RedisServiceImpl {

    @Resource
    private RedisUtil redisUtil;

    public Long redisIncrBy(String key){
        /**
         * 下单前获取库存 如果库存大于0 去购买 库存-1
         */
        Long number = Long.parseLong(JSON.toJSONString(redisUtil.get(key)));
        if(number > 0L){

            return redisUtil.decrbyKey(key);
        }
        return 0L;

    }
}
