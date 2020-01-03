package com.xhg.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Description： aop日志打印类
 * @Date: Created in 2019/9/2 18:22
 * @Modified By:
 */
@Aspect
@Component
public class MethodParamsAop {
	
	//@Autowired SessionToken token;
	
    private static final Logger LOG = LoggerFactory.getLogger(MethodParamsAop.class);

    @Pointcut("execution(public * com.jsqing.community.controller..*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void before(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        
        LOG.info("请求开始 : request:{ url: " + request.getRequestURL().toString() + ", ip: " + request.getRemoteAddr() + ", method: " + joinPoint.getSignature().getName() + ", params: " + Arrays.toString(joinPoint.getArgs()) + " }");
        
        String url = "ajaxLogin|code|regUser|loginUser|getToken";
        String str = request.getRequestURI();
        
        str = str.substring(str.lastIndexOf("/")+1,str.length());
        System.out.println("-----------------> " + url.indexOf(str));
		/*
		 * if(url.indexOf(str) == -1) {
		 * token.getUserBySessionToken(request.getParameter("sessionToken")); }else {
		 * 
		 * }
		 */
    }
    
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        LOG.error("请求结束 : response:" + ret);
    }

}
