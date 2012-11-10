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
package com.salesmanager.core.service.shipping;

import java.util.List;

public class AddressMatchVO {

	private List shippingaddress;// Contains a list of shippingaddressvo for
									// match
	private int responsemessage = 0;
	private String addressmatchprovider;// ups, fedex, usps...

	public AddressMatchVO(List shippingaddress) {
		super();
		this.shippingaddress = shippingaddress;
	}

	public int getResponsemessage() {
		return responsemessage;
	}

	public void setResponsemessage(int responsemessage) {
		this.responsemessage = responsemessage;
	}

	public List getShippingaddress() {
		return shippingaddress;
	}

	public String getAddressmatchprovider() {
		return addressmatchprovider;
	}

	public void setAddressmatchprovider(String addressmatchprovider) {
		this.addressmatchprovider = addressmatchprovider;
	}

}
