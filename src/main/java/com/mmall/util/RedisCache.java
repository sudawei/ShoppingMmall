package com.mmall.util;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Administrator on 2017/7/25/025.
 */
public class RedisCache {

    @Autowired
    private JedisPool jedisPool = new JedisPool();


    //从redis缓存中查询，反序列化
    public Object getDataFromRedis(String redisKey){
        //查询
        Jedis jedis = jedisPool.getResource();
        byte[] result = jedis.get(redisKey.getBytes());

        //如果查询没有为空
        if(null == result){
            return null;
        }

        //查询到了，反序列化
        return SerializeUtil.unSerialize(result);
    }

    //将数据库中查询到的数据放入redis
    public void setDataToRedis(String redisKey, Object obj){

        String expireTime = redisKey.substring(redisKey.indexOf("_")+1,redisKey.indexOf("=")).trim();

        //序列化
        byte[] bytes = SerializeUtil.serialize(obj);

        //存入redis
        Jedis jedis = jedisPool.getResource();
        String success = jedis.set(redisKey.getBytes(), bytes);
        //设置多长时间过期
        jedis.expire(redisKey.getBytes(),Integer.parseInt(expireTime));

        if("OK".equals(success)){
            System.out.println("数据成功保存到redis...");
        }
    }
}
