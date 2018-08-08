package com.example.peggytsai.restaurantreservationapp.Check;

import java.io.Serializable;
import java.sql.Timestamp;

public class CheckOrdered implements Serializable{
	
	private int orderId, count, price;
	private String name;
	private Timestamp time_order, date_order;
	public CheckOrdered(int orderId, int count, int price, String name, Timestamp time_order, Timestamp date_order) {
		super();
		this.orderId = orderId;
		this.count = count;
		this.price = price;
		this.name = name;
		this.time_order = time_order;
		this.date_order = date_order;
	}
	public int getOrderId() { 
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getTime_order() {
		return time_order;
	}
	public void setTime_order(Timestamp time_order) {
		this.time_order = time_order;
	}
	public Timestamp getDate_order() {
		return date_order;
	}
	public void setDate_order(Timestamp date_order) {
		this.date_order = date_order;
	}

}
