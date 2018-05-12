package com.example.peggytsai.restaurantreservationapp.Message;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private int id, coupon_id;
    private String message_title, message_content;
    private Date coupon_start, coupon_end;
    private float coupon_discount;

    public Message(int id, String message_title, String message_content, int coupon_id, float coupon_discount,
                   Date coupon_start, Date coupon_end) {
        super();
        this.id = id;
        this.message_title = message_title;
        this.message_content = message_content;
        this.coupon_id = coupon_id;
        this.coupon_discount = coupon_discount;
        this.coupon_start = coupon_start;
        this.coupon_end = coupon_end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getMessage_title() {
        return message_title;
    }

    public void setMessage_title(String message_title) {
        this.message_title = message_title;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public Date getCoupon_start() {
        return coupon_start;
    }

    public void setCoupon_start(Date coupon_start) {
        this.coupon_start = coupon_start;
    }

    public Date getCoupon_end() {
        return coupon_end;
    }

    public void setCoupon_end(Date coupon_end) {
        this.coupon_end = coupon_end;
    }

    public float getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(float coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

}
