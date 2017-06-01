package com.framework.cache.redis.mq;

import com.framework.Config;
import com.framework.tools.FrameworkDaemonThreadPool;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Message接收客户端(Listener是一个发布/订阅模式的客户端，即*订阅*消息而不是*消费*消息)
 * Created by guoyongzheng on 15/3/26.
 */
class MessageListener {

    //pubsub 实例
    private final PubSub ps = new PubSub();

    /**
     * spring文件中配置的参数
     */
    private Map<String, MessageHandler> channels;


    /**
     * 开始监听方法，应配置为spring配置文件的init-method。<br/>
     * 注意：jedis的subscribe()是一个阻塞方法，所以我用线程池新开一个线程来运行它。
     */
    public void start(){

        FrameworkDaemonThreadPool.execute(() -> {

            //注册回调函数
            for (Map.Entry<String, MessageHandler> entry : channels.entrySet()) {
                ps.registerHandler(entry.getKey(), entry.getValue());
            }
            //因为Jedis的API设计问题，所以必须把set转换成数组...
            String[] channelNames = new String[channels.size()];
            channels.keySet().toArray(channelNames);

            Jedis jedis = new Jedis(Config.properties.getProperty(Config.REDIS_HOST_KEY, ""));

            //订阅。注意：subscribe()是个阻塞方法！
            jedis.subscribe(ps, channelNames);
        });
    }

    /**
     * 结束方法
     */
    public void stop(){
        ps.unsubscribe();
    }

    public void setChannels(Map<String, MessageHandler> channels) {
        this.channels = new ConcurrentHashMap<>(channels);
    }
}
