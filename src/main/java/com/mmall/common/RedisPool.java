package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author : suwei
 * @description : redis连接池
 * @date : 2017\11\28 0028 9:30
 */
public class RedisPool {

    private static JedisPool pool;//连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));//最大连接数
    private static Integer maxIdle= Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));//最大空闲连接数
    private static Integer minIdle= Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));//最小空闲连接数
    private static Boolean testOnBorrow= Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));//在borrow的时候是否要进行测试
    private static Boolean testOnReturn= Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));//在return的时候是否要进行测试

    private static String redisIp = (PropertiesUtil.getProperty("redis.ip"));
    private static Integer redisPort= Integer.parseInt(PropertiesUtil.getProperty("redis.port"));//最小空闲连接数

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        //连接耗尽时，是否阻塞，false表示不阻塞抛出异常，true表示阻塞知道连接超时，默认为true
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config,redisIp,redisPort,1000*2);
    }

    static {
        initPool();
    }

    public static Jedis getJedis(){
        return pool.getResource();
    }

    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }

    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

}
