package com.itxiaohu.example.disruptor;

import com.itxiaohu.example.disruptor.model.MyOrderEvent;
import com.lmax.disruptor.WorkHandler;

public class MyWorkHandler implements WorkHandler<MyOrderEvent> {

    private String name;

    public MyWorkHandler(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(MyOrderEvent event) throws Exception {
        System.out.println(this.name + "-onEvent-id:" + event.getId());
    }

}
