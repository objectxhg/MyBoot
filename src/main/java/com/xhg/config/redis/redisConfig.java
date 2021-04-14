package com.xhg.config.redis;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.Duration;


@Configuration
public class redisConfig extends CachingConfigurerSupport {

    /**
     * springboot2.x 使用LettuceConnectionFactory 代替 RedisConnectionFactory
     * application.yml配置基本信息后,springboot2.x  RedisAutoConfiguration能够自动装配
     * LettuceConnectionFactory 和 RedisConnectionFactory 及其 RedisTemplate
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> RedisTemplate(LettuceConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        //开启事务 单节点
        redisTemplate.setEnableTransactionSupport(true);
        //key的序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //value的序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //key的hash序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //value的hash序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }



    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, java.lang.reflect.Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append("-" + obj.toString());
                }
                return sb.toString();
            }
        };

    }

    /**
     * 缓存管理器 @SuppressWarnings("rawtypes")
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration
                // 默认缓存设置
                .defaultCacheConfig()
                // 设置key为String
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
                // 设置value为自动转Json的Object
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                // 不缓存null
                .disableCachingNullValues()
                //配置缓存时间 分钟
                .entryTtl(Duration.ofMinutes(30));
        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder
                // Redis连接工厂
                .fromConnectionFactory(redisTemplate.getConnectionFactory())
                // 缓存配置
                .cacheDefaults(defaultCacheConfiguration)
                // 配置同步修改或删除 put/evict
                .transactionAware()
                .build();

        return redisCacheManager;
    }

}
