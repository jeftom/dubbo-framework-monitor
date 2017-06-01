package com.nfsq.framework.repository;

import com.nfsq.framework.Config;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;

/**
 * 用ehcache存储字典表数据
 * Created by guoyongzheng on 14-11-28.
 */
public abstract class EhCachedRepository<K, V> implements EntityRepository<K, V> {

    /**
     * timeMachine，标记ehcache的缓存时间是否加速60倍
     */
    private static final int timeMachine = Config.properties.getProperty(Config.REDIS_HOST_KEY, "off").equals("on") ? 60 : 1;

    /**
     * ehcache default live time
     */
    private static final long liveTime = Long.valueOf(Config.properties.getProperty(Config.EHCACHE_LIVE_TIME, "1800"));

    /**
     * Ehcache实例
     */
    private final Ehcache cache;

    /**
     * ehcache的service
     */
    private final EhcacheService ehcacheService;

    /**
     * 设置默认获取锁的超时时间，一定要设置，否则 deadlock 分分钟找上你
     */
    private static final int LOCK_TIMEOUT_MILLIS = 60000;

    /**
     * 初始化缓存
     */
    public EhCachedRepository() {
        this.ehcacheService = new EhcacheService();
        SelfPopulatingCache selfPopulatingCache = new SelfPopulatingCache(ehcacheService.createCache(this.getCacheName(), this.defineCacheSize(), this.defineLiveSeconds() / timeMachine), new CacheEntryFactory() {
            /**
             * Creates the cacheEntry for the given cache key.
             * <p>
             * ehcache requires cache entries to be serializable.
             * <p>
             * Note that this method must be thread safe.
             *
             * @param key
             * @return The entry, or null if it does not exist.
             * @throws Exception On failure creating the object.
             */
            @Override
            public Object createEntry(Object key) throws Exception {
                return queryItem((K) key);
            }
        });
        selfPopulatingCache.setTimeoutMillis(LOCK_TIMEOUT_MILLIS);
        cache = selfPopulatingCache;

    }

    protected EhCachedRepository(String cacheName, int cacheSize, long liveSeconds) {
        this.ehcacheService = new EhcacheService();
        SelfPopulatingCache selfPopulatingCache = new SelfPopulatingCache(ehcacheService.createCache(cacheName, cacheSize, liveSeconds / timeMachine), new CacheEntryFactory() {
            /**
             * Creates the cacheEntry for the given cache key.
             * <p>
             * ehcache requires cache entries to be serializable.
             * <p>
             * Note that this method must be thread safe.
             *
             * @param key
             * @return The entry, or null if it does not exist.
             * @throws Exception On failure creating the object.
             */
            @Override
            public Object createEntry(Object key) throws Exception {
                return queryItem((K) key);
            }
        });
        selfPopulatingCache.setTimeoutMillis(LOCK_TIMEOUT_MILLIS);
        cache = selfPopulatingCache;
    }

    /**
     * 根据id获取数据对象<br/>
     * **注意：非常重要：因为缓存是在本地，所以不要对取出的对象进行修改操作！！**
     *
     * @param id
     * @return
     */
    @Override
    public final V getValue(K id) {
        if (cache == null) {
            throw new CacheException("Ehcache is null!");
        }

        Element e = cache.get(id);
        if (e == null) {
            return null;
        } else {
            Object o = e.getObjectValue();
            return (V) o;
        }
    }

    /**
     * 清空id所代表的数据对象
     *
     * @param id
     */
    @Override
    public boolean removeValue(K id) {
        if (cache == null) {
            throw new CacheException("Ehcache is null");
        }

        return cache.remove(id);
    }

    /**
     * 定义cache大小，默认值为5000（个对象）
     *
     * @return
     */
    protected int defineCacheSize() {
        return 5000;
    }

    /**
     * 定义对象存活时间，默认值为1800s（30min）
     *
     * @return
     */
    protected long defineLiveSeconds() {
        return liveTime;
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
}
