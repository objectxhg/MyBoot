package com.xhg;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xhg.mapper.SysUserMapper;
import com.xhg.pojo.sysUser;
import com.xhg.utils.RedisUtil;
import com.xhg.utils.SnowflakeIdWorker;
import com.xhg.utils.SnowflakeUtil;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    private DefaultRedisScript redisScript;

    @Resource
    private DefaultRedisScript LockScript;

    @Resource
    private DefaultRedisScript LockDelScript;

    public DemoTest() {
    }

    @Test
    public void SnowflakeTest() {
        System.out.println("SnowflakeID: " + SnowflakeUtil.getSnowflakeID());

    }

    /**
     * redis 管道 set
     */
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

    /**
     * get
     */
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
        /**
         * DefaultRedisScript： 为注入需要执行的lua脚本Bean
         * keys: 需要操作的key
         * 0：传入的参数
         * 我们点进去 execute 源码能看到 第三个参数为 Object... args 说明可以传多个参数
         */
        Integer state = (Integer) redisTemplate.execute(redisScript, keys,0);

        System.out.println("state : " + state);

    }

    /**
     * redis分布式锁 模拟
     */

    @Test
    public void RedisLockTest() {

        List<String> keys = new ArrayList<>();
        keys.add("lock_value");
        /**
         * 过期时间： 这里的时间单位是秒
         */
        for (int i = 0; i < 20; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long parame = SnowflakeIdWorker.generateId();
                    Integer lock = (Integer) redisTemplate.execute(LockScript, keys,parame, 15);
                    System.out.println("lock: " + lock);
                    if(lock == 1){
                        System.out.println("加锁成功");
                        try {
                            /**
                             * 模拟 number为商品库存
                             */
                            if(Integer.parseInt(JSON.toJSONString(redisUtil.get("number"))) > 0){

                                System.out.println("【线程】---------> " + Thread.currentThread().getName() + " : " + redisUtil.decr("number", null));
                            }
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            /**
                             * 逻辑结束 开始解锁
                             */
                            Integer state = (Integer) redisTemplate.execute(LockDelScript, keys,parame);
                            if(state == 1) System.out.println("解锁成功");

                            else System.out.println("解锁失败");
                        }
                    }
                    if(lock == 0){
                        System.out.println("加锁失败");
                    }
                }
            }).run();
        }
    }

}