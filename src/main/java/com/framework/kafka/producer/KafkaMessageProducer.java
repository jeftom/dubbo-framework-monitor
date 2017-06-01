package com.framework.kafka.producer;

import com.framework.kafka.config.KafkaConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by lipeijie on 16/6/6.
 */
public class KafkaMessageProducer {

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaMessageProducer.class);

    private KafkaProducer producer = null;

    public KafkaMessageProducer() {
    }

    /**
     * 逐条发送消息
     * @param topic
     * @param message
     */
    public void sendMessage(String topic, String message) {
        try (KafkaProducer producer = getProducer()) {
            producer.send(new ProducerRecord(topic, message), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    LOGGER.info(String.format("send SUCCESS-->Topic:[%s],Partition:[%s],offset:[%s],Message:[%s]", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),message));
                }
            });
        }
    }

    /**
     * 批量发送消息
     * @param topic
     * @param messages
     */
    public void batchSendMessage(String topic, List<String> messages) {
        try (KafkaProducer producer = getProducer()) {
            messages.forEach(message -> {
                producer.send(new ProducerRecord(topic, message), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {

                        LOGGER.info(String.format("send SUCCESS-->Topic:[%s],Partition:[%s],offset:[%s],Message:[%s]", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),message));
                    }
                });
            });
        }
    }


    public KafkaProducer getProducer() {
        return producer = new KafkaProducer(KafkaConfig.properties);
    }
}
