package com.framework.kafka.handler;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.List;

/**
 * Created by lipeijie on 16/4/22.
 * 暴露给消费消息客户端的接口
 */
public interface KafkaConsumeHandler {

    /**
     * 针对每个主题,批量处理消息
     * 手动提交
     * @param consumerRecords
     * @param consumer
     */
    void manualHandler(List<ConsumerRecord<String, String>> consumerRecords, KafkaConsumer consumer);

    /**
     * 针对每个主题,批量处理消息
     * 自动提交
     * @param consumerRecords
     */
    void autoHandler(List<ConsumerRecord<String, String>> consumerRecords);

}
