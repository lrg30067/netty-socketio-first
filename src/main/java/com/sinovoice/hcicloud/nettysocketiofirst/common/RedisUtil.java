package com.sinovoice.hcicloud.nettysocketiofirst.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.Properties;


public class RedisUtil {
	private static Logger log = Logger.getLogger(RedisUtil.class);

	private static Properties pro;

//	@Value("${redis_ip}")
	private static String ADDR;

//	@Value("${redis_port}")
	private static int PORT;

//	@Value("${redis_max_active}")
	private static int MAX_ACTIVE;


	private static int MAX_IDLE = 30;
	private static int MAX_WAIT = 10000;
	private static int TIMEOUT = 10000;
	private static boolean TEST_ON_BORROW = true;
	private static JedisPool jedisPool = null;

	static{
		try {
			pro=new java.util.Properties();
			pro.load(new RedisUtil().getClass().getClassLoader().getResourceAsStream("system.properties"));
			ADDR = pro.getProperty("redis_ip");
			PORT = Integer.parseInt(pro.getProperty("redis_port"));
			MAX_ACTIVE = Integer.parseInt(pro.getProperty("redis_max_active"));


			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWaitMillis(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}
}
