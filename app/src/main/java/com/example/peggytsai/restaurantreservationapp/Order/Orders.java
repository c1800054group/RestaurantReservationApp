package com.example.peggytsai.restaurantreservationapp.Order;

import java.sql.Date;
import java.sql.Timestamp;

public class Orders {

	private Timestamp date_order;
    private String person;
	private int member_id, orderId;
//	private String note, checkout_method;

	public Orders(Timestamp date_order, String person, int member_id, int orderId) {
		super();
		this.date_order = date_order;
		this.person = person;
		this. member_id =  member_id;
		this.orderId = orderId;
		
	}



	public Orders(Timestamp date_order, String person, int member_id) {
		super();
		this.date_order = date_order;
		this.person = person;
		this. member_id =  member_id;
	}
	
	


	public int getMember_id() {
		return member_id;
	}




	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}




	public int getOrderId() {
		return orderId;
	}




	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}




	public Timestamp getDate_order() {
		return date_order;
	}

	public void setDate_order(Timestamp date_order) {
		this.date_order = date_order;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

}
