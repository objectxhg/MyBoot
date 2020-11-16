package com.xhg.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.xhg.config.rabbitMQ.Sender;
import com.xhg.pojo.Order;
import com.xhg.pojo.sysUser;
import com.xhg.service.OrderService;
import com.xhg.service.UserService;
import com.xhg.threadPool.service.AsyncTaskService;
import com.xhg.utils.RedisUtil;
import com.xhg.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author xiaoh
 * @create 2020/8/24 16:00
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private OrderService orderService;

    @Resource
    private UserService userService;

    @Autowired
    private AsyncTaskService asyncTaskService;

    /**
     * https://blog.csdn.net/u010391342/article/details/86678637
     */
    @PostMapping("/addOrder")
    @SentinelResource(value = "order", blockHandler = "orderHandleException", fallback = "orderFallback")
    public JsonResult addOrder(Integer userId, String orderDescribe){

        if(null == userId || null == userService.getUserInfo(userId)){
            return JsonResult.fail("用户不存在");
        }

        if(null == orderDescribe){
            return JsonResult.fail("请填写订单描述");
        }
        Integer state = orderService.addOrder(new Order(userId, orderDescribe));
        if(state != 1){
            return JsonResult.fail("订单添加失败");
        }
        /**
         * 模拟订单添加成功后 使用mq去消费订单消息 调用用户卡券积分系统增加购物积分或卡券
         */
        sysUser user = new sysUser();
        user.setId(userId);
        /**
         * 线程任务：给mq发送消息
        */
        asyncTaskService.sendMQAsyncTask(user);

        return JsonResult.success("添加成功");
    }

    /**
     *
     * 存储list
     * Collections.singletonList
     * orderList = Collections.singletonList(orderService.orderList());
     * 返回的是不可变的集合，但是这个长度的集合只有1，可以减少内存空间。但是返回的值依然是Collections的内部实现类，同样没有add的方法，调用add，set方法会报错
    */
    @PostMapping("/orderList")
    public JsonResult selectOrderList(){

        List<Object> orderList = redisUtil.getList("orderList");
        System.out.println(orderList);
        if(null == orderList){
            orderList = orderService.orderList().stream().collect(Collectors.toList());
            redisUtil.setList("orderList", orderList.stream().collect(Collectors.toList()), 10);
        }

        return JsonResult.success(orderList);
    }

    public JsonResult orderHandleException(Integer userId, String orderDescribe){


        return null;
    }

    public JsonResult orderFallback(Integer userId, String orderDescribe){



        return null;
    }
}

