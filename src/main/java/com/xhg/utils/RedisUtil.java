package com.xhg.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RedisUtil {
	
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	
	public boolean expire(String key, long time) {
		        try {
		            if (time > 0) {
		                redisTemplate.expire(key, time, TimeUnit.SECONDS);
		            }
		            return true;
		        } catch (Exception e) {
		            e.printStackTrace();
		            return false;
		        }
		    }
	
	/**
             *  判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
	public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	/**
	     * 删除缓存
	     * @param key 可以传一个值 或多个
	     */
	 @SuppressWarnings("unchecked")
	     public void del(String... key) {
	         if (key != null && key.length > 0) {
	             if (key.length == 1) {
	                 redisTemplate.delete(key[0]);
	             } else {
	                 redisTemplate.delete(CollectionUtils.arrayToList(key));
	             }
	         }
	     }
	
	// ============================String=============================
	    /**
	             * 普通缓存获取
	     * @param key 键
	     * @return 值
	     */
	    public Object get(String key) {
	        return key == null ? null : redisTemplate.opsForValue().get(key);
	    }
	    
	    /**
	         * 普通缓存放入
	         * @param key 键
	         * @param value 值
	         * @return true成功 false失败
	         */
	        public boolean set(String key, Object value) {
	            try {
	                redisTemplate.opsForValue().set(key, value);
	                return true;
	            } catch (Exception e) {
	                e.printStackTrace();
	                return false;
	            }
	        }
	        
//			TimeUnit.DAYS          天
//			TimeUnit.HOURS         小时
//			TimeUnit.MINUTES       分钟
//			TimeUnit.SECONDS       秒
//			TimeUnit.MILLISECONDS  毫秒
	        public boolean set(String key, Object value, long time) {
	        	        try {
	        	            if (time > 0) {
	        	                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
	        	            } else {
	        	                set(key, value);
	        	            }
	        	            return true;
	        	        } catch (Exception e) {
	        	            e.printStackTrace();
	        	            return false;
	        	        }
	        	    }
	        
	     // ================================Map=================================
	            /**
	             * HashGet
	             * @param key 键 不能为null
	             * @param item 项 不能为null
	             * @return 值
	             */
	            public Object hget(String key, String item,long time) {
	            	if (time > 0) {
	            		                expire(key, time);
	            		            }
	                return redisTemplate.opsForHash().get(key, item);
	            }
}
