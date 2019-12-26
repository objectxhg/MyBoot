package com.xhg;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xhg.pojo.User;

@SpringBootTest
class MyBootApplicationTests {
	
	
	public static void main(String[] args) {
		
		test1();
		
	}
	public static void test1() {
		
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
