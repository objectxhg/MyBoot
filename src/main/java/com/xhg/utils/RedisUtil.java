package com.xhg.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Component
@SuppressWarnings("all")
public class RedisUtil {


	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private DefaultRedisScript<Integer> DefaultRedisScript;

	/**
	 * Redis保证了脚本执行的原子性，所以在当前脚本没执行完之前，别的命令和脚本都是等待状态，所以一定要控制好脚本中的内容，防止出现需要消耗大量时间的内容(逻辑相对简单)。
	 */
	public boolean decrLuaScript(List<String> keys, Object... parames){
		Integer state = 0;
		try {
			state = redisTemplate.execute(DefaultRedisScript, keys,0);

		}catch (IllegalStateException e){
			throw new IllegalStateException("LuaScript-IllegalStateException");
		}

		return state == 0 ? false : true ;
	}

	public boolean decr(String key, Integer testTime){
		boolean flag = true;
		try {
			redisTemplate.watch(key);
			redisTemplate.multi();
			Long decrement = redisTemplate.opsForValue().decrement(key);
			//模拟网络延迟 睡眠的时候在另一台机器上去redis修改个值 从而测试当前事务是否会提交
			if(null != testTime){
				Thread.sleep((long)testTime);
			}
		}catch (InterruptedException e){
			throw new InterruptedException("redis-watch-multi-fail");
		}finally {
			List<Object> execList = redisTemplate.exec();
			if(execList.size() <= 0){
				flag = false;
			}
			return flag;
		}

	}

	public boolean decr(String key, long number){
		boolean flag = true;
		try {
			redisTemplate.watch(key);
			redisTemplate.multi();
			Long decrement = redisTemplate.opsForValue().decrement(key, number);
			//模拟网络延迟 睡眠的时候在另一台机器上去redis修改个值 从而测试当前事务是否会提交
//			Thread.sleep(5000);
		}catch (Exception e){
			throw new Exception("redis-watch-multi-fail");
		}finally {
			List<Object> execList = redisTemplate.exec();
			if(execList.size() <= 0) {
				flag = false;
			}
			return flag;
		}
	}

	public boolean incr(String key){
		boolean flag = true;
		try {
			redisTemplate.watch(key);
			redisTemplate.multi();
			Long decrement = redisTemplate.opsForValue().increment(key);
			//模拟网络延迟 睡眠的时候在另一台机器上去redis修改个值 从而测试当前事务是否会提交
//			Thread.sleep(5000);
		}catch (Exception e){
			throw new Exception("redis-watch-multi-fail");
		}finally {
			List<Object> execList = redisTemplate.exec();
			if(execList.size() <= 0) {
				flag = false;
			}
			return flag;
		}

	}

	public boolean incr(String key, long number){
		boolean flag = true;
		try {
			redisTemplate.watch(key);
			redisTemplate.multi();
			Long decrement = redisTemplate.opsForValue().increment(key, number);
			//模拟网络延迟 睡眠的时候在另一台机器上去redis修改个值 从而测试当前事务是否会提交
//			Thread.sleep(5000);
		}catch (Exception e){
			throw new Exception("redis-watch-multi-fail");
		}finally {
			List<Object> execList = redisTemplate.exec();
			if(execList.size() <= 0) {
				flag = false;
			}
			return flag;
		}

	}
	
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
     * 判断key是否存在
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

		/**
		 * TimeUnit.DAYS          天
		 * TimeUnit.HOURS         小时
		 * TimeUnit.MINUTES       分钟
		 * TimeUnit.SECONDS       秒
		 * TimeUnit.MILLISECONDS  毫秒
		 */
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


		//List存储
		public void setList(String key,List<Object> value2){
			redisTemplate.opsForList().leftPush(key,value2);
		}
		//List存储(设置失效时间)
		public void setList(String key,List<Object> value2,Integer expise){
			redisTemplate.opsForList().leftPush(key,value2);
			redisTemplate.expire(key, expise, TimeUnit.SECONDS);
		}
		//List获取
		@SuppressWarnings("unchecked")
		public List<Object> getList(String key){
			return (List<Object>)redisTemplate.opsForList().leftPop(key);
		}



		//Map存储
		public void setMap(String key, Map<Object,Object> value3){
			redisTemplate.opsForHash().putAll(key,value3);
		}
		//Map存储(设置失效)
		public void setMapAndTimeOut(String key,Map<Object,Object> value3,Integer expise){
			redisTemplate.opsForHash().putAll(key,value3);
			redisTemplate.expire(key, expise, TimeUnit.SECONDS);
		}
		//Map获取
		public Map<Object,Object> getMap(String key){
			return redisTemplate.opsForHash().entries(key);
		}


}
