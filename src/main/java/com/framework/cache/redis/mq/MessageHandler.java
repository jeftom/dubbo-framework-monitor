package com.framework.cache.redis.mq;

/**
 * 处理特定message的handler（作为一个FunctionalInterface，为使用JDK8的函数式风格提供方便）<br/>
 * 注意：该函数的实现类可能会在多个线程间共享，故不应包含状态。或在不得已的情况下，需对状态做好同步。
 * Created by guoyongzheng on 15/4/9.
 */
@FunctionalInterface
public interface MessageHandler {

    /**
     * 处理消息
     * @param message
     * @return
     */
    void handle(String message);
}
