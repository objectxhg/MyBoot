package com.xhg.controller;

import com.alibaba.fastjson.JSON;
import com.xhg.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**

/**
 * @Author xiaoh
 * @create 2020/8/7 16:15
 *
 */
@RestController
public class RedisController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/redis/setNum/{key}/{num}")
    public String redisSet(@PathVariable("num") String keyStr, @PathVariable("num") String num){

        if(redisUtil.set(keyStr, num)){
            return "进货成功";
        }
        return "进货失败";
    }


    @RequestMapping("/redis/shoping/{key}")
    public String redisSeckill(@PathVariable("key") String keyStr){
        System.out.println("------------>" + keyStr);
        String number = JSON.toJSONString(redisUtil.get(keyStr));
        System.out.println(number);
        if(Long.parseLong(number) > 0){
            try {
                long num = redisUtil.decrbyKey(keyStr);
            }catch(Exception e){
                e.printStackTrace();
            }

            return "购买成功";
        }

        return "库存不足";
    }

}

