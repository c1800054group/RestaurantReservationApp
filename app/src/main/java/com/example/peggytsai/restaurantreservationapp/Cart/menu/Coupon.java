package com.example.peggytsai.restaurantreservationapp.Cart.menu;

public class Coupon {  //�˸�ƥ�
	
	private String coupon;
	private Float discount;
	
	public Coupon(String coupon, Float discount) {
		super();
		this.coupon = coupon;
		this.discount = discount;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	
	
	

}
