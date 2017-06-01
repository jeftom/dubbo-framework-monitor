package com.nfsq.framework.cache.redis.mq;

import com.nfsq.framework.cache.redis.RedisSchema;

/**
 * MqExchange的实现
 * Created by guoyongzheng on 15/4/15.
 */
public enum Exchanges implements MqExchange {

    /**
     * 所有客户端共享的共有频道
     */
    ALL{
        /**
         * 取得消息通知频道
         *
         * @return
         */
        @Override
        public String getNotifyChannel() {
            return RedisSchema.EXCHANGE.produceKey(name());
        }
    },
    /**
     * 延时消息的存储频道
     */
    GUARDIAN{
        /**
         * 取得消息通知频道
         *
         * @return
         */
        @Override
        public String getNotifyChannel() {
            return RedisSchema.EXCHANGE.produceKey(name());
        }
    };
}
