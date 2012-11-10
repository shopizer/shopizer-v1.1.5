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

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.PropertiesUtil;

public class PackageDetail implements java.io.Serializable {

	private double shippingWeight;
	private double shippingMaxWeight;
	private double shippingLength;
	private double shippingHeight;
	private double shippingWidth;
	private int shippingQuantity;
	private int treshold;

	private String weight;
	private String maxWeight;
	private String length;
	private String height;
	private String width;
	private String productName;

	private String currency;

	public String getCurrency() {
		if (currency == null) {
			currency = PropertiesUtil.getConfiguration().getString(
					"core.system.defaultcurrency", Constants.CURRENCY_CODE_USD);
		}
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getShippingHeight() {
		return shippingHeight;
	}

	public void setShippingHeight(double shippingHeight) {
		this.shippingHeight = shippingHeight;
	}

	public double getShippingLength() {
		return shippingLength;
	}

	public void setShippingLength(double shippingLength) {
		this.shippingLength = shippingLength;
	}

	public double getShippingMaxWeight() {
		return shippingMaxWeight;
	}

	public void setShippingMaxWeight(double shippingMaxWeight) {
		this.shippingMaxWeight = shippingMaxWeight;
	}

	public int getShippingQuantity() {
		return shippingQuantity;
	}

	public void setShippingQuantity(int shippingQuantity) {
		this.shippingQuantity = shippingQuantity;
	}

	public double getShippingWeight() {
		return shippingWeight;
	}

	public void setShippingWeight(double shippingWeight) {
		this.shippingWeight = shippingWeight;
	}

	public double getShippingWidth() {
		return shippingWidth;
	}

	public void setShippingWidth(double shippingWidth) {
		this.shippingWidth = shippingWidth;
	}

	public String getHeight() {
		return CurrencyUtil.displayMeasure(new BigDecimal(this
				.getShippingHeight()), currency);
	}

	public void setHeight(String height) {

		this.height = height;
	}

	public String getLength() {
		return CurrencyUtil.displayMeasure(new BigDecimal(this
				.getShippingLength()), currency);

	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getMaxWeight() {
		return CurrencyUtil.displayMeasure(new BigDecimal(this
				.getShippingMaxWeight()), currency);

	}

	public void setMaxWeight(String maxWeight) {
		this.maxWeight = maxWeight;
	}

	public String getWeight() {

		return CurrencyUtil.displayMeasure(new BigDecimal(this
				.getShippingWeight()), currency);

	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWidth() {
		return CurrencyUtil.displayMeasure(new BigDecimal(this
				.getShippingWidth()), currency);

	}

	public void setWidth(String width) {
		this.width = width;
	}

	public int getTreshold() {
		return treshold;
	}

	public void setTreshold(int treshold) {
		this.treshold = treshold;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
