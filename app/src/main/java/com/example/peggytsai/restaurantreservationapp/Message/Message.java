package com.example.peggytsai.restaurantreservationapp.Message;

import java.io.Serializable;

public class Message implements Serializable {
    private int id;
    private String message_title, message_content;

    public Message(int id, String message_title, String message_content) {
        this.id = id;
        this.message_title = message_title;
        this.message_content = message_content;
    }

    public int getId() {
        return id;
    }

    public String getMessage_title() {
        return message_title;
    }

    public String getMessage_content() {
        return message_content;
    }


}
