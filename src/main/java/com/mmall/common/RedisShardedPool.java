package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : suwei
 * @description : redis分片连接池
 * @date : 2017\12\1 0001 9:53
 */
public class RedisShardedPool {

    private static ShardedJedisPool pool;//ShardedJedisPool连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));//最大连接数
    private static Integer maxIdle= Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));//最大空闲连接数
    private static Integer minIdle= Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));//最小空闲连接数
    private static Boolean testOnBorrow= Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));//在borrow的时候是否要进行测试
    private static Boolean testOnReturn= Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));//在return的时候是否要进行测试

    private static String redis1Ip = (PropertiesUtil.getProperty("redis1.ip"));
    private static Integer redis1Port= Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String redis2Ip = (PropertiesUtil.getProperty("redis2.ip"));
    private static Integer redis2Port= Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        //连接耗尽时，是否阻塞，false表示不阻塞抛出异常，true表示阻塞直到连接超时，默认为true
        config.setBlockWhenExhausted(true);

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip, redis1Port, 1000 * 2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip, redis2Port, 1000 * 2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config,jedisShardInfoList,Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }

    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);
    }

    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }

}
