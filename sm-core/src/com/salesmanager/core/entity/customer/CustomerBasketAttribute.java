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
package com.salesmanager.core.entity.customer;

import java.io.Serializable;

/**
 * This is an object that contains data related to the
 * customers_basket_attributes table. Do not modify this class because it will
 * be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="customers_basket_attributes"
 */

public class CustomerBasketAttribute implements Serializable {

	public static String REF = "CustomerBasketAttribute";
	public static String PROP_PRODUCT_OPTION_VALUE_ID = "productOptionValueId";
	public static String PROP_MERCHANTID = "merchantid";
	public static String PROP_PRODUCT_OPTION_VALUE_TEXT = "productOptionValueText";
	public static String PROP_CUSTOMER_BASKET_ATTRIBUTE_ID = "customerBasketAttributeId";
	public static String PROP_PRODUCT_ID = "productId";
	public static String PROP_CUSTOMER_ID = "customerId";
	public static String PROP_PRODUCT_OPTION_ID = "productOptionId";
	public static String PROP_PRODUCT_OPTION_SORT_ORDER = "productOptionSortOrder";

	// constructors
	public CustomerBasketAttribute() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public CustomerBasketAttribute(long customerBasketAttributeId) {
		this.setCustomerBasketAttributeId(customerBasketAttributeId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private long customerBasketAttributeId;

	// fields
	private long customerId;
	private java.lang.String productId;
	private java.lang.String productOptionId;
	private long productOptionValueId;
	private byte[] productOptionValueText;
	private java.lang.String productOptionSortOrder;
	private java.lang.Integer merchantid;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="hilo" column="customerBasketAttributeId"
	 */
	public long getCustomerBasketAttributeId() {
		return customerBasketAttributeId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param customerBasketAttributeId
	 *            the new ID
	 */
	public void setCustomerBasketAttributeId(long customerBasketAttributeId) {
		this.customerBasketAttributeId = customerBasketAttributeId;
		this.hashCode = Integer.MIN_VALUE;
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
	 * Return the value associated with the column: products_id
	 */
	public java.lang.String getProductId() {
		return productId;
	}

	/**
	 * Set the value related to the column: products_id
	 * 
	 * @param productId
	 *            the products_id value
	 */
	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}

	/**
	 * Return the value associated with the column: products_options_id
	 */
	public java.lang.String getProductOptionId() {
		return productOptionId;
	}

	/**
	 * Set the value related to the column: products_options_id
	 * 
	 * @param productOptionId
	 *            the products_options_id value
	 */
	public void setProductOptionId(java.lang.String productOptionId) {
		this.productOptionId = productOptionId;
	}

	/**
	 * Return the value associated with the column: products_options_value_id
	 */
	public long getProductOptionValueId() {
		return productOptionValueId;
	}

	/**
	 * Set the value related to the column: products_options_value_id
	 * 
	 * @param productOptionValueId
	 *            the products_options_value_id value
	 */
	public void setProductOptionValueId(long productOptionValueId) {
		this.productOptionValueId = productOptionValueId;
	}

	/**
	 * Return the value associated with the column: products_options_value_text
	 */
	public byte[] getProductOptionValueText() {
		return productOptionValueText;
	}

	/**
	 * Set the value related to the column: products_options_value_text
	 * 
	 * @param productOptionValueText
	 *            the products_options_value_text value
	 */
	public void setProductOptionValueText(byte[] productOptionValueText) {
		this.productOptionValueText = productOptionValueText;
	}

	/**
	 * Return the value associated with the column: products_options_sort_order
	 */
	public java.lang.String getProductOptionSortOrder() {
		return productOptionSortOrder;
	}

	/**
	 * Set the value related to the column: products_options_sort_order
	 * 
	 * @param productOptionSortOrder
	 *            the products_options_sort_order value
	 */
	public void setProductOptionSortOrder(
			java.lang.String productOptionSortOrder) {
		this.productOptionSortOrder = productOptionSortOrder;
	}

	/**
	 * Return the value associated with the column: merchantid
	 */
	public java.lang.Integer getMerchantid() {
		return merchantid;
	}

	/**
	 * Set the value related to the column: merchantid
	 * 
	 * @param merchantid
	 *            the merchantid value
	 */
	public void setMerchantid(java.lang.Integer merchantid) {
		this.merchantid = merchantid;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.customer.CustomerBasketAttribute))
			return false;
		else {
			com.salesmanager.core.entity.customer.CustomerBasketAttribute customerBasketAttribute = (com.salesmanager.core.entity.customer.CustomerBasketAttribute) obj;
			return (this.getCustomerBasketAttributeId() == customerBasketAttribute
					.getCustomerBasketAttributeId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getCustomerBasketAttributeId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}