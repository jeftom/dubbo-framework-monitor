package com.nfsq.framework.cache.redis.mq;

import com.nfsq.framework.cache.redis.JedisPoolHolder;
import com.nfsq.framework.cache.redis.RedisSchema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;

/**
 * 消息生产者，用于 生产／消费 模式的消息
 * Created by guoyongzheng on 15/4/14.
 */
public class MessageProducer {

    private static Log log = LogFactory.getLog(MessageProducer.class);

    /**
     * 发出消息推送通知的sender
     */
    private MessageSender notificationSender = new MessageSender();

    /**
     * 跟通知频道相关的exchange
     */
    private MqExchange exchange = Exchanges.ALL;

    /**
     * 发送消息到一个消费队列中，每个消息会且只会被消费一次
     * @param channel
     * @param message
     * @return 队列中存在的消息数
     */
    public long send(String channel, String message) {
        if (channel == null || channel.equals("")) {
            throw new IllegalArgumentException("channel 名称不能为空。");
        }

        //try-with-resource
        try(Jedis jedis = JedisPoolHolder.jedisInstance()) {
            String key = RedisSchema.MQ.produceKey(channel);

            //消息队列采用左进右出的形式
            long result = jedis.lpush(key, message);
            log.info("[MQ]: 在channel:["+key+"]上发送message["+message+"]。");

            //推送通知
            String notifyChannel = exchange.getNotifyChannel();
            notificationSender.send(notifyChannel, channel);

            return result;
        }
    }
}
