package com.example.peggytsai.restaurantreservationapp.Message;

public class Coupon {  //�˸�ƥ�
	
	private String coupon;
	private Double discount;
	
	public Coupon(String coupon, Double discount) {
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

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	
	

}
