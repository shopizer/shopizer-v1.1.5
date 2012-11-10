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

public class ShippingPricePound implements Comparable {

	private int maxpound;
	private BigDecimal price;

	public int getMaxpound() {
		return maxpound;
	}

	public void setMaxpound(int maxpound) {
		this.maxpound = maxpound;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int compareTo(Object o) {
		if (!(o instanceof ShippingPricePound))
			throw new ClassCastException();
		if (((ShippingPricePound) o).getMaxpound() < this.getMaxpound())
			return 1;
		if (((ShippingPricePound) o).getMaxpound() > this.getMaxpound())
			return -1;
		else
			return 0;
	}

	public String toString() {
		// return new
		// StringBuffer().append(maxpound).append(",").append(price.doubleValue()).toString();
		return "NOT IMPLEMENTED";
	}

}
