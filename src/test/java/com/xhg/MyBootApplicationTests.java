package com.xhg;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.xhg.utils.RedisUtil;

@SpringBootTest
class MyBootApplicationTests {
	
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Test
	void contextLoads() {
		
		System.out.println(redisUtil.get("name"));
	}

}
