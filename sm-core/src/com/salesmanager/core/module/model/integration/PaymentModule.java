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
package com.salesmanager.core.module.model.integration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.service.common.model.ConfigurableModule;
import com.salesmanager.core.service.payment.GatewayTransactionVO;
import com.salesmanager.core.service.payment.SalesManagerTransactionVO;
import com.salesmanager.core.service.payment.TransactionException;

public interface PaymentModule extends ConfigurableModule {

	public List<com.salesmanager.core.service.payment.SalesManagerTransactionVO> retreiveTransactions(
			int merchantid, Order order) throws Exception;



	/**
	 * Will process the transaction (authorization or sale) This is the entry
	 * point from checkout. From that method you need to target to the
	 * appropriate method [authorization or sale] for credit card processing. 
	 * It may be authorize or capture or authorizeAndCapture according to merchant configuration
	 * 
	 * This method is also the entry point to non credit card payments
	 * @param store
	 * @param order
	 * @throws TransactionException
	 */
	public SalesManagerTransactionVO processTransaction(CoreModuleService serviceDefinition,
			PaymentMethod paymentMethod, Order order, Customer customer)
			throws TransactionException;

	/**
	 * Returns token-value related to the initialization of the transaction This
	 * method is invoked for paypal express checkout
	 * 
	 * @param order
	 * @return
	 * @throws TransactionException
	 */
	public Map<String, String> initTransaction(
			CoreModuleService serviceDefinition, Order order)
			throws TransactionException;

	/** when the transaction is on an external server **/
	public Order postTransaction(Order order) throws TransactionException;

}
