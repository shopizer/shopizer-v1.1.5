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
package com.salesmanager.core.entity.orders;

import java.util.Date;

import com.salesmanager.core.entity.common.SearchCriteria;
import com.salesmanager.core.util.DateUtil;

public class SearchOrdersCriteria extends SearchCriteria {

	private Date sdate;
	private Date edate;

	private long orderId = -1;
	private long customerId;

	private String customerName;

	public Date getEdate() {
		return edate;
	}

	public void setEdate(Date edate) {
		this.edate = edate;
	}

	public Date getSdate() {
		return sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getStartDateString() {
		return DateUtil.formatDate(sdate);
	}

	public String getEndDateString() {
		return DateUtil.formatDate(edate);
	}

	public boolean isSet() {
		if (customerName != null || orderId != -1 || sdate != null
				|| edate != null) {
			return true;
		} else {
			return false;
		}
	}

	public void resetCriteria() {
		this.orderId = -1;
		this.sdate = null;
		this.edate = null;
		this.customerName = null;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
}
