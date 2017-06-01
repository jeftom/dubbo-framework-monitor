package com.framework.cache.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

/**
 * RedisLock的单元测试
 * Created by guoyongzheng on 15/6/2.
 */
public class RedisLockTest {

    private int flag = 0;

    private void setFlag(int value) {
        this.flag = value;
    }

    /**
     * 简单使用
     */
    @Test
    public void testSimplyLock() {
        RedisLock rl = new RedisLock("test");

        boolean success = rl.tryLock();

        assertTrue(success);

        //here do sth.

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            assertTrue(jedis.exists(RedisSchema.LOCK.produceKey("test")));
        }

        rl.release();

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            assertFalse(jedis.exists(RedisSchema.LOCK.produceKey("test")));
        }
    }

    /**
     * 测试超时
     */
    @Test
    public void testExpire() throws InterruptedException {
        RedisLock rl = new RedisLock("test");
        rl.setTtlSeconds(1L);

        boolean success = rl.tryLock();

        assertTrue(success);

        Thread.sleep(1500L);

        //try-with-resource
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            assertFalse(jedis.exists(RedisSchema.LOCK.produceKey("test")));
        }
    }

    /**
     * 并发的情况
     */
    @Test
    public void testConcurrency() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        /* 首先是不加锁的情况 */
        final CountDownLatch latch1 = new CountDownLatch(1);
        //启动线程1
        Future<Boolean> f1 =  es.submit(() -> {
            latch1.await();
            Thread.sleep(100L);
            setFlag(1);
            return true;
        });

        //启动线程2
        Future<Boolean> f2 = es.submit(() -> {
            latch1.countDown();
            setFlag(2);
            return true;
        });

        //在未加锁的情况下，因为线程1在线程2工作后才开始工作，所以最终的flag值应当为1
        if (f1.get() && f2.get()) {
            assertEquals(1, this.flag);
        } else {
            fail();
        }

        /* 加锁将带来变化 */

        //首先将flag复位
        setFlag(0);
        assertEquals(0, this.flag);

        // latch2 保证线程3先于线程4获得锁
        final CountDownLatch latch2 = new CountDownLatch(1);
        //latch3 保证线程4先开始工作
        final CountDownLatch latch3 = new CountDownLatch(1);
        //启动线程3
        Future<Boolean> f3 =  es.submit(() -> {
            latch3.await();
            RedisLock rl = new RedisLock("test");
            if (rl.tryLock()) {
                latch2.countDown();
                try {
                    //线程睡眠 500 ms
                    Thread.sleep(500L);
                    setFlag(3);
                    return true;
                } finally {
                    rl.release();
                }
            } else {
                //获取不到锁表明case失败
                fail();
                return true;
            }
        });

        //启动线程4
        Future<Boolean> f4 = es.submit(() -> {
            latch3.countDown();
            latch2.await();
            RedisLock rl = new RedisLock("test");
            for (int i = 0; i < 10; i++){
                if (!rl.tryLock()) {
                    Thread.sleep(100L);
                } else {
                    try {
                        setFlag(4);
                    } finally {
                        rl.release();
                    }
                    return true;
                }
            }

            fail();
            return true;
        });

        //在加锁的情况下，虽然线程3在线程4后才开始工作，但因为它在睡眠之前获取了锁，所以线程4要等它释放后才能工作，所以结果应当是4
        if (f3.get() && f4.get()) {
            assertEquals(4, this.flag);
        } else {
            fail();
        }

        es.shutdown();
    }
}
