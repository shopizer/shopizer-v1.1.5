package com.salesmanager.core.entity.orders.ws;

public class Product {

	private long productId;
	private int quantity;
	
	private boolean overWritePrice;
	private double price;
	
	private Attribute[] attributes;
	
	public Attribute[] getAttributes() {
		return attributes;
	}
	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isOverWritePrice() {
		return overWritePrice;
	}
	public void setOverWritePrice(boolean overWritePrice) {
		this.overWritePrice = overWritePrice;
	}
}
