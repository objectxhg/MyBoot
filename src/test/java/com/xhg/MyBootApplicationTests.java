package com.xhg;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xhg.config.rabbitMQ.Sender;
import com.xhg.pojo.Message;
import com.xhg.pojo.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyBootApplication.class})
class MyBootApplicationTests {
	
	@Autowired
	private Sender sender;
	
	@Test
	public void test2() {
		
		User user = new User();
		user.setAddress("长沙");
		sender.send(user);
	}
	
	
	
	@Test
	public void test1() {
		
		User user = new User();
		user.setId(1);
		user.setMobile("15573679072");
		user.setPwd("123456");
		user.setAddress("湖南长沙");
		user.setBirthday("0816");
		
		String userStr = JSON.toJSONString(user,
				SerializerFeature.PrettyFormat,
				//单引号输出
				SerializerFeature.UseSingleQuotes,
				//输出为空的字段
				SerializerFeature.WriteNullStringAsEmpty);
		
		System.out.println(userStr);
		
		String jsonString = "{\"birthday\":"
				+ "\"2018-08-17 14:38:38\",\"id\":11,\"address\":\"湖南常德\"}";
	
		User userJson = JSON.parseObject(jsonString, User.class);
		
		System.out.println(userJson);
	}

}
