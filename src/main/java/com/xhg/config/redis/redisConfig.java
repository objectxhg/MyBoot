package com.xhg.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.Duration;


@Configuration
public class redisConfig extends CachingConfigurerSupport {

    /**
     * Redis配置value的序列化方式
     */
    @Bean
    public RedisTemplate<String, Object> RedisTemplate(RedisConnectionFactory factory) {
        // 创建redis模板
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jackson = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域以及修饰符范围
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定要序列化的输入类型
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson.setObjectMapper(om);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        // 使用jackson来序列化和反序列化redis的value值
        template.setValueSerializer(jackson);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public KeyGenerator MykeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, java.lang.reflect.Method method, Object... params) {

                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());

//                for (Object obj : params) {
//                    sb.append(obj.toString());
//                }

                sb.append((Integer) params[0]);

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

//    @Bean
//    @SuppressWarnings("all")
//    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
//
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
//        //开启事务 单节点
//        redisTemplate.setEnableTransactionSupport(true);
//        //key的序列化
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        //value的序列化
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        //key的hash序列化
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        //value的hash序列化
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        return redisTemplate;
//    }


}
