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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.payment.MerchantPaymentGatewayTrx;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.util.EncryptionUtil;

public class TransactionHelper {

	public List<com.salesmanager.core.entity.payment.MerchantPaymentGatewayTrx> getSentData(
			int merchantid, long orderid) throws Exception {

		PaymentService pservice = (PaymentService) ServiceFactory
				.getService(ServiceFactory.PaymentService);

		Collection trxs = pservice
				.findMerchantPaymentGatewayTrxByMerchantIdAndOrderId(
						merchantid, orderid);

		if (trxs != null) {
			Iterator i = trxs.iterator();
			while (i.hasNext()) {
				MerchantPaymentGatewayTrx trx = (MerchantPaymentGatewayTrx) i
						.next();
				if (trx.getMerchantPaymentGwMethod().equals(
						PaymentConstants.PAYMENT_PAYPALNAME)) {
					trx.setGatewaySentDecrypted(trx.getMerchantPaymentGwSent());
				} else {
					String key = EncryptionUtil.generatekey(String
							.valueOf(merchantid));

					trx.setGatewaySentDecrypted(EncryptionUtil
							.decryptFromExternal(key, trx
									.getMerchantPaymentGwSent()));
				}
			}
			return (List) trxs;
		} else {

			throw new Exception("Transaction not found for merchant id "
					+ merchantid + " order id " + orderid);
		}

	}

}
