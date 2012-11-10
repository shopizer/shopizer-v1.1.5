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
package com.salesmanager.core.service.workflow.order;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.reference.SystemUrlEntryType;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.workflow.Activity;
import com.salesmanager.core.service.workflow.ProcessorContext;

public class ProcessCustomer implements Activity {

	private Logger log = Logger.getLogger(ProcessCustomer.class);

	public ProcessorContext execute(ProcessorContext context) throws Exception {
		// TODO Auto-generated method stub
		Order order = (Order) context.getObject("Order");
		Customer customer = (Customer) context.getObject("Customer");
		Locale l = (Locale) context.getObject("Locale");

		try {

			CustomerService custservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			custservice.saveOrUpdateCustomer(customer,
					SystemUrlEntryType.PORTAL, l);

			order.setCustomerId(customer.getCustomerId());

		} catch (Exception e) {
			log.error(e);
		}

		return context;

	}

}
