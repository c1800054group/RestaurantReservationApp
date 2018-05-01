package com.example.peggytsai.restaurantreservationapp.Order;

import java.sql.Date;

public class Orders {
	
	private Date date_order, tinme_chef, time_wsiter, wtime_pick_up;
	private int  id, member_id, person, tables_id, unified_business_id, coupon_id ;
	private String note, checkout_method;
	public Orders(int member_id, int person) {
		super();
		this.member_id = member_id;
		this.person = person;
	}


	public Orders(int memeber_id, int person, Date date_order) {
		super();
		this.member_id = member_id;
		this.person = person;
		this.date_order = date_order;
		
	}
	
	
	public int getMember_id() {
		return member_id;
	}


	public void setMember_id(int memeber_id) {
		this.member_id = memeber_id;
	}


	public Date getDate_order() {
		return date_order;
	}
	public void setDate_order(Date date_order) {
		this.date_order = date_order;
	}
	public int getPerson() {
		return person;
	}
	public void setPerson(int person) {
		this.person = person;
	}
       
	
	  

}
