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
 * This is an object that contains data related to the zones_description table.
 * Do not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="zones_description"
 */

public class ZoneDescription implements Serializable {

	public static String REF = "ZoneDescription";
	public static String PROP_ZONE_NAME = "zoneName";
	public static String PROP_ID = "id";

	// constructors
	public ZoneDescription() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public ZoneDescription(
			com.salesmanager.core.entity.reference.ZoneDescriptionId id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private com.salesmanager.core.entity.reference.ZoneDescriptionId id;

	// fields
	private java.lang.String zoneName;

	private Zone zone;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id
	 */
	public com.salesmanager.core.entity.reference.ZoneDescriptionId getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(
			com.salesmanager.core.entity.reference.ZoneDescriptionId id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: zone_name
	 */
	public java.lang.String getZoneName() {
		return zoneName;
	}

	/**
	 * Set the value related to the column: zone_name
	 * 
	 * @param zoneName
	 *            the zone_name value
	 */
	public void setZoneName(java.lang.String zoneName) {
		this.zoneName = zoneName;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.ZoneDescription))
			return false;
		else {
			com.salesmanager.core.entity.reference.ZoneDescription zoneDescription = (com.salesmanager.core.entity.reference.ZoneDescription) obj;
			if (null == this.getId() || null == zoneDescription.getId())
				return false;
			else
				return (this.getId().equals(zoneDescription.getId()));
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

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

}