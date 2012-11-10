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
import java.util.Set;

import com.salesmanager.core.entity.reference.ZoneToGeoZone;

/**
 * This is an object that contains data related to the tax_rates table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="tax_rates"
 */

public class TaxRate implements Serializable {

	public static String REF = "TaxRate";
	public static String PROP_LAST_MODIFIED = "lastModified";
	public static String PROP_TAX_PRIORITY = "taxPriority";
	public static String PROP_TAX_RATE = "taxRate";
	public static String PROP_TAX_CLASS_ID = "taxClassId";
	public static String PROP_TAX_ZONE_ID = "taxZoneId";
	public static String PROP_TAX_RATE_ID = "taxRateId";
	public static String PROP_DATE_ADDED = "dateAdded";

	// constructors
	public TaxRate() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public TaxRate(long taxRateId) {
		this.setTaxRateId(taxRateId);
		initialize();
	}

	protected void initialize() {

		Date date = new Date();
		lastModified = new Date(date.getTime());
		dateAdded = new Date(date.getTime());
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private long taxRateId;

	// fields
	private long taxZoneId;
	private long taxClassId;
	private java.lang.Integer taxPriority;
	private java.math.BigDecimal taxRate;
	private java.util.Date lastModified;
	private java.util.Date dateAdded;
	private int merchantId;

	private Set descriptions;

	private ZoneToGeoZone zoneToGeoZone;

	private boolean piggyback;

	public ZoneToGeoZone getZoneToGeoZone() {
		return zoneToGeoZone;
	}

	public void setZoneToGeoZone(ZoneToGeoZone zoneToGeoZone) {
		this.zoneToGeoZone = zoneToGeoZone;
	}

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="tax_rates_id"
	 */
	public long getTaxRateId() {
		return taxRateId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param taxRateId
	 *            the new ID
	 */
	public void setTaxRateId(long taxRateId) {
		this.taxRateId = taxRateId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: tax_zone_id
	 */
	public long getTaxZoneId() {
		return taxZoneId;
	}

	/**
	 * Set the value related to the column: tax_zone_id
	 * 
	 * @param taxZoneId
	 *            the tax_zone_id value
	 */
	public void setTaxZoneId(long taxZoneId) {
		this.taxZoneId = taxZoneId;
	}

	/**
	 * Return the value associated with the column: tax_class_id
	 */
	public long getTaxClassId() {
		return taxClassId;
	}

	/**
	 * Set the value related to the column: tax_class_id
	 * 
	 * @param taxClassId
	 *            the tax_class_id value
	 */
	public void setTaxClassId(long taxClassId) {
		this.taxClassId = taxClassId;
	}

	/**
	 * Return the value associated with the column: tax_priority
	 */
	public java.lang.Integer getTaxPriority() {
		return taxPriority;
	}

	/**
	 * Set the value related to the column: tax_priority
	 * 
	 * @param taxPriority
	 *            the tax_priority value
	 */
	public void setTaxPriority(java.lang.Integer taxPriority) {
		this.taxPriority = taxPriority;
	}

	/**
	 * Return the value associated with the column: tax_rate
	 */
	public java.math.BigDecimal getTaxRate() {
		return taxRate;
	}

	/**
	 * Set the value related to the column: tax_rate
	 * 
	 * @param taxRate
	 *            the tax_rate value
	 */
	public void setTaxRate(java.math.BigDecimal taxRate) {
		this.taxRate = taxRate;
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

	public String toString() {
		return super.toString();
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
		result = PRIME * result
				+ ((dateAdded == null) ? 0 : dateAdded.hashCode());
		result = PRIME * result + hashCode;
		result = PRIME * result
				+ ((lastModified == null) ? 0 : lastModified.hashCode());
		result = PRIME * result + merchantId;
		result = PRIME * result + (int) (taxClassId ^ (taxClassId >>> 32));
		result = PRIME * result
				+ ((taxPriority == null) ? 0 : taxPriority.hashCode());
		result = PRIME * result + ((taxRate == null) ? 0 : taxRate.hashCode());
		result = PRIME * result + (int) (taxRateId ^ (taxRateId >>> 32));
		result = PRIME * result + (int) (taxZoneId ^ (taxZoneId >>> 32));
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
		final TaxRate other = (TaxRate) obj;
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
		if (taxClassId != other.taxClassId)
			return false;
		if (taxPriority == null) {
			if (other.taxPriority != null)
				return false;
		} else if (!taxPriority.equals(other.taxPriority))
			return false;
		if (taxRate == null) {
			if (other.taxRate != null)
				return false;
		} else if (!taxRate.equals(other.taxRate))
			return false;
		if (taxRateId != other.taxRateId)
			return false;
		if (taxZoneId != other.taxZoneId)
			return false;
		return true;
	}

	public Set getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set descriptions) {
		this.descriptions = descriptions;
	}

	public boolean isPiggyback() {
		return piggyback;
	}

	public void setPiggyback(boolean piggyback) {
		this.piggyback = piggyback;
	}

}