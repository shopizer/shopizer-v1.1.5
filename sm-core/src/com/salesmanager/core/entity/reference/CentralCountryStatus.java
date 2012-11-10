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
 * This is an object that contains data related to the central_countries_status
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="central_countries_status"
 */

public class CentralCountryStatus implements Serializable {

	public static String REF = "CentralCountryStatus";
	public static String PROP_CENTRAL_COUNTRY_STATUS_ID = "centralCountryStatusId";
	public static String PROP_CENTRAL_COUNTRY_CODE = "centralCountryCode";
	public static String PROP_CENTRAL_COUNTRY_STATUS_CODE = "centralCountryStatusCode";
	public static String PROP_COUNTRY_ID = "countryId";
	public static String PROP_DATE_ADDED = "dateAdded";

	// constructors
	public CentralCountryStatus() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public CentralCountryStatus(int centralCountryStatusId) {
		this.setCentralCountryStatusId(centralCountryStatusId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private int centralCountryStatusId;

	// fields
	private int countryId;
	private int centralCountryStatusCode;
	private java.lang.String centralCountryCode;
	private java.util.Date dateAdded;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned"
	 *               column="central_countries_status_id"
	 */
	public int getCentralCountryStatusId() {
		return centralCountryStatusId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param centralCountryStatusId
	 *            the new ID
	 */
	public void setCentralCountryStatusId(int centralCountryStatusId) {
		this.centralCountryStatusId = centralCountryStatusId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: countries_id
	 */
	public int getCountryId() {
		return countryId;
	}

	/**
	 * Set the value related to the column: countries_id
	 * 
	 * @param countryId
	 *            the countries_id value
	 */
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	/**
	 * Return the value associated with the column:
	 * central_countries_status_code
	 */
	public int getCentralCountryStatusCode() {
		return centralCountryStatusCode;
	}

	/**
	 * Set the value related to the column: central_countries_status_code
	 * 
	 * @param centralCountryStatusCode
	 *            the central_countries_status_code value
	 */
	public void setCentralCountryStatusCode(int centralCountryStatusCode) {
		this.centralCountryStatusCode = centralCountryStatusCode;
	}

	/**
	 * Return the value associated with the column: central_countries_code
	 */
	public java.lang.String getCentralCountryCode() {
		return centralCountryCode;
	}

	/**
	 * Set the value related to the column: central_countries_code
	 * 
	 * @param centralCountryCode
	 *            the central_countries_code value
	 */
	public void setCentralCountryCode(java.lang.String centralCountryCode) {
		this.centralCountryCode = centralCountryCode;
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

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.CentralCountryStatus))
			return false;
		else {
			com.salesmanager.core.entity.reference.CentralCountryStatus centralCountryStatus = (com.salesmanager.core.entity.reference.CentralCountryStatus) obj;
			return (this.getCentralCountryStatusId() == centralCountryStatus
					.getCentralCountryStatusId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getCentralCountryStatusId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}