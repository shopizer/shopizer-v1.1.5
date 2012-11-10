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
 * This is an object that contains data related to the
 * products_attributes_download table. Do not modify this class because it will
 * be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="products_attributes_download"
 */

public class ProductAttributeDownload implements Serializable {

	public static String REF = "ProductAttributeDownload";
	public static String PROP_PRODUCT_ATTRIBUTE_FILENAME = "productAttributeFilename";
	public static String PROP_PRODUCT_ATTRIBUTE = "productAttribute";
	public static String PROP_PRODUCT_ATTRIBUTE_ID = "productAttributeId";
	public static String PROP_PRODUCT_ATTRIBUTE_MAXDAYS = "productAttributeMaxdays";
	public static String PROP_PRODUCT_ATTRIBUTE_MAXCOUNT = "productAttributeMaxcount";

	// constructors
	public ProductAttributeDownload() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public ProductAttributeDownload(int productAttributeId) {
		this.setProductAttributeId(productAttributeId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private long productAttributeId;

	// fields
	private java.lang.String productAttributeFilename;
	private java.lang.Integer productAttributeMaxdays;
	private java.lang.Integer productAttributeMaxcount;

	// one to one
	private com.salesmanager.core.entity.catalog.ProductAttribute productAttribute;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="products_attributes_id"
	 */
	public long getProductAttributeId() {
		return productAttributeId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param productAttributeId
	 *            the new ID
	 */
	public void setProductAttributeId(long productAttributeId) {
		this.productAttributeId = productAttributeId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: products_attributes_filename
	 */
	public java.lang.String getProductAttributeFilename() {
		return productAttributeFilename;
	}

	/**
	 * Set the value related to the column: products_attributes_filename
	 * 
	 * @param productAttributeFilename
	 *            the products_attributes_filename value
	 */
	public void setProductAttributeFilename(
			java.lang.String productAttributeFilename) {
		this.productAttributeFilename = productAttributeFilename;
	}

	/**
	 * Return the value associated with the column: products_attributes_maxdays
	 */
	public java.lang.Integer getProductAttributeMaxdays() {
		return productAttributeMaxdays;
	}

	/**
	 * Set the value related to the column: products_attributes_maxdays
	 * 
	 * @param productAttributeMaxdays
	 *            the products_attributes_maxdays value
	 */
	public void setProductAttributeMaxdays(
			java.lang.Integer productAttributeMaxdays) {
		this.productAttributeMaxdays = productAttributeMaxdays;
	}

	/**
	 * Return the value associated with the column: products_attributes_maxcount
	 */
	public java.lang.Integer getProductAttributeMaxcount() {
		return productAttributeMaxcount;
	}

	/**
	 * Set the value related to the column: products_attributes_maxcount
	 * 
	 * @param productAttributeMaxcount
	 *            the products_attributes_maxcount value
	 */
	public void setProductAttributeMaxcount(
			java.lang.Integer productAttributeMaxcount) {
		this.productAttributeMaxcount = productAttributeMaxcount;
	}

	/**
	 * Return the value associated with the column: productAttribute
	 */
	public com.salesmanager.core.entity.catalog.ProductAttribute getProductAttribute() {
		return productAttribute;
	}

	/**
	 * Set the value related to the column: productAttribute
	 * 
	 * @param productAttribute
	 *            the productAttribute value
	 */
	public void setProductAttribute(
			com.salesmanager.core.entity.catalog.ProductAttribute productAttribute) {
		this.productAttribute = productAttribute;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.catalog.ProductAttributeDownload))
			return false;
		else {
			com.salesmanager.core.entity.catalog.ProductAttributeDownload productAttributeDownload = (com.salesmanager.core.entity.catalog.ProductAttributeDownload) obj;
			return (this.getProductAttributeId() == productAttributeDownload
					.getProductAttributeId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getProductAttributeId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}