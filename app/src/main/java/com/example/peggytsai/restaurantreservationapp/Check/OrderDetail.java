package com.example.peggytsai.restaurantreservationapp.Check;

import java.sql.Timestamp;

public class OrderDetail {
	private Timestamp time_order, date_order;
	private int person, orderId;
	public OrderDetail(Timestamp time_order, Timestamp date_order, int person, int orderId) {
		super();
		this.time_order = time_order;
		this.date_order = date_order;
		this.person = person;
		this.orderId = orderId;
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
	public int getPerson() {
		return person;
	}
	public void setPerson(int person) {
		this.person = person;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	

}
