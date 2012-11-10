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
package com.salesmanager.core.service.shipping;

public class ShippingAddressVO {

	private String name;
	private String street;
	private String city;
	private String postalcode;
	private String stateprovince;
	private String country;
	private float quality = -1;

	private boolean validated = false;

	public ShippingAddressVO(String name, String street, String city,
			String postalcode, String stateprovince, String country) {
		super();
		this.name = name;
		this.street = street;
		this.city = city;
		this.postalcode = postalcode;
		this.stateprovince = stateprovince;
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public String getStreet() {
		return street;
	}

	public boolean isValidated() {
		return validated;
	}

	public String getStateprovince() {
		return stateprovince;
	}

	public float getQuality() {
		return quality;
	}

	public void setQuality(float quality) {
		this.quality = quality;
	}

}
