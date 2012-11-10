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
import java.util.Collection;

public class ShippingMethod implements java.io.Serializable {

	private String shippingMethodName;
	private String shippingModule;
	private String image;
	private int priority = 0;

	private Collection<ShippingOption> options;

	public ShippingMethod() {
		options = new ArrayList();
	}

	public void addOption(ShippingOption option) {
		options.add(option);
	}

	public Collection<ShippingOption> getOptions() {
		return options;
	}

	public void setOptions(Collection<ShippingOption> options) {
		this.options = options;
	}

	public String getShippingMethodName() {
		return shippingMethodName;
	}

	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}

	public String getShippingModule() {
		return shippingModule;
	}

	public void setShippingModule(String shippingModule) {
		this.shippingModule = shippingModule;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
