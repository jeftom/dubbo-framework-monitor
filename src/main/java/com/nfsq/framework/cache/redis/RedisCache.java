package com.nfsq.framework.cache.redis;

import com.nfsq.framework.cache.RemoteCache;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * RemoteCache接口的redis实现。
 * Created by guoyongzheng on 15/3/12.
 */
public class RedisCache implements RemoteCache {

    /**
     * 在key前添加cache的prefix
     * @param key
     * @return
     */
    private String cacheKey(final String key){
        return RedisSchema.CACHE.produceKey(key);
    }

    /*
    以下多为使用try with resource 对Jedis类进行的封装，参见：
    https://github.com/xetorthio/jedis/wiki/Getting-started#using-jedis-in-a-multithreaded-environment
    */

    /**
     * The command is exactly equivalent to the following group of commands:
     * {@link Jedis#set(String, String) SET} + {@link Jedis#expire(String, int) EXPIRE}. The operation is
     * atomic.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param seconds
     * @param value
     * @return Status code reply
     */
    @Override
    public String setex(final String key, final int seconds, final String value) {
        //超时时间不能小于0
        if (seconds <= 0) {
            throw new IllegalArgumentException("使用redis cache设置超时时间不能小于等于0！");
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.setex(cacheKey(key), seconds, value);
        }
    }

    public String setex(final String key, final int seconds, final byte[] value) {
        //超时时间不能小于0
        if (seconds <= 0) {
            throw new IllegalArgumentException("使用redis cache设置超时时间不能小于等于0！");
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.setex(cacheKey(key).getBytes(), seconds, value);
        }
    }

    /**
     * Get the value of the specified key. If the key does not exist null is returned. If the value
     * stored at key is not a string an error is returned because GET can only handle string values.
     * <p>
     * Time complexity: O(1)
     * @param key
     * @return Bulk reply
     */
    @Override
    public String get(final String key) {
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.get(cacheKey(key));
        }
    }

    public byte[] binaryGet(final String key) {
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.get(cacheKey(key).getBytes());
        }
    }

    /**
     * Test if the specified key exists. The command returns "1" if the key exists, otherwise "0" is
     * returned. Note that even keys set with an empty string as value will return "1". Time
     * complexity: O(1)
     * @param key
     * @return Boolean reply, true if the key exists, otherwise false
     */
    @Override
    public Boolean exists(final String key) {
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.exists(cacheKey(key));
        }
    }

    /**
     * Remove the specified keys. If a given key does not exist no operation is performed for this
     * key. The command returns the number of keys removed. Time complexity: O(1)
     * @param key
     * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
     *         0 if none of the specified key existed
     */
    @Override
    public Long del(final String key) {
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.del(cacheKey(key));
        }
    }

    /**
     * 注意：仅供测试使用
     *
     * Returns all the keys matching the glob-style pattern as space separated strings. For example if
     * you have in the database the keys "foo" and "foobar" the command "KEYS foo*" will return
     * "foo foobar".
     * <p>
     * Note that while the time complexity for this operation is O(n) the constant times are pretty
     * low. For example Redis running on an entry level laptop can scan a 1 million keys database in
     * 40 milliseconds. <b>Still it's better to consider this one of the slow commands that may ruin
     * the DB performance if not used with care.</b>
     * <p>
     * In other words this command is intended only for debugging and special operations like creating
     * a script to change the DB schema. Don't use it in your normal code. Use Redis Sets in order to
     * group together a subset of objects.
     * <p>
     * Glob style patterns examples:
     * <ul>
     * <li>h?llo will match hello hallo hhllo
     * <li>h*llo will match hllo heeeello
     * <li>h[ae]llo will match hello and hallo, but not hillo
     * </ul>
     * <p>
     * Use \ to escape special chars if you want to match them verbatim.
     * <p>
     * Time complexity: O(n) (with n being the number of keys in the DB, and assuming keys and pattern
     * of limited length)
     * @param pattern
     * @return Multi bulk reply
     */
    @Deprecated
    public Set<String> keys(final String pattern) {
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.keys(pattern);
        }
    }

    // to be continued...
}
