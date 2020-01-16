package com.xhg;

import java.io.BufferedInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
import com.xhg.pojo.sysUser;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyBootApplication.class})
class MyBootApplicationTests {
	
	@Autowired
	private Sender sender;
	
	
	@Test
	public void test3() throws Exception {
		URL urlfile = new URL("http://jsqing.cn/MarriageLawServiceDocument/Upload/Contract/File/044_20190509155939.mp3");
		//File file = new File("C:\\music\\test2.mp3");
		//URL urlfile = file.toURI().toURL();
		URLConnection con = urlfile.openConnection();
		int b = con.getContentLength();// 得到音乐文件的总长度
		BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
		Bitstream bt = new Bitstream(bis);
		Header h = bt.readFrame();
		int time = (int) h.total_ms(b);
		System.out.println(time / 1000);
	}
	
	@Test
	public void test2() {
		
		sysUser user = new sysUser();
		user.setAddress("长沙");
		sender.send(user);
	}
	
	
	
	@Test
	public void test1() {
		
		sysUser user = new sysUser();
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
	
		sysUser userJson = JSON.parseObject(jsonString, sysUser.class);
		
		System.out.println(userJson);
	}

}
