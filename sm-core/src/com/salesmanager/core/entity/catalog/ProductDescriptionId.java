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

public class ProductDescriptionId implements Serializable {

	protected int hashCode = Integer.MIN_VALUE;

	private long productId;
	private int languageId;

	public ProductDescriptionId() {
	}

	public ProductDescriptionId(long productId, int languageId) {

		this.setProductId(productId);
		this.setLanguageId(languageId);
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
	 * Return the value associated with the column: language_id
	 */
	public int getLanguageId() {
		return languageId;
	}

	/**
	 * Set the value related to the column: language_id
	 * 
	 * @param languageId
	 *            the language_id value
	 */
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.catalog.ProductDescriptionId))
			return false;
		else {
			com.salesmanager.core.entity.catalog.ProductDescriptionId mObj = (com.salesmanager.core.entity.catalog.ProductDescriptionId) obj;
			if (this.getProductId() != mObj.getProductId()) {
				return false;
			}
			if (this.getLanguageId() != mObj.getLanguageId()) {
				return false;
			}
			return true;
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			StringBuilder sb = new StringBuilder();
			sb.append(new java.lang.Long(this.getProductId()).hashCode());
			sb.append(":");
			sb.append(new java.lang.Integer(this.getLanguageId()).hashCode());
			sb.append(":");
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}

}