package com.xhg;


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
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author xiaoh
 * @create 2020/9/11 10:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyBootApplication.class})
public class DemoTest {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void SnowflakeTest(){
        System.out.println("SnowflakeID: " + SnowflakeUtil.getSnowflakeID());

    }

    @Test
    public void volatileTestDemo(){

        String valueStr = "idea";
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.sAdd("good".getBytes(), "idea".toString().getBytes());
                return null;
            }
        });
    }

}

