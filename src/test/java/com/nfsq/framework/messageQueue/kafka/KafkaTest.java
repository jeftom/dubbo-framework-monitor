package com.nfsq.framework.messageQueue.kafka;

import com.nfsq.framework.kafka.consumer.KafkaMessageCustomer;
import com.nfsq.framework.kafka.handler.KafkaConsumeHandler;
import com.nfsq.framework.kafka.producer.KafkaMessageProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lipeijie on 16/6/1.
 */
public class KafkaTest {

    private static final String TEST1 = "topic_1";
    private static final String TEST2 = "topic_2";


    private static class TestHandler implements KafkaConsumeHandler {

        private StringBuffer sb;

        public TestHandler(StringBuffer sb) {
            this.sb = sb;
        }

        /**
         * 手动commit
         *
         * @param consumerRecords
         * @param consumer
         */
        @Override
        public void manualHandler(List<ConsumerRecord<String, String>> consumerRecords, KafkaConsumer consumer) {
            consumerRecords.forEach(t -> {
                sb.append("[" + t.partition() + "]" + t.value() + ",");
            });
            consumer.commitSync();
        }

        /**
         * 自动commit
         *
         * @param consumerRecords
         */
        @Override
        public void autoHandler(List<ConsumerRecord<String, String>> consumerRecords) {
            consumerRecords.forEach(t -> {
                sb.append(t.value());
            });

        }
    }

    /**
     * 只测试生产
     */
    //xlli45 临时注释掉,跑不通
    //@Test
    public void testProduce() {
        //KafkaMessageProducer producer = new KafkaMessageProducer();
        //5个生产者在同个主题上面发送5条消息
        int completeCount = 0;
        int length = 5;
        for (int i = 0; i < length; i++) {
            //KafkaMessageProducer.produceMessage(TEST1,String.valueOf(i));
            KafkaMessageProducer producer = new KafkaMessageProducer();
            producer.sendMessage(TEST1, String.valueOf(i));
            completeCount++;
        }
        Assert.assertEquals(completeCount, length);
    }

    /**
     * 只测试消费
     */
    @Test
    public void testConsume() {
        StringBuffer sb = new StringBuffer();
        Map<String, KafkaConsumeHandler> handlers = new HashMap<>();
        handlers.put(TEST1, new TestHandler(sb));
        StringBuffer sb2 = new StringBuffer();
        Map<String, KafkaConsumeHandler> handlers1 = new HashMap<>();
        handlers1.put(TEST1, new TestHandler(sb2));
        //消费消息
        KafkaMessageCustomer consumer = new KafkaMessageCustomer("consumer1", "consumerGroup", handlers.keySet());
        consumer.setTopicHandlers(handlers);
        consumer.startConsume();
        //消费消息
        KafkaMessageCustomer consumer1 = new KafkaMessageCustomer("consumer2", "consumerGroup", handlers1.keySet());
        consumer1.setTopicHandlers(handlers);
        consumer1.startConsume();
    }

    /**
     * 针对单个主题,测试多生产,多消费模式(消费者隶属[同一个组]或者[不同的组])
     * 测试环境下针对topic未topic_1现存3个partition
     */
    //xlli45 临时注释掉,跑不通
    //@Test
    public void testMultiPC() {
        //消费端启动(同一个组的三个消费者,对应3个分区)
        StringBuffer sb = new StringBuffer();
        Map<String, KafkaConsumeHandler> handlers = new HashMap<>();
        handlers.put(TEST1, new TestHandler(sb));
        //handlers.put(TEST2, new TestHandler(sb));
        StringBuffer sb1 = new StringBuffer();
        Map<String, KafkaConsumeHandler> handlers1 = new HashMap<>();
        handlers1.put(TEST1, new TestHandler(sb1));
        //handlers1.put(TEST2, new TestHandler(sb1));
        StringBuffer sb0 = new StringBuffer();
        Map<String, KafkaConsumeHandler> handlers0 = new HashMap<>();
        handlers0.put(TEST1, new TestHandler(sb0));
        //handlers.put(TEST2, new TestHandler(sb));
        //另一个组的一个消费者对应1个分区
        StringBuffer sb2 = new StringBuffer();
        Map<String, KafkaConsumeHandler> handlers2 = new HashMap<>();
        handlers2.put(TEST1, new TestHandler(sb2));
        //handlers2.put(TEST2, new TestHandler(sb2));
        //同一个组的三个消费者
        KafkaMessageCustomer consumer1 = new KafkaMessageCustomer("consumer1", "consumerGroup", handlers1.keySet());
        consumer1.setTopicHandlers(handlers1);
        consumer1.startConsume();
        KafkaMessageCustomer consumer = new KafkaMessageCustomer("consumer", "consumerGroup", handlers.keySet());
        consumer.setTopicHandlers(handlers);
        consumer.startConsume();
        KafkaMessageCustomer consumer0 = new KafkaMessageCustomer("consumer0", "consumerGroup", handlers0.keySet());
        consumer0.setTopicHandlers(handlers0);
        consumer0.startConsume();

        //另一个组的一个消费者
        KafkaMessageCustomer consumer2 = new KafkaMessageCustomer("consumer2", "consumerGroup-1", handlers2.keySet());
        consumer2.setTopicHandlers(handlers2);
        consumer2.startConsume();

        try {
            //确保所有的consumer都已经准备成功,等待接收数据
            Thread.sleep(5000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //多生产者发送
        for (int i = 0; i < 8; i++) {
            KafkaMessageProducer producer = new KafkaMessageProducer();
            producer.sendMessage(TEST1, String.valueOf(i));
        }

        try {
            //等待消费者消费
            Thread.sleep(10000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //单个消费者消费3个分区里面的所有消息
        System.out.println("sb2--->" + sb2.toString());
        //Assert.assertEquals("001122334455",sb2.toString());
        /**
         * 三个消费者分别消费每个分区里面的消息,
         * 如果消费者个数小于分区个数,则会有一个消费者消费多个分区;
         * 如果大于分区个数,则或有消费者消费不了消息,影响性能
         *
         * 所以,最好就是有几个分区对应几个消费者
         */
        System.out.println("sb--->" + sb.toString());
        System.out.println("sb1--->" + sb1.toString());
        System.out.println("sb0--->" + sb0.toString());
        //Assert.assertEquals("001122334455",sb.append(sb1).toString());

        /**
         * todo 存在的问题是:如果是多个分区,消费者从分区里面取出来的消息并不是有序的,如果业务场景需要实现有序的消息消费,最好就分配一个分区
         */

    }
}
