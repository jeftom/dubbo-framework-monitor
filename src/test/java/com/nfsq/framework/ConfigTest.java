package com.nfsq.framework;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by guoyongzheng on 15/3/17.
 */
public class ConfigTest {

    /**
     * 测试加载nfsq-framework.properties
     */
    @Test
    public void testLoadConfig(){
        String redisHost = Config.properties.getProperty("redisHost");

        assertEquals("redis-test.idc.yst.com.cn", redisHost);
    }

}
