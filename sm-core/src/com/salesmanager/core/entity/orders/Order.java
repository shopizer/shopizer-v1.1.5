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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.entity.common.I18NEntity;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.IMerchant;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;

/**
 * This is an object that contains data related to the orders table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="orders"
 */

public class Order implements Serializable, IMerchant, I18NEntity {

	public Order(long orderId, long customerId, String customerName,
			String customerCompany, String customerStreetAddress,
			String customerSuburb, String customerCity,
			String customerPostcode, String customerState,
			String customerCountry, String customerTelephone,
			String customerEmailAddress, int customerAddressFormatId,
			String deliveryName, String deliveryCompany,
			String deliveryStreetAddress, String deliverySuburb,
			String deliveryCity, String deliveryPostcode, String deliveryState,
			String deliveryCountry, int deliveryAddressFormatId,
			String billingName, String billingCompany,
			String billingStreetAddress, String billingSuburb,
			String billingCity, String billingPostcode, String billingState,
			String billingCountry, int billingAddressFormatId,
			String paymentMethod, String paymentModuleCode,
			String shippingMethod, String shippingModuleCode,
			String couponCode, Date lastModified, Date datePurchased,
			int orderStatus, Date orderDateFinished, String currency,
			BigDecimal currencyValue, BigDecimal total, BigDecimal orderTax,
			String ipAddress, Integer merchantId, int channel, String cardType,
			String ccOwner, String ccNumber, String ccExpires, String ccCvv) {
		super();
		this.orderId = orderId;
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerCompany = customerCompany;
		this.customerStreetAddress = customerStreetAddress;
		this.customerSuburb = customerSuburb;
		this.customerCity = customerCity;
		this.customerPostcode = customerPostcode;
		this.customerState = customerState;
		this.customerCountry = customerCountry;
		this.customerTelephone = customerTelephone;
		this.customerEmailAddress = customerEmailAddress;
		this.customerAddressFormatId = customerAddressFormatId;
		this.deliveryName = deliveryName;
		this.deliveryCompany = deliveryCompany;
		this.deliveryStreetAddress = deliveryStreetAddress;
		this.deliverySuburb = deliverySuburb;
		this.deliveryCity = deliveryCity;
		this.deliveryPostcode = deliveryPostcode;
		this.deliveryState = deliveryState;
		this.deliveryCountry = deliveryCountry;
		this.deliveryAddressFormatId = deliveryAddressFormatId;
		this.billingName = billingName;
		this.billingCompany = billingCompany;
		this.billingStreetAddress = billingStreetAddress;
		this.billingSuburb = billingSuburb;
		this.billingCity = billingCity;
		this.billingPostcode = billingPostcode;
		this.billingState = billingState;
		this.billingCountry = billingCountry;
		this.billingAddressFormatId = billingAddressFormatId;
		this.paymentMethod = paymentMethod;
		this.paymentModuleCode = paymentModuleCode;
		this.shippingMethod = shippingMethod;
		this.shippingModuleCode = shippingModuleCode;
		this.couponCode = couponCode;
		this.lastModified = lastModified;
		this.datePurchased = datePurchased;
		this.orderStatus = orderStatus;
		this.orderDateFinished = orderDateFinished;
		this.currency = currency;
		this.currencyValue = currencyValue;
		this.total = total;
		this.orderTax = orderTax;
		this.ipAddress = ipAddress;
		this.merchantId = merchantId;
		this.channel = channel;
		this.cardType = cardType;
		this.ccOwner = ccOwner;
		this.ccNumber = ccNumber;
		this.ccExpires = ccExpires;
		this.ccCvv = ccCvv;
	}

	// constructors
	public Order() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Order(long orderId) {
		this.setOrderId(orderId);
		initialize();
	}

	protected void initialize() {

		this.customerId = 0;
		this.customerName = "";
		this.customerStreetAddress = "";
		this.customerCity = "";
		this.customerPostcode = "";
		this.customerCountry = "";
		this.customerTelephone = "";
		this.customerEmailAddress = "";
		this.customerAddressFormatId = 0;
		this.deliveryName = "";
		this.deliveryStreetAddress = "";
		this.deliveryCity = "";
		this.deliveryPostcode = "";
		this.deliveryCountry = "";
		this.deliveryAddressFormatId = 0;
		this.billingName = "";
		this.billingStreetAddress = "";
		this.billingCity = "";
		this.billingPostcode = "";
		this.billingCountry = "";
		this.billingAddressFormatId = 0;
		this.paymentMethod = "";
		this.paymentModuleCode = "";
		this.shippingMethod = "";
		this.shippingModuleCode = "";
		this.orderStatus = 0;
		this.ipAddress = "";
		this.channel = OrderConstants.ONLINE_CHANNEL;
		this.couponCode = "";
		this.currencyValue = new BigDecimal("1");

	}

	// primary key
	private long orderId;

	// fields
	private long customerId;
	private java.lang.String customerName;
	private java.lang.String customerCompany;
	private java.lang.String customerStreetAddress;
	private java.lang.String customerSuburb;
	private java.lang.String customerCity;
	private java.lang.String customerPostcode;
	private java.lang.String customerState;
	private java.lang.String customerCountry;
	private java.lang.String customerTelephone;
	private java.lang.String customerEmailAddress;
	private int customerAddressFormatId;
	private java.lang.String deliveryName;
	private java.lang.String deliveryCompany;
	private java.lang.String deliveryStreetAddress;
	private java.lang.String deliverySuburb;
	private java.lang.String deliveryCity;
	private java.lang.String deliveryPostcode;
	private java.lang.String deliveryState;
	private java.lang.String deliveryCountry;
	private int deliveryAddressFormatId;
	private java.lang.String billingName;
	private java.lang.String billingCompany;
	private java.lang.String billingStreetAddress;
	private java.lang.String billingSuburb;
	private java.lang.String billingCity;
	private java.lang.String billingPostcode;
	private java.lang.String billingState;
	private java.lang.String billingCountry;
	private int billingAddressFormatId;
	private java.lang.String paymentMethod;
	private java.lang.String paymentModuleCode;
	private java.lang.String shippingMethod;
	private java.lang.String shippingModuleCode;
	private java.lang.String couponCode;
	private java.util.Date lastModified;
	private java.util.Date datePurchased;
	private int orderStatus;
	private java.util.Date orderDateFinished;
	private java.lang.String currency;
	private java.math.BigDecimal currencyValue;
	private java.math.BigDecimal total;
	private java.math.BigDecimal orderTax;
	private java.lang.String ipAddress;
	private int merchantId;
	private int channel;
	private String cardType;
	private String ccOwner;
	private String ccNumber;
	private String ccExpires;
	private String ccCvv;
	private boolean displayInvoicePayments;



	// collections
	private java.util.Set<com.salesmanager.core.entity.orders.OrderProduct> orderProducts;
	private java.util.Set<com.salesmanager.core.entity.orders.OrderTotal> orderTotal;
	private java.util.Set<com.salesmanager.core.entity.orders.OrderStatusHistory> orderHistory;

	// for central text
	private String orderTotalText;
	private String orderChannelText;
	private Locale locale;
	private BigDecimal recursiveAmount;

	// for offsystem registry
	private Customer transientCustomer;
	private PaymentMethod transientPaymentMethod;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="orders_id"
	 */
	public long getOrderId() {
		return orderId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param orderId
	 *            the new ID
	 */
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	/**
	 * Return the value associated with the column: customers_id
	 */
	public long getCustomerId() {
		return customerId;
	}

	/**
	 * Set the value related to the column: customers_id
	 * 
	 * @param customerId
	 *            the customers_id value
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/**
	 * Return the value associated with the column: customers_name
	 */
	public java.lang.String getCustomerName() {
		return customerName;
	}

	/**
	 * Set the value related to the column: customers_name
	 * 
	 * @param customerName
	 *            the customers_name value
	 */
	public void setCustomerName(java.lang.String customerName) {
		this.customerName = customerName;
	}

	/**
	 * Return the value associated with the column: customers_company
	 */
	public java.lang.String getCustomerCompany() {
		return customerCompany;
	}

	/**
	 * Set the value related to the column: customers_company
	 * 
	 * @param customerCompany
	 *            the customers_company value
	 */
	public void setCustomerCompany(java.lang.String customerCompany) {
		this.customerCompany = customerCompany;
	}

	/**
	 * Return the value associated with the column: customers_street_address
	 */
	public java.lang.String getCustomerStreetAddress() {
		return customerStreetAddress;
	}

	/**
	 * Set the value related to the column: customers_street_address
	 * 
	 * @param customerStreetAddress
	 *            the customers_street_address value
	 */
	public void setCustomerStreetAddress(java.lang.String customerStreetAddress) {
		this.customerStreetAddress = customerStreetAddress;
	}

	/**
	 * Return the value associated with the column: customers_suburb
	 */
	public java.lang.String getCustomerSuburb() {
		return customerSuburb;
	}

	/**
	 * Set the value related to the column: customers_suburb
	 * 
	 * @param customerSuburb
	 *            the customers_suburb value
	 */
	public void setCustomerSuburb(java.lang.String customerSuburb) {
		this.customerSuburb = customerSuburb;
	}

	/**
	 * Return the value associated with the column: customers_city
	 */
	public java.lang.String getCustomerCity() {
		return customerCity;
	}

	/**
	 * Set the value related to the column: customers_city
	 * 
	 * @param customerCity
	 *            the customers_city value
	 */
	public void setCustomerCity(java.lang.String customerCity) {
		this.customerCity = customerCity;
	}

	/**
	 * Return the value associated with the column: customers_postcode
	 */
	public java.lang.String getCustomerPostcode() {
		return customerPostcode;
	}

	/**
	 * Set the value related to the column: customers_postcode
	 * 
	 * @param customerPostcode
	 *            the customers_postcode value
	 */
	public void setCustomerPostcode(java.lang.String customerPostcode) {
		this.customerPostcode = customerPostcode;
	}

	/**
	 * Return the value associated with the column: customers_state
	 */
	public java.lang.String getCustomerState() {
		return customerState;
	}

	/**
	 * Set the value related to the column: customers_state
	 * 
	 * @param customerState
	 *            the customers_state value
	 */
	public void setCustomerState(java.lang.String customerState) {
		this.customerState = customerState;
	}

	/**
	 * Return the value associated with the column: customers_country
	 */
	public java.lang.String getCustomerCountry() {
		return customerCountry;
	}

	/**
	 * Set the value related to the column: customers_country
	 * 
	 * @param customerCountry
	 *            the customers_country value
	 */
	public void setCustomerCountry(java.lang.String customerCountry) {
		this.customerCountry = customerCountry;
	}

	/**
	 * Return the value associated with the column: customers_telephone
	 */
	public java.lang.String getCustomerTelephone() {
		return customerTelephone;
	}

	/**
	 * Set the value related to the column: customers_telephone
	 * 
	 * @param customerTelephone
	 *            the customers_telephone value
	 */
	public void setCustomerTelephone(java.lang.String customerTelephone) {
		this.customerTelephone = customerTelephone;
	}

	/**
	 * Return the value associated with the column: customers_email_address
	 */
	public java.lang.String getCustomerEmailAddress() {
		return customerEmailAddress;
	}

	/**
	 * Set the value related to the column: customers_email_address
	 * 
	 * @param customerEmailAddress
	 *            the customers_email_address value
	 */
	public void setCustomerEmailAddress(java.lang.String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}

	/**
	 * Return the value associated with the column: customers_address_format_id
	 */
	public int getCustomerAddressFormatId() {
		return customerAddressFormatId;
	}

	/**
	 * Set the value related to the column: customers_address_format_id
	 * 
	 * @param customerAddressFormatId
	 *            the customers_address_format_id value
	 */
	public void setCustomerAddressFormatId(int customerAddressFormatId) {
		this.customerAddressFormatId = customerAddressFormatId;
	}

	/**
	 * Return the value associated with the column: delivery_name
	 */
	public java.lang.String getDeliveryName() {
		return deliveryName;
	}

	/**
	 * Set the value related to the column: delivery_name
	 * 
	 * @param deliveryName
	 *            the delivery_name value
	 */
	public void setDeliveryName(java.lang.String deliveryName) {
		this.deliveryName = deliveryName;
	}

	/**
	 * Return the value associated with the column: delivery_company
	 */
	public java.lang.String getDeliveryCompany() {
		return deliveryCompany;
	}

	/**
	 * Set the value related to the column: delivery_company
	 * 
	 * @param deliveryCompany
	 *            the delivery_company value
	 */
	public void setDeliveryCompany(java.lang.String deliveryCompany) {
		this.deliveryCompany = deliveryCompany;
	}

	/**
	 * Return the value associated with the column: delivery_street_address
	 */
	public java.lang.String getDeliveryStreetAddress() {
		return deliveryStreetAddress;
	}

	/**
	 * Set the value related to the column: delivery_street_address
	 * 
	 * @param deliveryStreetAddress
	 *            the delivery_street_address value
	 */
	public void setDeliveryStreetAddress(java.lang.String deliveryStreetAddress) {
		this.deliveryStreetAddress = deliveryStreetAddress;
	}

	/**
	 * Return the value associated with the column: delivery_suburb
	 */
	public java.lang.String getDeliverySuburb() {
		return deliverySuburb;
	}

	/**
	 * Set the value related to the column: delivery_suburb
	 * 
	 * @param deliverySuburb
	 *            the delivery_suburb value
	 */
	public void setDeliverySuburb(java.lang.String deliverySuburb) {
		this.deliverySuburb = deliverySuburb;
	}

	/**
	 * Return the value associated with the column: delivery_city
	 */
	public java.lang.String getDeliveryCity() {
		return deliveryCity;
	}

	/**
	 * Set the value related to the column: delivery_city
	 * 
	 * @param deliveryCity
	 *            the delivery_city value
	 */
	public void setDeliveryCity(java.lang.String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}

	/**
	 * Return the value associated with the column: delivery_postcode
	 */
	public java.lang.String getDeliveryPostcode() {
		return deliveryPostcode;
	}

	/**
	 * Set the value related to the column: delivery_postcode
	 * 
	 * @param deliveryPostcode
	 *            the delivery_postcode value
	 */
	public void setDeliveryPostcode(java.lang.String deliveryPostcode) {
		this.deliveryPostcode = deliveryPostcode;
	}

	/**
	 * Return the value associated with the column: delivery_state
	 */
	public java.lang.String getDeliveryState() {
		return deliveryState;
	}

	/**
	 * Set the value related to the column: delivery_state
	 * 
	 * @param deliveryState
	 *            the delivery_state value
	 */
	public void setDeliveryState(java.lang.String deliveryState) {
		this.deliveryState = deliveryState;
	}

	/**
	 * Return the value associated with the column: delivery_country
	 */
	public java.lang.String getDeliveryCountry() {
		return deliveryCountry;
	}

	/**
	 * Set the value related to the column: delivery_country
	 * 
	 * @param deliveryCountry
	 *            the delivery_country value
	 */
	public void setDeliveryCountry(java.lang.String deliveryCountry) {
		this.deliveryCountry = deliveryCountry;
	}

	/**
	 * Return the value associated with the column: delivery_address_format_id
	 */
	public int getDeliveryAddressFormatId() {
		return deliveryAddressFormatId;
	}

	/**
	 * Set the value related to the column: delivery_address_format_id
	 * 
	 * @param deliveryAddressFormatId
	 *            the delivery_address_format_id value
	 */
	public void setDeliveryAddressFormatId(int deliveryAddressFormatId) {
		this.deliveryAddressFormatId = deliveryAddressFormatId;
	}

	/**
	 * Return the value associated with the column: billing_name
	 */
	public java.lang.String getBillingName() {
		return billingName;
	}

	/**
	 * Set the value related to the column: billing_name
	 * 
	 * @param billingName
	 *            the billing_name value
	 */
	public void setBillingName(java.lang.String billingName) {
		this.billingName = billingName;
	}

	/**
	 * Return the value associated with the column: billing_company
	 */
	public java.lang.String getBillingCompany() {
		return billingCompany;
	}

	/**
	 * Set the value related to the column: billing_company
	 * 
	 * @param billingCompany
	 *            the billing_company value
	 */
	public void setBillingCompany(java.lang.String billingCompany) {
		this.billingCompany = billingCompany;
	}

	/**
	 * Return the value associated with the column: billing_street_address
	 */
	public java.lang.String getBillingStreetAddress() {
		return billingStreetAddress;
	}

	/**
	 * Set the value related to the column: billing_street_address
	 * 
	 * @param billingStreetAddress
	 *            the billing_street_address value
	 */
	public void setBillingStreetAddress(java.lang.String billingStreetAddress) {
		this.billingStreetAddress = billingStreetAddress;
	}

	/**
	 * Return the value associated with the column: billing_suburb
	 */
	public java.lang.String getBillingSuburb() {
		return billingSuburb;
	}

	/**
	 * Set the value related to the column: billing_suburb
	 * 
	 * @param billingSuburb
	 *            the billing_suburb value
	 */
	public void setBillingSuburb(java.lang.String billingSuburb) {
		this.billingSuburb = billingSuburb;
	}

	/**
	 * Return the value associated with the column: billing_city
	 */
	public java.lang.String getBillingCity() {
		return billingCity;
	}

	/**
	 * Set the value related to the column: billing_city
	 * 
	 * @param billingCity
	 *            the billing_city value
	 */
	public void setBillingCity(java.lang.String billingCity) {
		this.billingCity = billingCity;
	}

	/**
	 * Return the value associated with the column: billing_postcode
	 */
	public java.lang.String getBillingPostcode() {
		return billingPostcode;
	}

	/**
	 * Set the value related to the column: billing_postcode
	 * 
	 * @param billingPostcode
	 *            the billing_postcode value
	 */
	public void setBillingPostcode(java.lang.String billingPostcode) {
		this.billingPostcode = billingPostcode;
	}

	/**
	 * Return the value associated with the column: billing_state
	 */
	public java.lang.String getBillingState() {
		return billingState;
	}

	/**
	 * Set the value related to the column: billing_state
	 * 
	 * @param billingState
	 *            the billing_state value
	 */
	public void setBillingState(java.lang.String billingState) {
		this.billingState = billingState;
	}

	/**
	 * Return the value associated with the column: billing_country
	 */
	public java.lang.String getBillingCountry() {
		return billingCountry;
	}

	/**
	 * Set the value related to the column: billing_country
	 * 
	 * @param billingCountry
	 *            the billing_country value
	 */
	public void setBillingCountry(java.lang.String billingCountry) {
		this.billingCountry = billingCountry;
	}

	/**
	 * Return the value associated with the column: billing_address_format_id
	 */
	public int getBillingAddressFormatId() {
		return billingAddressFormatId;
	}

	/**
	 * Set the value related to the column: billing_address_format_id
	 * 
	 * @param billingAddressFormatId
	 *            the billing_address_format_id value
	 */
	public void setBillingAddressFormatId(int billingAddressFormatId) {
		this.billingAddressFormatId = billingAddressFormatId;
	}

	/**
	 * Return the value associated with the column: payment_method
	 */
	public java.lang.String getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * Set the value related to the column: payment_method
	 * 
	 * @param paymentMethod
	 *            the payment_method value
	 */
	public void setPaymentMethod(java.lang.String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * Return the value associated with the column: payment_module_code
	 */
	public java.lang.String getPaymentModuleCode() {
		return paymentModuleCode;
	}

	/**
	 * Set the value related to the column: payment_module_code
	 * 
	 * @param paymentModuleCode
	 *            the payment_module_code value
	 */
	public void setPaymentModuleCode(java.lang.String paymentModuleCode) {
		this.paymentModuleCode = paymentModuleCode;
	}

	/**
	 * Return the value associated with the column: shipping_method
	 */
	public java.lang.String getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * Set the value related to the column: shipping_method
	 * 
	 * @param shippingMethod
	 *            the shipping_method value
	 */
	public void setShippingMethod(java.lang.String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * Return the value associated with the column: shipping_module_code
	 */
	public java.lang.String getShippingModuleCode() {
		return shippingModuleCode;
	}

	/**
	 * Set the value related to the column: shipping_module_code
	 * 
	 * @param shippingModuleCode
	 *            the shipping_module_code value
	 */
	public void setShippingModuleCode(java.lang.String shippingModuleCode) {
		this.shippingModuleCode = shippingModuleCode;
	}

	/**
	 * Return the value associated with the column: coupon_code
	 */
	public java.lang.String getCouponCode() {
		return couponCode;
	}

	/**
	 * Set the value related to the column: coupon_code
	 * 
	 * @param couponCode
	 *            the coupon_code value
	 */
	public void setCouponCode(java.lang.String couponCode) {
		this.couponCode = couponCode;
	}

	/**
	 * Return the value associated with the column: last_modified
	 */
	public java.util.Date getLastModified() {
		return lastModified;
	}

	/**
	 * Set the value related to the column: last_modified
	 * 
	 * @param lastModified
	 *            the last_modified value
	 */
	public void setLastModified(java.util.Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Return the value associated with the column: date_purchased
	 */
	public java.util.Date getDatePurchased() {
		return datePurchased;
	}

	/**
	 * Set the value related to the column: date_purchased
	 * 
	 * @param datePurchased
	 *            the date_purchased value
	 */
	public void setDatePurchased(java.util.Date datePurchased) {
		this.datePurchased = datePurchased;
	}

	/**
	 * Return the value associated with the column: orders_status
	 */
	public int getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Set the value related to the column: orders_status
	 * 
	 * @param orderStatus
	 *            the orders_status value
	 */
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * Return the value associated with the column: orders_date_finished
	 */
	public java.util.Date getOrderDateFinished() {
		return orderDateFinished;
	}

	/**
	 * Set the value related to the column: orders_date_finished
	 * 
	 * @param orderDateFinished
	 *            the orders_date_finished value
	 */
	public void setOrderDateFinished(java.util.Date orderDateFinished) {
		this.orderDateFinished = orderDateFinished;
	}

	/**
	 * Return the value associated with the column: currency
	 */
	public java.lang.String getCurrency() {
		return currency;
	}

	/**
	 * Set the value related to the column: currency
	 * 
	 * @param currency
	 *            the currency value
	 */
	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	/**
	 * Return the value associated with the column: currency_value
	 */
	public java.math.BigDecimal getCurrencyValue() {
		return currencyValue;
	}

	/**
	 * Set the value related to the column: currency_value
	 * 
	 * @param currencyValue
	 *            the currency_value value
	 */
	public void setCurrencyValue(java.math.BigDecimal currencyValue) {
		this.currencyValue = currencyValue;
	}

	/**
	 * Return the value associated with the column: order_total
	 */
	public java.math.BigDecimal getTotal() {
		return total;
	}

	/**
	 * Set the value related to the column: order_total
	 * 
	 * @param total
	 *            the order_total value
	 */
	public void setTotal(java.math.BigDecimal total) {
		this.total = total;
	}

	/**
	 * Return the value associated with the column: order_tax
	 */
	public java.math.BigDecimal getOrderTax() {
		return orderTax;
	}

	/**
	 * Set the value related to the column: order_tax
	 * 
	 * @param orderTax
	 *            the order_tax value
	 */
	public void setOrderTax(java.math.BigDecimal orderTax) {
		this.orderTax = orderTax;
	}

	/**
	 * Return the value associated with the column: ip_address
	 */
	public java.lang.String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Set the value related to the column: ip_address
	 * 
	 * @param ipAddress
	 *            the ip_address value
	 */
	public void setIpAddress(java.lang.String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Return the value associated with the column: merchantid
	 */
	public int getMerchantId() {
		return merchantId;
	}

	/**
	 * Set the value related to the column: merchantid
	 * 
	 * @param merchantid
	 *            the merchantid value
	 */
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * Return the value associated with the column: channel
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * Set the value related to the column: channel
	 * 
	 * @param channel
	 *            the channel value
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	/**
	 * Return the value associated with the column: orderProducts
	 */
	public java.util.Set<com.salesmanager.core.entity.orders.OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	/**
	 * Set the value related to the column: orderProducts
	 * 
	 * @param orderProducts
	 *            the orderProducts value
	 */
	public void setOrderProducts(
			java.util.Set<com.salesmanager.core.entity.orders.OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public void addToorderProducts(
			com.salesmanager.core.entity.orders.OrderProduct orderProducts) {
		if (null == getOrderProducts())
			setOrderProducts(new java.util.TreeSet<com.salesmanager.core.entity.orders.OrderProduct>());
		getOrderProducts().add(orderProducts);
	}

	/**
	 * Return the value associated with the column: orderTotal
	 */
	public java.util.Set<com.salesmanager.core.entity.orders.OrderTotal> getOrderTotal() {
		return orderTotal;
	}

	/**
	 * Set the value related to the column: orderTotal
	 * 
	 * @param orderTotal
	 *            the orderTotal value
	 */
	public void setOrderTotal(
			java.util.Set<com.salesmanager.core.entity.orders.OrderTotal> orderTotal) {
		this.orderTotal = orderTotal;
	}

	public void addToorderTotal(
			com.salesmanager.core.entity.orders.OrderTotal orderTotal) {
		if (null == getOrderTotal())
			setOrderTotal(new java.util.TreeSet<com.salesmanager.core.entity.orders.OrderTotal>());
		getOrderTotal().add(orderTotal);
	}

	/**
	 * Return the value associated with the column: orderHistory
	 */
	public java.util.Set<com.salesmanager.core.entity.orders.OrderStatusHistory> getOrderHistory() {
		return orderHistory;
	}

	/**
	 * Set the value related to the column: orderHistory
	 * 
	 * @param orderHistory
	 *            the orderHistory value
	 */
	public void setOrderHistory(
			java.util.Set<com.salesmanager.core.entity.orders.OrderStatusHistory> orderHistory) {
		this.orderHistory = orderHistory;
	}

	public void addToorderHistory(
			com.salesmanager.core.entity.orders.OrderStatusHistory orderStatusHistory) {
		if (null == getOrderHistory())
			setOrderHistory(new java.util.TreeSet<com.salesmanager.core.entity.orders.OrderStatusHistory>());
		getOrderHistory().add(orderStatusHistory);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + billingAddressFormatId;
		result = PRIME * result
				+ ((billingCity == null) ? 0 : billingCity.hashCode());
		result = PRIME * result
				+ ((billingCompany == null) ? 0 : billingCompany.hashCode());
		result = PRIME * result
				+ ((billingCountry == null) ? 0 : billingCountry.hashCode());
		result = PRIME * result
				+ ((billingName == null) ? 0 : billingName.hashCode());
		result = PRIME * result
				+ ((billingPostcode == null) ? 0 : billingPostcode.hashCode());
		result = PRIME * result
				+ ((billingState == null) ? 0 : billingState.hashCode());
		result = PRIME
				* result
				+ ((billingStreetAddress == null) ? 0 : billingStreetAddress
						.hashCode());
		result = PRIME * result
				+ ((billingSuburb == null) ? 0 : billingSuburb.hashCode());
		result = PRIME * result
				+ ((cardType == null) ? 0 : cardType.hashCode());
		result = PRIME * result + ((ccCvv == null) ? 0 : ccCvv.hashCode());
		result = PRIME * result
				+ ((ccExpires == null) ? 0 : ccExpires.hashCode());
		result = PRIME * result
				+ ((ccNumber == null) ? 0 : ccNumber.hashCode());
		result = PRIME * result + ((ccOwner == null) ? 0 : ccOwner.hashCode());
		result = PRIME * result + channel;
		result = PRIME * result
				+ ((couponCode == null) ? 0 : couponCode.hashCode());
		result = PRIME * result
				+ ((currency == null) ? 0 : currency.hashCode());
		result = PRIME * result
				+ ((currencyValue == null) ? 0 : currencyValue.hashCode());
		result = PRIME * result + customerAddressFormatId;
		result = PRIME * result
				+ ((customerCity == null) ? 0 : customerCity.hashCode());
		result = PRIME * result
				+ ((customerCompany == null) ? 0 : customerCompany.hashCode());
		result = PRIME * result
				+ ((customerCountry == null) ? 0 : customerCountry.hashCode());
		result = PRIME
				* result
				+ ((customerEmailAddress == null) ? 0 : customerEmailAddress
						.hashCode());
		result = PRIME * result + (int) (customerId ^ (customerId >>> 32));
		result = PRIME * result
				+ ((customerName == null) ? 0 : customerName.hashCode());
		result = PRIME
				* result
				+ ((customerPostcode == null) ? 0 : customerPostcode.hashCode());
		result = PRIME * result
				+ ((customerState == null) ? 0 : customerState.hashCode());
		result = PRIME
				* result
				+ ((customerStreetAddress == null) ? 0 : customerStreetAddress
						.hashCode());
		result = PRIME * result
				+ ((customerSuburb == null) ? 0 : customerSuburb.hashCode());
		result = PRIME
				* result
				+ ((customerTelephone == null) ? 0 : customerTelephone
						.hashCode());
		result = PRIME * result
				+ ((datePurchased == null) ? 0 : datePurchased.hashCode());
		result = PRIME * result + deliveryAddressFormatId;
		result = PRIME * result
				+ ((deliveryCity == null) ? 0 : deliveryCity.hashCode());
		result = PRIME * result
				+ ((deliveryCompany == null) ? 0 : deliveryCompany.hashCode());
		result = PRIME * result
				+ ((deliveryCountry == null) ? 0 : deliveryCountry.hashCode());
		result = PRIME * result
				+ ((deliveryName == null) ? 0 : deliveryName.hashCode());
		result = PRIME
				* result
				+ ((deliveryPostcode == null) ? 0 : deliveryPostcode.hashCode());
		result = PRIME * result
				+ ((deliveryState == null) ? 0 : deliveryState.hashCode());
		result = PRIME
				* result
				+ ((deliveryStreetAddress == null) ? 0 : deliveryStreetAddress
						.hashCode());
		result = PRIME * result
				+ ((deliverySuburb == null) ? 0 : deliverySuburb.hashCode());
		result = PRIME * result
				+ ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = PRIME * result
				+ ((lastModified == null) ? 0 : lastModified.hashCode());
		result = PRIME * result + merchantId;
		result = PRIME
				* result
				+ ((orderDateFinished == null) ? 0 : orderDateFinished
						.hashCode());
		result = PRIME * result + (int) (orderId ^ (orderId >>> 32));
		result = PRIME * result + orderStatus;
		result = PRIME * result
				+ ((orderTax == null) ? 0 : orderTax.hashCode());
		result = PRIME * result
				+ ((paymentMethod == null) ? 0 : paymentMethod.hashCode());
		result = PRIME
				* result
				+ ((paymentModuleCode == null) ? 0 : paymentModuleCode
						.hashCode());
		result = PRIME * result
				+ ((shippingMethod == null) ? 0 : shippingMethod.hashCode());
		result = PRIME
				* result
				+ ((shippingModuleCode == null) ? 0 : shippingModuleCode
						.hashCode());
		result = PRIME * result + ((total == null) ? 0 : total.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Order other = (Order) obj;
		if (billingAddressFormatId != other.billingAddressFormatId)
			return false;
		if (billingCity == null) {
			if (other.billingCity != null)
				return false;
		} else if (!billingCity.equals(other.billingCity))
			return false;
		if (billingCompany == null) {
			if (other.billingCompany != null)
				return false;
		} else if (!billingCompany.equals(other.billingCompany))
			return false;
		if (billingCountry == null) {
			if (other.billingCountry != null)
				return false;
		} else if (!billingCountry.equals(other.billingCountry))
			return false;
		if (billingName == null) {
			if (other.billingName != null)
				return false;
		} else if (!billingName.equals(other.billingName))
			return false;
		if (billingPostcode == null) {
			if (other.billingPostcode != null)
				return false;
		} else if (!billingPostcode.equals(other.billingPostcode))
			return false;
		if (billingState == null) {
			if (other.billingState != null)
				return false;
		} else if (!billingState.equals(other.billingState))
			return false;
		if (billingStreetAddress == null) {
			if (other.billingStreetAddress != null)
				return false;
		} else if (!billingStreetAddress.equals(other.billingStreetAddress))
			return false;
		if (billingSuburb == null) {
			if (other.billingSuburb != null)
				return false;
		} else if (!billingSuburb.equals(other.billingSuburb))
			return false;
		if (cardType == null) {
			if (other.cardType != null)
				return false;
		} else if (!cardType.equals(other.cardType))
			return false;
		if (ccCvv == null) {
			if (other.ccCvv != null)
				return false;
		} else if (!ccCvv.equals(other.ccCvv))
			return false;
		if (ccExpires == null) {
			if (other.ccExpires != null)
				return false;
		} else if (!ccExpires.equals(other.ccExpires))
			return false;
		if (ccNumber == null) {
			if (other.ccNumber != null)
				return false;
		} else if (!ccNumber.equals(other.ccNumber))
			return false;
		if (ccOwner == null) {
			if (other.ccOwner != null)
				return false;
		} else if (!ccOwner.equals(other.ccOwner))
			return false;
		if (channel != other.channel)
			return false;
		if (couponCode == null) {
			if (other.couponCode != null)
				return false;
		} else if (!couponCode.equals(other.couponCode))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (currencyValue == null) {
			if (other.currencyValue != null)
				return false;
		} else if (!currencyValue.equals(other.currencyValue))
			return false;
		if (customerAddressFormatId != other.customerAddressFormatId)
			return false;
		if (customerCity == null) {
			if (other.customerCity != null)
				return false;
		} else if (!customerCity.equals(other.customerCity))
			return false;
		if (customerCompany == null) {
			if (other.customerCompany != null)
				return false;
		} else if (!customerCompany.equals(other.customerCompany))
			return false;
		if (customerCountry == null) {
			if (other.customerCountry != null)
				return false;
		} else if (!customerCountry.equals(other.customerCountry))
			return false;
		if (customerEmailAddress == null) {
			if (other.customerEmailAddress != null)
				return false;
		} else if (!customerEmailAddress.equals(other.customerEmailAddress))
			return false;
		if (customerId != other.customerId)
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (customerPostcode == null) {
			if (other.customerPostcode != null)
				return false;
		} else if (!customerPostcode.equals(other.customerPostcode))
			return false;
		if (customerState == null) {
			if (other.customerState != null)
				return false;
		} else if (!customerState.equals(other.customerState))
			return false;
		if (customerStreetAddress == null) {
			if (other.customerStreetAddress != null)
				return false;
		} else if (!customerStreetAddress.equals(other.customerStreetAddress))
			return false;
		if (customerSuburb == null) {
			if (other.customerSuburb != null)
				return false;
		} else if (!customerSuburb.equals(other.customerSuburb))
			return false;
		if (customerTelephone == null) {
			if (other.customerTelephone != null)
				return false;
		} else if (!customerTelephone.equals(other.customerTelephone))
			return false;
		if (datePurchased == null) {
			if (other.datePurchased != null)
				return false;
		} else if (!datePurchased.equals(other.datePurchased))
			return false;
		if (deliveryAddressFormatId != other.deliveryAddressFormatId)
			return false;
		if (deliveryCity == null) {
			if (other.deliveryCity != null)
				return false;
		} else if (!deliveryCity.equals(other.deliveryCity))
			return false;
		if (deliveryCompany == null) {
			if (other.deliveryCompany != null)
				return false;
		} else if (!deliveryCompany.equals(other.deliveryCompany))
			return false;
		if (deliveryCountry == null) {
			if (other.deliveryCountry != null)
				return false;
		} else if (!deliveryCountry.equals(other.deliveryCountry))
			return false;
		if (deliveryName == null) {
			if (other.deliveryName != null)
				return false;
		} else if (!deliveryName.equals(other.deliveryName))
			return false;
		if (deliveryPostcode == null) {
			if (other.deliveryPostcode != null)
				return false;
		} else if (!deliveryPostcode.equals(other.deliveryPostcode))
			return false;
		if (deliveryState == null) {
			if (other.deliveryState != null)
				return false;
		} else if (!deliveryState.equals(other.deliveryState))
			return false;
		if (deliveryStreetAddress == null) {
			if (other.deliveryStreetAddress != null)
				return false;
		} else if (!deliveryStreetAddress.equals(other.deliveryStreetAddress))
			return false;
		if (deliverySuburb == null) {
			if (other.deliverySuburb != null)
				return false;
		} else if (!deliverySuburb.equals(other.deliverySuburb))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		if (merchantId != other.merchantId)
			return false;
		if (orderDateFinished == null) {
			if (other.orderDateFinished != null)
				return false;
		} else if (!orderDateFinished.equals(other.orderDateFinished))
			return false;
		if (orderId != other.orderId)
			return false;
		if (orderStatus != other.orderStatus)
			return false;
		if (orderTax == null) {
			if (other.orderTax != null)
				return false;
		} else if (!orderTax.equals(other.orderTax))
			return false;
		if (paymentMethod == null) {
			if (other.paymentMethod != null)
				return false;
		} else if (!paymentMethod.equals(other.paymentMethod))
			return false;
		if (paymentModuleCode == null) {
			if (other.paymentModuleCode != null)
				return false;
		} else if (!paymentModuleCode.equals(other.paymentModuleCode))
			return false;
		if (shippingMethod == null) {
			if (other.shippingMethod != null)
				return false;
		} else if (!shippingMethod.equals(other.shippingMethod))
			return false;
		if (shippingModuleCode == null) {
			if (other.shippingModuleCode != null)
				return false;
		} else if (!shippingModuleCode.equals(other.shippingModuleCode))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}

	public String toString() {
		return super.toString();
	}

	public String getStatusmark() {

		if (this.getOrderStatus() == OrderConstants.STATUSDELIVERED
				|| this.getOrderStatus() == OrderConstants.STATUSREFUND) {
			return "green-check.jpg";
		} else {
			return "red-dot.jpg";
		}

	}

	public String getOrderChannelText() {

		String txt = LabelUtil.getInstance().getText(
				"label.dashboard.channel." + this.getChannel());
		return txt;
	}

	public String getStatus() {
		if (this.getLocale() == null) {
			this.setLocale(LocaleUtil.getDefaultLocale());
		}
		Map statusmap = RefCache.getOrderstatuswithlang(LanguageUtil
				.getLanguageNumberCode(this.getLocale().getLanguage()));
		if (statusmap.containsKey(this.getOrderStatus())) {
			OrderStatus os = (OrderStatus) statusmap.get(this.getOrderStatus());
			return os.getOrderStatusName();
		} else {
			return String.valueOf(this.getOrderStatus());
		}
	}

	public String getStatusText() {
		if (this.getLocale() == null) {
			this.setLocale(LocaleUtil.getDefaultLocale());
		}
		Map statusmap = RefCache.getOrderstatuswithlang(LanguageUtil
				.getLanguageNumberCode(this.getLocale().getLanguage()));
		if (statusmap.containsKey(this.getOrderStatus())) {
			OrderStatus os = (OrderStatus) statusmap.get(this.getOrderStatus());

			String txt = "";
			// if(os.getId().getOrderStatusId()==OrderConstants.STATUSPROCESSING
			// || os.getId().getOrderStatusId()==OrderConstants.STATUSBASE) {
			// txt = LabelUtil.getInstance().getText("label.generic.is");
			// if(os.getId().getOrderStatusId()==OrderConstants.STATUSDELIVERED
			// || os.getId().getOrderStatusId()==OrderConstants.STATUSREFUND) {
			// txt = LabelUtil.getInstance().getText("label.generic.hasbeen");
			// } else {
			// //nothing for now
			// }
			return txt + (StringUtils.isBlank(txt) ? "" : " ")
					+ os.getOrderStatusName();

		} else {
			return String.valueOf(this.getOrderStatus());
		}
	}

	// @deprecated
	public String getFormatedDatepurchased() {
		return DateUtil.formatDate(this.getDatePurchased());
	}

	public String getOrderTotalText() {

		return CurrencyUtil.displayFormatedAmountWithCurrency(this.getTotal(),
				this.getCurrency());

	}

	public String getOrderTotalTextNoCurrency() {

		return CurrencyUtil.displayFormatedAmountNoCurrency(this.getTotal(),
				this.getCurrency());

	}

	public String getOrderTotalTaxTextNoCurrency() {

		return CurrencyUtil.displayFormatedAmountNoCurrency(this.getOrderTax(),
				this.getCurrency());

	}

	public void setOrderTotalText(String orderTotalText) {
		this.orderTotalText = orderTotalText;
	}

	public void setOrderChannelText(String orderChannelText) {
		this.orderChannelText = orderChannelText;
	}

	public String getDatePurchasedString() {
		return DateUtil.formatDate(this.getDatePurchased());
	}

	public String getEndDateString() {
		return DateUtil.formatDate(this.getOrderDateFinished());
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCcCvv() {
		return ccCvv;
	}

	public void setCcCvv(String ccCvv) {
		this.ccCvv = ccCvv;
	}

	public String getCcExpires() {
		return ccExpires;
	}

	public void setCcExpires(String ccExpires) {
		this.ccExpires = ccExpires;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public String getCcOwner() {
		return ccOwner;
	}

	public void setCcOwner(String ccOwner) {
		this.ccOwner = ccOwner;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getDescription() {
		return "";
	}

	public Customer getTransientCustomer() {
		return transientCustomer;
	}

	public void setTransientCustomer(Customer transientCustomer) {
		this.transientCustomer = transientCustomer;
	}

	public PaymentMethod getTransientPaymentMethod() {
		return transientPaymentMethod;
	}

	public void setTransientPaymentMethod(PaymentMethod transientPaymentMethod) {
		this.transientPaymentMethod = transientPaymentMethod;
	}

	public BigDecimal getRecursiveAmount() {
		return recursiveAmount;
	}

	public void setRecursiveAmount(BigDecimal recursiveAmount) {
		this.recursiveAmount = recursiveAmount;
	}

	public void setLocale(Locale locale, String currency) {
		this.locale = locale;
	}
	
	public boolean isDisplayInvoicePayments() {
		return displayInvoicePayments;
	}

	public void setDisplayInvoicePayments(boolean displayInvoicePayments) {
		this.displayInvoicePayments = displayInvoicePayments;
	}

}