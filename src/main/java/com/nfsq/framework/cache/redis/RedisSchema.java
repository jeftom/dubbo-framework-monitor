package com.nfsq.framework.cache.redis;

/**
 * redis的key前缀
 * Created by guoyongzheng on 15/3/16.
 */
public enum RedisSchema {
    /**
     * 缓存，该scheme下存储的值必须加上过期时间
     */
    CACHE,
    /**
     * 小水DB，该scheme下存储的值永远存在，不用加过期时间，但必须有所规划以节省内存。
     */
    DB,
    /**
     * 生产／消费 消息队列的前缀
     */
    MQ,
    /**
     * 生产／消费 消息队列的日志前缀
     */
    MQLOG,
    /**
     * 生产／消费 消息队列的消息推送通知
     */
    EXCHANGE,
    /**
     * 序列
     */
    SEQUENCE,
    /**
     * 锁
     */
    LOCK;

    /**
     * 获取redis存储的前缀
     * @return
     */
    public String getPrefix() {
        return this.name() + ".";
    }

    /**
     * 包装
     * @param originKey
     * @return
     */
    public String produceKey(String originKey) {
        if (originKey == null || originKey.isEmpty() || originKey.indexOf("%") > -1) {
            throw new IllegalArgumentException("key的名字不能为空且不能包含'%'字符！");
        }
        return this.getPrefix() + originKey;
    }
}
