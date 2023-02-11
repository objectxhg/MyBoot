package com.xhg;

import java.io.*;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import com.xhg.mapper.OrderMapper;
import com.xhg.mapper.SysUserMapper;
import com.xhg.pojo.ApiRequest;
import com.xhg.pojo.PointGeo;
import com.xhg.utils.MapPoint;
import com.xhg.utils.ShowHttpResponseHeaders;
import com.xhg.utils.code.HexByteUtil;
import com.xhg.vo.AccessToken;
import com.xhg.utils.RedisUtil;
import com.xhg.vo.TemplateDataVo;
import org.apache.http.client.utils.DateUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xhg.config.rabbitMQ.Sender;
import com.xhg.pojo.sysUser;
import com.xhg.threadPool.service.AsyncTaskService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyBootApplication.class})
public class MyBootApplicationTests {
	
	@Autowired
	private Sender sender;

	@Autowired(required = false)
	private SysUserMapper sysUserMapper;

	@Autowired
    private AsyncTaskService asyncTaskService;

	@Autowired(required = false)
	private OrderMapper orderMapper;

	@Autowired
	private RedisUtil redisUtil;


	@Test
    public void contextLoads() {

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

		AtomicReference<Integer> num = new AtomicReference<>(10);

		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);

		list.forEach( i ->{
			if(i == 2){
				return;
			}
			num.updateAndGet(v -> v + 1);

		});

		System.out.println("num:" + num);
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
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;


	@Test
    public void  redisTest() throws InterruptedException {

//		String number = JSON.toJSONString(redisUtil.get("qwdas"));
//
//		System.out.println("-------> number:" + number);
//		if(null == number){
//			System.out.println("true");
//		}
//		if("null".equals(number)){
//			System.out.println("false");
//		}
//
//		System.out.println("number:length:" + number.length());

//
//		System.out.println("number incr:" + redisTemplate.opsForValue().increment("number", 1L));
//
//		System.out.println("iflybank:" + redisTemplate.opsForValue().increment("iflybank", 1L));
//
//		System.out.println("iflybank:" + redisTemplate.opsForValue().increment("iflybank"));
		//获取key的过期时间
//		System.out.println("getExpire:" + redisTemplate.opsForValue().getOperations().getExpire("xhg"));

//		1648800315292-1648799986446
//		1648800336897-1648799986446

    }

	/**
	 * 获取微信用户手机号 第一步
	 * 获取用户的微信id
	 * @throws Exception
	 */
	@Test
	@SuppressWarnings("all")
	public void getWeiXinInfo() throws Exception {

		String appid = "wx82f903cd9b8967c6";
		String secret = "5f14bdd482966d1bd0ae6292abda9133";

		//微信前端获取
		String js_code = "053Fay0w3uiCTU2oTe1w387kko1Fay0B";

		String url = "https://api.weixin.qq.com/sns/jscode2sessionjscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + js_code + "&grant_type=authorization_code";
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

	/**
	 *
	 * 获取微信用户手机号 第二步
	 * 解析微信返回的手机号密文 解密用户手机号码
	 *
	 */
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

	/**
	 * 小程序订阅 获取 Access_token 第一步
	 * parame :{
	 * 	appid 小程序id
	 * 	appsecret 小程序密钥
	 * 	}
	 */
	@Test
	public void getAccess_token(){
		RestTemplate restTemplate = new RestTemplate();
		//获取access_token
		String appid = "wx82f903cd9b8967c6";
		String appsecret = "5f14bdd482966d1bd0ae6292abda9133";

		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + appsecret;
		String json = restTemplate.getForObject(url, String.class);
		//System.out.println("【微信返回的JSON】:------------" + json);
//		AccessToken accessToken = new Gson().fromJson(json, AccessToken.class);
		Map<String, Object> map = new Gson().fromJson(json, Map.class);
		System.out.println("【Access_token】:" + map.get("access_token"));

	}

	/**
	 * 小程序订阅 获取 Access_token 第二步
	 * parame :{
	 * 	appid 小程序id
	 * 	appsecret 小程序密钥
	 * 	}
	 */
	@Test
	public void sendMessage(){
		RestTemplate restTemplate = new RestTemplate();
		//获取access_token
		String appid = "wx82f903cd9b8967c6";
		String appsecret = "5f14bdd482966d1bd0ae6292abda9133";

		String access_token = "37_sYifrzGNdiQt9XvjPNkqHPmBsNvNy3xlZMD8ZlE8IivLUKps5F-Wph81wrY0R2WKqH0_WCKQzsOQtwax33kmjcH4qi2c0VVmir_kxe0tpbDtwNAdCIKl-nc4SVwHZRkfoMmjD3tHpDY3j1rWFKXgAEAUZM";
		String openid = "oPuv_47TCRS-5852kQtp7fn4ZTaA";
		String template_id = "NMR1iGcEIWYFBKrjnbg5w_4Yg5Y4DTJnULRAF9WZrms";
		String page = "/pages/manage/detailsManage/detailsManage?workid=43";
		/**
		 * 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
		 */
		String miniprogram_state = "trial";


		Map<String, TemplateDataVo> dataVo = new HashMap<>();

		TemplateDataVo templateDataVo1 = new TemplateDataVo();
		templateDataVo1.setValue("吴伶");
		dataVo.put("thing1", templateDataVo1);

		TemplateDataVo templateDataVo2 = new TemplateDataVo();
		templateDataVo2.setValue("今天吃了吗");
		dataVo.put("thing2", templateDataVo2);



		JSONObject postData = new JSONObject();
		//postData.put("access_token", access_token);
		postData.put("touser", openid);
		postData.put("template_id", template_id);
		postData.put("page", page);
		postData.put("data", dataVo);
		postData.put("miniprogram_state",miniprogram_state);

		System.out.println("【postData】：" + postData);
		String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + access_token;
		ResponseEntity<String> resultData = restTemplate.postForEntity(sendUrl, postData, String.class);
		System.out.println("【resultData】：" + resultData.getBody());

		Map maps = (Map)JSON.parse(resultData.getBody());
		if(maps.get("errmsg").equals("ok")){
			System.out.println("发送成功");
		}else{
			System.out.println("发送失败");
		}

	}

	/**
	 * 测试存储过程
	 */
	@Test
	public void addCallsql(){
//		System.out.println(orderMapper.addlog("肖华刚","123466","15573679072"));

		float num = (float) 0.35123;

		String str = Float.toString(num);

		if(str.contains(".")){
			String[] arr = str.split("\\.");
			str = arr[0] + "." + arr[1].substring(0, 2);
			Float foat = Float.valueOf(str);
			System.out.println(foat);
		}




	}


	@Test
	public void testAPI(){
		{

			RestTemplate restTemplate = new RestTemplate();

			String url = "https://fintech.changsha.gov.cn/fins-pre-gateway/gateway/unifiedService/qthf";

			JSONObject jsonObject = new JSONObject();



			ResponseEntity<Map> responseEntity =  restTemplate.postForEntity(url, jsonObject, Map.class);

			System.out.println(responseEntity.getBody());

		}
	}



	@Test
	public void aaa() throws ParseException {

		String endDate = "2022-03-08 10:25:00";

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date end = simpleDateFormat.parse(endDate);

		System.out.println(end.compareTo(new Date()));

	}
	@Test
	public void times() throws ParseException{
		long now = System.currentTimeMillis();
		SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd");
		long overTime = (now - (sdfOne.parse(sdfOne.format(now)).getTime()))/1000;
		//当前毫秒数
		System.out.println(now);
		//当前时间  距离当天凌晨  秒数 也就是今天过了多少秒
		System.out.println(overTime);
		//当前时间  距离当天晚上23:59:59  秒数 也就是今天还剩多少秒
		long TimeNext = 24*60*60 - overTime;
		System.out.println("当前时间距离凌晨12点还剩：{} 秒"+TimeNext);
		//当天凌晨毫秒数
		System.out.println(sdfOne.parse(sdfOne.format(now)).getTime());
		//当天凌晨日期
		SimpleDateFormat sdfTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.print(sdfTwo.format(sdfOne.parse(sdfOne.format(now)).getTime()));
	}

	@Autowired
	MapPoint mapPoint;

	/**
	 * txt文件按行读取
	 */
	public void readTxt(String txt){

		if(org.apache.commons.lang3.StringUtils.isEmpty(txt)) txt = "E:\\testFile\\test3.txt";

		File file = new File(txt);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempStr;
			List<String> list = new ArrayList<>();
			while ((tempStr = reader.readLine()) != null) {
				list.add(tempStr);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	/**
	 * 网格坐标匹配
	 */
	@Test
	public void mapPointCheck(){

		String str = "";
//
//		RestTemplate restTemplate = new RestTemplate();
//
//		String url = "https://restapi.amap.com/v3/geocode/geo?" +
//				"address=湖南省长沙市岳麓区观沙岭街道滨江路188号湘江基金小镇13%23栋3层" +
//				"&output=JOSN" +
//				"&key=b3fca3378dc818dad0d4e55320ec5558";
//
//		Map<String, Object> map = new HashMap<>();
//		map.put("output","JOSN");
//		map.put("key","b3fca3378dc818dad0d4e55320ec5558");
//		map.put("address","湖南省长沙市岳麓区观沙岭街道滨江路188号湘江基金小镇");
//
//		ResponseEntity<JSONObject> res = restTemplate.getForEntity(url, JSONObject.class);
//		ShowHttpResponseHeaders.showHeaders(res.getHeaders());
//		Gson gson = new Gson();
//
//		Map<String, Object> valaueMap = gson.fromJson(String.valueOf(res.getBody()), Map.class);
//		if("1".equals(valaueMap.get("status"))){
//			ArrayList list = (ArrayList) valaueMap.get("geocodes");
//			Map<String, Object> geocodesMap = (Map<String, Object>) list.get(0);
//			System.out.println("目标经纬度:" + geocodesMap.get("location"));
//			str = geocodesMap.get("location").toString();
//		}

		str = "113.013541,28.194162";

		if(StringUtils.isNotBlank(str)){
			//目标经度
			double targetY = Double.parseDouble(str.split(",")[0]);
			//目标纬度
			double targetX = Double.parseDouble(str.split(",")[1]);

			PointGeo pointGeo = new PointGeo(targetY, targetX);

			List<PointGeo> pointGeoList = new ArrayList<>();
			pointGeoList.add(new PointGeo(112.9833984375, 28.1964111328125));
			pointGeoList.add(new PointGeo(112.994384765625, 28.1964111328125));
			pointGeoList.add(new PointGeo(112.9833984375, 28.201904296875));
			pointGeoList.add(new PointGeo(112.994384765625,28.201904296875));

			boolean flag =  mapPoint.isPnpoly(pointGeo, pointGeoList);

			System.out.println("目标是否在区域内：" + flag);
		}
	}

	@Autowired
	private RestTemplate restTemplateUtil;

	/**
	 * gson
	 */
	@Test
	public void restTemplateTest(){

		String result = "{\"words_result\":{\"姓名\":{\"location\":{\"top\":277,\"left\":618,\"width\":372,\"height\":164},\"words\":\"张三\"},\"民族\":{\"location\":{\"top\":568,\"left\":1262,\"width\":101,\"height\":126},\"words\":\"汉\"},\"住址\":{\"location\":{\"top\":1082,\"left\":552,\"width\":1392,\"height\":486},\"words\":\"住址住址住址\"},\"公民身份号码\":{\"location\":{\"top\":1733,\"left\":1105,\"width\":1749,\"height\":157},\"words\":\"***************\"},\"出生\":{\"location\":{\"top\":814,\"left\":561,\"width\":969,\"height\":132},\"words\":\"********\"},\"性别\":{\"location\":{\"top\":561,\"left\":580,\"width\":110,\"height\":129},\"words\":\"男\"}},\"idcard_number_type\":1,\"words_result_num\":6,\"image_status\":\"normal\",\"log_id\":1453528763137919643}";

		JsonParser jp = new JsonParser();
		//将json字符串转化成json对象
		JsonObject jo = jp.parse(result).getAsJsonObject();
		//获取姓名对应的值
		String height = jo.get("words_result").getAsJsonObject()
				.get("姓名").getAsJsonObject()
				.get("location").getAsJsonObject()
				.get("height").getAsString();

		System.out.println(height);
	}

	@Test
	public void regexDemo(){

		long startTime = System.currentTimeMillis();
		System.out.println(hasChinese("nsiubwuid"));
		long endTime = System.currentTimeMillis();

		System.out.println(endTime-startTime);

		String str = "e527b13d1dbcc6ecf1e620e9ffd354cd35a44d59b7c6db87aed87f087e4c1b97b285af2421fdb18d29b4c7e7c8402860b72566e82363b1fea7d24ca5626b1de9";

		byte[] bytes = HexByteUtil.hexToByte(str);

		System.out.println("bytes:" + Arrays.toString(bytes));

		System.out.println("str:" + HexByteUtil.byteToHex(bytes));


	}

	/**
	 * 根据正则表达式判断字符是否为汉字
	 * 字符串中包含汉字时返回true
	 */
	public static boolean hasChinese(String value) {

		// 汉字的Unicode取值范围
		String regex = "[\u4e00-\u9fa5]";
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(value);

		return match.find();

	}

}

