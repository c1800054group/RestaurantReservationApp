package com.example.peggytsai.restaurantreservationapp.Message;

import java.io.Serializable;

public class Message implements Serializable {
    private int id;
    private String message_title, message_content, message_date;

    public Message(int id, String message_title, String message_content, String message_date) {
        this.id = id;
        this.message_title = message_title;
        this.message_content = message_content;
        this.message_date = message_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMessage_date() {
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }
}
