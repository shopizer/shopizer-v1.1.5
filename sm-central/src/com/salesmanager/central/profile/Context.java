/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.profile;

import java.io.Serializable;
import java.util.Map;

public class Context implements Serializable {

	private Integer merchantid;
	private Integer promoCode = null;
	private int registrationCode;
	private int countryid;
	private int zoneid;
	private String username;
	private String lang;// corresponding language
	private String currency;
	private Map supportedlang;// store languages
	private String weightunit;
	private String sizeunit;
	private String masterRole;

	private String gcode;

	private boolean existingStore = true;

	public Integer getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(Integer merchantid) {
		this.merchantid = merchantid;
	}

	public Integer getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(Integer promoCode) {
		this.promoCode = promoCode;
	}

	public int getRegistrationCode() {
		return registrationCode;
	}

	public void setRegistrationCode(int registrationCode) {
		this.registrationCode = registrationCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Map getSupportedlang() {
		return supportedlang;
	}

	public void setSupportedlang(Map supportedlang) {
		this.supportedlang = supportedlang;
	}

	public int getCountryid() {
		return countryid;
	}

	public void setCountryid(int countryid) {
		this.countryid = countryid;
	}

	public int getZoneid() {
		return zoneid;
	}

	public void setZoneid(int zoneid) {
		this.zoneid = zoneid;
	}

	public String getSizeunit() {
		return sizeunit;
	}

	public void setSizeunit(String sizeunit) {
		this.sizeunit = sizeunit;
	}

	public String getWeightunit() {
		return weightunit;
	}

	public void setWeightunit(String weightunit) {
		this.weightunit = weightunit;
	}

	public boolean isExistingStore() {
		return existingStore;
	}

	public void setExistingStore(boolean existingStore) {
		this.existingStore = existingStore;
	}


	public String getMasterRole() {
		return masterRole;
	}

	public void setMasterRole(String masterRole) {
		this.masterRole = masterRole;
	}

	public String getGcode() {
		return gcode;
	}

	public void setGcode(String gcode) {
		this.gcode = gcode;
	}

}
