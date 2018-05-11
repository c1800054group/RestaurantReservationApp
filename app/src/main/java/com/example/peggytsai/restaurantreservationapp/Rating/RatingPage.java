package com.example.peggytsai.restaurantreservationapp.Rating;

import java.io.Serializable;

public class RatingPage implements Serializable {

    private int id, member_id;
    private float score;
    private String comment, comment_time, member_name, comment_reply;

    public RatingPage (int id, String comment, String comment_time, String member_name, int member_id, float score,
                  String comment_reply) {
        this.id = id;
        this.comment = comment;
        this.comment_time = comment_time;
        this.member_name = member_name;
        this.member_id = member_id;
        this.score = score;
        this.comment_reply = comment_reply;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getComment_reply() {
        return comment_reply;
    }

    public void setComment_reply(String comment_reply) {
        this.comment_reply = comment_reply;
    }

}
