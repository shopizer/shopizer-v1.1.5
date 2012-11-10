/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Sep 8, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.entity.orders;

import java.util.Collection;


import com.salesmanager.core.entity.common.Report;

public class OrderReport extends Report {
	
	private Collection<Order> orders;
	private Collection<OrderTotal> totals;

	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}

	public Collection<OrderTotal> getTotals() {
		return totals;
	}

	public void setTotals(Collection<OrderTotal> totals) {
		this.totals = totals;
	}

}
