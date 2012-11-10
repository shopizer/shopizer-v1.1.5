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

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

import JavaAPI.Completion;
import JavaAPI.HttpsPostRequest;
import JavaAPI.PreAuth;
import JavaAPI.Purchase;
import JavaAPI.Receipt;
import JavaAPI.Refund;

import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.MerchantPaymentGatewayTrx;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.impl.ModuleManagerImpl;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.payment.GatewayTransactionVO;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.payment.TransactionException;
import com.salesmanager.core.service.payment.impl.TransactionHelper;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CreditCardUtil;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.EncryptionUtil;
import com.salesmanager.core.util.LogMerchantUtil;

public class MonerisTransactionImpl extends CreditCardGatewayTransactionImpl {

	private Logger log = Logger.getLogger(MonerisTransactionImpl.class);

	public Map<String, String> initTransaction(
			CoreModuleService serviceDefinition, Order order)
			throws TransactionException {
		return null;
	}

	public Order postTransaction(Order order) throws TransactionException {
		return null;
	}

	public GatewayTransactionVO authorizeTransaction(IntegrationKeys keys,
			IntegrationProperties properties, MerchantStore store, Order order, Customer customer, CoreModuleService cms)
			throws TransactionException {
		return makeTransaction("Preauth", keys, properties, store, order, customer, cms);
	}

	public GatewayTransactionVO authorizeAndCapture(IntegrationKeys keys,
			IntegrationProperties properties, MerchantStore store, Order order, Customer customer, CoreModuleService cms)
			throws TransactionException {
		return makeTransaction("Purchase", keys, properties, store, order, customer, cms);
	}

	private GatewayTransactionVO makeTransaction(String type,
			IntegrationKeys ik, IntegrationProperties props,
			MerchantStore store, Order order, Customer customer, CoreModuleService cis) throws TransactionException {

		Receipt receipt = null;

		HttpsPostRequest mpgReq = null;

		try {



			String host = cis.getCoreModuleServiceDevDomain();
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.PRODUCTION_ENVIRONMENT))) {

				host = cis.getCoreModuleServiceProdDomain();
			}

			// host = "esqa.moneris.com";

			String store_id = ik.getUserid();
			String api_token = ik.getTransactionKey();
			String order_id = String.valueOf("SHOPIZER" + order.getOrderId());
			String cust_id = String.valueOf(order.getCustomerId());
			String amount = order.getTotal().toString();
			String crypt = "7";
			
			
			String uniqueOrderId = new StringBuffer().append(order.getOrderId()).append(DateUtil.generateTimeStamp()).toString();



			// HttpsPostRequest mpgReq =
			// new HttpsPostRequest(host, store_id, api_token,
			// new Purchase(order_id, cust_id, amount, pan, expiry_date,
			// crypt));

			// /gateway2/servlet/MpgRequest
			
			StringBuffer debugInfo = new StringBuffer();

			if (type.equals("Preauth")) {

				mpgReq = new HttpsPostRequest(host, store_id, api_token,
						new PreAuth(uniqueOrderId, cust_id, amount, order
								.getCcNumber(), order.getCcExpires(), crypt));
				
				debugInfo.append(uniqueOrderId).append(" ").append(cust_id).append(" ").append(CreditCardUtil.maskCardNumber(order
								.getCcNumber())).append(" ").append(order.getCcExpires()).append(" ").append(crypt);
				// mpgReq =
				// new HttpsPostRequest(host, store_id, api_token,
				// new PreAuth(order_id, cust_id, amount, pan, expiry_date,
				// crypt));
			} else {

				mpgReq = new HttpsPostRequest(host, store_id, api_token,
						new Purchase(uniqueOrderId, cust_id, amount, order
								.getCcNumber(), order.getCcExpires(), crypt));
				
				debugInfo.append(uniqueOrderId).append(" ").append(cust_id).append(" ").append(CreditCardUtil.maskCardNumber(order
						.getCcNumber())).append(" ").append(order.getCcExpires()).append(" ").append(crypt);
				// mpgReq =
				// new HttpsPostRequest(host, store_id, api_token,
				// new Purchase(order_id, cust_id, amount, pan, expiry_date,
				// crypt));
			}

			log.debug(debugInfo.toString());

			receipt = mpgReq.getReceipt();

			/*
			 * System.out.println("CardType = " + receipt.getCardType());
			 * System.out.println("TransAmount = " + receipt.getTransAmount());
			 * System.out.println("TxnNumber = " + receipt.getTxnNumber());
			 * System.out.println("ReceiptId = " + receipt.getReceiptId());
			 * System.out.println("TransType = " + receipt.getTransType());
			 * System.out.println("ReferenceNum = " +
			 * receipt.getReferenceNum()); System.out.println("ResponseCode = "
			 * + receipt.getResponseCode()); System.out.println("ISO = " +
			 * receipt.getISO()); System.out.println("BankTotals = " +
			 * receipt.getBankTotals()); System.out.println("Message = " +
			 * receipt.getMessage()); System.out.println("AuthCode = " +
			 * receipt.getAuthCode()); System.out.println("Complete = " +
			 * receipt.getComplete()); System.out.println("TransDate = " +
			 * receipt.getTransDate()); System.out.println("TransTime = " +
			 * receipt.getTransTime()); System.out.println("Ticket = " +
			 * receipt.getTicket()); System.out.println("TimedOut = " +
			 * receipt.getTimedOut());
			 */

			if (receipt.getComplete().equals("false")) {
				log.error("Cannot proceed transaction " + receipt.getMessage());
				TransactionException e = new TransactionException(
						"Cannot proceed transaction " + receipt.getMessage());
				e.setReason(receipt.getMessage());
				e.setErrorcode("01");
				throw e;
			}

			if (type.equals("Preauth")) {
				return this.parseResponse(PaymentConstants.PREAUTH, uniqueOrderId ,mpgReq,
						order, order.getTotal(), receipt);
			} else {
				return this.parseResponse(PaymentConstants.SALE, uniqueOrderId, mpgReq, order,
						order.getTotal(), receipt);// purchase
			}

		} catch (Exception e) {
			log
					.error("Error while processing the transaction"
							+ e.getMessage());
			TransactionException pe = new TransactionException(
					"Error while processing the transaction", e);
			pe.setErrorcode("01");
			throw pe;
		}

	}

	public void storeConfiguration(int merchantid, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {

		// Managed in Central PaymentmonerisAction

	}

	public ConfigurationResponse getConfiguration(
			MerchantConfiguration configurations, ConfigurationResponse vo) {

		try {
			// vo.addConfiguration("paymentmethod",
			// configurations.getConfigurationValue());

			String decryptedvalue = EncryptionUtil.decrypt(
					EncryptionUtil.generatekey(String.valueOf(configurations
							.getMerchantId())), configurations
							.getConfigurationValue());
			IntegrationKeys ik = this.stripCredentials(decryptedvalue);
			vo.addConfiguration("keys", ik);

			vo.addConfiguration("paymentmethod", configurations
					.getConfigurationModule());

			IntegrationProperties props = this.stripProperties(configurations
					.getConfigurationValue2());
			vo.addConfiguration("properties", props);

			vo.addConfiguration(PaymentConstants.PAYMENT_MONERIS,
					configurations);
		} catch (Exception e) {
			log.error("Can't understand MerchantConfiguration"
					+ configurations.getConfigurationId());
		}

		vo.addMerchantConfiguration(configurations);
		return vo;
	}

	public GatewayTransactionVO captureTransaction(IntegrationKeys ik, IntegrationProperties props,
			MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService cis) throws TransactionException {

		Receipt receipt = null;

		HttpsPostRequest mpgReq = null;

		try {


			
			
			String uniqueOrderId = trx.getInternalGatewayOrderId();

			String host = cis.getCoreModuleServiceDevDomain();
			if (props.getProperties1().equals(
					String.valueOf(PaymentConstants.PRODUCTION_ENVIRONMENT))) {

				host = cis.getCoreModuleServiceProdDomain();
			}

			String store_id = ik.getUserid();
			String api_token = ik.getTransactionKey();

			String order_id = trx.getInternalGatewayOrderId();
			String amount = trx.getAmount().toString();
			String txn_number = trx.getTransactionID();
			String crypt = "7";

			mpgReq = new HttpsPostRequest(host, store_id, api_token,
					new Completion(uniqueOrderId, amount, txn_number, crypt));

			/*
			 * host = "esqa.moneris.com"; store_id = "s"; api_token =
			 * "s"; order_id = trx.getInternalGatewayOrderId(); amount =
			 * trx.getAmount().toString(); txn_number = trx.getTransactionID();
			 * 
			 * mpgReq = new HttpsPostRequest(host, store_id, api_token, new
			 * Completion(order_id, amount, txn_number, crypt));
			 */

			log.debug(mpgReq.toString());

			receipt = mpgReq.getReceipt();

			/*
			 * System.out.println("CardType = " + receipt.getCardType());
			 * System.out.println("TransAmount = " + receipt.getTransAmount());
			 * System.out.println("TxnNumber = " + receipt.getTxnNumber());
			 * System.out.println("ReceiptId = " + receipt.getReceiptId());
			 * System.out.println("TransType = " + receipt.getTransType());
			 * System.out.println("ReferenceNum = " +
			 * receipt.getReferenceNum()); System.out.println("ResponseCode = "
			 * + receipt.getResponseCode()); System.out.println("ISO = " +
			 * receipt.getISO()); System.out.println("BankTotals = " +
			 * receipt.getBankTotals()); System.out.println("Message = " +
			 * receipt.getMessage()); System.out.println("AuthCode = " +
			 * receipt.getAuthCode()); System.out.println("Complete = " +
			 * receipt.getComplete()); System.out.println("TransDate = " +
			 * receipt.getTransDate()); System.out.println("TransTime = " +
			 * receipt.getTransTime()); System.out.println("Ticket = " +
			 * receipt.getTicket()); System.out.println("TimedOut = " +
			 * receipt.getTimedOut());
			 */

			if (receipt.getComplete().equals("false")) {
				log.error("Cannot proceed to refund " + receipt.getMessage());
				TransactionException e = new TransactionException(
						"Cannot proceed to refund " + receipt.getMessage());
				e.setReason(receipt.getMessage());
				e.setErrorcode("01");
				throw e;
			}

			return this.parseResponse(PaymentConstants.CAPTURE, uniqueOrderId, mpgReq, order,
					order.getTotal(), receipt);

		} catch (Exception e) {
			log
					.error("Error while processing the transaction"
							+ e.getMessage());
			TransactionException pe = new TransactionException(
					"Error while processing the transaction", e);
			pe.setErrorcode("01");
			throw pe;
		}

	}

	public GatewayTransactionVO refundTransaction(IntegrationKeys keys, IntegrationProperties props,
			MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService cis, BigDecimal amount) throws TransactionException {

		try {

			
			
			String uniqueOrderId = trx.getInternalGatewayOrderId();
			
			String host = cis.getCoreModuleServiceDevDomain();
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.PRODUCTION_ENVIRONMENT))) {

				host = cis.getCoreModuleServiceProdDomain();
			}

			String store_id = keys.getUserid();
			String api_token = keys.getTransactionKey();
			String order_id = trx.getInternalGatewayOrderId();
			String amnt = amount.toString();
			String txn_number = trx.getTransactionID();
			String crypt = "7";

			// For saving the amount in the transaction tables
			// order.setTotal(amount);

			HttpsPostRequest mpgReq = new HttpsPostRequest(host, store_id,
					api_token, new Refund(uniqueOrderId, amnt, txn_number, crypt));

			log.debug(mpgReq.toString());

			Receipt receipt = null;

			receipt = mpgReq.getReceipt();

			/*
			 * System.out.println("CardType = " + receipt.getCardType());
			 * System.out.println("TransAmount = " + receipt.getTransAmount());
			 * System.out.println("TxnNumber = " + receipt.getTxnNumber());
			 * System.out.println("ReceiptId = " + receipt.getReceiptId());
			 * System.out.println("TransType = " + receipt.getTransType());
			 * System.out.println("ReferenceNum = " +
			 * receipt.getReferenceNum()); System.out.println("ResponseCode = "
			 * + receipt.getResponseCode()); System.out.println("ISO = " +
			 * receipt.getISO()); System.out.println("BankTotals = " +
			 * receipt.getBankTotals()); System.out.println("Message = " +
			 * receipt.getMessage()); System.out.println("AuthCode = " +
			 * receipt.getAuthCode()); System.out.println("Complete = " +
			 * receipt.getComplete()); System.out.println("TransDate = " +
			 * receipt.getTransDate()); System.out.println("TransTime = " +
			 * receipt.getTransTime()); System.out.println("Ticket = " +
			 * receipt.getTicket()); System.out.println("TimedOut = " +
			 * receipt.getTimedOut());
			 */

			if (receipt.getComplete().equals("false")) {
				log.error("Cannot proceed to refund " + receipt.getMessage());
				TransactionException e = new TransactionException(
						"Cannot proceed to refund " + receipt.getMessage());
				e.setReason(receipt.getMessage());
				throw e;
			}

			return this.parseResponse(PaymentConstants.REFUND, uniqueOrderId, mpgReq, order,
					amount, receipt);

		} catch (Exception e) {
			log
					.error("Error while processing the transaction"
							+ e.getMessage());
			// throw new Exception("Error while processing the transaction",e);
			TransactionException pe = new TransactionException(
					"Error while processing the transaction" + e.getMessage());
			pe.setErrorcode("01");
			throw pe;
		}

	}

	private GatewayTransactionVO parseResponse(int transactiontype,
			String uniqueOrderId, HttpsPostRequest mpgReq, Order order, BigDecimal amount,
			Receipt receipt) throws Exception {

		MerchantPaymentGatewayTrx gtrx = null;
		if (receipt.getResponseCode() == null) {
			String message = "N/A";
			if (receipt.getMessage() == null) {
				// this is a connectivity problem
				log.error("Connectivity problem with Moneris");
			} else {
				LogMerchantUtil
						.log(order.getMerchantId(), receipt.getMessage());
				message = receipt.getMessage();
			}
			// throw new
			// Exception("Problem with moneris transaction, message-> " +
			// message);
			// End user should see the message
			TransactionException e = new TransactionException(
					"Problem with moneris transaction, message " + message);
			e.setReason(message);
			e.setErrorcode("01");
			throw e;
		}

		/**
		 * StringBuffer trxresponsebuffer = new StringBuffer();
		 * trxresponsebuffer.append("ReceiptId = " + receipt.getReceiptId());
		 * trxresponsebuffer.append("ReferenceNum = " +
		 * receipt.getReferenceNum());
		 * trxresponsebuffer.append("ResponseCode = " +
		 * receipt.getResponseCode()); trxresponsebuffer.append("ISO = " +
		 * receipt.getISO()); trxresponsebuffer.append("AuthCode = " +
		 * receipt.getAuthCode()); trxresponsebuffer.append("TransTime = " +
		 * receipt.getTransTime()); trxresponsebuffer.append("TransDate = " +
		 * receipt.getTransDate()); trxresponsebuffer.append("TransType = " +
		 * receipt.getTransType()); trxresponsebuffer.append("Complete = " +
		 * receipt.getComplete()); trxresponsebuffer.append("Message = " +
		 * receipt.getMessage()); trxresponsebuffer.append("TransAmount = " +
		 * receipt.getTransAmount()); trxresponsebuffer.append("CardType = " +
		 * receipt.getCardType()); trxresponsebuffer.append("TransID = " +
		 * receipt.getTxnNumber()); trxresponsebuffer.append("TimedOut = " +
		 * receipt.getTimedOut()); trxresponsebuffer.append("BankTotals = " +
		 * receipt.getBankTotals()); trxresponsebuffer.append("Ticket = " +
		 * receipt.getTicket());
		 **/

		StringBuffer trxresponsebufferxml = new StringBuffer();
		trxresponsebufferxml
				.append("<?xml version=\"1.0\" standalone=\"yes\"?><response><receipt>");
		trxresponsebufferxml.append("<ReceiptId>").append(
				receipt.getReceiptId()).append("</ReceiptId>");
		trxresponsebufferxml.append("<ReferenceNum>").append(
				receipt.getReferenceNum()).append("</ReferenceNum>");
		trxresponsebufferxml.append("<ResponseCode>").append(
				receipt.getResponseCode()).append("</ResponseCode>");
		trxresponsebufferxml.append("<ISO>").append(receipt.getISO()).append(
				"</ISO>");
		trxresponsebufferxml.append("<AuthCode>").append(receipt.getAuthCode())
				.append("</AuthCode>");
		trxresponsebufferxml.append("<TransTime>").append(
				receipt.getTransTime()).append("</TransTime>");
		trxresponsebufferxml.append("<TransDate>").append(
				receipt.getTransDate()).append("</TransDate>");
		trxresponsebufferxml.append("<TransType>").append(
				receipt.getTransType()).append("</TransType>");
		trxresponsebufferxml.append("<Complete>").append(receipt.getComplete())
				.append("</Complete>");
		trxresponsebufferxml.append("<Message>").append(receipt.getMessage())
				.append("</Message>");
		trxresponsebufferxml.append("<TransAmount>").append(
				receipt.getTransAmount()).append("</TransAmount>");
		trxresponsebufferxml.append("<CardType>").append(receipt.getCardType())
				.append("</CardType>");
		trxresponsebufferxml.append("<TransID>").append(receipt.getTxnNumber())
				.append("</TransID>");
		trxresponsebufferxml.append("<TimedOut>").append(receipt.getTimedOut())
				.append("</TimedOut>");
		trxresponsebufferxml.append("<BankTotals>").append(
				receipt.getBankTotals()).append("</BankTotals>");
		trxresponsebufferxml.append("<Ticket>").append(receipt.getTicket())
				.append("</Ticket>");
		trxresponsebufferxml.append("</receipt></response>");

		log.debug(trxresponsebufferxml.toString());
		int responsecode = Integer.parseInt(receipt.getResponseCode());
		if (responsecode >= 50) {// End user should see the message
			LogMerchantUtil.log(order.getMerchantId(),
					"Can't process transaction for card type "
							+ receipt.getCardType() + ", got response code "
							+ responsecode);

			TransactionException e = new TransactionException(
					"Can't process transaction for card type "
							+ receipt.getCardType() + ", got response code "
							+ responsecode);
			e.setReason(String.valueOf(responsecode));
			e.setErrorcode("02");
			throw e;
		} else {

			try {

				PaymentService pservice = (PaymentService) ServiceFactory
						.getService(ServiceFactory.PaymentService);

				gtrx = new MerchantPaymentGatewayTrx();
				gtrx.setMerchantId(order.getMerchantId());
				gtrx.setCustomerid(order.getCustomerId());
				gtrx.setOrderId(order.getOrderId());
				gtrx.setAmount(amount);
				gtrx.setMerchantPaymentGwMethod(order.getPaymentModuleCode());
				gtrx.setMerchantPaymentGwOrderid(uniqueOrderId);
				gtrx.setMerchantPaymentGwRespcode(String.valueOf(responsecode));
				gtrx.setMerchantPaymentGwTrxid(receipt.getTxnNumber());
				gtrx.setMerchantPaymentGwAuthtype(String
						.valueOf(transactiontype));
				gtrx.setMerchantPaymentGwSession("");

				String cryptedvalue = EncryptionUtil.encrypt(EncryptionUtil
						.generatekey(String.valueOf(order.getMerchantId())),
						mpgReq.toString());
				gtrx.setMerchantPaymentGwSent(cryptedvalue);
				gtrx.setMerchantPaymentGwReceived(trxresponsebufferxml
						.toString());
				gtrx.setDateAdded(new Date(new Date().getTime()));
				gtrx.setAmount(amount);

				pservice.saveMerchantPaymentGatewayTrx(gtrx);

			} catch (Exception e) {
				log.error("Can't persist MerchantPaymentGatewayTrx receipt id "
						+ receipt.getReceiptId(), e);
			}
		}

		GatewayTransactionVO vo = new GatewayTransactionVO();
		vo.setAmount(order.getTotal());
		vo.setCreditcard(receipt.getCardType());
		vo.setCreditcardtransaction(true);
		vo.setExpirydate(order.getCcExpires());
		vo.setInternalGatewayOrderId(receipt.getTxnNumber());
		vo.setTransactionDetails(gtrx);
		return vo;
	}

	public List<com.salesmanager.core.service.payment.SalesManagerTransactionVO> retreiveTransactions(
			int merchantid, Order order) throws Exception {

		long orderid = order.getOrderId();
		TransactionHelper trxhelper = new TransactionHelper();
		// MerchantPaymentGatewayTrx trx = trxhelper.getSentData(merchantid,
		// orderid, "00");
		List trxs = trxhelper.getSentData(merchantid, orderid);

		// <?xml version="1.0" standalone="yes"?>
		// <response>
		// <receipt>
		// <ReceiptId>19W2X407311197825073</ReceiptId>
		// <ReferenceNum>660021730012090950</ReferenceNum>
		// <ResponseCode>027</ResponseCode>
		// <ISO>01</ISO>
		// <AuthCode>009116</AuthCode>
		// <TransTime>12:06:51</TransTime>
		// <TransDate>2007-12-16</TransDate>
		// <TransType>00</TransType>
		// <Complete>true</Complete>
		// <Message>APPROVED * =</Message>
		// <TransAmount>43.00</TransAmount>
		// <CardType>M</CardType>
		// <TransID>276000-0_6</TransID>
		// <TimedOut>false</TimedOut>
		// <BankTotals>null</BankTotals>
		// <Ticket>null</Ticket>
		// </receipt>
		// </response>

		if (trxs == null) {
			return null;
		}

		Iterator i = trxs.iterator();

		List returnlist = new ArrayList();

		while (i.hasNext()) {

			MerchantPaymentGatewayTrx trx = (MerchantPaymentGatewayTrx) i
					.next();

			// pan
			// expdate

			ParsedElements elems = new ParsedElements();

			Digester digester = new Digester();
			digester.push(elems);
			digester.addCallMethod("response/receipt/ReceiptId",
					"setReceiptId", 0);
			digester.addCallMethod("response/receipt/TransID", "setTransID", 0);
			digester.addCallMethod("response/receipt/TransAmount",
					"setTransactionAmount", 0);

			Reader reader = new StringReader(trx.getMerchantPaymentGwReceived());

			digester.parse(reader);

			GatewayTransactionVO mtrx = new GatewayTransactionVO();

			// GatewayTransaction gt = new GatewayTransaction();
			mtrx.setOrderID(String.valueOf(orderid));
			mtrx.setInternalGatewayOrderId(elems.getReceiptId());// id created
																	// in
																	// payment
																	// gateway
			mtrx.setTransactionID(elems.getTransID());// id created in SM
			mtrx.setTransactionDetails(trx);
			mtrx.setType(Integer.parseInt(trx.getMerchantPaymentGwAuthtype()));
			mtrx.setAmount(new BigDecimal(elems.getTransactionAmount()));
			mtrx.setName(trx.getMerchantPaymentGwMethod());
			returnlist.add(mtrx);

		}
		return returnlist;

	}

	private IntegrationKeys stripCredentials(String configvalue)
			throws Exception {
		if (configvalue == null)
			return new IntegrationKeys();
		StringTokenizer st = new StringTokenizer(configvalue, ";");
		int i = 1;
		int j = 1;
		IntegrationKeys keys = new IntegrationKeys();
		while (st.hasMoreTokens()) {
			String value = st.nextToken();

			if (i == 1) {
				// decrypt
				keys.setUserid(value);
			} else if (i == 2) {
				// decrypt
				keys.setPassword(value);
			} else if (i == 3) {
				// decrypt
				keys.setTransactionKey(value);
			} else {
				if (j == 1) {
					keys.setKey1(value);
				} else if (j == 2) {
					keys.setKey2(value);
				} else if (j == 3) {
					keys.setKey3(value);
				}
				j++;
			}
			i++;
		}
		return keys;
	}

	/**
	 * Properties are 1) Production(1) - Test(2) 2) Pre-Auth(1) - Capture (2) -
	 * Sale (0) 3) No CCV (1) - With CCV (2)
	 * 
	 * @param configvalue
	 * @return
	 */
	private IntegrationProperties stripProperties(String configvalue) {
		if (configvalue == null)
			return new IntegrationProperties();
		StringTokenizer st = new StringTokenizer(configvalue, ";");
		int i = 1;
		IntegrationProperties keys = new IntegrationProperties();
		while (st.hasMoreTokens()) {
			String value = st.nextToken();
			if (i == 1) {
				keys.setProperties1(value);
			} else if (i == 2) {
				keys.setProperties2(value);
			} else if (i == 3) {
				keys.setProperties3(value);
			} else {
				keys.setProperties4(value);
			}
			i++;
		}
		return keys;
	}

}

class ParsedElements {

	private String ReceiptId;
	private String TransID;
	private String amount;

	public void setReceiptId(String ReceiptId) {
		this.ReceiptId = ReceiptId;
	}

	public String getReceiptId() {
		return ReceiptId;
	}

	public String getTransID() {
		return TransID;
	}

	public void setTransID(String transID) {
		TransID = transID;
	}

	public void setTransactionAmount(String amount) {
		this.amount = amount;
	}

	public String getTransactionAmount() {
		return this.amount;
	}

}
