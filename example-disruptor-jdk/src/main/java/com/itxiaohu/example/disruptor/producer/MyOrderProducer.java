package com.itxiaohu.example.disruptor.producer;

import com.itxiaohu.example.disruptor.model.MyOrderEvent;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.List;

public class MyOrderProducer {

    private final Disruptor<MyOrderEvent> disruptor;

    public MyOrderProducer(Disruptor<MyOrderEvent> disruptor){
        this.disruptor = disruptor;
    }

    public void send(String id){
        RingBuffer<MyOrderEvent> ringBuffer = this.disruptor.getRingBuffer();
        long next = ringBuffer.next();
        try{
            MyOrderEvent event = ringBuffer.get(next);
            event.setId(id);
        }finally {
            ringBuffer.publish(next);
        }
    }

    public void send(List<String> ids){
        ids.forEach(this::send);
    }

}
