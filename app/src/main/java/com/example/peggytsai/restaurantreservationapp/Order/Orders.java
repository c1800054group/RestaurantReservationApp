package com.example.peggytsai.restaurantreservationapp.Order;

import java.sql.Date;
import java.sql.Timestamp;

public class Orders {

	private Timestamp date_order;
    private String person;
	private String note, checkout_method;

	public Orders(Timestamp date_order, String person) {
		super();
		this.date_order = date_order;
		this.person = person;
		

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
