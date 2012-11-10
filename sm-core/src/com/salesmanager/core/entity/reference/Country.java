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
import java.util.Set;

/**
 * This is an object that contains data related to the countries table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="countries"
 */

public class Country implements Serializable {

	public static String REF = "Country";
	public static String PROP_COUNTRY_NAME_FR = "countryNameFr";
	public static String PROP_COUNTRY_NAME = "countryName";
	public static String PROP_COUNTRY_ISO_CODE3 = "countryIsoCode3";
	public static String PROP_COUNTRY_ID = "countryId";
	public static String PROP_SUPPORTED = "supported";
	public static String PROP_ADDRESS_FORMAT_ID = "addressFormatId";
	public static String PROP_COUNTRY_ISO_CODE2 = "countryIsoCode2";

	// constructors
	public Country() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Country(int countryId) {
		this.setCountryId(countryId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private int countryId;

	// fields
	private java.lang.String countryName;
	private java.lang.String countryIsoCode2;
	private java.lang.String countryIsoCode3;
	private java.lang.String countryGroupCode;
	private int addressFormatId;
	private boolean supported;
	// private java.lang.String countryNameFr;
	private Set<CountryDescription> descriptions;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="countries_id"
	 */
	public int getCountryId() {
		return countryId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param countryId
	 *            the new ID
	 */
	public void setCountryId(int countryId) {
		this.countryId = countryId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: countries_name
	 */
	public java.lang.String getCountryName() {

		return countryName;
	}

	/**
	 * Return the value associated with the column: countries_iso_code_2
	 */
	public java.lang.String getCountryIsoCode2() {
		return countryIsoCode2;
	}

	/**
	 * Set the value related to the column: countries_iso_code_2
	 * 
	 * @param countryIsoCode2
	 *            the countries_iso_code_2 value
	 */
	public void setCountryIsoCode2(java.lang.String countryIsoCode2) {
		this.countryIsoCode2 = countryIsoCode2;
	}

	/**
	 * Return the value associated with the column: countries_iso_code_3
	 */
	public java.lang.String getCountryIsoCode3() {
		return countryIsoCode3;
	}

	/**
	 * Set the value related to the column: countries_iso_code_3
	 * 
	 * @param countryIsoCode3
	 *            the countries_iso_code_3 value
	 */
	public void setCountryIsoCode3(java.lang.String countryIsoCode3) {
		this.countryIsoCode3 = countryIsoCode3;
	}

	/**
	 * Return the value associated with the column: address_format_id
	 */
	public int getAddressFormatId() {
		return addressFormatId;
	}

	/**
	 * Set the value related to the column: address_format_id
	 * 
	 * @param addressFormatId
	 *            the address_format_id value
	 */
	public void setAddressFormatId(int addressFormatId) {
		this.addressFormatId = addressFormatId;
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
		if (!(obj instanceof com.salesmanager.core.entity.reference.Country))
			return false;
		else {
			com.salesmanager.core.entity.reference.Country country = (com.salesmanager.core.entity.reference.Country) obj;
			return (this.getCountryId() == country.getCountryId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getCountryId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public java.lang.String getCountryGroupCode() {
		return countryGroupCode;
	}

	public void setCountryGroupCode(java.lang.String countryGroupCode) {
		this.countryGroupCode = countryGroupCode;
	}

	public Set<CountryDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<CountryDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public void setCountryName(java.lang.String countryName) {
		this.countryName = countryName;
	}

}