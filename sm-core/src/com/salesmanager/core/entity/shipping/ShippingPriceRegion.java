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

public class ShippingPriceRegion {

	private List prices = new ArrayList();
	private List countries = new ArrayList();
	private String countryline = null;
	private String priceLine = null;
	private int minDays = -1;
	private int maxDays = -1;
	private boolean estimatedTimeEnabled = false;

	public boolean isEstimatedTimeEnabled() {
		return estimatedTimeEnabled;
	}

	public void setEstimatedTimeEnabled(boolean estimatedTimeEnabled) {
		this.estimatedTimeEnabled = estimatedTimeEnabled;
	}

	public List getCountries() {
		return countries;
	}

	public void addCountry(String c) {
		countries.add(c);
	}

	public List getPrices() {
		return prices;
	}

	public void addPrice(ShippingPricePound spb) {
		prices.add(spb);
	}

	public String toString() {
		return new StringBuffer().append("countries").append(" ").append(
				this.getCountries().toString()).append(" ").append(
				this.getPrices().toString()).toString();
	}

	public String getCountryline() {
		return countryline;
	}

	public void setCountryline(String countryline) {
		this.countryline = countryline;
	}

	public String getPriceLine() {
		return priceLine;
	}

	public void setPriceLine(String priceLine) {
		this.priceLine = priceLine;
	}

	public int getMinDays() {
		return minDays;
	}

	public void setMinDays(int minDays) {
		this.minDays = minDays;
	}

	public int getMaxDays() {
		return maxDays;
	}

	public void setMaxDays(int maxDays) {
		this.maxDays = maxDays;
	}

}
