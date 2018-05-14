package com.example.peggytsai.restaurantreservationapp.Check;


public class CheckOrder {
    private String orderName,tableName,count,status;

    public CheckOrder(String orderName, String tableName, String count, String status) {
        this.orderName = orderName;
        this.tableName = tableName;
        this.count = count;
        this.status = status;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
