package com.xhg.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @Author xiaoh
 * @create 2020/11/19 10:35
 */

@Configuration
public class LuaConfiguration {

    @Bean
    /**
     *  @Primary : 当有多个相同类型的bean时 通过Autowired 类型注入时默认使用 优先使用 Primary 标注的bean
     *
     *  @Resource ： 通过Resource名称注入时， 根据方法名注入
     */
    @Primary
    public DefaultRedisScript<Integer> redisScript() {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/script/LuaRedis.lua")));
        redisScript.setResultType(Integer.class);
        return redisScript;
    }

    /**
     * redis
     * @return
     */
    @Bean
    public DefaultRedisScript<Integer> DecrScript() {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/script/LuaRedis-decr.lua")));
        redisScript.setResultType(Integer.class);
        return redisScript;
    }

    /**
     * redis 分布式锁 加锁
     * @return
     */
    @Bean
    public DefaultRedisScript<Integer> LockScript() {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/script/LuaRedis-lock.lua")));
        redisScript.setResultType(Integer.class);
        return redisScript;
    }

    /**
     * redis 分布式锁 加锁 加强版
     * @return
     */
    @Bean
    public DefaultRedisScript<Integer> LockScriptNew() {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/script/LuaRedis-lock-new.lua")));
        redisScript.setResultType(Integer.class);
        return redisScript;
    }
    /**
     * redis 分布式锁 解锁
     * @return
     */
    @Bean
    public DefaultRedisScript<Integer> LockDelScript() {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/script/LuaRedis-lock-del.lua")));
        redisScript.setResultType(Integer.class);
        return redisScript;
    }
}

