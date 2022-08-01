package com.xhg.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.xhg.pojo.sysUser;
import com.xhg.service.Impl.RedisServiceImpl;
import com.xhg.threadPool.service.AsyncTaskService;
import com.xhg.utils.RedisUtil;

import com.xhg.vo.JsonResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private AsyncTaskService asyncTaskService;

    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @PostMapping("/redis/setNum/{key}/{num}")
    public JsonResult redisSet(@PathVariable("key") String keyStr, @PathVariable("num") String num){
        System.out.println(keyStr);
        if(redisUtil.set(keyStr, Integer.parseInt(num))){
            return JsonResult.success("进货成功");
        }
        return JsonResult.fail("进货失败");
    }


    @RequestMapping("/redis/shoping/{key}")
    @SentinelResource(value = "shoping", blockHandler = "shopingHandleException", fallback = "shopingFallback")
    public JsonResult redisSeckill(@PathVariable("key") String keyStr, Integer userId, String orderDescribe, Integer testTime){

        /**
         * 双重检查
         */
        System.out.println("检查redis 订单库存状态");
        if(Integer.parseInt(redisUtil.get(keyStr).toString()) > 0){
            /**
             * lua 脚本里面再检查一次
             */
            Integer state = redisServiceImpl.redisIncrBy(keyStr, userId, orderDescribe, testTime);
            System.out.println("检查redis 订单库存状态" + state);
            if(state == 1){
                sysUser user = new sysUser();
                user.setId(userId);
                //为了流控测试可以注释掉 mq发送消息
                asyncTaskService.sendMQAsyncTask(JSON.toJSONString(user));
                return JsonResult.success("购买成功");
            }
        }

        return JsonResult.success(400, "商品已经卖光了, 0.0");


    }

    public JsonResult shopingHandleException(@PathVariable("key") String keyStr, Integer userId, String orderDescribe, Integer testTime){

        logger.info("异常数达到阈值：开始降级");
        return JsonResult.success("活动火爆，请重试1");
    }

    //流控返回报文
    public JsonResult shopingFallback(@PathVariable("key") String keyStr, Integer userId, String orderDescribe, Integer testTime){

        logger.info("QPS达到阈值：开始限流");
        return JsonResult.success("活动火爆，请重试2");
    }

    /**
     * 分布式锁 lock
     */

    public JsonResult LockController(Integer userId){



        return JsonResult.success("lock test");
    }
}

