package com.framework.repository;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

/**
 * Ehcache缓存服务的封装
 * Created by guoyongzheng on 14-11-28.
 */
public class EhcacheService {

    /**
     * cacheManager实例
     */
    private final CacheManager cacheManager;

    /**
     * 默认构造函数
     */
    public EhcacheService() {
        //创建cacheManager单例
        cacheManager = CacheManager.create().getInstance();
    }

    /**
     * 创建一个Ehcache对象实例，并加入cacheManager
     * @param size
     * @param liveSeconds
     * @return
     */
    public Ehcache createCache(String cacheName, int size, long liveSeconds) {
        Ehcache newCache = cacheManager.getCache(cacheName);
        if (newCache != null) {
            throw new CacheException("Ehcache with the name:" + cacheName + " already exists.");
        }

        newCache = new Cache(cacheName, size, false, false, liveSeconds, 0);
        cacheManager.addCache(newCache);

        newCache = cacheManager.getCache(cacheName);
        return newCache;
    }

}
