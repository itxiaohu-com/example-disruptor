package com.itxiaohu.example.disruptor;

import com.itxiaohu.example.disruptor.consumer.MyOrderConsumer;
import com.itxiaohu.example.disruptor.model.MyOrderEvent;
import com.itxiaohu.example.disruptor.producer.MyOrderProducer;
import com.lmax.disruptor.dsl.Disruptor;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * https://www.yuque.com/simonalong/jishu/qhdcb2
 */
public class MyOrderTest {

    @Test
    public void test() {
        Disruptor<MyOrderEvent> disruptor =
                new Disruptor<>(MyOrderEvent::new, 1024, Executors.defaultThreadFactory());

        // 定义消费者
        MyOrderConsumer consumer1 = new MyOrderConsumer("consumer1");
        MyOrderConsumer consumer2 = new MyOrderConsumer("consumer2");
        MyOrderConsumer consumer3 = new MyOrderConsumer("consumer3");

        // 绑定配置关系
        disruptor.handleEventsWith(consumer1, consumer2, consumer3);
        disruptor.start();

        // 定义要发送的数据
        MyOrderProducer myOrderProducer = new MyOrderProducer(disruptor);
        myOrderProducer.send(Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
    }

}
