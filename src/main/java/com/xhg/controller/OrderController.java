package com.xhg.controller;

import com.xhg.config.rabbitMQ.Sender;
import com.xhg.pojo.Order;
import com.xhg.pojo.sysUser;
import com.xhg.service.OrderService;
import com.xhg.threadPool.service.AsyncTaskService;
import com.xhg.vo.JsonResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xiaoh
 * @create 2020/8/24 16:00
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;


    @Resource
    private AsyncTaskService asyncTaskService;

    @RequestMapping("/addOrder")
    public JsonResult addOrder(Integer userId, String orderDescribe){

        if(null == userId){
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

        System.out.println("--- 添加成功 ---");

        return JsonResult.success("添加成功");
    }

    @RequestMapping("/orderList")
    public JsonResult selectOrderList(Order order){


        List<Order> orderList = orderService.orderList();

        return JsonResult.success(orderList);
    }



}

