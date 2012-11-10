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
 * This is an object that contains data related to the
 * central_integration_services table. Do not modify this class because it will
 * be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="central_integration_services"
 */

public class CentralIntegrationService implements Serializable {

	// constructors
	public CentralIntegrationService() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public CentralIntegrationService(int centralIntegrationServiceId) {
		this.setCentralIntegrationServiceId(centralIntegrationServiceId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private int centralIntegrationServiceId;

	// fields
	private int centralIntegrationServiceCode;
	private java.lang.String centralIntegrationModule;
	// private int countryId;
	private int centralIntegrationServiceSubtype;
	private java.lang.String centralIntegrationServiceDescription;
	// private java.lang.String centralIntegrationServiceDescriptionFr;
	private java.lang.String centralIntegrationServiceLogoPath;
	private byte centralIntegrationServicePosition;
	private boolean centralIntegrationServiceVisible;
	private boolean centralIntegrationServiceNew;
	private java.lang.String centralIntegrationServiceUrl;
	private java.lang.String centralIntegrationServiceDevProtocol;
	private java.lang.String centralIntegrationServiceDevEnv;
	private java.lang.String centralIntegrationServiceDevPort;
	private java.lang.String centralIntegrationServiceDevDomain;
	private java.lang.String centralIntegrationServiceProdEnv;
	private java.lang.String centralIntegrationServiceProdPort;
	private java.lang.String centralIntegrationServiceProdProtocol;
	private java.lang.String centralIntegrationServiceProdDomain;
	// private java.util.Date dateAdded;
	private String countryIsoCode2;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned"
	 *               column="central_integration_services_id"
	 */
	public int getCentralIntegrationServiceId() {
		return centralIntegrationServiceId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param centralIntegrationServiceId
	 *            the new ID
	 */
	public void setCentralIntegrationServiceId(int centralIntegrationServiceId) {
		this.centralIntegrationServiceId = centralIntegrationServiceId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_code
	 */
	public int getCentralIntegrationServiceCode() {
		return centralIntegrationServiceCode;
	}

	/**
	 * Set the value related to the column: central_integration_services_code
	 * 
	 * @param centralIntegrationServiceCode
	 *            the central_integration_services_code value
	 */
	public void setCentralIntegrationServiceCode(
			int centralIntegrationServiceCode) {
		this.centralIntegrationServiceCode = centralIntegrationServiceCode;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_name
	 */
	public java.lang.String getCentralIntegrationModule() {
		return centralIntegrationModule;
	}

	/**
	 * Set the value related to the column: central_integration_services_name
	 * 
	 * @param centralIntegrationServiceName
	 *            the central_integration_services_name value
	 */
	public void setCentralIntegrationModule(
			java.lang.String centralIntegrationModule) {
		this.centralIntegrationModule = centralIntegrationModule;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_subtype
	 */
	public int getCentralIntegrationServiceSubtype() {
		return centralIntegrationServiceSubtype;
	}

	/**
	 * Set the value related to the column: central_integration_services_subtype
	 * 
	 * @param centralIntegrationServiceSubtype
	 *            the central_integration_services_subtype value
	 */
	public void setCentralIntegrationServiceSubtype(
			int centralIntegrationServiceSubtype) {
		this.centralIntegrationServiceSubtype = centralIntegrationServiceSubtype;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_logo_path
	 */
	public java.lang.String getCentralIntegrationServiceLogoPath() {
		return centralIntegrationServiceLogoPath;
	}

	/**
	 * Set the value related to the column:
	 * central_integration_services_logo_path
	 * 
	 * @param centralIntegrationServiceLogoPath
	 *            the central_integration_services_logo_path value
	 */
	public void setCentralIntegrationServiceLogoPath(
			java.lang.String centralIntegrationServiceLogoPath) {
		this.centralIntegrationServiceLogoPath = centralIntegrationServiceLogoPath;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_position
	 */
	public byte getCentralIntegrationServicePosition() {
		return centralIntegrationServicePosition;
	}

	/**
	 * Set the value related to the column:
	 * central_integration_services_position
	 * 
	 * @param centralIntegrationServicePosition
	 *            the central_integration_services_position value
	 */
	public void setCentralIntegrationServicePosition(
			byte centralIntegrationServicePosition) {
		this.centralIntegrationServicePosition = centralIntegrationServicePosition;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_visible
	 */
	public boolean isCentralIntegrationServiceVisible() {
		return centralIntegrationServiceVisible;
	}

	/**
	 * Set the value related to the column: central_integration_services_visible
	 * 
	 * @param centralIntegrationServiceVisible
	 *            the central_integration_services_visible value
	 */
	public void setCentralIntegrationServiceVisible(
			boolean centralIntegrationServiceVisible) {
		this.centralIntegrationServiceVisible = centralIntegrationServiceVisible;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_new
	 */
	public boolean isCentralIntegrationServiceNew() {
		return centralIntegrationServiceNew;
	}

	/**
	 * Set the value related to the column: central_integration_services_new
	 * 
	 * @param centralIntegrationServiceNew
	 *            the central_integration_services_new value
	 */
	public void setCentralIntegrationServiceNew(
			boolean centralIntegrationServiceNew) {
		this.centralIntegrationServiceNew = centralIntegrationServiceNew;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_url
	 */
	public java.lang.String getCentralIntegrationServiceUrl() {
		return centralIntegrationServiceUrl;
	}

	/**
	 * Set the value related to the column: central_integration_services_url
	 * 
	 * @param centralIntegrationServiceUrl
	 *            the central_integration_services_url value
	 */
	public void setCentralIntegrationServiceUrl(
			java.lang.String centralIntegrationServiceUrl) {
		this.centralIntegrationServiceUrl = centralIntegrationServiceUrl;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_dev_protocol
	 */
	public java.lang.String getCentralIntegrationServiceDevProtocol() {
		return centralIntegrationServiceDevProtocol;
	}

	/**
	 * Set the value related to the column:
	 * central_integration_services_dev_protocol
	 * 
	 * @param centralIntegrationServiceDevProtocol
	 *            the central_integration_services_dev_protocol value
	 */
	public void setCentralIntegrationServiceDevProtocol(
			java.lang.String centralIntegrationServiceDevProtocol) {
		this.centralIntegrationServiceDevProtocol = centralIntegrationServiceDevProtocol;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_dev_env
	 */
	public java.lang.String getCentralIntegrationServiceDevEnv() {
		return centralIntegrationServiceDevEnv;
	}

	/**
	 * Set the value related to the column: central_integration_services_dev_env
	 * 
	 * @param centralIntegrationServiceDevEnv
	 *            the central_integration_services_dev_env value
	 */
	public void setCentralIntegrationServiceDevEnv(
			java.lang.String centralIntegrationServiceDevEnv) {
		this.centralIntegrationServiceDevEnv = centralIntegrationServiceDevEnv;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_dev_port
	 */
	public java.lang.String getCentralIntegrationServiceDevPort() {
		return centralIntegrationServiceDevPort;
	}

	/**
	 * Set the value related to the column:
	 * central_integration_services_dev_port
	 * 
	 * @param centralIntegrationServiceDevPort
	 *            the central_integration_services_dev_port value
	 */
	public void setCentralIntegrationServiceDevPort(
			java.lang.String centralIntegrationServiceDevPort) {
		this.centralIntegrationServiceDevPort = centralIntegrationServiceDevPort;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_dev_domain
	 */
	public java.lang.String getCentralIntegrationServiceDevDomain() {
		return centralIntegrationServiceDevDomain;
	}

	/**
	 * Set the value related to the column:
	 * central_integration_services_dev_domain
	 * 
	 * @param centralIntegrationServiceDevDomain
	 *            the central_integration_services_dev_domain value
	 */
	public void setCentralIntegrationServiceDevDomain(
			java.lang.String centralIntegrationServiceDevDomain) {
		this.centralIntegrationServiceDevDomain = centralIntegrationServiceDevDomain;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_prod_env
	 */
	public java.lang.String getCentralIntegrationServiceProdEnv() {
		return centralIntegrationServiceProdEnv;
	}

	/**
	 * Set the value related to the column:
	 * central_integration_services_prod_env
	 * 
	 * @param centralIntegrationServiceProdEnv
	 *            the central_integration_services_prod_env value
	 */
	public void setCentralIntegrationServiceProdEnv(
			java.lang.String centralIntegrationServiceProdEnv) {
		this.centralIntegrationServiceProdEnv = centralIntegrationServiceProdEnv;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_prod_port
	 */
	public java.lang.String getCentralIntegrationServiceProdPort() {
		return centralIntegrationServiceProdPort;
	}

	/**
	 * Set the value related to the column:
	 * central_integration_services_prod_port
	 * 
	 * @param centralIntegrationServiceProdPort
	 *            the central_integration_services_prod_port value
	 */
	public void setCentralIntegrationServiceProdPort(
			java.lang.String centralIntegrationServiceProdPort) {
		this.centralIntegrationServiceProdPort = centralIntegrationServiceProdPort;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_prod_protocol
	 */
	public java.lang.String getCentralIntegrationServiceProdProtocol() {
		return centralIntegrationServiceProdProtocol;
	}

	/**
	 * Set the value related to the column:
	 * central_integration_services_prod_protocol
	 * 
	 * @param centralIntegrationServiceProdProtocol
	 *            the central_integration_services_prod_protocol value
	 */
	public void setCentralIntegrationServiceProdProtocol(
			java.lang.String centralIntegrationServiceProdProtocol) {
		this.centralIntegrationServiceProdProtocol = centralIntegrationServiceProdProtocol;
	}

	/**
	 * Return the value associated with the column:
	 * central_integration_services_prod_domain
	 */
	public java.lang.String getCentralIntegrationServiceProdDomain() {
		return centralIntegrationServiceProdDomain;
	}

	/**
	 * Set the value related to the column:
	 * central_integration_services_prod_domain
	 * 
	 * @param centralIntegrationServiceProdDomain
	 *            the central_integration_services_prod_domain value
	 */
	public void setCentralIntegrationServiceProdDomain(
			java.lang.String centralIntegrationServiceProdDomain) {
		this.centralIntegrationServiceProdDomain = centralIntegrationServiceProdDomain;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.CentralIntegrationService))
			return false;
		else {
			com.salesmanager.core.entity.reference.CentralIntegrationService centralIntegrationService = (com.salesmanager.core.entity.reference.CentralIntegrationService) obj;
			return (this.getCentralIntegrationServiceId() == centralIntegrationService
					.getCentralIntegrationServiceId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getCentralIntegrationServiceId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public String getCountryIsoCode2() {
		return countryIsoCode2;
	}

	public void setCountryIsoCode2(String countryIsoCode2) {
		this.countryIsoCode2 = countryIsoCode2;
	}

	public java.lang.String getCentralIntegrationServiceDescription() {
		return centralIntegrationServiceDescription;
	}

	public void setCentralIntegrationServiceDescription(
			java.lang.String centralIntegrationServiceDescription) {
		this.centralIntegrationServiceDescription = centralIntegrationServiceDescription;
	}

}