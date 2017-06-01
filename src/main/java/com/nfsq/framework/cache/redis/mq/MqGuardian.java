package com.nfsq.framework.cache.redis.mq;

import com.nfsq.framework.cache.redis.JedisPoolHolder;
import com.nfsq.framework.cache.redis.RedisSequence;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Mq的守护程序
 * <p>
 * Created by guoyongzheng on 15/11/20.
 */
public class MqGuardian {

    private static Log log = LogFactory.getLog(MqGuardian.class);

    /**
     * 延时消息的存储键
     */
    private static final MqExchange exchange = Exchanges.GUARDIAN;

    /**
     * 分隔符
     */
    private static final String SPLITTER = "%";

    /**
     * 单例
     */
    private static final MqGuardian instance = new MqGuardian();

    /**
     * 计时器
     */
    private final Timer timer = new Timer(true);

    /**
     * 表示已经启动的标记，以保证tictac()方法的幂等性
     */
    private volatile boolean started = false;

    /**
     * 消息发送者
     */
    private final MessageProducer mp = new MessageProducer();

    /**
     * 私有化构造函数
     */
    private MqGuardian() {
    }

    /**
     * 发送一个延时消息
     *
     * @param channel
     * @param message
     * @param seconds
     */
    public static boolean timeBomb(String channel, String message, int seconds) {
        if (channel == null || channel.equals("")) {
            throw new IllegalArgumentException("channel 名称不能为空。");
        }
        if (seconds < 0) {
            throw new IllegalArgumentException("延时不能小于0。");
        }

        // 获取时间戳
        LocalDateTime dt = new RedisSequence().redisTime();
        dt = dt.plus(seconds, ChronoUnit.SECONDS);
        long epoch = dt.atZone(ZoneId.systemDefault()).toEpochSecond();

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            //使用uuid来确保message的唯一性
            String str = String.valueOf(epoch) + SPLITTER + UUID.randomUUID().toString() +
                    SPLITTER + channel + SPLITTER + message;

            //使用zadd来加入待发队列
            long result = jedis.zadd(exchange.getNotifyChannel(), Double.valueOf(epoch), str);

            return result > 0;
        }
    }

    /**
     * 计时开始~!!
     */
    public static void tictac() {
        if (instance.started) {
            return;
        } else {
            instance.started = true;
        }

        // 开启定时处理
        instance.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //try-with-resource
                try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
                    int i = 0;
                    while (instance.check(jedis) && i++ < 30) {
                        // infinite loop
                    }

                }
            }
        }, 10 * 1000L, 60 * 1000L);
    }

    /**
     * @param jedis
     */
    private boolean check(Jedis jedis) {
        // zrange 获取前三条记录
        Set<String> set = jedis.zrange(exchange.getNotifyChannel(), 0, 2);
        if (set == null || set.isEmpty()) {
            return false;
        }

        boolean result = true;
//        LocalDateTime now = LocalDateTime.now();
        // 获取延迟消息的时间由本地时间修改成redis服务器时间(发送时间是redis服务器时间) by wh.he
        LocalDateTime now = new RedisSequence().redisTime();
        long epoch = now.atZone(ZoneId.systemDefault()).toEpochSecond();
        for (String str : set) {
            if (str == null || str.isEmpty()) {
                continue;
            }

            String[] array = str.split(SPLITTER);
            if (array.length >= 4 && Long.parseLong(array[0]) > epoch) {
                result = false;
                continue;
            }

            long flag = jedis.zrem(exchange.getNotifyChannel(), str);
            if (flag == 1L) { // zrem成功
                //兼容错误情况：
                if (array.length < 4) {
                    log.error("发现错误的延时消息：" + str);
                } else {
                    String channel = array[2];
                    String message = str.substring(array[0].length() + array[1].length() +
                            array[2].length() + 3 * SPLITTER.length());

                    instance.mp.send(channel, message);
                }
            }
        }

        return result;
    }
}
