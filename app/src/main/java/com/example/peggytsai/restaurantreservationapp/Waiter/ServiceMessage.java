package com.example.peggytsai.restaurantreservationapp.Waiter;

/**
 * Created by user on 2018/8/8.
 */

public class ServiceMessage {
    private int id;
    private String tableNumber;
    private String time;

    public ServiceMessage(int id, String tableNumber, String time) {
        super();
        this.id = id;
        this.tableNumber = tableNumber;
        this.time = time;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTableNumber() {
        return tableNumber;
    }
    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

}
