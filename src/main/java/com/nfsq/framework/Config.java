package com.nfsq.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置和常量
 * Created by guoyongzheng on 15/3/17.
 */
public final class Config {

    private static Log log = LogFactory.getLog(Config.class);

    /**
     * 私有化构造函数，避免实例化
     */
    private Config(){}

    /**
     * 配置文件路径
     */
    private static final String PROPERTIES_FILE_PATH = "nfsq-framework.properties";

    /**
     * 配置
     */
    public static final Properties properties = new Properties();

    // 通过静态block尝试读取配置文件
    static {
        try {
            InputStream in = Config.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH);
            if (in != null) {
                properties.load(in);
            }
        } catch (Exception e) {
            log.error(e);
            throw new IllegalStateException("读取配置文件出错: " + e.getMessage(), e);
        }
    }

    // 以下为常量

    /**
     * Redis host 地址
     */
    public static final String REDIS_HOST_KEY = "redisHost";

    /**
     * Ehcache 时间机器 —— 默认值为off，当值为"on"的时候，时间将会被加速60倍
     */
    public static final String EHCACHE_TIME_MACHINE = "ehcacheTimeMachine";

    /**
     * 默认ehcacheLiveTime存活时间
     */
    public static final String EHCACHE_LIVE_TIME = "ehcacheLiveTime";

}
