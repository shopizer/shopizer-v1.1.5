package com.salesmanager.core.entity.orders.ws;

public class OrderTotal {

	private long orderId;
	private java.lang.String title;
	private java.lang.String text;
	private double value;
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public java.lang.String getTitle() {
		return title;
	}
	public void setTitle(java.lang.String title) {
		this.title = title;
	}
	public java.lang.String getText() {
		return text;
	}
	public void setText(java.lang.String text) {
		this.text = text;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
}
