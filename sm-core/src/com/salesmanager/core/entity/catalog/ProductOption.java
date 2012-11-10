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
import java.util.Set;

/**
 * This is an object that contains data related to the products_options table.
 * Do not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="products_options"
 */

public class ProductOption implements Serializable {

	public static String REF = "ProductOption";
	public static String PROP_PRODUCT_OPTION_TYPE = "productOptionType";
	public static String PROP_PRODUCT_OPTION_SORT_ORDER = "productOptionSortOrder";
	public static String PROP_ID = "id";

	// constructors
	public ProductOption() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public ProductOption(long id) {
		this.setProductOptionId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private long productOptionId;

	// fields
	private int productOptionSortOrder;
	private int productOptionType;

	private int merchantId;

	private Set descriptions;

	private Set values;

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
	 * Return the value associated with the column: products_options_type
	 */
	public int getProductOptionType() {
		return productOptionType;
	}

	/**
	 * Set the value related to the column: products_options_type
	 * 
	 * @param productOptionType
	 *            the products_options_type value
	 */
	public void setProductOptionType(int productOptionType) {
		this.productOptionType = productOptionType;
	}

	public String toString() {
		return super.toString();
	}

	public long getProductOptionId() {
		return productOptionId;
	}

	public void setProductOptionId(long productOptionId) {
		this.productOptionId = productOptionId;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + hashCode;
		result = PRIME * result + merchantId;
		result = PRIME * result
				+ (int) (productOptionId ^ (productOptionId >>> 32));
		result = PRIME * result + productOptionSortOrder;
		result = PRIME * result + productOptionType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ProductOption other = (ProductOption) obj;
		if (hashCode != other.hashCode)
			return false;
		if (merchantId != other.merchantId)
			return false;
		if (productOptionId != other.productOptionId)
			return false;
		if (productOptionSortOrder != other.productOptionSortOrder)
			return false;
		if (productOptionType != other.productOptionType)
			return false;
		return true;
	}

	public Set getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set descriptions) {
		this.descriptions = descriptions;
	}

	public String getName() {
		ProductOptionDescription desc = null;
		if (this.getDescriptions() != null && this.getDescriptions().size() > 0) {
			ProductOptionDescription[] descArray = (ProductOptionDescription[]) this
					.getDescriptions().toArray(
							new ProductOptionDescription[this.getDescriptions()
									.size()]);
			if (descArray != null && descArray.length > 0) {
				desc = descArray[0];
			}
			return desc.getProductOptionName();
		}
		return "";
	}

	public Set getValues() {
		return values;
	}

	public void setValues(Set values) {
		this.values = values;
	}

}