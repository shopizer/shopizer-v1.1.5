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
 * This is an object that contains data related to the countries_description
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="countries_description"
 */

public class CountryDescription implements Serializable {

	public static String REF = "CountryDescription";
	public static String PROP_COUNTRY_NAME = "countryName";
	public static String PROP_ID = "id";

	// constructors
	public CountryDescription() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public CountryDescription(
			com.salesmanager.core.entity.reference.CountryDescriptionId id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private com.salesmanager.core.entity.reference.CountryDescriptionId id;

	// fields
	private java.lang.String countryName;

	private Country country;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id
	 */
	public com.salesmanager.core.entity.reference.CountryDescriptionId getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(
			com.salesmanager.core.entity.reference.CountryDescriptionId id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: countries_name
	 */
	public java.lang.String getCountryName() {
		return countryName;
	}

	/**
	 * Set the value related to the column: countries_name
	 * 
	 * @param countryName
	 *            the countries_name value
	 */
	public void setCountryName(java.lang.String countryName) {
		this.countryName = countryName;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.CountryDescription))
			return false;
		else {
			com.salesmanager.core.entity.reference.CountryDescription countryDescription = (com.salesmanager.core.entity.reference.CountryDescription) obj;
			if (null == this.getId() || null == countryDescription.getId())
				return false;
			else
				return (this.getId().equals(countryDescription.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":"
						+ this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}