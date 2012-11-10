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

import com.salesmanager.core.util.FileUtil;

/**
 * This is an object that contains data related to the products_options_values
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="products_options_values"
 */

public class ProductOptionValue implements Serializable {

	public static String REF = "ProductOptionValue";
	public static String PROP_PRODUCT_OPTION_VALUE_SORT_ORDER = "productOptionValueSortOrder";

	// constructors
	public ProductOptionValue() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public ProductOptionValue(long productOptionValueId) {
		this.setProductOptionValueId(productOptionValueId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// fields
	private int productOptionValueSortOrder;
	private long productOptionValueId;
	private String productOptionValueImage;
	private int merchantId;

	private Set descriptions;

	/**
	 * Return the value associated with the column:
	 * products_options_values_sort_order
	 */
	public int getProductOptionValueSortOrder() {
		return productOptionValueSortOrder;
	}

	/**
	 * Set the value related to the column: products_options_values_sort_order
	 * 
	 * @param productOptionValueSortOrder
	 *            the products_options_values_sort_order value
	 */
	public void setProductOptionValueSortOrder(int productOptionValueSortOrder) {
		this.productOptionValueSortOrder = productOptionValueSortOrder;
	}

	public String toString() {
		return super.toString();
	}

	public long getProductOptionValueId() {
		return productOptionValueId;
	}

	public void setProductOptionValueId(long productOptionValueId) {
		this.productOptionValueId = productOptionValueId;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ (int) (productOptionValueId ^ (productOptionValueId >>> 32));
		result = PRIME * result + productOptionValueSortOrder;
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
		final ProductOptionValue other = (ProductOptionValue) obj;
		if (productOptionValueId != other.productOptionValueId)
			return false;
		if (productOptionValueSortOrder != other.productOptionValueSortOrder)
			return false;
		return true;
	}

	public Set getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set descriptions) {
		this.descriptions = descriptions;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		ProductOptionValueDescription desc = null;
		if (this.getDescriptions() != null && this.getDescriptions().size() > 0) {
			ProductOptionValueDescription[] descArray = (ProductOptionValueDescription[]) this
					.getDescriptions().toArray(
							new ProductOptionValueDescription[this
									.getDescriptions().size()]);
			if (descArray != null && descArray.length > 0) {
				desc = descArray[0];
			}
			return desc.getProductOptionValueName();
		}
		return "";
	}

	public String getProductOptionValueImage() {
		return productOptionValueImage;
	}

	public void setProductOptionValueImage(String productOptionValueImage) {
		this.productOptionValueImage = productOptionValueImage;
	}

	public String getOptionValueImagePath() {
		/** in product image folder **/
		return FileUtil.getProductImagePath(this.getMerchantId(), this
				.getProductOptionValueImage());
	}

}