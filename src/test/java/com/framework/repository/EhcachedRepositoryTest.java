package com.framework.repository;

import com.framework.tools.CommonPair;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * 测试ehcache repository
 * Created by guoyongzheng on 14-12-4.
 */
public class EhcachedRepositoryTest {

    /**
     * 测试类1
     */
    private static class TestRepository1 extends EhCachedRepository<String, CommonPair<String,String>> {
        /**
         * 实现类要实现从数据库查询对象的方法
         *
         * @param s
         * @return
         */
        @Override
        protected CommonPair<String, String> queryItem(String s) {
            if (s == null || !s.equals("key")) {
                return null;
            }
            return new CommonPair<>("key","value");
        }

        /**
         * 获取cacheName
         *
         * @return
         */
        @Override
        protected String getCacheName() {
            return "testRepository1";
        }
    }

    /**
     * 测试类2
     */
    private static class TestRepository2 extends EhCachedRepository<String, Map<String,String>> {
        /**
         * 实现类要实现从数据库查询对象的方法
         *
         * @param s
         * @return
         */
        @Override
        protected Map<String, String> queryItem(String s) {
            if (s == null || !s.equals("key")) {
                return null;
            }
            return new HashMap<>();
        }

        /**
         * 获取cacheName
         *
         * @return
         */
        @Override
        protected String getCacheName() {
            return "testRepository2";
        }
    }

    @Test
    public void testCachedRepository() {

        TestRepository1 repo1 = new TestRepository1();

        assertNotNull(repo1);

        assertNull(repo1.getValue("test"));

        assertEquals(repo1.getValue("key"), new CommonPair<String, String>("key","value"));
    }

    @Test
    public void testModify() {
        TestRepository2 repo2 = new TestRepository2();

        Map<String, String> map1 = repo2.getValue("key");

        Map<String, String> map2 = repo2.getValue("key");

        assertTrue(map1.isEmpty());
        assertTrue(map2.isEmpty());

        map1.put("test", "test");
        assertFalse(map1.isEmpty());
        assertFalse(map2.isEmpty());

        assertEquals(map1, map2);
    }
}
