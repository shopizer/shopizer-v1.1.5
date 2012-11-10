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
package com.salesmanager.core.util.reports.invoice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;

public class InvoiceInformationFactory {

	private List<Order> orders = new ArrayList();
	private List<MerchantStore> stores = new ArrayList();

	public Collection getOrder() {
		return orders;
	}

	public Collection getStore() {
		return stores;
	}

	public void setOrder(Order order) {
		orders.add(order);
	}

	public void setStore(MerchantStore store) {
		stores.add(store);
	}

}
