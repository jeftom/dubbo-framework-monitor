package com.framework.cache.redis;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * 测试 redis db
 * Created by guoyongzheng on 15/3/18.
 */
public class RedisDBTest {

    @Test
    public void testDB() throws InterruptedException {
        // 默认使用测试redis  (10.1.3.126)

        RedisDB db = new RedisDB();

        String result = db.set("test", "testValue");
        assertEquals("OK", result);

        RedisCache cache = new RedisCache();
        //测试通过redisCache插入的key带有"cache."前缀
        Set<String> keys = cache.keys("*");
        assertTrue(keys.contains("DB.test"));
        //assertFalse(keys.contains("test"));

        String value = db.get("test");
        assertTrue(db.exists("test"));
        assertEquals("testValue", value);

        assertFalse(db.exists("xxxxxxxxxxxxxxxxx"));
        String nullStr = db.get("xxxxxxxxxxxxxxxxx");
        assertNull(nullStr);

        //验证不过期
        result = db.set("test1s", "test1sValue");
        assertEquals("OK", result);
        assertTrue(db.exists("test1s"));

        // sleep 1000 ms
        Thread.sleep(1000);

        value = db.get("test1s");
        assertTrue(db.exists("test1s"));
        assertEquals("test1sValue", value);

        //删除keys
        long delRet = db.del("test");
        assertEquals(1L, delRet);
        assertFalse(db.exists("test"));

        delRet = db.del("test", "test1s");
        assertEquals(1l, delRet);
        assertFalse(db.exists("test"));
        assertFalse(db.exists("test1s"));

    }
}
