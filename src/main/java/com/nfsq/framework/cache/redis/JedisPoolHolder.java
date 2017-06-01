package com.nfsq.framework.cache.redis;

import com.nfsq.framework.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisPool的包装类。注意：从JedisPool中取得的Jedis实例需要被作为资源释放。
 * Created by guoyongzheng on 15/3/17.
 */
public class JedisPoolHolder {

    /**
     * 用静态内部类保证只有使用到redis的时候，才会初始化JedisPool。
     * —— lazy-initialization（延迟初始化）
     */
    private static class PoolHolder {

        private static final JedisPoolConfig jedisConfig = new JedisPoolConfig();

        static{
            //jedis默认的最大连接数是8，太小了。
            jedisConfig.setMaxTotal(8);
        }

        /**
         * 静态唯一的jedis pool(从配置文件读取redisHost，默认为"")
         */
        private final static JedisPool pool = new JedisPool(jedisConfig, Config.properties.getProperty(Config.REDIS_HOST_KEY, ""));

    }

    /**
     * 获取Jedis实例，注意需使用 try-with-resource 释放资源或者手动close此实例。
     *
     * @return
     */
    public static Jedis jedisInstance() {

        return PoolHolder.pool.getResource();
    }

}
