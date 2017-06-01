package com.framework.cache.redis;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * 测试 redis cache
 * Created by guoyongzheng on 15/3/17.
 */
public class RedisCacheTest {

    @Test
    public void testCache() throws InterruptedException {
        //默认使用测试redis  (10.1.3.126)

        RedisCache cache = new RedisCache();

        String result = cache.setex("test", 1000, "testValue");
        assertEquals("OK", result);

        //测试通过redisCache插入的key带有"cache."前缀
        Set<String> keys = cache.keys("*");
        assertTrue(keys.contains("CACHE.test"));
        //assertFalse(keys.contains("test"));

        String value = cache.get("test");
        assertTrue(cache.exists("test"));
        assertEquals("testValue", value);

        assertFalse(cache.exists("xxxxxxxxxxxxxxxxx"));
        String nullStr = cache.get("xxxxxxxxxxxxxxxxx");
        assertNull(nullStr);

        // del
        assertTrue(cache.exists("test"));
        Long dr = cache.del("test");
        assertEquals(Long.valueOf(1L), dr);
        assertFalse(cache.exists("test"));

        //验证过期
        result = cache.setex("test1s", 1, "test1sValue");
        assertEquals("OK", result);
        assertTrue(cache.exists("test1s"));

        // sleep 500 ms
        Thread.sleep(500);

        value = cache.get("test1s");
        assertTrue(cache.exists("test1s"));
        assertEquals("test1sValue", value);

        // sleep 500ms again
        Thread.sleep(500);

        value = cache.get("test1s");
        assertFalse(cache.exists("test1s"));
        assertNull(value);

    }
}
