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
 * This is an object that contains data related to the currencies table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="currencies"
 */

public class Currency implements Serializable {

	public static String REF = "Currency";
	public static String PROP_VALUE = "value";
	public static String PROP_DECIMAL_POINT = "decimalPoint";
	public static String PROP_CURRENCY_ID = "currencyId";
	public static String PROP_LAST_UPDATED = "lastUpdated";
	public static String PROP_DECIMAL_PLACES = "decimalPlaces";
	public static String PROP_CODE = "code";
	public static String PROP_SUPPORTED = "supported";
	public static String PROP_THOUSANDS_POINT = "thousandsPoint";
	public static String PROP_TITLE = "title";

	// constructors
	public Currency() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Currency(int currencyId) {
		this.setCurrencyId(currencyId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private int currencyId;

	// fields
	private java.lang.String title;
	private java.lang.String code;
	private java.lang.Character decimalPoint;
	private java.lang.Character thousandsPoint;
	private java.lang.Character decimalPlaces;
	private java.lang.Float value;
	private java.util.Date lastUpdated;
	private boolean supported;
	private String suffix;
	private String symbol;

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="currencies_id"
	 */
	public int getCurrencyId() {
		return currencyId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param currencyId
	 *            the new ID
	 */
	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: title
	 */
	public java.lang.String getTitle() {
		return title;
	}

	/**
	 * Set the value related to the column: title
	 * 
	 * @param title
	 *            the title value
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	/**
	 * Return the value associated with the column: code
	 */
	public java.lang.String getCode() {
		return code;
	}

	/**
	 * Set the value related to the column: code
	 * 
	 * @param code
	 *            the code value
	 */
	public void setCode(java.lang.String code) {
		this.code = code;
	}

	/**
	 * Return the value associated with the column: decimal_point
	 */
	public java.lang.Character getDecimalPoint() {
		return decimalPoint;
	}

	/**
	 * Set the value related to the column: decimal_point
	 * 
	 * @param decimalPoint
	 *            the decimal_point value
	 */
	public void setDecimalPoint(java.lang.Character decimalPoint) {
		this.decimalPoint = decimalPoint;
	}

	/**
	 * Return the value associated with the column: thousands_point
	 */
	public java.lang.Character getThousandsPoint() {
		return thousandsPoint;
	}

	/**
	 * Set the value related to the column: thousands_point
	 * 
	 * @param thousandsPoint
	 *            the thousands_point value
	 */
	public void setThousandsPoint(java.lang.Character thousandsPoint) {
		this.thousandsPoint = thousandsPoint;
	}

	/**
	 * Return the value associated with the column: decimal_places
	 */
	public java.lang.Character getDecimalPlaces() {
		return decimalPlaces;
	}

	/**
	 * Set the value related to the column: decimal_places
	 * 
	 * @param decimalPlaces
	 *            the decimal_places value
	 */
	public void setDecimalPlaces(java.lang.Character decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	/**
	 * Return the value associated with the column: value
	 */
	public java.lang.Float getValue() {
		return value;
	}

	/**
	 * Set the value related to the column: value
	 * 
	 * @param value
	 *            the value value
	 */
	public void setValue(java.lang.Float value) {
		this.value = value;
	}

	/**
	 * Return the value associated with the column: last_updated
	 */
	public java.util.Date getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Set the value related to the column: last_updated
	 * 
	 * @param lastUpdated
	 *            the last_updated value
	 */
	public void setLastUpdated(java.util.Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * Return the value associated with the column: supported
	 */
	public boolean isSupported() {
		return supported;
	}

	/**
	 * Set the value related to the column: supported
	 * 
	 * @param supported
	 *            the supported value
	 */
	public void setSupported(boolean supported) {
		this.supported = supported;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.Currency))
			return false;
		else {
			com.salesmanager.core.entity.reference.Currency currency = (com.salesmanager.core.entity.reference.Currency) obj;
			return (this.getCurrencyId() == currency.getCurrencyId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getCurrencyId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}