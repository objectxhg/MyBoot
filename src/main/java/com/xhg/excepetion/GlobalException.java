package com.xhg.excepetion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xhg.vo.JsonResult;

//@ControllerAdvice 可以配置basePackage下的所有controller
//原理是使用AOP对Controller控制器进行增强（前置增强、后置增强、环绕增强，AOP原理请自行查阅）

@ControllerAdvice
public class GlobalException {

	private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	/**
	 * @ExceptionHandler，可以处理异常
	 * 配合@ControllerAdvice 便可以处理全局异常
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public JsonResult resultException(Exception e){
		//e.printStackTrace();
		logger.info("-----> Global-Exception： " + e);
		return JsonResult.fail(e.getMessage());
	}

	@ExceptionHandler(BaseException.class)
	@ResponseBody
	public JsonResult resultException(BaseException e){
		//e.printStackTrace();
		logger.info("-----> Global-BaseException： " + e);
		return JsonResult.fail(e.getCode(), e.getMessage());
	}

}
