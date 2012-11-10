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
package com.salesmanager.core.entity.shipping;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;

/**
 * This object is used in the catalogue
 * 
 * @author Carl Samson
 * 
 */
public class ShippingEstimate {

	private Map<Integer, ShippingPriceRegion> regions;// index, all regions
														// configured
	private String shippingModule;// shipping company
	private ShippingType shippingType;// national - international

	private String defaultShippingEstimateText = "";
	private String customerCountry;
	private String storeCountry;

	private Locale locale;
	private String currency;

	private String shippingCompanyLogo = null;

	public String getShippingCompanyLogo() {
		return shippingCompanyLogo;
	}

	public void setShippingCompanyLogo(String shippingCompanyLogo) {
		this.shippingCompanyLogo = shippingCompanyLogo;
	}

	public String getCustomerCountry() {
		return customerCountry;
	}

	public void setCustomerCountry(String customerCountry) {
		this.customerCountry = customerCountry;
	}

	public String getDefaultShippingEstimateText() {
		return defaultShippingEstimateText;
	}

	public void setDefaultShippingEstimateText(
			String defaultShippingEstimateText) {
		this.defaultShippingEstimateText = defaultShippingEstimateText;
	}

	private int customerZoneIndex = -1;

	public int getCustomerZoneIndex() {
		return customerZoneIndex;
	}

	public void setCustomerZoneIndex(int customerZoneIndex) {
		this.customerZoneIndex = customerZoneIndex;
	}

	public String getStoreCountry() {
		return storeCountry;
	}

	public void setStoreCountry(String storeCountry) {
		this.storeCountry = storeCountry;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public ShippingType getShippingType() {
		return shippingType;
	}

	public void setShippingType(ShippingType shippingType) {
		this.shippingType = shippingType;
	}

	public Map<Integer, ShippingPriceRegion> getRegions() {
		return regions;
	}

	public void setRegions(Map<Integer, ShippingPriceRegion> regions) {
		this.regions = regions;
	}

	public String getShippingModule() {
		return shippingModule;
	}

	public void setShippingModule(String shippingModule) {
		this.shippingModule = shippingModule;
	}

	public String getShippingEstimateDescription() {

		if (regions != null) {

			ShippingPriceRegion spr = regions.get(customerZoneIndex);

			if (spr != null) {

				if (spr.getMinDays() == -1 && spr.getMaxDays() == -1) {
					return this.getDefaultShippingEstimateText();
				}

				LabelUtil label = LabelUtil.getInstance();
				String returnText = "";

				if (locale == null) {
					locale = LocaleUtil.getDefaultLocale();
				}
				label.setLocale(locale);

				if (spr.getMinDays() == spr.getMaxDays()) {
					List parameters = new ArrayList();
					parameters.add(this.getCustomerCountry());
					parameters.add(spr.getMaxDays());
					returnText = label.getText(locale,
							"message.delivery.estimate.precise", parameters);
				} else {
					List parameters = new ArrayList();
					parameters.add(this.getCustomerCountry());
					parameters.add(spr.getMinDays());
					parameters.add(spr.getMaxDays());
					returnText = label.getText(locale,
							"message.delivery.estimate.range", parameters);
				}

				return returnText;

			} else {

				return this.getDefaultShippingEstimateText();

			}

		}

		return this.getDefaultShippingEstimateText();

	}

	public String getShippingCompany() {

		if (StringUtils.isBlank(this.getShippingModule())) {
			return null;
		}

		LabelUtil label = LabelUtil.getInstance();
		String shippingCompany = label.getText(locale, "module."
				+ this.getShippingModule());

		return shippingCompany;

	}

	public String getShippingTypeDescription() {

		if (this.getShippingType() == null) {
			return "";
		}

		LabelUtil label = LabelUtil.getInstance();

		String shippingText = "";

		if (this.getShippingType() == ShippingType.NATIONAL) {
			shippingText = this.getStoreCountry();
		} else {
			shippingText = label
					.getText(locale, "label.shipping.international");
		}

		return shippingText;

	}

}
