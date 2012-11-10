/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.validator.ValidationException;
import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.MerchantPaymentGatewayTrx;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.payment.GatewayTransactionVO;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.payment.SalesManagerTransactionVO;
import com.salesmanager.core.service.payment.TransactionException;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

public class TransactionDetailsAction extends BaseAction {

	private Logger log = Logger.getLogger(TransactionDetailsAction.class);

	private Order order = null;

	private List gatewaytransactions = new ArrayList();
	private boolean creditcardtransaction = false;
	private int nextaction = -1;
	private String process;

	private String orderTotal = null;
	private String refundAmount = null;

	private void prepareOrderDetails() throws Exception {
		
		super.setPageTitle("label.order.paymentdetails.title");

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		// Get the order
		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);

		Order o = oservice.getOrder(this.getOrder().getOrderId());

		if(o.getTotal().doubleValue() > new BigDecimal("0").doubleValue()) {
			this.setOrderTotal(CurrencyUtil.displayFormatedAmountNoCurrency(o
				.getTotal(), ctx.getCurrency()));
		}

		// check if that entity realy belongs to merchantid
		if (o == null) {
			throw new AuthorizationException("Order is null for orderId "
					+ this.getOrder().getOrderId());
		}

		// Check if user is authorized (entity belongs to merchant)
		super.authorize(o);

		this.setOrder(o);

	}

	/**
	 * Process a new transaction, currently supports refund and capture
	 * following a pre-authorize
	 * 
	 * @return
	 * @throws Exception
	 */
	public String processTransaction() throws Exception {

		Context ctx = super.getContext();

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		MerchantStore store = mservice.getMerchantStore(ctx.getMerchantid());

		try {

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			prepareOrderDetails();

			/** INPUT VALIDATION **/
			// validate the presence of a transaction type in the request
			// parameter
			if (this.getProcess() == null) {
				log
						.error("No transaction process id in request parameter. Require &process=1 or &process=2 or &process=3");
				return SUCCESS;
			}

			int process = -1;
			try {
				process = Integer.parseInt(this.getProcess());
			} catch (NumberFormatException nfe) {
				log.error("Can't parse process id in request parameter ["
						+ this.getProcess()
						+ "],  require &process=1 or &process=2 or &process=3");
				return SUCCESS;
			}

			// process 2 and 3 supported
			if (process < 2 || process > 3) {
				log.error("Transaction process type not supported "
						+ this.getProcess());
				return SUCCESS;
			}

			/** END VALIDATION **/

			PaymentService service = (PaymentService) ServiceFactory
					.getService(ServiceFactory.PaymentService);

			switch (process) {

			case PaymentConstants.CAPTURE:
				service.captureTransaction(store, this.getOrder());
				break;

			case PaymentConstants.REFUND:

				// get amount

				if (StringUtils.isBlank(this.getRefundAmount())) {
					super.addFieldError("orderTotal",
							"transaction.error.transactionamount");
					return INPUT;
				}

				BigDecimal originalAmount = this.getOrder().getTotal();

				BigDecimal newAmount = null;
				try {

					newAmount = CurrencyUtil.validateCurrency(this
							.getRefundAmount(), order.getCurrency());
				} catch (ValidationException e) {
					super.addFieldError("orderTotal",
							"transaction.error.transactionamount");
					return INPUT;
				}

				if (newAmount == null
						|| newAmount.floatValue() > originalAmount.floatValue()) {
					super.addFieldError("orderTotal",
							"transaction.error.transactionamounttoohigh");
					return INPUT;
				}

				OrderService oservice = (OrderService) ServiceFactory
						.getService(ServiceFactory.OrderService);
				oservice.refundOrder(this.getOrder(), newAmount, super
						.getLocale());

				break;

			default:
				log.error("Transaction process type not supported "
						+ this.getProcess());
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText("errors.technical"));

			}

			super.setSuccessMessage();

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			if (e instanceof TransactionException) {
				TransactionException te = (TransactionException) e;
				// Display appropriate message to end user
				if (te.getErrorcode() != null
						&& !te.getErrorcode().trim().equals("")) {
					String textkey = "transaction.errors." + te.getErrorcode();
					if (te.getReason() != null
							&& !te.getReason().trim().equals("")) {
						MessageUtil.addErrorMessage(super.getServletRequest(),
								LabelUtil.getInstance().getText(textkey) + " ["
										+ te.getReason() + "]");
					} else {
						MessageUtil.addErrorMessage(super.getServletRequest(),
								LabelUtil.getInstance().getText(textkey));
					}
				} else {
					MessageUtil.addErrorMessage(super.getServletRequest(), te.getMessage() + " [" + te.getErrorcode() + "]");
					//super.setTechnicalMessage();
				}
			} else {
				log.error(e);
				super.setTechnicalMessage();
			}
		}

		return SUCCESS;
	}

	/**
	 * Displays transaction details
	 */
	public String displayTransactions() throws Exception {

		try {

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setTechnicalMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			prepareOrderDetails();

			PaymentService service = (PaymentService) ServiceFactory
					.getService(ServiceFactory.PaymentService);

			List smt = service.getTransactions(order);

			MerchantPaymentGatewayTrx lasttransaction = null;
			if (smt != null) {

				Iterator i = smt.iterator();

				while (i.hasNext()) {
					SalesManagerTransactionVO trx = (SalesManagerTransactionVO) i
							.next();
					if (trx instanceof GatewayTransactionVO) {
						this.creditcardtransaction = true;
						// downcast to the appropriate object
						GatewayTransactionVO gtx = (GatewayTransactionVO) trx;
						// Determines allowed actions
						MerchantPaymentGatewayTrx transaction = gtx
								.getTransactionDetails();
						if (lasttransaction != null) {
							if (transaction.getDateAdded().after(
									lasttransaction.getDateAdded())) {
								lasttransaction = transaction;
							}
						} else {
							lasttransaction = transaction;
						}
						gatewaytransactions.add(gtx);
					}
				}

				nextaction = Integer.parseInt(lasttransaction
						.getMerchantPaymentGwAuthtype()) + 1;
				int trtype = Integer.parseInt(lasttransaction
						.getMerchantPaymentGwAuthtype());

				if (trtype == PaymentConstants.PREAUTH) {
					nextaction = PaymentConstants.CAPTURE;
				} else if (trtype == PaymentConstants.SALE) {
					nextaction = PaymentConstants.REFUND;
				} else if (trtype == PaymentConstants.CAPTURE) {
					nextaction = PaymentConstants.REFUND;
				} else if (trtype == PaymentConstants.REFUND) {
					if (lasttransaction.getAmount().doubleValue() > new BigDecimal(
							"0").doubleValue()) {
						nextaction = PaymentConstants.REFUND;
					} else {
						nextaction = -1;
					}
				} else {
					nextaction = -1;
				}
			}

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public List getTransactions() {
		return gatewaytransactions;
	}

	public boolean isCreditcardtransaction() {
		return creditcardtransaction;
	}

	public int getNextaction() {
		return nextaction;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

}
