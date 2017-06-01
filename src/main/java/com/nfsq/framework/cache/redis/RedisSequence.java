package com.nfsq.framework.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

/**
 * 利用redis生成递增序列
 * Created by guoyongzheng on 15/4/28.
 */
public class RedisSequence {

    /**
     * 系统默认的offset
     */
    private static final ZoneOffset OFFSET = ZoneOffset.from(LocalDateTime.now().atZone(ZoneId.systemDefault()));

    /**
     * 获取redis服务器时间（返回值类型是JDK8的新datetime类型，代替原来的java.util.Date）
     *
     * @return LocalDateTime
     */
    public LocalDateTime redisTime() {
        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return redisTime(jedis);
        }
    }

    /**
     * 获取redis服务器时间（返回值类型是JDK8的新datetime类型，代替原来的java.util.Date）
     *
     * @param jedis
     * @return
     */
    LocalDateTime redisTime(Jedis jedis) {
        List<String> times = jedis.time();

        if (times != null && times.size() == 2) {
            //redis的time指令返回值为2元组，分别为UNIX时间戳和微秒数，转换成LocalDateTime要把后者乘1000变成纳秒
            //第三个参数是时区，因为UNIX时间戳是UTC时间，而北京时间是东八区，所以要加系统默认的offset。
            return LocalDateTime.ofEpochSecond(Long.valueOf(times.get(0)), Integer.valueOf(times.get(1)) * 1000, OFFSET);
        } else {
            return null;
        }
    }

    /**
     * 获取指定seq_name的序列的下一个序号
     *
     * @param seqName
     * @return
     */
    public long nextVal(String seqName) {
        if (seqName == null || seqName.isEmpty()) {
            throw new IllegalArgumentException("序列名称不能为空");
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.incr(RedisSchema.SEQUENCE.produceKey(seqName));
        }
    }

    /**
     * 获取指定seq_name的序列的当前序号
     *
     * @param seqName
     * @return
     */
    public long currVal(String seqName) {
        if (seqName == null || seqName.isEmpty()) {
            throw new IllegalArgumentException("序列名称不能为空");
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            String result = jedis.get(RedisSchema.SEQUENCE.produceKey(seqName));
            if (result == null) {
                return 0L;
            } else {
                return Long.parseLong(result);
            }
        }
    }

    /**
     * 获取指定seq_name的序列的下一个序号（指定步长为step）
     *
     * @param seqName
     * @param step
     * @return
     */
    public long incrBy(String seqName, int step) {
        if (seqName == null || seqName.isEmpty()) {
            throw new IllegalArgumentException("序列名称不能为空");
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.incrBy(RedisSchema.SEQUENCE.produceKey(seqName), step);
        }
    }

    /**
     * 获取按天变化的sequenceName
     * @param sequenceName
     * @return
     */
    private String sequenceNameOfDay(String sequenceName, Jedis jedis) {
        LocalDateTime dt = redisTime(jedis);
        String date = dt.toLocalDate().toString();
        //在sequenceName后增加".2015-01-01"格式的后缀
        return RedisSchema.SEQUENCE.produceKey(sequenceName) + "." + date;
    }

    /**
     * 获取指定seq_name的序列的下一个序号，每天重新开始计数
     * <br/>注意，按天计数的key 会在大概两天后过期
     *
     * @param seqName
     * @return
     */
    public long nextValOfDay(String seqName) {
        if (seqName == null || seqName.isEmpty()) {
            throw new IllegalArgumentException("序列名称不能为空");
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            String key = sequenceNameOfDay(seqName, jedis);

            // using a pipeline to execute 2 commands at the same time.
            Pipeline p = jedis.pipelined();
            Response<Long> r = p.incr(key);
            p.expire(key, 60 * 60 * 24 * 2); // expires 2 days later

            p.sync();

            return r.get();
        }
    }

    /**
     * 获取指定seq_name的序列的当前序号(当天版本)
     *
     * @param seqName
     * @return
     */
    public long currValOfDay(String seqName) {
        if (seqName == null || seqName.isEmpty()) {
            throw new IllegalArgumentException("序列名称不能为空");
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            String result = jedis.get(sequenceNameOfDay(seqName, jedis));
            if (result == null) {
                return 0L;
            } else {
                return Long.parseLong(result);
            }
        }
    }

    /**
     * 获取指定seq_name的序列的下一个序号，每天重新开始计数（指定步长为step）
     *
     * @param seqName
     * @param step
     * @return
     */
    public long incrByOfDay(String seqName, int step) {
        if (seqName == null || seqName.isEmpty()) {
            throw new IllegalArgumentException("序列名称不能为空");
        }

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {

            String key = sequenceNameOfDay(seqName, jedis);

            // using a pipeline to execute 2 commands at the same time.
            Pipeline p = jedis.pipelined();
            Response<Long> r = p.incrBy(key, step);
            p.expire(key, 60 * 60 * 24 * 2); // expires 2 days later

            p.sync();

            return r.get();
        }
    }

    /**
     * 删除一个指定序列（等于把序列置为0）
     * @param seqName
     * @return
     */
    public long delSequence(String seqName) {
        if (seqName == null || seqName.isEmpty()) {
            throw new IllegalArgumentException("序列名称不能为空");
        }
        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.del(RedisSchema.SEQUENCE.produceKey(seqName));
        }
    }

    /**
     * 删除一个指定的当天序列（等于把序列置为0）
     * @param seqName
     * @return
     */
    public long delSequenceOfToday(String seqName) {
        if (seqName == null || seqName.isEmpty()) {
            throw new IllegalArgumentException("序列名称不能为空");
        }
        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            return jedis.del(sequenceNameOfDay(seqName, jedis));
        }
    }
}
