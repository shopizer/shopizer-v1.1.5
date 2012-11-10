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
import java.util.List;

/**
 * This is an object that contains data related to the customers_basket table.
 * Do not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="customers_basket"
 */

public class CustomerBasket implements Serializable {

	public static String REF = "CustomerBasket";
	public static String PROP_MERCHANTID = "merchantid";
	public static String PROP_FINAL_PRICE = "finalPrice";
	public static String PROP_CUSTOMER_BASKET_QUANTITY = "customerBasketQuantity";
	public static String PROP_PRODUCT_ID = "productId";
	public static String PROP_CUSTOMER_ID = "customerId";
	public static String PROP_CUSTOMER_BASKET_ID = "customerBasketId";
	public static String PROP_CUSTOMER_BASKET_DATE_ADDED = "customerBasketDateAdded";

	// constructors
	public CustomerBasket() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public CustomerBasket(long customerBasketId) {
		this.setCustomerBasketId(customerBasketId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private long customerBasketId;

	// fields
	private long customerId;
	private long productId;
	private int customerBasketQuantity;
	private java.math.BigDecimal finalPrice;
	private java.lang.String customerBasketDateAdded;
	private java.lang.Integer merchantid;

	private List<CustomerBasketAttribute> customerBasketAttributes;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="customers_basket_id"
	 */
	public long getCustomerBasketId() {
		return customerBasketId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param customerBasketId
	 *            the new ID
	 */
	public void setCustomerBasketId(long customerBasketId) {
		this.customerBasketId = customerBasketId;
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
	public long getProductId() {
		return productId;
	}

	/**
	 * Set the value related to the column: products_id
	 * 
	 * @param productId
	 *            the products_id value
	 */
	public void setProductId(long productId) {
		this.productId = productId;
	}

	/**
	 * Return the value associated with the column: customers_basket_quantity
	 */
	public int getCustomerBasketQuantity() {
		return customerBasketQuantity;
	}

	/**
	 * Set the value related to the column: customers_basket_quantity
	 * 
	 * @param customerBasketQuantity
	 *            the customers_basket_quantity value
	 */
	public void setCustomerBasketQuantity(int customerBasketQuantity) {
		this.customerBasketQuantity = customerBasketQuantity;
	}

	/**
	 * Return the value associated with the column: final_price
	 */
	public java.math.BigDecimal getFinalPrice() {
		return finalPrice;
	}

	/**
	 * Set the value related to the column: final_price
	 * 
	 * @param finalPrice
	 *            the final_price value
	 */
	public void setFinalPrice(java.math.BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}

	/**
	 * Return the value associated with the column: customers_basket_date_added
	 */
	public java.lang.String getCustomerBasketDateAdded() {
		return customerBasketDateAdded;
	}

	/**
	 * Set the value related to the column: customers_basket_date_added
	 * 
	 * @param customerBasketDateAdded
	 *            the customers_basket_date_added value
	 */
	public void setCustomerBasketDateAdded(
			java.lang.String customerBasketDateAdded) {
		this.customerBasketDateAdded = customerBasketDateAdded;
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
		if (!(obj instanceof com.salesmanager.core.entity.customer.CustomerBasket))
			return false;
		else {
			com.salesmanager.core.entity.customer.CustomerBasket customerBasket = (com.salesmanager.core.entity.customer.CustomerBasket) obj;
			return (this.getCustomerBasketId() == customerBasket
					.getCustomerBasketId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getCustomerBasketId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public List getCustomerBasketAttributes() {
		return customerBasketAttributes;
	}

	public void setCustomerBasketAttributes(List customerBasketAttributes) {
		this.customerBasketAttributes = customerBasketAttributes;
	}

}