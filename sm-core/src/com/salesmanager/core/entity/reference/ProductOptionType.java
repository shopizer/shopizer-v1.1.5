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
 * This is an object that contains data related to the products_options_types
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="products_options_types"
 */

public class ProductOptionType implements Serializable {

	public static String REF = "ProductOptionType";
	public static String PROP_PRODUCT_OPTION_TYPE_NAME = "productOptionTypeName";
	public static String PROP_PRODUCT_OPTION_TYPE_ID = "productOptionTypeId";

	// constructors
	public ProductOptionType() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public ProductOptionType(int productOptionTypeId) {
		this.setProductOptionTypeId(productOptionTypeId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private int productOptionTypeId;

	// fields
	private java.lang.String productOptionTypeName;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned"
	 *               column="products_options_types_id"
	 */
	public int getProductOptionTypeId() {
		return productOptionTypeId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param productOptionTypeId
	 *            the new ID
	 */
	public void setProductOptionTypeId(int productOptionTypeId) {
		this.productOptionTypeId = productOptionTypeId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: products_options_types_name
	 */
	public java.lang.String getProductOptionTypeName() {
		return productOptionTypeName;
	}

	/**
	 * Set the value related to the column: products_options_types_name
	 * 
	 * @param productOptionTypeName
	 *            the products_options_types_name value
	 */
	public void setProductOptionTypeName(java.lang.String productOptionTypeName) {
		this.productOptionTypeName = productOptionTypeName;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.ProductOptionType))
			return false;
		else {
			com.salesmanager.core.entity.reference.ProductOptionType productOptionType = (com.salesmanager.core.entity.reference.ProductOptionType) obj;
			return (this.getProductOptionTypeId() == productOptionType
					.getProductOptionTypeId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getProductOptionTypeId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}