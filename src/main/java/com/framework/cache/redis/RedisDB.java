package com.framework.cache.redis;

import com.framework.cache.RemoteDB;
import redis.clients.jedis.Jedis;

/**
 * Created by guoyongzheng on 15/3/17.
 */
public class RedisDB implements RemoteDB {

    /**
     * 在key前添加cache的prefix
     *
     * @param key
     * @return
     */
    private String cacheKey(final String key) {
        return RedisSchema.DB.produceKey(key);
    }

    /*
    以下多为使用try with resource 对Jedis类进行的封装，参见：
    https://github.com/xetorthio/jedis/wiki/Getting-started#using-jedis-in-a-multithreaded-environment
    */

    /**
     * 在缓存数据库中设置key的值为value
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String set(final String key, final String value) {
        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.set(cacheKey(key), value);
        }
    }

    /**
     * Get the value of the specified key. If the key does not exist null is returned. If the value
     * stored at key is not a string an error is returned because GET can only handle string values.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Bulk reply
     */
    @Override
    public String get(final String key) {
        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.get(cacheKey(key));
        }
    }

    /**
     * Test if the specified key exists. The command returns "1" if the key exists, otherwise "0" is
     * returned. Note that even keys set with an empty string as value will return "1". Time
     * complexity: O(1)
     *
     * @param key
     * @return Boolean reply, true if the key exists, otherwise false
     */
    @Override
    public Boolean exists(final String key) {
        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.exists(cacheKey(key));
        }
    }

    /**
     * Remove the specified keys. If a given key does not exist no operation is performed for this
     * key. The command returns the number of keys removed. Time complexity: O(1)
     *
     * @param keys
     * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
     * 0 if none of the specified key existed
     */
    @Override
    public Long del(final String... keys) {
        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            String[] cacheKeys = new String[keys.length];
            int i = 0;
            for (String key : keys) {
                cacheKeys[i++] = cacheKey(key);
            }
            return jedis.del(cacheKeys);
        }
    }

    @Override
    public Long del(final String key) {
        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.del(cacheKey(key));
        }
    }
}
