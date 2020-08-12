package com.xhg.config.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @Author xiaoh
 * @create 2020/8/12 11:42
 */
@Data
public class RedisSentinelConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.database}")
    private int database;

    //pool映射
    @Value("${spring.redis.lettuce.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    private long maxWait;

    //setinel映射
    //@ConfigurationProperties(prefix="spring.redis3.sentinel")映射
    List<String> nodes;

    @Value("${spring.redis.sentinel.master}")
    private String master;

}

