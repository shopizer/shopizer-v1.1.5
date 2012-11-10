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
package com.salesmanager.core.entity.catalog;

import java.io.Serializable;

/**
 * This is an object that contains data related to the specials table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="specials"
 */

public class Special implements Serializable {

	public static String REF = "Special";
	public static String PROP_STATUS = "status";
	public static String PROP_DATE_STATUS_CHANGE = "dateStatusChange";
	public static String PROP_SPECIAL_LAST_MODIFIED = "specialLastModified";
	public static String PROP_SPECIAL_NEW_PRODUCTS_PRICE = "specialNewProductsPrice";
	public static String PROP_EXPIRES_DATE = "expiresDate";
	public static String PROP_SPECIAL_DATE_AVAILABLE = "specialDateAvailable";
	public static String PROP_PRODUCT_ID = "productId";
	public static String PROP_SPECIAL_DATE_ADDED = "specialDateAdded";

	// constructors
	public Special() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Special(long productId) {
		this.setProductId(productId);
		initialize();
	}

	protected void initialize() {
		specialDateAdded = new java.util.Date(new java.util.Date().getTime());
		specialLastModified = new java.util.Date(new java.util.Date().getTime());
		dateStatusChange = new java.util.Date(new java.util.Date().getTime());
		expiresDate = new java.util.Date(new java.util.Date().getTime());
		specialDateAvailable = new java.util.Date(new java.util.Date()
				.getTime());
	}

	// fields
	private long specialId;

	public long getSpecialId() {
		return specialId;
	}

	public void setSpecialId(long specialId) {
		this.specialId = specialId;
	}

	private long productId;
	private java.math.BigDecimal specialNewProductPrice;
	private java.util.Date specialDateAdded;
	private java.util.Date specialLastModified;
	private java.util.Date expiresDate;
	private java.util.Date dateStatusChange;
	private int status;
	private java.util.Date specialDateAvailable;

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
	 * Return the value associated with the column: specials_new_products_price
	 */
	public java.math.BigDecimal getSpecialNewProductPrice() {
		return specialNewProductPrice;
	}

	/**
	 * Set the value related to the column: specials_new_products_price
	 * 
	 * @param specialNewProductsPrice
	 *            the specials_new_products_price value
	 */
	public void setSpecialNewProductPrice(
			java.math.BigDecimal specialNewProductPrice) {
		this.specialNewProductPrice = specialNewProductPrice;
	}

	/**
	 * Return the value associated with the column: specials_date_added
	 */
	public java.util.Date getSpecialDateAdded() {
		return specialDateAdded;
	}

	/**
	 * Set the value related to the column: specials_date_added
	 * 
	 * @param specialDateAdded
	 *            the specials_date_added value
	 */
	public void setSpecialDateAdded(java.util.Date specialDateAdded) {
		this.specialDateAdded = specialDateAdded;
	}

	/**
	 * Return the value associated with the column: specials_last_modified
	 */
	public java.util.Date getSpecialLastModified() {
		return specialLastModified;
	}

	/**
	 * Set the value related to the column: specials_last_modified
	 * 
	 * @param specialLastModified
	 *            the specials_last_modified value
	 */
	public void setSpecialLastModified(java.util.Date specialLastModified) {
		this.specialLastModified = specialLastModified;
	}

	/**
	 * Return the value associated with the column: expires_date
	 */
	public java.util.Date getExpiresDate() {
		return expiresDate;
	}

	/**
	 * Set the value related to the column: expires_date
	 * 
	 * @param expiresDate
	 *            the expires_date value
	 */
	public void setExpiresDate(java.util.Date expiresDate) {
		this.expiresDate = expiresDate;
	}

	/**
	 * Return the value associated with the column: date_status_change
	 */
	public java.util.Date getDateStatusChange() {
		return dateStatusChange;
	}

	/**
	 * Set the value related to the column: date_status_change
	 * 
	 * @param dateStatusChange
	 *            the date_status_change value
	 */
	public void setDateStatusChange(java.util.Date dateStatusChange) {
		this.dateStatusChange = dateStatusChange;
	}

	/**
	 * Return the value associated with the column: status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Set the value related to the column: status
	 * 
	 * @param status
	 *            the status value
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Return the value associated with the column: specials_date_available
	 */
	public java.util.Date getSpecialDateAvailable() {
		return specialDateAvailable;
	}

	/**
	 * Set the value related to the column: specials_date_available
	 * 
	 * @param specialDateAvailable
	 *            the specials_date_available value
	 */
	public void setSpecialDateAvailable(java.util.Date specialDateAvailable) {
		this.specialDateAvailable = specialDateAvailable;
	}

	public String toString() {
		return super.toString();
	}

}