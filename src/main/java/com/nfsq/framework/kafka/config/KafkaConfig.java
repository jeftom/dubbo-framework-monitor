package com.nfsq.framework.kafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lipeijie on 16/4/15.
 */
public class KafkaConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaConfig.class);

    public final static Properties properties = new Properties();

    private final static String CONFIG_FILE_PATH = "kafka/kafka.properties";

    static {
        //加载主配置文件
        InputStream resourceAsStream = KafkaConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            LOGGER.error("load config file is faild",e.getMessage());
        }
    }

    private KafkaConfig() {
    }
}
