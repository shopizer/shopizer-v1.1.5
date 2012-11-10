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
package com.salesmanager.core.entity.payment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PaymentMethod implements Serializable {


	private String paymentMethodName;
	private String paymentModuleName;
	private String paymentModuleText;
	private String paymentImage;
	
	private CreditCard creditCard = null;

	public String getPaymentImage() {
		return paymentImage;
	}

	public void setPaymentImage(String paymentImage) {
		this.paymentImage = paymentImage;
	}

	private boolean enabled = false;
	private int type = 0;// core_modules_services subtype [0 or 1-> credit card]

	private Map paymentMethodInfoSubmited = new HashMap();
	private Map paymentMethodConfig = new HashMap();

	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	public String getPaymentModuleName() {
		return paymentModuleName;
	}

	public void setPaymentModuleName(String paymentModuleName) {
		this.paymentModuleName = paymentModuleName;
	}

	public void addConfig(String key, Object value) {
		paymentMethodConfig.put(key, value);
	}

	public Object getConfig(String key) {
		return paymentMethodConfig.get(key);
	}

	public void addInfo(String key, String value) {
		paymentMethodInfoSubmited.put(key, value);
	}

	public String getInfo(String key) {
		return (String) paymentMethodInfoSubmited.get(key);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPaymentModuleText() {
		return paymentModuleText;
	}

	public void setPaymentModuleText(String paymentModuleText) {
		this.paymentModuleText = paymentModuleText;
	}

	public Map getPaymentMethodConfig() {
		return paymentMethodConfig;
	}

	public void setPaymentMethodConfig(Map paymentMethodConfig) {
		this.paymentMethodConfig = paymentMethodConfig;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
		paymentMethodConfig.put("CARD", creditCard);
	}

}
