package com.sinovoice.hcicloud.nettysocketiofirst.common;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class RedisOpr {
	
	private static Logger log = Logger.getLogger(RedisOpr.class);

	/**
	 * @Title: saveObject 
	 * @Description: 保存对象 
	 * @param key
	 * @param object
	 * @return void
	 * @throws
	 */
	public static void saveObject(String key, Object object){
		Jedis jedis= RedisUtil.getJedis();
		try{
			jedis.set(key.getBytes(), SerializeUtil.serialize(object));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			RedisUtil.returnResource(jedis);
		}
	}
	
	/**
	 * @Title: getObject 
	 * @Description: 获取对象 
	 * @param key
	 * @return void
	 * @throws
	 */
	public static Object getObject(String key){
		Jedis jedis= RedisUtil.getJedis();
		try{
			byte[] object =  jedis.get(key.getBytes());
			if(object ==null){
				return null;
			}
			return SerializeUtil.unserialize(object);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * @Title: lpush 
	 * @Description: 向链表末尾添加数据
	 * @param key
	 * @param value
	 * @return void
	 * @throws
	 */
	public static void lpush(String key, String value){
		Jedis jedis= RedisUtil.getJedis();
		try{
			jedis.lpush(key, value);
		}catch(Exception e){
			log.error(e.getMessage());
			MDC.clear();
		}finally{
			RedisUtil.returnResource(jedis);
		}
	}
	
	/**
	 * @Title: llen 
	 * @Description: 获得链表长度 
	 * @param key
	 * @return long
	 * @throws
	 */
	public static long llen(String key){
		Jedis jedis= RedisUtil.getJedis();
		try{
			return jedis.llen(key.getBytes());
		}catch(Exception e){
			log.error(e.getMessage());
			MDC.clear();
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return 0;
	}
	/**
	 * @Title: RemoveObject 
	 * @Description: 删除key 
	 * @param key
	 * @return void
	 * @throws
	 */
	public static void removeObject(String key){
		Jedis jedis= RedisUtil.getJedis();
		try{
		jedis.del(key.getBytes());
		}catch(Exception e){
			log.error("error:"+e.getMessage());
			MDC.clear();
		}finally{
		RedisUtil.returnResource(jedis);
		}
	}
	/**
	 * @Title: rPop 
	 * @Description: 取出并删除List最后一个元素 
	 * @param key
	 * @return String
	 * @throws
	 */
	public static String rPop(String key){
		Jedis jedis= RedisUtil.getJedis();
		try{
		String object = jedis.rpop(key);
		if(object==null)
			return null;
		return object;
		}catch(Exception e){
			log.error("error:"+e.getMessage());
			MDC.clear();
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * @Title: isExistsKey 
	 * @Description: 判断key是否存在
	 * @param key
	 * @return boolean
	 * @throws
	 */
	public static boolean isExistsKey(String key){
		Jedis jedis= RedisUtil.getJedis();
		try{
			return jedis.exists(key);
		}catch(Exception e){
			log.error("error:"+e.getMessage());
			MDC.clear();
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return false;
	}
	
	
	/**
	 * @Title: getRedisQueueObject 
	 * @Description: 获得告警队列对象
	 * @param key
	 * @return List<Integer>
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static List<Integer> getRedisQueueObject(String key){
		Jedis jedis= RedisUtil.getJedis();
		try{
			byte[] object=jedis.get(key.getBytes());
			if(object==null)
				return null;
			return (List<Integer>) SerializeUtil.unserialize(object);
		}catch(Exception e){
			log.error("error:"+e.getMessage());
			MDC.clear();
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * @Title: getRedisWarnState 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param key
	 * @return Integer
	 * @throws
	 */
	public static Integer getRedisWarnState(String key){
		Jedis jedis= RedisUtil.getJedis();
		try{
			byte[] object=jedis.get(key.getBytes());
			if(object==null)
				return null;
			return (Integer)SerializeUtil.unserialize(object);
		}catch(Exception e){
			log.error("error:"+e.getMessage());
			MDC.clear();
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * @Title: getAllKeyByPattern 
	 * @Description: 通过正则表达式获得key
	 * @param pattern
	 * @return List<String>
	 * @throws
	 */
	public static Set<String> getAllKeyByPattern(String pattern){
		Jedis jedis= RedisUtil.getJedis();
		try{
			return jedis.keys(pattern);
		}catch(Exception e){
			log.error("error:"+e.getMessage());
			MDC.clear();
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * @Title: getString 
	 * @Description: redis获得string
	 * @param key
	 * @return String
	 * @throws
	 */
	public static String getString(String key){
		Jedis jedis= RedisUtil.getJedis();
		try{
			byte[] object=jedis.get(key.getBytes());
			if(object==null)
				return null;
			return (String) SerializeUtil.unserialize(object);
		}catch(Exception e){
			log.error("error:"+e.getMessage());
			MDC.clear();
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * @Title: lrange 
	 * @Description: 获得链表指定范围内的数据
	 * @param key
	 * @param start
	 * @param end
	 * @return List<byte[]>
	 * @throws
	 */
	public static List<byte[]> lrange(String key, long start, long end){
		Jedis jedis= RedisUtil.getJedis();
		try{
			return jedis.lrange(key.getBytes(), start, end);
		}catch(Exception e){
			log.error("error:"+e.getMessage());
			MDC.clear();
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * @Title: lindex 
	 * @Description: 根据索引获得list数据
	 * @param @param key
	 * @param @param index
	 * @return byte[]
	 * @throws
	 */
	public static byte[] lindex(String key, long index){
		Jedis jedis= RedisUtil.getJedis();
		try{
			return jedis.lindex(key.getBytes(),index);
		}catch(Exception e){
			log.error("error:"+e.getMessage());
			MDC.clear();
		}finally{
			RedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	
	
	public static void main(String[] args) {
		String key = "temp";
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		RedisOpr.saveObject(key, list);
		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(3);
		RedisOpr.saveObject(key, list2);
		Object object = RedisOpr.getObject(key);
		System.out.println(object);
	}
}
