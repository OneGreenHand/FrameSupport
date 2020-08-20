package com.frame.bean;

/**
 * description: EventBus封装Bean
 */
public class EventBean<T> {
    public int code;
    public T data;

    public EventBean(int code) {
        this.code = code;
    }

    public EventBean(int code, T data) {
        this.code = code;
        this.data = data;
    }

}