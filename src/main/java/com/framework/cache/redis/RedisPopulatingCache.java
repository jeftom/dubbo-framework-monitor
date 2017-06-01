package com.framework.cache.redis;

import com.google.gson.Gson;
import net.sf.ehcache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by yuanjinglin on 16/7/28.
 */
public class RedisPopulatingCache {
    private static final Logger Logger = LoggerFactory.getLogger(RedisPopulatingCache.class.getName());
    protected final RedisCacheEntryFactory factory;
    protected final int liveTime;
    protected final String cacheName;

    public RedisPopulatingCache(String cacheName,int liveTime ,RedisCacheEntryFactory factory) throws CacheException {
        this.cacheName=cacheName;
        this.liveTime=liveTime;
        this.factory = factory;
    }
    public String get(Object key){
        Jedis jedis=JedisPoolHolder.jedisInstance();
        Gson gson = new Gson();
        String cacheKey=cacheName+"_"+gson.toJson(key);
        String element = jedis.get(cacheKey);
        try {
            if(element == null) {
                Object throwable = this.factory.createEntry(key);
                element = gson.toJson(throwable);
                jedis.setex(cacheKey, liveTime, element);
            }
            jedis.close();
        } catch (Throwable var7) {
            Logger.error("RedisPopulatingCache:redis cache erro!",var7);
            if(jedis!=null){
                jedis.close();
            }
        }
        return element;
    }

    public void remove(String key){
        Jedis jedis=JedisPoolHolder.jedisInstance();
        try {
            jedis.del(key);
            jedis.close();
        }catch (Exception e){
            Logger.error("RedisPopulatingCache:redis remove erro!",e);
            if(jedis!=null){
                jedis.close();
            }
        }
    }
}
