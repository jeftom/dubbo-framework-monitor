package com.framework.repository;

import com.framework.tools.CommonPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 对大表的一个分表缓存
 * Created by guoyongzheng on 14-12-14.
 */
public abstract class ShardingCachedRepository<F, S, V> implements EntityRepository<CommonPair<F, S>, V> {

    /*
     本类的实现思想是：将K拆分成一个major key和一个minor key，
     以major key为键建立EhCachedRepository，
     并额外维护一个contains列表以提升性能
     */

    /**
     * 内部实现repo
     */
    private final ShardRepository<F, S, V> innerRepo;

    /**
     * 构造函数
     */
    public ShardingCachedRepository() {
        this.innerRepo = new ShardRepository<>(this, getCacheName(), defineLiveSeconds(), defineCacheSize());
    }

    /**
     * 查询给定的keys列表有多少组合是存在值的
     * @param majorKeys
     * @param minorKeys
     * @return
     */
    public List<CommonPair<F, S>> containsKey(List<F> majorKeys, List<S> minorKeys) {
        if (majorKeys == null || minorKeys == null) {
            return Collections.emptyList();
        }

        //遍历两个列表
        List<CommonPair<F, S>> result = new ArrayList<>();
        for (F first : majorKeys) {
            for (S second : minorKeys) {
                if (this.innerRepo.containsKey(first, second)) {
                    result.add(new CommonPair<F, S>(first, second));
                }
            }
        }

        return result;
    }

    /**
     * 根据id查询/获取数据对象
     *
     * @param id
     * @return
     */
    @Override
    public V getValue(CommonPair<F, S> id) {
        if (id == null) {
            return null;
        }

        F first = id.getFirst();
        Map<S, V> items = this.innerRepo.getValue(first);
        if (items == null) {
            return null;
        }

        S second = id.getSecond();
        return items.get(second);
    }

    /**
     * 清空id所代表的数据对象
     *
     * @param id
     */
    @Override
    public final boolean removeValue(CommonPair<F, S> id) {
        throw new UnsupportedOperationException("因为维护了containsKey的命中缓存，故暂不支持此方法。");
    }

    /**
     * 定义对象存活时间，默认值为1800s（30min）
     *
     * @return
     */
    protected long defineLiveSeconds() {
        return 1800;
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
     * repo的唯一名称
     *
     * @return
     */
    protected abstract String getCacheName();

    /**
     * 数据库查询方法
     *
     * @param id
     * @return
     */
    protected abstract Map<S, V> queryItems(F id);

    /**
     * 内嵌的EhcachedRepository，额外实现了contains方法
     *
     * @param <F>
     * @param <S>
     * @param <V>
     */
    private static class ShardRepository<F, S, V> extends EhCachedRepository<F, Map<S, V>> {

        /**
         * 外部类cache名称
         */
        private final String cacheName;

        /**
         * 缓存时间
         */
        private final long liveSeconds;

        /**
         * 缓存大小
         */
        private final int cacheSize;

        /**
         * 外缓存的引用
         */
        private final ShardingCachedRepository<F, S, V> outerCache;

        /**
         * 查看是否包含某元素的表
         */
        private final Map<F, Set<S>> keyMap = new ConcurrentHashMap<>();

        /**
         * 时间戳
         */
        private final Map<F, Long> timeStamps = new ConcurrentHashMap<>();

        /**
         * 构造函数
         *
         * @param outerCacheName
         */
        ShardRepository(ShardingCachedRepository<F, S, V> outerCache, String outerCacheName, long liveSeconds, int cacheSize) {
            //这个默认构造器的调用不能忘掉。。。
            super(outerCacheName, cacheSize, liveSeconds);

            this.outerCache = outerCache;
            this.cacheName = outerCacheName;
            this.liveSeconds = liveSeconds;
            this.cacheSize = cacheSize;
        }

        /**
         * 查询某个<F,S>的pair在repo中是否有值
         *
         * @param majorKey
         * @param minorKey
         * @return
         */
        boolean containsKey(F majorKey, S minorKey) {
            if (majorKey == null || minorKey == null) {
                return false;
            }

            //先判断时间戳来检查是否key已经过期
            Long recordTime = timeStamps.get(majorKey);
            if (recordTime == null) {
                //进行一次查询来刷新缓存
                getValue(majorKey);
            } else {
                long now = System.nanoTime();
                long elapsedTime = now - recordTime;
                //计算时间差是否已经超过了liveSeconds
                if (this.liveSeconds * 1000 * 1000 * 1000 < elapsedTime) {
                    //进行一次查询来刷新缓存
                    getValue(majorKey);
                }
            }

            Set<S> keys = this.keyMap.get(majorKey);
            if (keys == null) {
                return false;
            } else {
                return keys.contains(minorKey);
            }
        }

        /**
         * 实现类要实现从数据库查询对象的方法
         *
         * @param id
         * @return
         */
        @Override
        protected Map<S, V> queryItem(F id) {
            //查询数据库
            Map<S, V> items = outerCache.queryItems(id);

            //根据查询数据库结果刷新keyMap
            this.keyMap.remove(id);
            if (items != null) {
//                Set<S> keys = new HashSet<>();
//                for (Map.Entry<S, V> entry : items.entrySet()) {
//                    keys.add(entry.getKey());
//                }

                // 以下是第一种java8的新写法
//                items.forEach((s, v) -> {
//                    keys.add(s);
//                });

                // 以下是第二种java8的新写法，使用collect —— IDE推荐如此。酷~~ :)
                Set<S> keys = items.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet());

                this.keyMap.put(id, keys);
            }

            //记录时间戳
            long currentNano = System.nanoTime();
            timeStamps.put(id, currentNano);

            return items;
        }

        /**
         * 获取cacheName
         *
         * @return
         */
        @Override
        protected String getCacheName() {
            return this.cacheName;
        }

        /**
         * 定义对象存活时间，默认值为1800s（30min）
         *
         * @return
         */
        @Override
        protected long defineLiveSeconds() {
            return this.liveSeconds;
        }

        /**
         * 定义cache大小，默认值为5000（个对象）
         *
         * @return
         */
        @Override
        protected int defineCacheSize() {
            return this.cacheSize;
        }
    }
}
