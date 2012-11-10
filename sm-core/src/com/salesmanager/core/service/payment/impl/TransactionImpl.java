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
package com.salesmanager.core.service.payment.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.module.model.integration.CreditCardPaymentModule;
import com.salesmanager.core.module.model.integration.PaymentModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.payment.GatewayTransactionVO;
import com.salesmanager.core.service.payment.TransactionException;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.SpringUtil;

public class TransactionImpl {


	private static Configuration conf = PropertiesUtil.getConfiguration();
	private static Logger log = Logger.getLogger(TransactionImpl.class);

	public List<com.salesmanager.core.service.payment.SalesManagerTransactionVO> getTransactions(
			Order order) throws Exception {


		PaymentModule module = (PaymentModule) SpringUtil.getBean(order
				.getPaymentModuleCode());
		if (module == null) {
			throw new Exception(order.getPaymentModuleCode()
					+ " not defined in sm-core config file");
		}

		return module.retreiveTransactions(order.getMerchantId(), order);

	}

	public GatewayTransactionVO getTransactionType(Order order, int[] types)
			throws Exception {

		PaymentModule module = (PaymentModule) SpringUtil.getBean(order
				.getPaymentModuleCode());
		if (module == null) {
			throw new Exception(order.getPaymentModuleCode()
					+ " not defined in sm-core config file");
		}

		List alltransactions = module.retreiveTransactions(order
				.getMerchantId(), order);
		if (alltransactions == null) {
			throw new Exception("No transaction recorded for orderid "
					+ order.getOrderId());
		}

		GatewayTransactionVO trx = null;
		int typelength = types.length;
		for (int i = 0; i < typelength; i++) {
			Iterator it = alltransactions.iterator();
			while (it.hasNext()) {
				GatewayTransactionVO gtvo = (GatewayTransactionVO) it.next();
				if (gtvo.getType() == types[i]) {
					if (trx != null) {
						if (trx.getType() != gtvo.getType()) {// Means the
																// transaction
																// has a void
																// and capture
							throw new Exception(
									"Cannot determine which transaction is refundable between "
											+ trx.getTransactionID() + " and "
											+ gtvo.getTransactionID());
						}
					}
					trx = gtvo;
				}
			}
		}
		return trx;
	}



}
