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

public class Shipping implements Serializable {

	private String shippingModule;// shipping module
	private BigDecimal shippingCost;// shipping option price
	private BigDecimal handlingCost;
	private String shippingDescription;// to be printed to the customer

	public Shipping() {
		handlingCost = new BigDecimal("0");
		//handlingCost.setScale(2,BigDecimal.ROUND_FLOOR);
		shippingCost = new BigDecimal("0");
		//shippingCost.setScale(2,BigDecimal.ROUND_FLOOR);
	}

	public BigDecimal getHandlingCost() {
		return handlingCost;
	}

	public void setHandlingCost(BigDecimal handlingCost) {
		this.handlingCost = handlingCost;
	}

	public BigDecimal getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(BigDecimal shippingCost) {
		this.shippingCost = shippingCost;
	}

	public String getShippingDescription() {
		return shippingDescription;
	}

	public void setShippingDescription(String shippingDescription) {
		this.shippingDescription = shippingDescription;
	}

	public String getShippingModule() {
		return shippingModule;
	}

	public void setShippingModule(String shippingModule) {
		this.shippingModule = shippingModule;
	}

}
