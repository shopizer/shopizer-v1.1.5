/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Sep 17, 2010 Consultation CS-TI inc. 
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

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.service.payment.GatewayTransactionVO;
import com.salesmanager.core.service.payment.TransactionException;

/**
 * Specific transactions for credit card
 * @author Carl Samson
 *
 */
public interface CreditCardPaymentModule {
	
	/**
	 * Capture a transaction that has been authorized
	 * 
	 * @param origincountryid
	 * @param order
	 * @return
	 * @throws TransactionException
	 */

	
	public GatewayTransactionVO processCapture(Order order, MerchantStore store, Customer customer, String paymentModule)
			throws TransactionException;
	
	/**
	 * Can be invoked after a SALE transaction
	 * 
	 * @param origincountryid
	 * @param order
	 * @return
	 * @throws TransactionException
	 */
	
	public GatewayTransactionVO processRefund(Order order, MerchantStore store, Customer customer, BigDecimal amount, String paymentModule)
		throws TransactionException;
	


}
