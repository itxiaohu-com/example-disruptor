package com.itxiaohu.example.disruptor.consumer;

import com.itxiaohu.example.disruptor.model.MyOrderEvent;
import com.lmax.disruptor.EventHandler;

public class MyOrderConsumer implements EventHandler<MyOrderEvent> {

    private final String name;

    public MyOrderConsumer(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(MyOrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(this.name + "-onEvent-id:" + event.getId());
    }

    public String getName() {
        return name;
    }
}
