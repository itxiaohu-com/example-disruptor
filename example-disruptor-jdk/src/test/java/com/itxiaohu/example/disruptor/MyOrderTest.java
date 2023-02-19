package com.itxiaohu.example.disruptor;

import com.itxiaohu.example.disruptor.consumer.MyOrderConsumer;
import com.itxiaohu.example.disruptor.model.MyOrderEvent;
import com.itxiaohu.example.disruptor.producer.MyOrderProducer;
import com.lmax.disruptor.dsl.Disruptor;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * <a href="https://www.yuque.com/simonalong/jishu/qhdcb2" target="_blank"><b>disruptor</b></a>
 *
 * 1.单生产者生产数据，单消费者消费数据
 * 2.单生产者生产数据，多个消费者消费数据
 * 3.多个生产者生产数据，单个消费者消费数据
 * 4.多个生产者分别生产数据，多个消费者消费数据
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


    /**
     * 分组处理
     */
    @Test
    public void test1(){
        Disruptor<MyOrderEvent> disruptor = new Disruptor<>(MyOrderEvent::new, 1024, Executors.defaultThreadFactory());
        disruptor.handleEventsWithWorkerPool(new MyWorkHandler("work1"), new MyWorkHandler("work2"));

        disruptor.start();

        MyOrderProducer myOrderProducer = new MyOrderProducer(disruptor);
        myOrderProducer.send(Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
    }


    /**
     * 顺序消费
     */
    @Test
    public void test2() {
        AtomicInteger counter = new AtomicInteger(0);

        Disruptor<MyOrderEvent> disruptor =
                new Disruptor<>(MyOrderEvent::new, 1024, Executors.defaultThreadFactory());

        // 定义消费者
        MyOrderConsumer consumer1 = new MyOrderConsumer("consumer1");
        MyOrderConsumer consumer2 = new MyOrderConsumer("consumer2");
        MyOrderConsumer consumer3 = new MyOrderConsumer("consumer3");

        // 绑定配置关系
        disruptor.handleEventsWith(consumer1, consumer3).then(consumer2);
        disruptor.start();

        // 定义要发送的数据
        MyOrderProducer myOrderProducer = new MyOrderProducer(disruptor);
        for(int i = 0; i < 10; i++) {
            myOrderProducer.send(String.valueOf(counter.incrementAndGet()));
        }
    }

    /**
     * 测试多分支消费
     * 消费者1和消费者3一个分支，消费者2和消费者4一个分支，消费者3和消费者4消费完毕后，消费者5再进行消费
     */
    @Test
    public void test3(){
        AtomicInteger counter = new AtomicInteger(0);

        Disruptor<MyOrderEvent> disruptor =
                new Disruptor<>(MyOrderEvent::new, 1024, Executors.defaultThreadFactory());

        MyOrderConsumer consumer1 = new MyOrderConsumer("1");
        MyOrderConsumer consumer2 = new MyOrderConsumer("2");
        MyOrderConsumer consumer3 = new MyOrderConsumer("3");
        MyOrderConsumer consumer4 = new MyOrderConsumer("4");
        MyOrderConsumer consumer5 = new MyOrderConsumer("5");

        // 分支:消费者1和消费者3
        disruptor.handleEventsWith(consumer1, consumer3);
        // 分支:消费者2和消费者4
        disruptor.handleEventsWith(consumer2, consumer4);
        // 消费者3和消费者4执行完之后再执行消费者5
        disruptor.after(consumer3, consumer4).handleEventsWith(consumer5);
        disruptor.start();

        MyOrderProducer myOrderProducer = new MyOrderProducer(disruptor);
        for(int i = 1; i <= 3; i++) {
            myOrderProducer.send(String.valueOf(counter.incrementAndGet()));
        }
    }

}
