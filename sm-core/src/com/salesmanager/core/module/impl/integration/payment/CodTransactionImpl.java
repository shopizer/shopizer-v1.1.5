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
package com.salesmanager.core.module.impl.integration.payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.module.model.integration.PaymentModule;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.payment.GatewayTransactionVO;
import com.salesmanager.core.service.payment.SalesManagerTransactionVO;
import com.salesmanager.core.service.payment.TransactionException;

public class CodTransactionImpl implements PaymentModule {

	public Map<String, String> initTransaction(
			CoreModuleService serviceDefinition, Order order)
			throws TransactionException {
		return null;
	}

	public Order postTransaction(Order order) throws TransactionException {
		return null;
	}

	public SalesManagerTransactionVO processTransaction(CoreModuleService serviceDefinition,
			PaymentMethod paymentMethod, Order order, Customer customer)
			throws TransactionException {
		throw new TransactionException("Not implemented");
	}


	public List<SalesManagerTransactionVO> retreiveTransactions(int merchantid,
			Order order) throws Exception {
		List list = new ArrayList();
		SalesManagerTransactionVO vo = new SalesManagerTransactionVO();
		vo.setCreditcardtransaction(false);
		vo.setAmount(order.getTotal());
		vo.setName(PaymentConstants.PAYMENT_CODNAME);
		list.add(vo);
		return list;
	}

	public ConfigurationResponse getConfiguration(
			MerchantConfiguration configurations, ConfigurationResponse vo)
			throws Exception {
		// implemented in action class
		return null;
	}

	public void storeConfiguration(int merchantid, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {
		// implemented in action class

	}

}
