package com.framework.cache.redis;

import com.framework.repository.CacheException;
import com.framework.repository.EntityRepository;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 用redis分布式缓存缓存数据
 * Created by yuanjinglin on 16-7-28.
 */
public abstract class RedisCachedRepository<K, V> implements EntityRepository<K, V> {


    final RedisPopulatingCache redisPopulatingCache;
    final Type type;
    /**
     * 初始化缓存
     */
    public RedisCachedRepository() {
        Type mySuperClass = this.getClass().getGenericSuperclass();
        type = ((ParameterizedType)mySuperClass).getActualTypeArguments()[1];
        RedisPopulatingCache redisPopulatingCache = new RedisPopulatingCache(getCacheName(),getCacheLiveTime(),new RedisCacheEntryFactory() {

            @Override
            public Object createEntry(Object key) throws Exception {
                return queryItem((K) key);
            }
        });
        this.redisPopulatingCache=redisPopulatingCache;

    }

    /**
     * 根据id获取数据对象<br/>
     *
     * @param id
     * @return
     */
    @Override
    public final V getValue(K id) {
        if (redisPopulatingCache == null) {
            throw new CacheException("redisPopulatingCache is null!");
        }
        String value = redisPopulatingCache.get(id);
        if (value == null) {
            return null;
        } else {
            Gson gson = new Gson();
            return  gson.fromJson(value, type);//(Type)(new TypeToken<V>(){}.getType())
        }
    }

    /**
     * 清空id所代表的数据对象
     *
     * @param id
     */
    @Override
    public boolean removeValue(K id) {
        if (redisPopulatingCache == null) {
            throw new CacheException("redisPopulatingCache is null");
        }
        redisPopulatingCache.remove((String)id);
        return true;
    }


    /**
     * 定义对象存活时间，默认值为1800s（30min）
     *
     * @return
     */
    protected long defineLiveSeconds() {
        return 1800L;
    }

    /**
     * 实现类要实现从数据库查询对象的方法
     *
     * @param k
     * @return
     */
    protected abstract V queryItem(K k);

    /**
     * 获取cacheName
     *
     * @return
     */
    protected abstract String getCacheName();
    /**
     * 获取cache 存活时间  秒
     *
     * @return
     */
    protected abstract int getCacheLiveTime();
}
