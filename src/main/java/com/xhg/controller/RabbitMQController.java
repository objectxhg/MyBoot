package com.xhg.controller;

import com.alibaba.fastjson.JSON;
import com.xhg.config.rabbitMQ.Sender;
import com.xhg.pojo.sysUser;
import com.xhg.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mq")
public class RabbitMQController {

    @Autowired Sender sender;

    @PostMapping("/addOrder/{userId}/{hello}")
    public JsonResult mqTest(@PathVariable("hello") String hello){

        sysUser user = new sysUser();
        user.setId(1);
        user.setUsername(hello);
        sender.send(JSON.toJSONString(user));
        return JsonResult.success("成功");
    }

    @RequestMapping("/delayOrder")
    public JsonResult mqdelayQueue(String userName){
        sysUser user = new sysUser();
        user.setId(1);
        user.setUsername(userName);
        sender.sendDelay(JSON.toJSONString(user));
        return JsonResult.success("成功");
    }
}
