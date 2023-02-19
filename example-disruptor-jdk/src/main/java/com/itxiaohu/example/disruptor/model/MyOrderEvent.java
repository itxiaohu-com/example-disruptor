package com.itxiaohu.example.disruptor.model;

public class MyOrderEvent implements MyEvent {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
