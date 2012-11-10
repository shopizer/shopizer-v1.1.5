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
package com.salesmanager.core.service.shipping.impl;

import java.util.List;

public class AddressValidationContextVO {

	private int merchantid;
	private String shippingcarrier;
	private int response = -1;
	private int origincountry;
	private String responsetext;
	private String internalresponsecode;

	private List validatedAddressList;

	public AddressValidationContextVO(int merchantid, String shippingcarrier,
			int origin) {
		super();
		this.merchantid = merchantid;
		this.shippingcarrier = shippingcarrier;
		this.origincountry = origin;
	}

	public int getMerchantid() {
		return merchantid;
	}

	public String getShippingcarrier() {
		return shippingcarrier;
	}

	public List getValidatedAddressList() {
		return validatedAddressList;
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}

	public int getOrigincountry() {
		return origincountry;
	}

	public String getInternalresponsecode() {
		return internalresponsecode;
	}

	public void setInternalresponsecode(String internalresponsecode) {
		this.internalresponsecode = internalresponsecode;
	}

	public String getResponsetext() {
		return responsetext;
	}

	public void setResponsetext(String responsetext) {
		this.responsetext = responsetext;
	}
}
