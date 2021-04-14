package com.xhg;

import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableScheduling //启动定时任务注解
/**
 * 
 *	@EnableTransactionManagement 开启事务管理 
 *	Service中，被 @Transactional 注解的方法，将支持事务。如果注解在类上，则整个类的所有方法都默认支持事务
 *
 *	@EnableAsync 开启Springboot对于异步任务的支持
 */
@EnableTransactionManagement 
@EnableAsync
@EnableCaching
@MapperScan(basePackages="com.xhg.mapper")//mybatis接口扫描
public class MyBootApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MyBootApplication.class, args);
	}
	

}
