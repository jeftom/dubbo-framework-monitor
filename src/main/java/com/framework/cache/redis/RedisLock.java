package com.framework.cache.redis;

import redis.clients.jedis.Jedis;
import java.util.UUID;

/**
 * 使用 Redis 作为分布式应用的锁
 * <br/>
 * 模式参见 http://redis.io/commands/set
 *
 * Created by guoyongzheng on 15/6/2.
 */
public class RedisLock {

    /**
     * uuid作为锁的标志
     */
    private final String uuid = UUID.randomUUID().toString();

    /**
     * 超时时间，默认为10秒
     */
    private long ttlSeconds = 10L;

    /**
     * 需要加锁的资源
     */
    private final String resourceName;

    /**
     * 用于释放锁的lua脚本：
     *
     * if redis.call("get",KEYS[1]) == ARGV[1]
     * then
     *     return redis.call("del",KEYS[1])
     * else
     *     return 0
     * end
     */
    private static final String DEL_SCRIPT = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
            "then\n return redis.call(\"del\",KEYS[1])\n else\n return 0\n end";

    /**
     * 构造函数，需要指定待锁定的资源（一般是业务上需要确保单线执行的id）
     *
     * @param resourceName
     */
    public RedisLock(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * 尝试获取锁。如果返回true，说明已经成功获取（稍后需要release），如果返回false，说明获取失败。
     * @return
     */
    public boolean tryLock() {
        if (resourceName == null || resourceName.isEmpty()) {
            throw new IllegalArgumentException("资源名称不能为空");
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            String key = jedis.set(RedisSchema.LOCK.produceKey(resourceName), uuid, "NX", "EX", ttlSeconds);
            if ("OK".equalsIgnoreCase(key)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 释放锁
     * @return
     */
    public void release() {
        if (resourceName == null || resourceName.isEmpty()) {
            throw new IllegalArgumentException("资源名称不能为空");
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {

            //执行script
            jedis.eval(DEL_SCRIPT, 1, RedisSchema.LOCK.produceKey(resourceName), uuid);
        }
    }

    public void setTtlSeconds(long ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }
}
