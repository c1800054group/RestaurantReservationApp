package com.example.peggytsai.restaurantreservationapp.Check;

import java.sql.Timestamp;

public class OrderMaster {
	private Timestamp time_order, date_order;
	private int person, member_id, orderId;

	public OrderMaster(Timestamp time_order, Timestamp date_order, int person, int member_id, int orderId) {
		super();
		this.time_order = time_order;
		this.date_order = date_order;
		this.person = person;
		this.member_id = member_id;
		this.orderId = orderId;
	}



	public int getOrderId() {
		return orderId;
	}



	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}



	public Timestamp getTime_order() {
		return time_order;
	}

	public void setTime_order(Timestamp time_order) {
		this.time_order = time_order;
	}

	public Timestamp getDate_orde() {
		return date_order;
	}

	public void setDate_order(Timestamp date_orde) {
		this.date_order = date_orde;
	}

	public int getPerson() {
		return person;
	}

	public void setPerson(int person) {
		this.person = person;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

}
