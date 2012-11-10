/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-3 Sep, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.entity.customer.ws;



/**
 * Customer entity used in the web service to create a Customer
 * requires a valid customerZoneId
 * requires a valid customerCountryId
 * localeStr is 'en', 'fr' ... 2 char language char
 * @author Carl Samson
 *
 */
public class Customer {


	private java.lang.String customerFirstname;
	private java.lang.String customerLastname;
	private java.lang.String customerEmailAddress;
	private java.lang.String customerTelephone;
	private java.lang.String customerLang;

	private String customerStreetAddress;
	private String customerPostalCode;
	private String customerCity;
	private int customerZoneId = 0;//0 means the zone is undefined, then will require zoneName
	private int customerCountryId;
	private String zoneName;//if customerZoneId > 0, it will ignore this field
	

	private long customerId;//leave it to 0 for creation or set it to customerId for edition

	

	public java.lang.String getCustomerFirstname() {
		return customerFirstname;
	}
	public void setCustomerFirstname(java.lang.String customerFirstname) {
		this.customerFirstname = customerFirstname;
	}
	public java.lang.String getCustomerLastname() {
		return customerLastname;
	}
	public void setCustomerLastname(java.lang.String customerLastname) {
		this.customerLastname = customerLastname;
	}
	public java.lang.String getCustomerEmailAddress() {
		return customerEmailAddress;
	}
	public void setCustomerEmailAddress(java.lang.String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}
	public java.lang.String getCustomerTelephone() {
		return customerTelephone;
	}
	public void setCustomerTelephone(java.lang.String customerTelephone) {
		this.customerTelephone = customerTelephone;
	}
	public java.lang.String getCustomerLang() {
		return customerLang;
	}
	public void setCustomerLang(java.lang.String customerLang) {
		this.customerLang = customerLang;
	}
	public String getCustomerStreetAddress() {
		return customerStreetAddress;
	}
	public void setCustomerStreetAddress(String customerStreetAddress) {
		this.customerStreetAddress = customerStreetAddress;
	}
	public String getCustomerPostalCode() {
		return customerPostalCode;
	}
	public void setCustomerPostalCode(String customerPostalCode) {
		this.customerPostalCode = customerPostalCode;
	}
	public String getCustomerCity() {
		return customerCity;
	}
	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}
	public int getCustomerZoneId() {
		return customerZoneId;
	}
	public void setCustomerZoneId(int customerZoneId) {
		this.customerZoneId = customerZoneId;
	}
	public int getCustomerCountryId() {
		return customerCountryId;
	}
	public void setCustomerCountryId(int customerCountryId) {
		this.customerCountryId = customerCountryId;
	}

	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	
	
}
