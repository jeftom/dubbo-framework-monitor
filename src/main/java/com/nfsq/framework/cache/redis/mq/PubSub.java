package com.nfsq.framework.cache.redis.mq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Jedis的pub/sub包装
 * Created by guoyongzheng on 15/3/26.
 */
public class PubSub extends JedisPubSub {

    private static Log log = LogFactory.getLog(PubSub.class);

    /**
     * handler跟channel的对应关系
     */
    private Map<String, MessageHandler> channels = new ConcurrentHashMap<>();

    /**
     * 收到消息的回调
     * @param channel
     * @param message
     */
    @Override
    public void onMessage(String channel, String message) {
        if (channel == null ){
            throw new NullPointerException("频道名称（channel）不能为空！");
        }

        MessageHandler mh = channels.get(channel);

        if (mh != null) {
            log.info("[MQ]:在channel:[" + channel + "]上收到message:[" + message + "]。");
            mh.handle(message);
        } // else 的情况，说明该Listener不关心这个消息。
    }

    /**
     * 注册指定channel的handler，如果原来已经有，那么将会被覆盖。<br/>
     * 注意：需保证MessageHandler不为空。<br/>
     * 返回this以便链式调用。
     *
     * @param channel
     * @param mh
     */
    public PubSub registerHandler(String channel, MessageHandler mh) {
        if (channel != null && mh != null) {
            channels.put(channel, mh);
        }
        return this;
    }

    /**
     * 移除指定channel上的handler。返回this以便链式调用。
     * @param channel
     */
    public PubSub unRegisterHandler(String channel) {
        if (channel != null) {
            channels.remove(channel);
        }
        return this;
    }
}
