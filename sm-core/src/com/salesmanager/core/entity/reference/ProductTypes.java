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
package com.salesmanager.core.entity.reference;

import java.io.Serializable;

/**
 * This is an object that contains data related to the product_types table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="product_types"
 */

public class ProductTypes implements Serializable {

	public static String REF = "ProductTypes";
	public static String PROP_LAST_MODIFIED = "lastModified";
	public static String PROP_ALLOW_ADD_TO_CART = "allowAddToCart";
	public static String PROP_DEFAULT_IMAGE = "defaultImage";
	public static String PROP_TYPE_NAME = "typeName";
	public static String PROP_TYPE_ID = "typeId";
	public static String PROP_TYPE_HANDLER = "typeHandler";
	public static String PROP_DATE_ADDED = "dateAdded";
	public static String PROP_TYPE_MASTER_TYPE = "typeMasterType";

	// constructors
	public ProductTypes() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public ProductTypes(int typeId) {
		this.setTypeId(typeId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private int typeId;

	// fields
	private java.lang.String typeName;
	private java.lang.String typeHandler;
	private int typeMasterType;
	private char allowAddToCart;
	private java.lang.String defaultImage;
	private java.util.Date dateAdded;
	private java.util.Date lastModified;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="type_id"
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param typeId
	 *            the new ID
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: type_name
	 */
	public java.lang.String getTypeName() {
		return typeName;
	}

	/**
	 * Set the value related to the column: type_name
	 * 
	 * @param typeName
	 *            the type_name value
	 */
	public void setTypeName(java.lang.String typeName) {
		this.typeName = typeName;
	}

	/**
	 * Return the value associated with the column: type_handler
	 */
	public java.lang.String getTypeHandler() {
		return typeHandler;
	}

	/**
	 * Set the value related to the column: type_handler
	 * 
	 * @param typeHandler
	 *            the type_handler value
	 */
	public void setTypeHandler(java.lang.String typeHandler) {
		this.typeHandler = typeHandler;
	}

	/**
	 * Return the value associated with the column: type_master_type
	 */
	public int getTypeMasterType() {
		return typeMasterType;
	}

	/**
	 * Set the value related to the column: type_master_type
	 * 
	 * @param typeMasterType
	 *            the type_master_type value
	 */
	public void setTypeMasterType(int typeMasterType) {
		this.typeMasterType = typeMasterType;
	}

	/**
	 * Return the value associated with the column: allow_add_to_cart
	 */
	public char getAllowAddToCart() {
		return allowAddToCart;
	}

	/**
	 * Set the value related to the column: allow_add_to_cart
	 * 
	 * @param allowAddToCart
	 *            the allow_add_to_cart value
	 */
	public void setAllowAddToCart(char allowAddToCart) {
		this.allowAddToCart = allowAddToCart;
	}

	/**
	 * Return the value associated with the column: default_image
	 */
	public java.lang.String getDefaultImage() {
		return defaultImage;
	}

	/**
	 * Set the value related to the column: default_image
	 * 
	 * @param defaultImage
	 *            the default_image value
	 */
	public void setDefaultImage(java.lang.String defaultImage) {
		this.defaultImage = defaultImage;
	}

	/**
	 * Return the value associated with the column: date_added
	 */
	public java.util.Date getDateAdded() {
		return dateAdded;
	}

	/**
	 * Set the value related to the column: date_added
	 * 
	 * @param dateAdded
	 *            the date_added value
	 */
	public void setDateAdded(java.util.Date dateAdded) {
		this.dateAdded = dateAdded;
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

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.ProductTypes))
			return false;
		else {
			com.salesmanager.core.entity.reference.ProductTypes productTypes = (com.salesmanager.core.entity.reference.ProductTypes) obj;
			return (this.getTypeId() == productTypes.getTypeId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getTypeId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}