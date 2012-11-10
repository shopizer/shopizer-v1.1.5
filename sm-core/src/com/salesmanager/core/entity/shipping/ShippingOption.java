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

import java.math.BigDecimal;

import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.PropertiesUtil;

public class ShippingOption implements java.io.Serializable {

	private String currency = null;
	private String optionId;
	private String description;
	private String module;
	private String shippingDate;
	private String deliveryDate;
	private BigDecimal optionPrice;
	private String optionName;
	private String optionCode;
	private String estimatedNumberOfDays = "";

	public String getOptionPriceText() {
		if (currency == null) {
			currency = PropertiesUtil.getConfiguration().getString(
					"core.system.defaultcurrency", "USD");
		}

		return CurrencyUtil.displayFormatedAmountWithCurrency(this
				.getOptionPrice(), currency);

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getOptionPrice() {
		return optionPrice;
	}

	public void setOptionPrice(BigDecimal optionPrice) {
		this.optionPrice = optionPrice;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(String shippingDate) {
		this.shippingDate = shippingDate;
	}

	public void setOptionPriceText(String price) {
		this.optionPrice = new BigDecimal(price);
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionCode() {
		return optionCode;
	}

	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}

	public String getEstimatedNumberOfDays() {
		return estimatedNumberOfDays;
	}

	public void setEstimatedNumberOfDays(String estimatedNumberOfDays) {
		this.estimatedNumberOfDays = estimatedNumberOfDays;
	}

}
