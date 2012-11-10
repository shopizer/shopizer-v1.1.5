package com.salesmanager.core.entity.orders.ws;

public class Invoice {

	private String date;
	private String dueDate;
	private long customerId;
	private String currency;
	private String language;
	
	private boolean calculateShipping;
	
	private Product[] products;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public Product[] getProducts() {
		return products;
	}
	public void setProducts(Product[] products) {
		this.products = products;
	}
	public boolean isCalculateShipping() {
		return calculateShipping;
	}
	public void setCalculateShipping(boolean calculateShipping) {
		this.calculateShipping = calculateShipping;
	}
}
