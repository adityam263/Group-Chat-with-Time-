package com.aditya.Adapter;

import java.util.Date;

public class BeanClass {
    public BeanClass(String name, String message, String type, String time) {
        this.name = name;
        this.message = message;
        this.type = type;
        this.time = time;
    }

    String name, message, type,time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
