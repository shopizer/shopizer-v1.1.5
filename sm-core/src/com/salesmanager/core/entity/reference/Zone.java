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

import javax.servlet.http.HttpServletRequest;

/**
 * This is an object that contains data related to the zones table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="zones"
 */

public class Zone implements Serializable {

	public static String REF = "Zone";
	public static String PROP_ZONE_ID = "zoneId";
	public static String PROP_ZONE_NAME_FR = "zoneNameFr";
	public static String PROP_ZONE_CODE = "zoneCode";
	public static String PROP_ZONE_NAME = "zoneName";
	public static String PROP_ZONE_COUNTRY_ID = "zoneCountryId";

	// constructors
	public Zone() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Zone(int zoneId) {
		this.setZoneId(zoneId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private int zoneId;

	// fields
	private int zoneCountryId;
	private java.lang.String zoneCode;
	private java.lang.String zoneName;
	// private java.lang.String zoneNameFr;

	private Set<ZoneDescription> descriptions;

	private HttpServletRequest request;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="zone_id"
	 */
	public int getZoneId() {
		return zoneId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param zoneId
	 *            the new ID
	 */
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: zone_country_id
	 */
	public int getZoneCountryId() {
		return zoneCountryId;
	}

	/**
	 * Set the value related to the column: zone_country_id
	 * 
	 * @param zoneCountryId
	 *            the zone_country_id value
	 */
	public void setZoneCountryId(int zoneCountryId) {
		this.zoneCountryId = zoneCountryId;
	}

	/**
	 * Return the value associated with the column: zone_code
	 */
	public java.lang.String getZoneCode() {
		return zoneCode;
	}

	/**
	 * Set the value related to the column: zone_code
	 * 
	 * @param zoneCode
	 *            the zone_code value
	 */
	public void setZoneCode(java.lang.String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public void setZoneName(String name) {
		this.zoneName = name;
	}

	/**
	 * Return the value associated with the column: zone_name
	 */
	public String getZoneName() {
		/**
		 * if(this.getRequest()!=null) { Locale loc =
		 * this.getRequest().getLocale(); if(loc.getLanguage().equals("fr")) {
		 * return this.getZoneNameFr(); } else { return zoneName; } } else {
		 * return zoneName; }
		 **/
		return zoneName;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.Zone))
			return false;
		else {
			com.salesmanager.core.entity.reference.Zone zone = (com.salesmanager.core.entity.reference.Zone) obj;
			return (this.getZoneId() == zone.getZoneId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getZoneId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Set<ZoneDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<ZoneDescription> descriptions) {
		this.descriptions = descriptions;
	}

}