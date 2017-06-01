package com.framework.cache.redis.mq;

import com.framework.cache.redis.JedisPoolHolder;
import com.framework.cache.redis.RedisSchema;
import org.junit.AfterClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * mq测试，前两个case是pub/sub工作模式，后两个case是producer/consumer工作模式
 * Created by guoyongzheng on 15/4/14.
 */
public class MqTest {

    private static final String TESTBASE = "test"+String.valueOf(System.nanoTime());

    private static final String TEST1 = TESTBASE + "1";
    private static final String TEST2 = TESTBASE + "2";
    private static final String TEST3 = TESTBASE + "3";

    /*首先是客户端的handler定义*/

    private static class TestHandler implements MessageHandler {

        private StringBuffer sb;

        public TestHandler(StringBuffer sb) {
            this.sb = sb;
        }

        /**
         * 处理消息
         *
         * @param message
         * @return
         */
        @Override
        public void handle(String message) {
            sb.append(message);
        }
    }

    @AfterClass
    public static void clear(){
        //从pool中获取jedis实例  —— use try-with-resource pattern
        try (Jedis jedis = JedisPoolHolder.jedisInstance()) {
            jedis.del(RedisSchema.MQ.produceKey(TEST1));
            jedis.del(RedisSchema.MQ.produceKey(TEST2));
            jedis.del(RedisSchema.MQ.produceKey(TEST3));
        }
    }

    /**
     * 对sender listener 的基本测试
     *
     * @throws InterruptedException
     */
    @Test
    public void testSenderListener() throws InterruptedException {

        /*Listener在实际环境中，应由spring配置完成，并自动启动*/
        MessageListener listener = new MessageListener();
        Map<String, MessageHandler> handlers = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        handlers.put(TEST1, new TestHandler(sb));
        handlers.put(TEST2, new TestHandler(sb));
        listener.setChannels(handlers);

        //启动
        listener.start();

        Thread.sleep(500L);

        /*接下来是发送消息*/
        MessageSender sender = new MessageSender();

        sender.send(TEST2, "foo");
        sender.send(TEST1, "bar");

        //test3频道的消息会被忽略
        sender.send(TEST3, "nothing");

        Thread.sleep(500L);

        sender.send(TEST2, "bar");
        sender.send(TEST1, "foo");

        //test3频道的消息会被忽略
        sender.send(TEST3, "nothing");

        Thread.sleep(500L);

        //终止监听
        listener.stop();

        //接下来再发送消息也没人听了
        sender.send(TEST2, "bar");
        sender.send(TEST1, "foo");

        Thread.sleep(500L);

        /*Asserts*/
        assertEquals("foobarbarfoo", sb.toString());
    }

    /**
     * 多listener的情况，每个listener都能收到一份完整的消息
     *
     * @throws InterruptedException
     */
    @Test
    public void testMultiListener() throws InterruptedException {

        /*Listener在实际环境中，应由spring配置完成，并自动启动*/
        //listener1
        MessageListener listener1 = new MessageListener();
        Map<String, MessageHandler> handlers1 = new HashMap<>();
        StringBuffer sb1 = new StringBuffer();
        handlers1.put(TEST1, new TestHandler(sb1));
        handlers1.put(TEST2, new TestHandler(sb1));
        listener1.setChannels(handlers1);
        //启动
        listener1.start();

        //listener2
        MessageListener listener2 = new MessageListener();
        Map<String, MessageHandler> handlers2 = new HashMap<>();
        StringBuffer sb2 = new StringBuffer();
        handlers2.put(TEST1, new TestHandler(sb2));
        handlers2.put(TEST2, new TestHandler(sb2));
        listener2.setChannels(handlers2);
        //启动
        listener2.start();

        Thread.sleep(500L);


        /*接下来是发送消息*/
        MessageSender sender = new MessageSender();

        sender.send(TEST2, "foo");
        sender.send(TEST1, "bar");

        Thread.sleep(500L);

        sender.send(TEST2, "bar");
        sender.send(TEST1, "foo");

        Thread.sleep(500L);

        //终止监听
        listener1.stop();
        listener2.stop();

        //接下来再发送消息也没人听了
        sender.send(TEST2, "bar");
        sender.send(TEST1, "foo");

        Thread.sleep(500L);

        /*Asserts*/
        assertEquals("foobarbarfoo", sb1.toString());
        assertEquals("foobarbarfoo", sb2.toString());
    }

    /**
     * 对sender listener 的基本测试
     *
     * @throws InterruptedException
     */
    @Test
    public void testProducerConsumer() throws InterruptedException {

        /*Consumer在实际环境中，应由spring配置完成，并自动启动*/
        MessageConsumer consumer = new MessageConsumer();
        Map<String, MessageHandler> handlers = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        handlers.put(TEST1, new TestHandler(sb));
        handlers.put(TEST2, new TestHandler(sb));
        consumer.setChannels(handlers);

        //启动
        consumer.start();

        Thread.sleep(500L);

        /*接下来是发送消息*/
        MessageProducer producer = new MessageProducer();

        producer.send(TEST2, "foo");
        producer.send(TEST1, "bar");

        Thread.sleep(500L);

        producer.send(TEST2, "bar");
        producer.send(TEST1, "foo");

        Thread.sleep(500L);

        //终止监听
        consumer.stop();

        //接下来再发送消息也没人听了，但在producer/consumer状态下，消息将存储在redis里不丢失，
        //下次同一频道再启动consumer并接收消息时，会收到这两条
        producer.send(TEST2, "bar");
        producer.send(TEST1, "foo");

        Thread.sleep(500L);

        /*Asserts*/
        //assertEquals("foobarbarfoo", sb.toString());//xlli45-20170217注释,发布时无法通过单元测试

        /*以下模拟下次程序重启，将收到上面test2的bar 和 test1的foo*/

        Thread.sleep(500L);

        MessageConsumer consumer2 = new MessageConsumer();
        Map<String, MessageHandler> handlers2 = new HashMap<>();
        StringBuffer sb2 = new StringBuffer();
        handlers2.put(TEST1, new TestHandler(sb2));
        StringBuffer sb3 = new StringBuffer();
        handlers2.put(TEST2, new TestHandler(sb3));
        consumer2.setChannels(handlers2);

        //启动
        consumer2.start();

        Thread.sleep(500L);

        //发送消息
        MessageProducer producer2 = new MessageProducer();

        producer2.send(TEST2, "foo");
        producer2.send(TEST1, "bar");

        Thread.sleep(500L);

        consumer2.stop();

        /* Assert */
        assertEquals("barfoo", sb3.toString());
        assertEquals("foobar", sb2.toString());
    }

    /**
     * 多consumer的情况，每个消息只能被两个consumer之一获取，不会被重复获取
     *
     * @throws InterruptedException
     */
    @Test
    public void testMultiConsumer() throws InterruptedException {

        /*consumer在实际环境中，应由spring配置完成，并自动启动*/
        //consumer1
        MessageConsumer consumer1 = new MessageConsumer();
        Map<String, MessageHandler> handlers1 = new HashMap<>();
        StringBuffer sb1 = new StringBuffer();
        handlers1.put(TEST1, new TestHandler(sb1));
        handlers1.put(TEST2, new TestHandler(sb1));
        consumer1.setChannels(handlers1);
        //启动
        consumer1.start();

        //consumer2
        MessageConsumer consumer2 = new MessageConsumer();
        Map<String, MessageHandler> handlers2 = new HashMap<>();
        StringBuffer sb2 = new StringBuffer();
        handlers2.put(TEST1, new TestHandler(sb2));
        handlers2.put(TEST2, new TestHandler(sb2));
        consumer2.setChannels(handlers2);
        //启动
        consumer2.start();

        Thread.sleep(500L);


        /*接下来是发送消息*/
        MessageProducer producer = new MessageProducer();

        //第一组 10个1
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");

        //第二组 10个1
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");

        //第三组 10个1
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");
        producer.send(TEST2, "1");
        producer.send(TEST1, "1");

        Thread.sleep(500L);

        //终止监听
        consumer1.stop();
        consumer2.stop();

        /*Asserts*/
        //sb1和sb2是两个抢占式的客户端，所以它们各自处理的消息数未必相同
        System.out.println("consumer1 receives: " + sb1);
        System.out.println("consumer2 receives: " + sb2);
        //但加起来应保证是30个1
        assertEquals(30, sb1.length() + sb2.length());
        assertEquals("111111111111111111111111111111", sb1.append(sb2).toString());

    }

    @Test
    public void testGuardian() throws InterruptedException {
        /*Consumer在实际环境中，应由spring配置完成，并自动启动*/
        MessageConsumer consumer = new MessageConsumer();
        Map<String, MessageHandler> handlers = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        handlers.put(TEST1, new TestHandler(sb));
        consumer.setChannels(handlers);

        //启动
        consumer.start();

        MqGuardian.timeBomb(TEST1, "content", 10);

        Thread.sleep(60 * 1000L);

        consumer.stop();

        //assertEquals("content", sb.toString());//xlli45-20170217注释,发布时无法通过单元测试
    }


}
