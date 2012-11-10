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
package com.salesmanager.checkout.flow;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.checkout.CheckoutBaseAction;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.payment.CreditCard;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.util.www.SessionUtil;

public class PaymentAction extends CheckoutBaseAction {
	private PaymentMethod paymentMethod;// submited

	private Logger log = Logger.getLogger(PaymentAction.class);

	public String displayPayment() {
		try {
			paymentMethod = SessionUtil.getPaymentMethod(getServletRequest());
			if (paymentMethod != null) {
				super.getServletRequest().setAttribute("SELECTEDPAYMENT",
						paymentMethod);
				if (com.salesmanager.core.util.PaymentUtil
						.isPaymentModuleCreditCardType(paymentMethod
								.getPaymentModuleName())) {
					super.setCreditCard((CreditCard) paymentMethod
							.getConfig("CARD"));
				}

			}
			super.preparePayments();
			super.prepareCreditCards();

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "GENERICERROR";
		}

		return SUCCESS;
	}

	public String selectPayment() {
		try {

			boolean isCreditCardPayment = false;

			if (this.getPaymentMethod() == null
					|| StringUtils.isBlank(this.getPaymentMethod()
							.getPaymentModuleName())) {
				super.addErrorMessage("error.nopaymentmethod");
				return INPUT;
			}

			if (com.salesmanager.core.util.PaymentUtil
					.isPaymentModuleCreditCardType(this.getPaymentMethod()
							.getPaymentModuleName())) {
				isCreditCardPayment = true;
				MerchantStore store = SessionUtil
						.getMerchantStore(getServletRequest());
				super.validateCreditCard(this.getPaymentMethod(), store
						.getMerchantId());

			}

			this.preparePayments();
			Map pms = super.getPaymentMethods();

			PaymentMethod tmpMethod = (PaymentMethod) pms.get(this
					.getPaymentMethod().getPaymentModuleName());

			/*
			 * if(tmpMethod==null && isCreditCardPayment) { tmpMethod =
			 * (PaymentMethod) pms.get("GATEWAY"); if(tmpMethod!=null) {
			 * if(!tmpMethod
			 * .getPaymentModuleName().equals(this.getPaymentMethod(
			 * ).getPaymentModuleName())) { tmpMethod = null; } } }
			 */

			if (tmpMethod != null) {
				this.getPaymentMethod().setPaymentMethodName(
						tmpMethod.getPaymentMethodName());
				this.getPaymentMethod().setPaymentModuleText(
						tmpMethod.getPaymentModuleText());
				this.getPaymentMethod().setPaymentMethodConfig(
						tmpMethod.getPaymentMethodConfig());
			}

			SessionUtil.setPaymentMethod(this.getPaymentMethod(),
					getServletRequest());

			// check paypal
			if (paymentMethod.getPaymentModuleName().equals(
					PaymentConstants.PAYMENT_PAYPALNAME)) {
				// set the number of steps
				return "payPalExpressCheckout";
			}

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}

		return SUCCESS;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
