package com.xhg.controller;

import com.alibaba.fastjson.JSON;
import com.xhg.service.Impl.RedisServiceImpl;
import com.xhg.utils.RedisUtil;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**

/**
 * @Author xiaoh
 * @create 2020/8/7 16:15
 *
 */
@RestController
public class RedisController {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RedisServiceImpl redisServiceImpl;

    @RequestMapping("/redis/setNum/{key}/{num}")
    public String redisSet(@PathVariable("key") String keyStr, @PathVariable("num") String num){
        System.out.println(keyStr);
        if(redisUtil.set(keyStr, Integer.parseInt(num))){
            return "进货成功";
        }
        return "进货失败";
    }


    @RequestMapping("/redis/shoping/{key}")
    public String redisSeckill(@PathVariable("key") String keyStr){

        if(redisServiceImpl.redisIncrBy(keyStr)){
            return "购买成功";
        }

        return "当前下单人数过多，请稍后重试";
    }

}

