/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.customer.profile;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.common.PageBaseAction;
import com.salesmanager.common.util.PropertiesHelper;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.SearchOrderResponse;
import com.salesmanager.core.entity.orders.SearchOrdersCriteria;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class OrdersAction extends PageBaseAction {

	private Logger log = Logger.getLogger(OrdersAction.class);
	private static Configuration config = PropertiesHelper.getConfiguration();

	private Collection orders;

	private String orderId;

	private static int size = 20;

	static {

		size = config.getInt("catalog.orderlist.maxsize", 10);

	}

	public String displayOrders() {

		try {

			super.setSize(size);// defined in configuration according to
								// template
			super.setPageStartNumber();

			SearchOrdersCriteria crit = getCriteria(super.getPageStartIndex());
			ordersQuery(crit);

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	private void ordersQuery(SearchOrdersCriteria criteria) throws Exception {

		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);

		SearchOrderResponse resp = oservice.searchOrdersByCustomer(criteria);
		if (resp != null) {
			orders = resp.getOrders();

			LocaleUtil.setLocaleToEntityCollection(orders, super.getLocale());

			super.setListingCount(resp.getCount());
			super.setRealCount(orders.size());
			super.setPageElements();

		}

	}

	private SearchOrdersCriteria getCriteria(int startIndex) {

		MerchantStore store = SessionUtil.getMerchantStore(super
				.getServletRequest());

		SearchOrdersCriteria criteria = new SearchOrdersCriteria();

		Customer customer = SessionUtil.getCustomer(super.getServletRequest());

		// 12 months
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MONTH, -12);

		criteria.setSdate(calendar.getTime());
		criteria.setEdate(new Date());

		criteria.setLanguageId(LanguageUtil.getLanguageNumberCode(super
				.getLocale().getLanguage()));
		criteria.setMerchantId(store.getMerchantId());
		criteria.setCustomerId(customer.getCustomerId());
		criteria.setQuantity(size);
		criteria.setStartindex(startIndex);

		return criteria;

	}

	public Collection getOrders() {
		return orders;
	}

	public void setOrders(Collection orders) {
		this.orders = orders;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
