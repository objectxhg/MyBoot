package com.xhg.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public DefaultRedisScript<Integer> redisScript() {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/script/LuaRedis.lua")));
        redisScript.setResultType(Integer.class);
        return redisScript;
    }
}

