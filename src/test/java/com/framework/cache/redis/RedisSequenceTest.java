package com.framework.cache.redis;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * RedisSequence的单元测试
 * Created by guoyongzheng on 15/4/28.
 */
public class RedisSequenceTest {

    /**
     * 测试获取redis服务器时间（转换成本时区时间）
     * @throws InterruptedException
     */
    @Test
    public void testRedisTime() throws InterruptedException {
        RedisSequence rs = new RedisSequence();

        LocalDateTime dt = rs.redisTime();
        System.out.println(dt);

        Thread.sleep(500L);

        //LocalDateTime now = LocalDateTime.now(); //测试机器的时间和本地时间有差别，所以这个单元测试只有在用本机测试时才可用
        LocalDateTime now = rs.redisTime();
        System.out.println(now);

        assertTrue(now.isAfter(dt));

        Thread.sleep(500L);

        LocalDateTime nextDt = rs.redisTime();
        System.out.println(nextDt);

        assertTrue(nextDt.isAfter(now));
    }

    /**
     * 验证获取序列id
     */
    @Test
    public void testNextVal() {

        String seq = "test";
        RedisSequence rs = new RedisSequence();

        //nextVal
        //先做seq的清理
        rs.delSequence(seq);

        long id = rs.nextVal(seq);

        assertEquals(1L, id);

        id = rs.nextVal(seq);

        assertEquals(2L, id);

        //nextVal of day
        //先做seq的清理
        rs.delSequenceOfToday(seq);

        long dayId = rs.nextValOfDay(seq);

        assertEquals(1L, dayId);

        dayId = rs.nextValOfDay(seq);

        assertEquals(2L, dayId);

        //验证nextVal和nextValOfDay没有互相污染
        id = rs.nextVal(seq);

        assertEquals(3L, id);

        dayId = rs.nextValOfDay(seq);
        assertEquals(3L, dayId);
    }

    /**
     * 验证incrBy
     */
    @Test
    public void testIncrBy() {

        String seq = "test";
        RedisSequence rs = new RedisSequence();

        //incrBy
        //先做seq的清理
        rs.delSequence(seq);

        long id = rs.incrBy(seq, 10);

        assertEquals(10L, id);

        id = rs.incrBy(seq, -11);

        assertEquals(-1L, id);

        //incrBy of day
        //先做seq的清理
        rs.delSequenceOfToday(seq);

        long dayId = rs.incrByOfDay(seq, 1);

        assertEquals(1L, dayId);

        dayId = rs.incrByOfDay(seq, -5);

        assertEquals(-4L, dayId);
    }

    /**
     * 验证incrBy
     */
    @Test
    public void testGetCurrentValue() {

        String seq = "test";
        RedisSequence rs = new RedisSequence();

        //incrBy
        //先做seq的清理
        rs.delSequence(seq);

        long id = rs.currVal(seq);

        assertEquals(0L, id);

        rs.incrBy(seq, 10);

        id = rs.currVal(seq);

        assertEquals(10L, id);

        rs.incrBy(seq, -11);

        id = rs.currVal(seq);

        assertEquals(-1L, id);

        //incrBy of day
        //先做seq的清理
        rs.delSequenceOfToday(seq);

        long dayId = rs.currValOfDay(seq);

        rs.incrByOfDay(seq, 1);

        dayId = rs.currValOfDay(seq);

        assertEquals(1L, dayId);

        rs.incrByOfDay(seq, -5);

        dayId = rs.currValOfDay(seq);

        assertEquals(-4L, dayId);
    }
}
