package com.nfsq.framework.repository;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Created by guoyongzheng on 14-12-15.
 */
public class ShardingCachedRepositoryTest {

    private static class TestRepo extends ShardingCachedRepository<String, String, String> {
        /**
         * repo的唯一名称
         *
         * @return
         */
        @Override
        protected String getCacheName() {
            return "testSharding";
        }

        /**
         * 数据库查询方法
         *
         * @param id
         * @return
         */
        @Override
        protected Map<String, String> queryItems(String id) {
            return new HashMap<>();
        }
    }

    @Test
    public void testShardingRepoInit() {
        TestRepo test = new TestRepo();
        assertNotNull(test);
    }
}
