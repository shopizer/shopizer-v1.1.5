package com.salesmanager.core.entity.orders.ws;

import com.salesmanager.core.service.ws.WebServiceResponse;

public class CreateInvoiceWebServiceResponse extends WebServiceResponse{
	
	private long invoiceId;
	private OrderTotal[] orderTotals;
	public long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	public OrderTotal[] getOrderTotals() {
		return orderTotals;
	}
	public void setOrderTotals(OrderTotal[] orderTotals) {
		this.orderTotals = orderTotals;
	}

}
