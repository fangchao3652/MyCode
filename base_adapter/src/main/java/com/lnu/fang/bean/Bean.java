package com.lnu.fang.bean;

/**
 * Created by Fang on 2015/11/14.
 */
public class Bean {
    String title;
    String  desc;
    String time;
    String  phone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Bean(String desc, String title, String phone, String time) {
        this.desc = desc;
        this.title = title;
        this.phone = phone;
        this.time = time;
    }
}
