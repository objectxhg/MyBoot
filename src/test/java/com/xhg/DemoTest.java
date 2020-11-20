package com.xhg;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xhg.mapper.SysUserMapper;
import com.xhg.pojo.sysUser;
import com.xhg.utils.RedisUtil;
import com.xhg.utils.SnowflakeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author xiaoh
 * @create 2020/9/11 10:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyBootApplication.class})
public class DemoTest {

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DefaultRedisScript<Integer> DefaultRedisScript;

    @Test
    public void SnowflakeTest() {
        System.out.println("SnowflakeID: " + SnowflakeUtil.getSnowflakeID());

    }

    @Test
    public void volatileTestDemo() {

        List<sysUser> list = userMapper.findAll();
        redisTemplate.executePipelined(new RedisCallback<List<sysUser>>() {
            @Override
            public List<sysUser> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                connection.set("userList".getBytes(), JSON.toJSONString(list).getBytes());
                connection.set("test".getBytes(), "asdsdf".getBytes());
                return null;
            }
        });

    }


    @Test
    public void userList() {

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        String s = (String) redisTemplate.boundValueOps("userList").get();
        //String s = (String) redisUtil.get("userList");
        if (null != s) {
            List<sysUser> list = JSON.parseArray(s, sysUser.class);
            for (sysUser user : list) {
                System.out.println(user.getUsername());
            }
        } else {
            System.out.println("null");
        }
    }

    /**
     * redis脚本 lua测试
     */
    @Test
    public void redisScriptDemoTest() {

        List<String> keys = new ArrayList<>();
        keys.add("number");

        Integer number = redisTemplate.execute(DefaultRedisScript, keys,0);

        System.out.println("number : " + number);

    }

}