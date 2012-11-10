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

public class CountryDescriptionId implements Serializable {

	protected int hashCode = Integer.MIN_VALUE;

	private int countryId;
	private int languageId;

	public CountryDescriptionId() {
	}

	public CountryDescriptionId(int countryId, int languageId) {

		this.setCountryId(countryId);
		this.setLanguageId(languageId);
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
	 * Return the value associated with the column: language_id
	 */
	public int getLanguageId() {
		return languageId;
	}

	/**
	 * Set the value related to the column: language_id
	 * 
	 * @param languageId
	 *            the language_id value
	 */
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.CountryDescriptionId))
			return false;
		else {
			com.salesmanager.core.entity.reference.CountryDescriptionId mObj = (com.salesmanager.core.entity.reference.CountryDescriptionId) obj;
			if (this.getCountryId() != mObj.getCountryId()) {
				return false;
			}
			if (this.getLanguageId() != mObj.getLanguageId()) {
				return false;
			}
			return true;
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			StringBuilder sb = new StringBuilder();
			sb.append(new java.lang.Integer(this.getCountryId()).hashCode());
			sb.append(":");
			sb.append(new java.lang.Integer(this.getLanguageId()).hashCode());
			sb.append(":");
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}

}