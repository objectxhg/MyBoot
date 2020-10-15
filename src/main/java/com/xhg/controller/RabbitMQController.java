package com.xhg.controller;

import com.xhg.config.rabbitMQ.Sender;
import com.xhg.pojo.sysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mp")
public class RabbitMQController {

    @Autowired Sender sender;

    @PostMapping("/addOrder/{userId}/{hello}")
    public String mqTest(@PathVariable("hello") String hello){

        sysUser user = new sysUser();
        user.setId(1);
        user.setUsername(hello);
        sender.send(user);
        return "已发送等待消费.....";
    }
}
