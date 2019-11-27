package com.xhg.excepetion;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xhg.vo.JsonResult;

@ControllerAdvice //原理是使用AOP对Controller控制器进行增强（前置增强、后置增强、环绕增强，AOP原理请自行查阅）
public class GlobalException {
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public JsonResult resultException(Exception e){
		
		return new JsonResult(e);
		
	}
}
