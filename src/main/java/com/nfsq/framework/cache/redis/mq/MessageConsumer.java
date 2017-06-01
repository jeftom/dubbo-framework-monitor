package com.nfsq.framework.cache.redis.mq;

import com.nfsq.framework.cache.redis.JedisPoolHolder;
import com.nfsq.framework.cache.redis.RedisSchema;
import com.nfsq.framework.tools.FrameworkThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 消息队列消费者，利用MessageListener来获取推送
 * Created by guoyongzheng on 15/4/14.
 */
public class MessageConsumer {

    private static final String NIL = "nil";

    private static final String LOG_SPLITTER = ":";

    /**
     * 启动标记
     */
    private volatile boolean started = false;

    /**
     * ip地址
     */
    private String ipAddress = "";

    private static Log log = LogFactory.getLog(MessageConsumer.class);

    /**
     * spring文件中配置的参数，代表 [channel名称，消息处理函数]
     */
    private Map<String, MessageHandler> channels;

    /**
     * 消息推送通知，从exchange对象获取消息推送频道，默认为全部
     */
    private MessageListener listener = new MessageListener();

    /**
     * 跟消息推送通知频道相关的exchange
     */
    private MqExchange exchange = Exchanges.ALL;

    /**
     * 开始消费
     */
    public void start() {
        if (this.started) {
            return;
        } else {
            this.started = true;
        }

        //注意map是线程间共享的，要用线程安全的类型
        Map<String, MessageHandler> notifications = new ConcurrentHashMap<>();
        notifications.put(exchange.getNotifyChannel(), new NotifyHandler(this));
        listener.setChannels(notifications);

        listener.start();

        try {
            this.ipAddress = this.detectIpAddress();
        } catch (SocketException e) {
            this.ipAddress = "unknownHost";
            log.error(e.getMessage(), e);
        }

        // 程序启动时，首先拉取一次消息
        channels.keySet().forEach(this::consume);

        // 开启延时任务
        MqGuardian.tictac();
    }

    /**
     * 在指定channel消费消息
     *
     * @param channel
     */
    void consume(String channel) {
        MessageHandler mh = channels.get(channel);

        if (mh == null) {
            //说明消息不是给这个客户端来处理的
            return;
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            String key = RedisSchema.MQ.produceKey(channel);

            //无限循环，直到取尽这个队列（当取尽时，rpop将返回'nil'）
            while (true) {
                final String message = jedis.rpop(key);

                // 处理message
                if (message == null || message.equals("") || message.equals(NIL)) {
                    break;
                } else {
                    log.info("[MQ]:在channel:[" + key + "]上收到message:[" + message + "]。");
                    // 记录两天内的接收日志
                    String datetime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
                    jedis.setex(RedisSchema.MQLOG.produceKey(channel + LOG_SPLITTER + datetime),
                            60 * 60 * 24 * 30, this.ipAddress + LOG_SPLITTER + message);

                    //在新线程里执行handler
                    FrameworkThreadPool.execute(() -> mh.handle(message));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 结束消费
     */
    public void stop() {
        listener.stop();
        this.started = false;
    }

    /**
     * set 方法
     *
     * @param channels
     */
    public void setChannels(Map<String, MessageHandler> channels) {
        this.channels = new ConcurrentHashMap<>(channels);
    }

    /**
     * 获取ip地址
     * @return
     * @throws SocketException
     */
    private String detectIpAddress() throws SocketException {
        Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
        for (; n.hasMoreElements();)
        {
            NetworkInterface e = n.nextElement();
            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements();)
            {
                InetAddress addr = a.nextElement();
                if (isValidAddress(addr)) {
                    return addr.getHostAddress();
                }
            }
        }

        return "unknownHost";
    }

    /**
     * 判断一个ip是否合法
     * @param address
     * @return
     */
    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null
                && ! "0.0.0.0".equals(name)
                && ! "127.0.0.1".equals(name)
                && Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$").matcher(name).matches());
    }


    /**
     * 消息推送服务的handler
     */
    private static class NotifyHandler implements MessageHandler {

        /**
         * 绑定的consumer实例
         */
        private final MessageConsumer consumer;

        /**
         * 传参构造函数
         *
         * @param consumer
         */
        NotifyHandler(MessageConsumer consumer) {
            this.consumer = consumer;
        }

        /**
         * 处理消息，消息体就是正式消息归属的队列
         *
         * @param channelName
         * @return
         */
        @Override
        public void handle(String channelName) {
            consumer.consume(channelName);
        }

    }
}
