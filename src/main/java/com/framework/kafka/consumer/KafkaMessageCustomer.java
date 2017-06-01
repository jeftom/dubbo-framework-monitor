package com.framework.kafka.consumer;

import com.framework.kafka.config.KafkaConfig;
import com.framework.kafka.handler.KafkaConsumeHandler;
import com.framework.tools.FrameworkThreadPool;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lipeijie on 16/6/3.
 */
@Component
public class KafkaMessageCustomer {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaMessageCustomer.class);

    //消费流
    private KafkaConsumer<String, String> consumer;
    //订阅的主题集合
    private Collection<String> topics = null;
    //主题对应的处理handler
    private Map<String, KafkaConsumeHandler> topicHandlers;

    private String customerId;

    /**
     * @param groupName(不同的组的成员可以消费同一条消息)
     * @param topics(需要消费的topic集合)
     */
    public KafkaMessageCustomer(String groupName, Collection<String> topics) {
        this.topics = topics;
        Properties properties = KafkaConfig.properties;
        properties.put("group.id", groupName);
        this.consumer = new KafkaConsumer<String, String>(properties);
    }

    public KafkaMessageCustomer(String customerId, String groupName, Collection<String> topics) {
        this.customerId = customerId;
        this.topics = topics;
        Properties properties = KafkaConfig.properties;
        properties.put("group.id", groupName);
        this.consumer = new KafkaConsumer<String, String>(properties);
    }

    /**
     * 开始消费
     */
    public void startConsume() {
        FrameworkThreadPool.execute(() -> {
            try {
                //订阅主题
                consumer.subscribe(topics);

                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(5000);

                /*Map<String, List<ConsumerRecord<String, String>>> recordMap = new HashMap<>();
                for (ConsumerRecord<String, String> record : records) {
                    List<ConsumerRecord<String, String>> consumerRecordList = new ArrayList<>();

                    if (recordMap.containsKey(record.topic())) {
                        recordMap.get(record.topic()).add(record);
                    } else {
                        consumerRecordList.add(record);
                        recordMap.put(record.topic(), consumerRecordList);
                    }
                }
                //针对每个消息确定每个主题的处理handler---->处理
                for (String topic : recordMap.keySet()) {
                    KafkaConsumeHandler kafkaConsumeHandler = topicHandlers.get(topic);
                    if (KafkaConfig.properties.get("enable.auto.commit").equals("false")) {
                        kafkaConsumeHandler.manualHandler(recordMap.get(topic), consumer);
                    } else {
                        kafkaConsumeHandler.autoHandler(recordMap.get(topic));
                    }
                }*/
                    //先循环分区
                    for (TopicPartition partitions : records.partitions()) {
                        List<ConsumerRecord<String, String>> partitionRecords = records.records(partitions);
                        Map<String, List<ConsumerRecord<String, String>>> recordMap = new HashMap<>();
                        //在循环分区里面的消息
                        for (ConsumerRecord<String, String> record : partitionRecords) {
                            List<ConsumerRecord<String, String>> consumerRecordList = new ArrayList<>();

                            if (recordMap.containsKey(record.topic())) {
                                recordMap.get(record.topic()).add(record);
                            } else {
                                consumerRecordList.add(record);
                                recordMap.put(record.topic(), consumerRecordList);
                            }
                        }
                        //针对每个消息确定每个主题的处理handler---->处理
                        for (String topic : recordMap.keySet()) {
                            KafkaConsumeHandler kafkaConsumeHandler = topicHandlers.get(topic);
                            System.out.println(customerId + " get the message from producer");
                            if (KafkaConfig.properties.get("enable.auto.commit").equals("false")) {
                                kafkaConsumeHandler.manualHandler(recordMap.get(topic), consumer);
                            } else {
                                kafkaConsumeHandler.autoHandler(recordMap.get(topic));
                            }
                        }
                    }
                }

            } catch (WakeupException e) {
                //ignore
            } finally {
                consumer.close();
            }
        });
    }

    public void shutdown() {
        consumer.wakeup();
    }

    public void setTopicHandlers(Map<String, KafkaConsumeHandler> topicHandlers) {
        this.topicHandlers = topicHandlers;
    }

    public KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }
}
