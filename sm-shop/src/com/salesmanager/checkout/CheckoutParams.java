/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.checkout;

import java.util.List;
import java.util.Map;

import com.salesmanager.checkout.web.Constants;

public class CheckoutParams {
	private int merchantId;
	private long productId;
	private List<Long> attributeId;
	private Map<Long, String> attributeValue;// attribute id with the
												// appropriate string value

	private String lang = Constants.DEFAULT_LANG;
	private int qty = 1;
	private String returnUrl;
	private int countryId = -1;
	private int zoneId = -1;

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	private int langId;

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public List<Long> getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(List<Long> attributeId) {
		this.attributeId = attributeId;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public Map<Long, String> getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(Map<Long, String> attributeValue) {
		this.attributeValue = attributeValue;
	}
}
