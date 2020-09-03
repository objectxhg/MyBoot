package com.xhg.aop;

import com.xhg.pojo.Order;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.VoiceStatus;
import java.util.Arrays;
import java.util.List;

/**
 * @Description： aop日志打印类
 * @Date: Created in 2019/9/2 18:22
 * @Modified By:
 */
@Aspect
@Component
@SuppressWarnings("all")
public class MethodParamsAop {


    @Pointcut(value = "execution(* com.xhg.service.Impl.OrderServiceImpl.orderList(..))")
    public void BrokerAspect() {
    }


    @Before("BrokerAspect()")
    public void doBeforeGame(){
        System.out.println("-----> Before");
    }

    @Around("BrokerAspect()")
    public List<Order> AroundGame(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("-----> Around1");
        List<Order> list = (List<Order>) joinPoint.proceed();

        System.out.println("-----> Around2");

        return list;
    }


    @After("BrokerAspect()")
    public void doAfterGame(){
        System.out.println("-----> After");
    }


    @AfterReturning("BrokerAspect()")
    public void doAfterReturningGame(){
        System.out.println("-----> AfterReturning");
    }

    @AfterThrowing("BrokerAspect()")
    public void doAfterThrowingGame(){
        System.out.println("-----> AfterThrowing");
    }



}
