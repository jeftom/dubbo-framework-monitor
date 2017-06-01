package com.nfsq.framework.cache.redis.mq;

/**
 * 消息转发器。此概念设计来自RabbitMQ。
 * Created by guoyongzheng on 15/4/15.
 */
public interface MqExchange {

    /**
     * 取得消息通知频道
     * @return
     */
    String getNotifyChannel();
}
