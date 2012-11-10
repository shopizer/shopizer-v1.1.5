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
import java.math.BigDecimal;
import java.util.Locale;

import com.salesmanager.core.entity.common.I18NEntity;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.FileUtil;

/**
 * This is an object that contains data related to the products_attributes
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="products_attributes"
 */

public class ProductAttribute implements Serializable, I18NEntity {

	public static String REF = "ProductAttribute";
	public static String PROP_OPTION_VALUE_ID = "optionValueId";
	public static String PROP_PRODUCT_ATTRIBUTE_ID = "productAttributeId";
	public static String PROP_ATTRIBUTE_REQUIRED = "attributeRequired";
	public static String PROP_OPTION_VALUE_PRICE = "optionValuePrice";
	public static String PROP_PRODUCT_ATTRIBUTE_DOWNLOAD = "productAttributeDownload";
	public static String PROP_PRODUCT_ATTRIBUTE_IS_FREE = "productAttributeIsFree";
	public static String PROP_PRODUCT_ID = "productId";
	public static String PROP_PRODUCT_ATTRIBUTE_WEIGHT = "productAttributeWeight";
	public static String PROP_OPTION_ID = "optionId";
	public static String PROP_PRODUCT_OPTION_SORT_ORDER = "productOptionSortOrder";
	public static String PROP_ATTRIBUTE_DEFAULT = "attributeDefault";

	// constructors
	public ProductAttribute() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public ProductAttribute(long productAttributeId) {
		this.setProductAttributeId(productAttributeId);
		initialize();
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setLocale(Locale locale, String currency) {
		this.locale = locale;
		this.currency = currency;
	}

	protected void initialize() {

		this.setAttributeDefault(false);
		this.setAttributeRequired(false);
		this.setOptionId(1);
		this.setOptionValueId(-1);
		this.setOptionValuePrice(new BigDecimal("0"));
		this.setProductAttributeIsFree(false);
		this.setProductAttributeWeight(new BigDecimal("0"));
		this.setProductOptionSortOrder(0);

	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private long productAttributeId;

	// fields
	private long productId;
	private long optionId;
	private long optionValueId;
	private java.math.BigDecimal optionValuePrice;
	private int productOptionSortOrder;
	private boolean productAttributeIsFree;
	private java.math.BigDecimal productAttributeWeight;
	private boolean attributeDefault;
	private boolean attributeRequired;
	private boolean attributeDisplayOnly;
	private boolean attributeDiscounted;

	private Locale locale;
	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Locale getLocale() {
		return locale;
	}

	public boolean isAttributeDiscounted() {
		return attributeDiscounted;
	}

	public void setAttributeDiscounted(boolean attributeDiscounted) {
		this.attributeDiscounted = attributeDiscounted;
	}

	public boolean isAttributeDisplayOnly() {
		return attributeDisplayOnly;
	}

	public void setAttributeDisplayOnly(boolean attributeDisplayOnly) {
		this.attributeDisplayOnly = attributeDisplayOnly;
	}

	// one to one
	private com.salesmanager.core.entity.catalog.ProductAttributeDownload productAttributeDownload;

	// one to one
	private com.salesmanager.core.entity.catalog.ProductOption productOption;

	// one to one
	private com.salesmanager.core.entity.catalog.ProductOptionValue productOptionValue;

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
	 * Return the value associated with the column: options_id
	 */
	public long getOptionId() {
		return optionId;
	}

	/**
	 * Set the value related to the column: options_id
	 * 
	 * @param optionId
	 *            the options_id value
	 */
	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	/**
	 * Return the value associated with the column: options_values_id
	 */
	public long getOptionValueId() {
		return optionValueId;
	}

	/**
	 * Set the value related to the column: options_values_id
	 * 
	 * @param optionValueId
	 *            the options_values_id value
	 */
	public void setOptionValueId(long optionValueId) {
		this.optionValueId = optionValueId;
	}

	/**
	 * Return the value associated with the column: options_values_price
	 */
	public java.math.BigDecimal getOptionValuePrice() {
		return optionValuePrice;
	}

	/**
	 * Set the value related to the column: options_values_price
	 * 
	 * @param optionValuePrice
	 *            the options_values_price value
	 */
	public void setOptionValuePrice(java.math.BigDecimal optionValuePrice) {
		this.optionValuePrice = optionValuePrice;
	}

	/**
	 * Return the value associated with the column: products_options_sort_order
	 */
	public int getProductOptionSortOrder() {
		return productOptionSortOrder;
	}

	/**
	 * Set the value related to the column: products_options_sort_order
	 * 
	 * @param productOptionSortOrder
	 *            the products_options_sort_order value
	 */
	public void setProductOptionSortOrder(int productOptionSortOrder) {
		this.productOptionSortOrder = productOptionSortOrder;
	}

	/**
	 * Return the value associated with the column: product_attribute_is_free
	 */
	public boolean isProductAttributeIsFree() {
		return productAttributeIsFree;
	}

	/**
	 * Set the value related to the column: product_attribute_is_free
	 * 
	 * @param productAttributeIsFree
	 *            the product_attribute_is_free value
	 */
	public void setProductAttributeIsFree(boolean productAttributeIsFree) {
		this.productAttributeIsFree = productAttributeIsFree;
	}

	/**
	 * Return the value associated with the column: products_attributes_weight
	 */
	public java.math.BigDecimal getProductAttributeWeight() {
		return productAttributeWeight;
	}

	/**
	 * Set the value related to the column: products_attributes_weight
	 * 
	 * @param productAttributeWeight
	 *            the products_attributes_weight value
	 */
	public void setProductAttributeWeight(
			java.math.BigDecimal productAttributeWeight) {
		this.productAttributeWeight = productAttributeWeight;
	}

	/**
	 * Return the value associated with the column: attributes_default
	 */
	public boolean isAttributeDefault() {
		return attributeDefault;
	}

	/**
	 * Set the value related to the column: attributes_default
	 * 
	 * @param attributeDefault
	 *            the attributes_default value
	 */
	public void setAttributeDefault(boolean attributeDefault) {
		this.attributeDefault = attributeDefault;
	}

	/**
	 * Return the value associated with the column: attributes_required
	 */
	public boolean isAttributeRequired() {
		return attributeRequired;
	}

	/**
	 * Set the value related to the column: attributes_required
	 * 
	 * @param attributeRequired
	 *            the attributes_required value
	 */
	public void setAttributeRequired(boolean attributeRequired) {
		this.attributeRequired = attributeRequired;
	}

	/**
	 * Return the value associated with the column: productAttributeDownload
	 */
	public com.salesmanager.core.entity.catalog.ProductAttributeDownload getProductAttributeDownload() {
		return productAttributeDownload;
	}

	/**
	 * Set the value related to the column: productAttributeDownload
	 * 
	 * @param productAttributeDownload
	 *            the productAttributeDownload value
	 */
	public void setProductAttributeDownload(
			com.salesmanager.core.entity.catalog.ProductAttributeDownload productAttributeDownload) {
		this.productAttributeDownload = productAttributeDownload;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.catalog.ProductAttribute))
			return false;
		else {
			com.salesmanager.core.entity.catalog.ProductAttribute productAttribute = (com.salesmanager.core.entity.catalog.ProductAttribute) obj;
			return (this.getProductAttributeId() == productAttribute
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

	public com.salesmanager.core.entity.catalog.ProductOption getProductOption() {
		return productOption;
	}

	public void setProductOption(
			com.salesmanager.core.entity.catalog.ProductOption productOption) {
		this.productOption = productOption;
	}

	public com.salesmanager.core.entity.catalog.ProductOptionValue getProductOptionValue() {
		return productOptionValue;
	}

	public void setProductOptionValue(
			com.salesmanager.core.entity.catalog.ProductOptionValue productOptionValue) {
		this.productOptionValue = productOptionValue;
	}

	public String getDescription() {
		if (this.getProductOptionValue() != null) {
			ProductOptionValue optionValue = this.getProductOptionValue();
			return optionValue.getName();
		} else {
			return "";
		}
	}

	public String getHtmlDescriptionPrice() {
		if (this.getProductOptionValue() != null) {
			ProductOptionValue optionValue = this.getProductOptionValue();
			StringBuffer value = new StringBuffer().append(optionValue
					.getName());
			if (this.isProductAttributeIsFree()
					|| (this.getOptionValuePrice() != null && this
							.getOptionValuePrice().doubleValue() > 0)) {
				value.append(" +");
				value.append(CurrencyUtil.displayFormatedAmountWithCurrency(
						this.getOptionValuePrice(), this.getCurrency()));
			} else {
				if (this.getOptionValuePrice() != null
						&& this.getOptionValuePrice().doubleValue() > 0) {
					value.append(" +");
					value
							.append(CurrencyUtil
									.displayFormatedAmountWithCurrency(this
											.getOptionValuePrice(), this
											.getCurrency()));
				}
			}
			return value.toString();
		}
		return "";
	}

	public String getAttributeImagePath() {
		if (this.getProductOptionValue() != null) {
			/** in product image folder **/
			return this.getProductOptionValue().getOptionValueImagePath();
		} else {
			return "";
		}
	}

	public String getAttributeImage() {
		if (this.getProductOptionValue() != null) {
			/** in product image folder **/
			return this.getProductOptionValue().getProductOptionValueImage();
		} else {
			return "";
		}
	}

}