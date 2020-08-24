package com.xhg;

import java.io.BufferedInputStream;

import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import java.security.Security;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.xhg.utils.RedisUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xhg.config.rabbitMQ.Sender;
import com.xhg.pojo.sysUser;
import com.xhg.threadPool.service.AsyncTaskService;
import javax.net.ssl.HttpsURLConnection;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyBootApplication.class})
public class MyBootApplicationTests {
	
	@Autowired
	private Sender sender;
	
	@Autowired
    private AsyncTaskService asyncTaskService;

	@Autowired
	private RedisUtil redisUtil;


	@Test
    public void contextLoads() {
    }
	 
	@Test
    public void threadTest() {
        for (int i = 0; i < 20; i++) {
            asyncTaskService.executeAsyncTask(i);
        }
    }
	
	@Test
	public void test3() throws Exception {
		URL urlfile = new URL("http://jsqing.cn/MarriageLawServiceDocument/Upload/Contract/File/044_20190509155939.mp3");
		//File file = new File("C:\\music\\test2.mp3");
		//URL urlfile = file.toURI().toURL();
		URLConnection con = urlfile.openConnection();
		int b = con.getContentLength();// 得到音乐文件的总长度
		BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
//		Bitstream bt = new Bitstream(bis);
//		Header h = bt.readFrame();
//		int time = (int) h.total_ms(b);
//		System.out.println(time / 1000);
	}
	
	// Java8 stream测试demo
	@Test
	public void test4() {

		String[] arrStr = {"1","2","3","4","5"};
							 //Arrays.stream(arrStr);
		List<Integer> list = Stream.of(arrStr).map(Integer::parseInt).collect(Collectors.toList());
		System.out.println(list);
		
	}
	
	//RabbitMQ测试demo
	@Test
	public void test2() {
		
		sysUser user = new sysUser();
		user.setId(1);
		user.setAddress("长沙");
		user.setUsername("钢铁侠");
		sender.send(user);
	}
	
	
	// fastjson测试demo
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
		
		String jsonString = "{\"birthday\":\"2018-08-17 14:38:38\",\"id\":11,\"address\":\"湖南常德\"}";
	
		sysUser userJson = JSON.parseObject(jsonString, sysUser.class);
		
		System.out.println(userJson);
	}

	@Test
    public void  redisTest(){

		String number = JSON.toJSONString(redisUtil.get("number"));
		System.out.println("-------> number:" + number);
		System.out.println("-------> incrbyKey:" + redisUtil.incr("number"));
    }

    @Test
	public void getWeiXinInfo() throws Exception {
		String appid = "wx43cf326b2797ca99";

		String secret = "05d6b6a8bcf0baf1e97dc51b424e6986";

		String js_code = "043RLoml2KWqp54oaSnl28D3g83RLoml";


		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + js_code + "&grant_type=authorization_code";
		URL reqURL = new URL(url); //创建URL对象
		HttpsURLConnection httpsConn = (HttpsURLConnection) reqURL.openConnection();

		//取得该连接的输入流，以读取响应内容
		InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());
		char[] cbuf = new char[512];

		//读取服务器的响应内容并显示
		int respInt = -1;
		while ((respInt = insr.read(cbuf)) > 0) {
		}

		System.out.println("----------> " + respInt);

		Map<String, Object> map = (Map<String, Object>) JSON.parse(new String(cbuf));

		if (map.containsKey("errcode")) {
			System.out.println(map.get("errcode").toString() + " " + map.get("errmsg").toString());
		}else{
			System.out.println(map);
		}

	}

	@Test
	public void getWeiXinTelephone() throws Exception {

		String sessionKey = "xf986D7m2uOuxKmMDnVDeA==";

		String encryptedData = "Mi3rc93crR4gLFCOBNup2yeenT54dAmTqQVo9HH5KQ6zgA7MW5r6z1bQlY5LtXh91Cbml1Di9sgFRVyWIVkrJCyuLcyh+7ihnBpgVsuAHJxcjPbcPxQUN59Gjl/GTCwAw5eYqN1HdnygNGv7hL/2sae1BLa1blEsPokNyddIzCgrSwDyvpNTzoEWT8xQasuL0zcupMdHmOLHriEFprNscQ==";

		String iv = "w0mXs2kUirMVvoMsn3grfg==";

		// 被加密的数据
		byte[] dataByte = Base64.decode(encryptedData);
		// 加密秘钥
		byte[] keyByte = Base64.decode(sessionKey);
		// 偏移量
		byte[] ivByte = Base64.decode(iv);


		try {
			// 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
			int base = 16;
			System.out.println("keyByte:" + keyByte);
			if (keyByte.length % base != 0) {
				int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
				byte[] temp = new byte[groups * base];
				Arrays.fill(temp, (byte) 0);
				System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
				keyByte = temp;
			}
			// 初始化
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
			parameters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
				String result = new String(resultByte, "UTF-8");
				System.out.println("result:" + result);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
