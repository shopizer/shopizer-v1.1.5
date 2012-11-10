/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 24, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.entity.orders;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class OrderInvoice implements Serializable {

	private static final long serialVersionUID = 1L;

	private long orderId;
	private String merchantStoreName;
	private int status;
	private String statusText;

	private String merchantStoreLogo;
	private String storeEmailAddress;
	private String storeAddress;
	private String storeCity;
	private String storeCountry;
	private String storeState;
	private String storepostalcode;

	private Date orderDate;
	private boolean isOrderUnpaid;
	private Date dueDate;

	// Customer Billing Address
	private String customerBillingStreetAddress;
	private String customerBillingPostalCode;
	private String customerBillingCity;
	private String customerBillingCountry;
	private String customerBillingState;
	private String customerBillingCountryName;
	private String customerBillingName;

	// If shipping is applicable
	private boolean shipping;

	// Shipping address
	private String customerStreetAddress;
	private String customerPostalCode;
	private String customerCity;
	private String customerCompany;
	private String customerZone;
	private String customerCountry;
	private String customerState;
	private String comments;

	private Collection<OrderProduct> orderProducts;

	private Collection<OrderTotal> orderTotal;
	private Collection<OrderTotal> orderSubTotals;
	private Collection<OrderTotal> orderCredits;
	private Collection<OrderTotal> orderRecurings;

	private String shippingMethods = "";
	private String paymentMethods = "";

	public String getMerchantStoreLogo() {
		return merchantStoreLogo;
	}

	public void setMerchantStoreLogo(String merchantStoreLogo) {
		this.merchantStoreLogo = merchantStoreLogo;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getMerchantStoreName() {
		return merchantStoreName;
	}

	public void setMerchantStoreName(String merchantStoreName) {
		this.merchantStoreName = merchantStoreName;
	}

	public String getStoreEmailAddress() {
		return storeEmailAddress;
	}

	public void setStoreEmailAddress(String storeEmailAddress) {
		this.storeEmailAddress = storeEmailAddress;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreCity() {
		return storeCity;
	}

	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}

	public String getStoreCountry() {
		return storeCountry;
	}

	public void setStoreCountry(String storeCountry) {
		this.storeCountry = storeCountry;
	}

	public String getStorepostalcode() {
		return storepostalcode;
	}

	public void setStorepostalcode(String storepostalcode) {
		this.storepostalcode = storepostalcode;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public boolean isOrderUnpaid() {
		return isOrderUnpaid;
	}

	public void setOrderUnpaid(boolean isOrderUnpaid) {
		this.isOrderUnpaid = isOrderUnpaid;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getCustomerBillingStreetAddress() {
		return customerBillingStreetAddress;
	}

	public void setCustomerBillingStreetAddress(
			String customerBillingStreetAddress) {
		this.customerBillingStreetAddress = customerBillingStreetAddress;
	}

	public String getCustomerBillingPostalCode() {
		return customerBillingPostalCode;
	}

	public void setCustomerBillingPostalCode(String customerBillingPostalCode) {
		this.customerBillingPostalCode = customerBillingPostalCode;
	}

	public String getCustomerBillingCity() {
		return customerBillingCity;
	}

	public void setCustomerBillingCity(String customerBillingCity) {
		this.customerBillingCity = customerBillingCity;
	}

	public String getStoreState() {
		return storeState;
	}

	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}

	public String getCustomerBillingCountry() {
		return customerBillingCountry;
	}

	public void setCustomerBillingCountry(String customerBillingCountry) {
		this.customerBillingCountry = customerBillingCountry;
	}

	public String getCustomerBillingState() {
		return customerBillingState;
	}

	public void setCustomerBillingState(String customerBillingState) {
		this.customerBillingState = customerBillingState;
	}

	public String getCustomerBillingCountryName() {
		return customerBillingCountryName;
	}

	public void setCustomerBillingCountryName(String customerBillingCountryName) {
		this.customerBillingCountryName = customerBillingCountryName;
	}

	public boolean isShipping() {
		return shipping;
	}

	public void setShipping(boolean shipping) {
		this.shipping = shipping;
	}

	public String getCustomerStreetAddress() {
		return customerStreetAddress;
	}

	public void setCustomerStreetAddress(String customerStreetAddress) {
		this.customerStreetAddress = customerStreetAddress;
	}

	public String getCustomerPostalCode() {
		return customerPostalCode;
	}

	public void setCustomerPostalCode(String customerPostalCode) {
		this.customerPostalCode = customerPostalCode;
	}

	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}

	public String getCustomerCompany() {
		return customerCompany;
	}

	public void setCustomerCompany(String customerCompany) {
		this.customerCompany = customerCompany;
	}

	public String getCustomerZone() {
		return customerZone;
	}

	public void setCustomerZone(String customerZone) {
		this.customerZone = customerZone;
	}

	public String getCustomerCountry() {
		return customerCountry;
	}

	public void setCustomerCountry(String customerCountry) {
		this.customerCountry = customerCountry;
	}

	public String getCustomerState() {
		return customerState;
	}

	public void setCustomerState(String customerState) {
		this.customerState = customerState;
	}

	public Collection<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(Collection<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public String getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(String shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	public String getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(String paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public String getCustomerBillingName() {
		return customerBillingName;
	}

	public void setCustomerBillingName(String customerBillingName) {
		this.customerBillingName = customerBillingName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Collection<OrderTotal> getOrderCredits() {
		return orderCredits;
	}

	public void setOrderCredits(Collection<OrderTotal> orderCredits) {
		this.orderCredits = orderCredits;
	}

	public Collection<OrderTotal> getOrderRecurings() {
		return orderRecurings;
	}

	public void setOrderRecurings(Collection<OrderTotal> orderRecurings) {
		this.orderRecurings = orderRecurings;
	}

	public Collection<OrderTotal> getOrderSubTotals() {
		return orderSubTotals;
	}

	public void setOrderSubTotals(Collection<OrderTotal> orderSubTotals) {
		this.orderSubTotals = orderSubTotals;
	}

	public Collection<OrderTotal> getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Collection<OrderTotal> orderTotal) {
		this.orderTotal = orderTotal;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

}
