package com.example.peggytsai.restaurantreservationapp.Rating;

import java.io.Serializable;

public class RatingPage implements Serializable {

    private int id, score;
    private String comment, comment_time, member_name;

    public RatingPage(int id, int score, String comment, String comment_time, String member_name) {
        this.id = id;
        this.score = score;
        this.comment = comment;
        this.comment_time = comment_time;
        this.member_name = member_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
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
}
