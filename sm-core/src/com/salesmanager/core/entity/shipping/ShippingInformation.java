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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * This object is used in the invoice and shopping cart presentation page
 * 
 * @author Administrator
 * 
 */
public class ShippingInformation implements Serializable {

	private String message = null;

	private String shippingMethod = null;// the shipping description selected
	private String shippingMethodId = null;// the carrier shipping option when
											// applies (1 day / 3 days / 5
											// days...)
	private String shippingModule = null;// the shipping module selected

	private boolean freeShipping = false;

	private BigDecimal shippingCost;
	private String shippingCostText = null;

	private ShippingOption shippingOptionSelected;// selected shipping option
													// from shipping method
													// collection available

	private BigDecimal handlingCost;
	private String handlingCostText = null;

	private long taxClass = -1;

	private Collection<ShippingMethod> shippingMethods;// shipping methods
														// available

	public ShippingInformation() {
		handlingCost = new BigDecimal("0");
		shippingCost = new BigDecimal("0");
	}

	private String orderTotalPrice;

	public String getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(String orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BigDecimal getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(BigDecimal shippingCost) {
		this.shippingCost = shippingCost;
	}

	public String getShippingCostText() {
		return shippingCostText;
	}

	public void setShippingCostText(String shippingCostText) {
		this.shippingCostText = shippingCostText;
	}

	public BigDecimal getHandlingCost() {
		return handlingCost;
	}

	public void setHandlingCost(BigDecimal handlingCost) {
		this.handlingCost = handlingCost;
	}

	public Collection<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(Collection<ShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	public boolean isFreeShipping() {
		return freeShipping;
	}

	public void setFreeShipping(boolean freeShipping) {
		this.freeShipping = freeShipping;
	}

	public String getHandlingCostText() {
		return handlingCostText;
	}

	public void setHandlingCostText(String handlingCostText) {
		this.handlingCostText = handlingCostText;
	}

	public long getTaxClass() {
		return taxClass;
	}

	public void setTaxClass(long taxClass) {
		this.taxClass = taxClass;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getShippingMethodId() {
		return shippingMethodId;
	}

	public void setShippingMethodId(String shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
	}

	public ShippingOption getShippingOptionSelected() {
		return shippingOptionSelected;
	}

	public void setShippingOptionSelected(ShippingOption shippingOptionSelected) {
		this.shippingOptionSelected = shippingOptionSelected;
	}

	public String getShippingModule() {
		return shippingModule;
	}

	public void setShippingModule(String shippingModule) {
		this.shippingModule = shippingModule;
	}

}
