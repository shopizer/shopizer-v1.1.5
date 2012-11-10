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
package com.salesmanager.core.entity.tax;

import java.io.Serializable;
import java.util.Date;

/**
 * This is an object that contains data related to the tax_class table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="tax_class"
 */

public class TaxClass implements Serializable {

	public static String REF = "TaxClass";
	public static String PROP_LAST_MODIFIED = "lastModified";
	public static String PROP_MERCHANTID = "merchantid";
	public static String PROP_TAX_CLASS_DESCRIPTION = "taxClassDescription";
	public static String PROP_TAX_CLASS_ID = "taxClassId";
	public static String PROP_DATE_ADDED = "dateAdded";
	public static String PROP_TAX_CLASS_TITLE = "taxClassTitle";

	// constructors
	public TaxClass() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public TaxClass(long taxClassId) {
		this.setTaxClassId(taxClassId);
		initialize();
	}

	protected void initialize() {

		Date date = new Date();
		lastModified = new Date(date.getTime());
		dateAdded = new Date(date.getTime());

	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private long taxClassId;

	// fields
	private java.lang.String taxClassTitle;
	private java.lang.String taxClassDescription;
	private java.util.Date lastModified;
	private java.util.Date dateAdded;
	private int merchantId;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="tax_class_id"
	 */
	public long getTaxClassId() {
		return taxClassId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param taxClassId
	 *            the new ID
	 */
	public void setTaxClassId(long taxClassId) {
		this.taxClassId = taxClassId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: tax_class_title
	 */
	public java.lang.String getTaxClassTitle() {
		return taxClassTitle;
	}

	/**
	 * Set the value related to the column: tax_class_title
	 * 
	 * @param taxClassTitle
	 *            the tax_class_title value
	 */
	public void setTaxClassTitle(java.lang.String taxClassTitle) {
		this.taxClassTitle = taxClassTitle;
	}

	/**
	 * Return the value associated with the column: tax_class_description
	 */
	public java.lang.String getTaxClassDescription() {
		return taxClassDescription;
	}

	/**
	 * Set the value related to the column: tax_class_description
	 * 
	 * @param taxClassDescription
	 *            the tax_class_description value
	 */
	public void setTaxClassDescription(java.lang.String taxClassDescription) {
		this.taxClassDescription = taxClassDescription;
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
	 * Return the value associated with the column: merchantid
	 */
	public int getMerchantId() {
		return merchantId;
	}

	/**
	 * Set the value related to the column: merchantid
	 * 
	 * @param merchantid
	 *            the merchantid value
	 */
	public void setMerchantId(int merchantid) {
		this.merchantId = merchantid;
	}

	public String toString() {
		return super.toString();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result
				+ ((dateAdded == null) ? 0 : dateAdded.hashCode());
		result = PRIME * result + hashCode;
		result = PRIME * result
				+ ((lastModified == null) ? 0 : lastModified.hashCode());
		result = PRIME * result + merchantId;
		result = PRIME
				* result
				+ ((taxClassDescription == null) ? 0 : taxClassDescription
						.hashCode());
		result = PRIME * result + (int) (taxClassId ^ (taxClassId >>> 32));
		result = PRIME * result
				+ ((taxClassTitle == null) ? 0 : taxClassTitle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TaxClass other = (TaxClass) obj;
		if (dateAdded == null) {
			if (other.dateAdded != null)
				return false;
		} else if (!dateAdded.equals(other.dateAdded))
			return false;
		if (hashCode != other.hashCode)
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		if (merchantId != other.merchantId)
			return false;
		if (taxClassDescription == null) {
			if (other.taxClassDescription != null)
				return false;
		} else if (!taxClassDescription.equals(other.taxClassDescription))
			return false;
		if (taxClassId != other.taxClassId)
			return false;
		if (taxClassTitle == null) {
			if (other.taxClassTitle != null)
				return false;
		} else if (!taxClassTitle.equals(other.taxClassTitle))
			return false;
		return true;
	}

}