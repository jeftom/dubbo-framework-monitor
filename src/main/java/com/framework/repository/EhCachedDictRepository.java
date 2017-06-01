package com.framework.repository;

/**
 * 用ehcache缓存的字典类
 * Created by guoyongzheng on 14-12-16.
 */
public abstract class EhCachedDictRepository<V> {

    /**
     * key
     */
    private final Object o = new Object();

    private final EhCachedRepository<Object,V> innerRepo = new EhCachedRepository<Object, V>() {
        @Override
        protected V queryItem(Object o) {
            return EhCachedDictRepository.this.queryAllItem();
        }

        @Override
        protected String getCacheName() {
            return EhCachedDictRepository.this.getCacheName();
        }

        /**
         * 定义cache大小，默认值为5000（个对象）
         *
         * @return
         */
        @Override
        protected int defineCacheSize() {
            return EhCachedDictRepository.this.defineCacheSize();
        }

        /**
         * 定义对象存活时间，默认值为1800s（30min）
         *
         * @return
         */
        @Override
        protected long defineLiveSeconds() {
            return EhCachedDictRepository.this.defineLiveSeconds();
        }
    };

    /**
     * 带缓存的整表查询
     * @return
     */
    public final V getValues() {
        return innerRepo.getValue(o);
    }

    /**
     * 实现类要实现从数据库查询对象的方法
     *
     * @return
     */
    protected abstract V queryAllItem();

    /**
     * 获取cacheName
     *
     * @return
     */
    protected abstract String getCacheName();

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
        return 1800;
    }
}
