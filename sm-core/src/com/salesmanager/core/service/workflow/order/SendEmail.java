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
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.workflow.Activity;
import com.salesmanager.core.service.workflow.ProcessorContext;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LogMerchantUtil;

public class SendEmail implements Activity {

	private Logger log = Logger.getLogger(ProcessOrder.class);

	public ProcessorContext execute(ProcessorContext context) throws Exception {

		Order order = (Order) context.getObject("Order");
		Customer customer = (Customer) context.getObject("Customer");
		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);

		order = oservice.getOrder(order.getOrderId());

		// send confirmation email
		try {
			oservice.sendOrderConfirmationEmail(order.getMerchantId(), order,
					customer);
		} catch (RuntimeException re) {
			Locale l = (Locale) context.getObject("Locale");
			String txt = LabelUtil.getInstance().getText(l.getLanguage(),
					"message.error.sendemail.error",
					String.valueOf(order.getOrderId()));
			LogMerchantUtil.log(order.getMerchantId(), txt);
			log.error(re);
		} catch (Exception ee) {
			Locale l = (Locale) context.getObject("Locale");
			String txt = LabelUtil.getInstance().getText(l.getLanguage(),
					"message.error.sendemail.error",
					String.valueOf(order.getOrderId()));
			LogMerchantUtil.log(order.getMerchantId(), txt);
			log.error(ee);
		}

		return context;
	}

}
