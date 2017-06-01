package com.framework.repository;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by guoyongzheng on 14-12-16.
 */
public class EhcachedDictRepositoryTest {

    private static class Repo extends EhCachedDictRepository<Map<String,String>> {
        /**
         * 实现类要实现从数据库查询对象的方法
         *
         * @return
         */
        @Override
        protected Map<String,String> queryAllItem() {
            Map<String, String> map = new HashMap<>();
            map.put("test", "testValue");
            return map;
        }

        /**
         * 获取cacheName
         *
         * @return
         */
        @Override
        protected String getCacheName() {
            return "EhcachedDictRepositoryTest";
        }
    }

    @Test
    public void testRepo() {
        Repo repo = new Repo();

        Map<String, String> result = repo.getValues();

        assertNotNull(result);
        assertEquals("testValue", result.get("test"));
    }

}
