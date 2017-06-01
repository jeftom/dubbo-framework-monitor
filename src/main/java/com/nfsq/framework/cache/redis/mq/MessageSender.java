package com.nfsq.framework.cache.redis.mq;

import com.nfsq.framework.cache.redis.JedisPoolHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;

/**
 * Message发送客户端——发布／订阅 模式
 * Created by guoyongzheng on 15/4/13.
 */
class MessageSender {

    private static Log log = LogFactory.getLog(MessageSender.class);

    /**
     * 发送（发布／订阅）模式的消息，每个消息会被送达到所有订阅了channel的客户端
     *
     * @param channel
     * @param message
     * @return 当前客户端订阅的频道数
     */
    public long send(String channel, String message) {
        //从pool中获取jedis实例  —— use try-with-resource pattern
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            long result = jedis.publish(channel, message);
            log.info("[MQ]: 在channel:[" + channel + "]上发送message[" + message + "]。");
            return result;
        }
    }

}
