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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

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
import com.salesmanager.core.service.payment.SalesManagerTransactionVO;
import com.salesmanager.core.service.payment.TransactionException;
import com.salesmanager.core.service.payment.impl.TransactionHelper;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CreditCardUtil;
import com.salesmanager.core.util.EncryptionUtil;
import com.salesmanager.core.util.LogMerchantUtil;

public class AuthorizeNetTransactionImpl extends
		CreditCardGatewayTransactionImpl {

	private static Logger log = Logger
			.getLogger(AuthorizeNetTransactionImpl.class);

	// ConfigurationResponse vo = null;

	public Map<String, String> initTransaction(
			CoreModuleService serviceDefinition, Order order)
			throws TransactionException {
		return null;
	}

	public Order postTransaction(Order order) throws TransactionException {
		return null;
	}

	/**
	 * Will target the transaction processing to the appropriate type according
	 * to merchant configuration
	 */


	public GatewayTransactionVO authorizeAndCapture(IntegrationKeys keys,
			IntegrationProperties properties, MerchantStore store, Order order, Customer customer, CoreModuleService cms)
			throws TransactionException {
		return makeTransaction("AUTH_CAPTURE", keys, properties, store, order, customer, cms);
	}

	public GatewayTransactionVO authorizeTransaction(IntegrationKeys keys,
			IntegrationProperties properties, MerchantStore store, Order order, Customer customer, CoreModuleService cms)
			throws TransactionException {
		return makeTransaction("AUTH_ONLY", keys, properties, store, order, customer, cms);
	}

	private GatewayTransactionVO makeTransaction(String type,
			IntegrationKeys ik, IntegrationProperties props,
			MerchantStore store, Order order, Customer customer, CoreModuleService cis) throws TransactionException {

		PostMethod httppost = null;
		try {


			String host = cis.getCoreModuleServiceProdDomain();
			String protocol = cis.getCoreModuleServiceProdProtocol();
			String port = cis.getCoreModuleServiceProdPort();
			String url = cis.getCoreModuleServiceProdEnv();
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				host = cis.getCoreModuleServiceDevDomain();
				protocol = cis.getCoreModuleServiceDevProtocol();
				port = cis.getCoreModuleServiceDevPort();
				url = cis.getCoreModuleServiceDevEnv();
			}

			// sb builds the nv pair to authorize net
			StringBuffer sb = new StringBuffer();

			// mandatory name/value pairs for all AIM CC transactions
			// as well as some "good to have" values
			sb.append("x_login=").append(ik.getUserid()).append("&"); // replace
																		// with
																		// your
																		// own
			sb.append("x_tran_key=").append(ik.getTransactionKey()).append("&"); // replace
																					// with
																					// your
																					// own
			sb.append("x_version=3.1&");// set to required in the specs

			// if dev
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				sb.append("x_test_request=TRUE&"); // for testing
			}

			sb.append("x_method=CC&");// required, CC or ECHECK
			sb.append("x_type=").append(type).append("&");

			// need ccard number
			sb.append("x_card_num=").append(order.getCcNumber()).append("&");
			sb.append("x_exp_date=").append(order.getCcExpires()).append("&");
			sb.append("x_amount=").append(order.getTotal()).append("&");
			if (props.getProperties3().equals("2")) {
				sb.append("x_card_code=").append(order.getCcCvv()).append("&");
			}

			sb.append("x_delim_data=TRUE&");
			sb.append("x_delim_char=|&");
			sb.append("x_relay_response=FALSE&");

			// sx builds the xml for persisting local
			StringBuffer sx = new StringBuffer();

			sx.append("<transaction>");

			sx.append("<x_login>").append(ik.getUserid()).append("</x_login>"); // replace
																				// with
																				// your
																				// own
			sx.append("<x_tran_key>").append(ik.getTransactionKey()).append(
					"</x_tran_key>"); // replace with your own
			sx.append("<x_version>3.1</x_version>");// set to required in the
													// specs

			// if dev
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				sx.append("<x_test_request>TRUE</x_test_request>"); // for
																	// testing
			}

			sx.append("<x_method>CC</x_method>");// required, CC or ECHECK
			sx.append("<x_type>AUTH_ONLY</x_type>");

			// need ccard number
			sx.append("<x_card_num>").append(order.getCcNumber()).append(
					"</x_card_num>");// at least 4 last card digit
			sx.append("<x_exp_date>").append(order.getCcExpires()).append(
					"</x_exp_date>");
			sx.append("<x_amount>").append(order.getTotal()).append(
					"</x_amount>");
			if (props.getProperties3().equals("2")) {
				sx.append("<x_card_code>").append(order.getCcCvv()).append(
						"</x_card_code>");
			}

			sx.append("<x_delim_data>TRUE</x_delim_data>");
			sx.append("<x_delim_char>|</x_delim_char>");
			sx.append("<x_relay_response>FALSE</x_relay_response>");

			sx.append("</transaction>");
			
			
			/** debug **/
			// sb builds the nv pair to authorize net
			StringBuffer sblog = new StringBuffer();
			sblog.append("x_login=").append(ik.getUserid()).append("&"); // replace
																		// with
																		// your
																		// own
			sblog.append("x_tran_key=").append(ik.getTransactionKey()).append("&"); // replace																	// own
			sblog.append("x_version=3.1&");// set to required in the specs
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				sblog.append("x_test_request=TRUE&"); // for testing
			}

			sblog.append("x_method=CC&");// required, CC or ECHECK
			sblog.append("x_type=").append(type).append("&");
			sblog.append("x_card_num=").append(CreditCardUtil.maskCardNumber(order.getCcNumber())).append("&");
			sblog.append("x_exp_date=").append(order.getCcExpires()).append("&");
			sblog.append("x_amount=").append(order.getTotal()).append("&");
			if (props.getProperties3().equals("2")) {
				sblog.append("x_card_code=").append(order.getCcCvv()).append("&");
			}
			sblog.append("x_delim_data=TRUE&");
			sblog.append("x_delim_char=|&");
			sblog.append("x_relay_response=FALSE&");
			
			
			log.debug("Transaction sent -> " + sblog.toString());
			/**
			 * log
			 */

			HttpClient client = new HttpClient();

			httppost = new PostMethod(protocol + "://" + host + ":" + port
					+ url);
			RequestEntity entity = new StringRequestEntity(sb.toString(),
					"text/plain", "UTF-8");
			httppost.setRequestEntity(entity);



			String stringresult = null;

			int result = client.executeMethod(httppost);
			if (result != 200) {
				log.error("Communication Error with Authorizenet " + protocol
						+ "://" + host + ":" + port + url);
				throw new Exception("Communication Error with Authorizenet "
						+ protocol + "://" + host + ":" + port + url);
			}

			// stringresult = httppost.getResponseBodyAsString();
			// log.debug("AuthorizeNet response " +stringresult);

			stringresult = httppost.getResponseBodyAsString();
			log.debug("AuthorizeNet response " + stringresult);

			StringBuffer appendresult = new StringBuffer().append(
					order.getTotal()).append("|").append(stringresult);

			// Format to xml
			//
			String finalresult = this
					.readresponse("|", appendresult.toString());

			AuthorizeNetParsedElements pe = new AuthorizeNetParsedElements();
			Digester digester = new Digester();
			digester.push(pe);

			digester.addCallMethod("transaction/transactionid",
					"setTransactionId", 0);
			digester.addCallMethod("transaction/approvalcode",
					"setApprovalCode", 0);
			digester.addCallMethod("transaction/responsecode",
					"setResponseCode", 0);
			digester.addCallMethod("transaction/amount",
					"setTransactionAmount", 0);
			digester
					.addCallMethod("transaction/reasontext", "setReasontext", 0);
			digester
					.addCallMethod("transaction/reasoncode", "setReasonCode", 0);

			Reader reader = new StringReader(finalresult);

			digester.parse(reader);

			// check if you need it
			// pe.setTransactionId(trx.getTransactionDetails().getMerchantPaymentGwOrderid());//BECAUSE
			// NOT RETURNED FROM AUTHORIZE NET !!!!

			if (type.equals("AUTH_CAPTURE")) {

				return this.parseResponse(PaymentConstants.SALE, sx.toString(),
						finalresult, pe, order, order.getTotal());

			} else {// AUTH_ONLY

				return this.parseResponse(PaymentConstants.PREAUTH, sx
						.toString(), finalresult, pe, order, order.getTotal());

			}

		} catch (Exception e) {
			if (e instanceof TransactionException) {
				throw (TransactionException) e;
			}
			log.error(e);

			TransactionException te = new TransactionException(
					"AuthorizeNet Gateway error ", e);
			throw te;
		} finally {
			if (httppost != null) {
				httppost.releaseConnection();
			}
		}

	}

	public GatewayTransactionVO captureTransaction(IntegrationKeys ik, IntegrationProperties props,
			MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService cis) throws TransactionException {

		PostMethod httppost = null;
		try {


			if (cis == null) {
				// throw new
				// Exception("Central integration services not configured for "
				// + PaymentConstants.PAYMENT_LINKPOINTNAME + " and country id "
				// + origincountryid);
				log.error("Central integration services not configured for "
						+ PaymentConstants.PAYMENT_AUTHORIZENETNAME
						+ " and country id " + store.getCountry());
				TransactionException te = new TransactionException(
						"Central integration services not configured for "
								+ PaymentConstants.PAYMENT_AUTHORIZENETNAME
								+ " and country id " + store.getCountry());
				te.setErrorcode("01");
				throw te;
			}

			String host = cis.getCoreModuleServiceProdDomain();
			String protocol = cis.getCoreModuleServiceProdProtocol();
			String port = cis.getCoreModuleServiceProdPort();
			String url = cis.getCoreModuleServiceProdEnv();
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				host = cis.getCoreModuleServiceDevDomain();
				protocol = cis.getCoreModuleServiceDevProtocol();
				port = cis.getCoreModuleServiceDevPort();
				url = cis.getCoreModuleServiceDevEnv();
			}

			// parse sent elements required
			AuthorizeNetParsedElements pe = null;
			pe = new AuthorizeNetParsedElements();
			Digester digester = new Digester();
			digester.push(pe);

			digester
					.addCallMethod("transaction/x_card_num", "setCardnumber", 0);
			digester.addCallMethod("transaction/approvalcode",
					"setApprovalCode", 0);
			digester
					.addCallMethod("transaction/x_exp_date", "setExpiration", 0);
			digester.addCallMethod("transaction/x_amount",
					"setTransactionAmount", 0);
			digester.addCallMethod("transaction/x_card_code", "setCvv", 0);

			Reader reader = new StringReader(trx.getTransactionDetails()
					.getGatewaySentDecrypted().trim());

			digester.parse(reader);

			// sb builds the nv pair to authorize net
			StringBuffer sb = new StringBuffer();

			// mandatory name/value pairs for all AIM CC transactions
			// as well as some "good to have" values
			sb.append("x_login=").append(ik.getUserid()).append("&"); // replace
																		// with
																		// your
																		// own
			sb.append("x_tran_key=").append(ik.getTransactionKey()).append("&"); // replace
																					// with
																					// your
																					// own
			sb.append("x_version=3.1&");// set to required in the specs

			// if dev
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				sb.append("x_test_request=TRUE&"); // for testing
			}

			sb.append("x_method=CC&");// required, CC or ECHECK
			sb.append("x_type=CAPTURE_ONLY&");

			// need ccard number
			sb.append("x_card_num=").append(pe.getCardnumber()).append("&");// at
																			// least
																			// 4
																			// last
																			// card
																			// digit
			sb.append("x_exp_date=").append(pe.getExpiration()).append("&");
			sb.append("x_amount=").append(pe.getTransactionAmount())
					.append("&");
			if (pe.getCvv() != null) {
				sb.append("x_card_code=").append(pe.getCvv()).append("&");
			}

			sb.append("x_delim_data=TRUE&");
			sb.append("x_delim_char=|&");
			sb.append("x_relay_response=FALSE&");
			sb.append("x_auth_code=").append(trx.getInternalGatewayOrderId())
					.append("&");
			
			
			/** debug **/
			StringBuffer sblog = new StringBuffer();
			sblog.append("x_login=").append(ik.getUserid()).append("&"); // replace
																		// with
																		// your
																		// own
			sblog.append("x_tran_key=").append(ik.getTransactionKey()).append("&"); // replace																// own
			sblog.append("x_version=3.1&");// set to required in the specs
			// if dev
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				sblog.append("x_test_request=TRUE&"); // for testing
			}
			sblog.append("x_method=CC&");// required, CC or ECHECK
			sblog.append("x_type=CAPTURE_ONLY&");
			// need ccard number
			sblog.append("x_card_num=").append(CreditCardUtil.maskCardNumber(pe.getCardnumber())).append("&");// at
																			// least
																			// 4
																			// last
																			// card
																			// digit
			sblog.append("x_exp_date=").append(pe.getExpiration()).append("&");
			sblog.append("x_amount=").append(pe.getTransactionAmount())
					.append("&");
			if (pe.getCvv() != null) {
				sblog.append("x_card_code=").append(pe.getCvv()).append("&");
			}
			sblog.append("x_delim_data=TRUE&");
			sblog.append("x_delim_char=|&");
			sblog.append("x_relay_response=FALSE&");
			sblog.append("x_auth_code=").append(trx.getInternalGatewayOrderId())
					.append("&");
			
			
			log.debug(sblog.toString());
			/** log **/
			

			// sx builds the xml for persisting local
			StringBuffer sx = new StringBuffer();

			sx.append("<transaction>");
			sx.append("<x_login>").append(ik.getUserid()).append("</x_login>"); // replace
																				// with
																				// your
																				// own
			sx.append("<x_tran_key>").append(ik.getTransactionKey()).append(
					"</x_tran_key>"); // replace with your own
			sx.append("<x_version>3.1</x_version>");// set to required in the
													// specs

			// if dev
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				sx.append("<x_test_request>TRUE</x_test_request>"); // for
																	// testing
			}

			sx.append("<x_method>CC</x_method>");// required, CC or ECHECK
			sx.append("<x_type>CAPTURE_ONLY</x_type>");

			// need ccard number
			sx.append("<x_card_num>").append(pe.getCardnumber()).append(
					"</x_card_num>");// at least 4 last card digit
			sx.append("<x_exp_date>").append(pe.getExpiration()).append(
					"</x_exp_date>");
			sx.append("<x_amount>").append(pe.getTransactionAmount()).append(
					"</x_amount>");
			if (pe.getCvv() != null) {
				sx.append("<x_card_code>").append(pe.getCvv()).append(
						"</x_card_code>");
			}

			sx.append("<x_delim_data>TRUE</x_delim_data>");
			sx.append("<x_delim_char>|</x_delim_char>");
			sx.append("<x_relay_response>FALSE</x_relay_response>");
			sx.append("<x_auth_code>").append(trx.getInternalGatewayOrderId())
					.append("</x_auth_code>");
			sx.append("</transaction>");

			// x_encap_char
			// use cvv x_card_code
			// x_amount
			// x_recurring_billing NO
			// x_customer_ip

			HttpClient client = new HttpClient();

			httppost = new PostMethod(protocol + "://" + host + ":" + port
					+ url);
			RequestEntity entity = new StringRequestEntity(sb.toString(),
					"text/plain", "UTF-8");
			httppost.setRequestEntity(entity);

			// open secure connection
			// URL anurl = new URL(protocol + "://" + host + ":" + port + url);

			/*
			 * NOTE: If you want to use SSL-specific features,change to:
			 * HttpsURLConnection connection = (HttpsURLConnection)
			 * url.openConnection();
			 */



			String stringresult = null;

			int result = client.executeMethod(httppost);
			if (result != 200) {
				log.error("Communication Error with Authorizenet " + protocol
						+ "://" + host + ":" + port + url);

				throw new Exception("Communication Error with Authorizenet "
						+ protocol + "://" + host + ":" + port + url);
			}

			// stringresult = httppost.getResponseBodyAsString();
			// log.debug("AuthorizeNet response " +stringresult);

			stringresult = httppost.getResponseBodyAsString();
			log.debug("AuthorizeNet response " + stringresult);

			StringBuffer appendresult = new StringBuffer().append(
					order.getTotal()).append("|").append(stringresult);

			// Format to xml
			//
			String finalresult = this
					.readresponse("|", appendresult.toString());

			pe = new AuthorizeNetParsedElements();
			digester = new Digester();
			digester.push(pe);

			digester.addCallMethod("transaction/transactionid",
					"setTransactionId", 0);
			digester.addCallMethod("transaction/approvalcode",
					"setApprovalCode", 0);
			digester.addCallMethod("transaction/responsecode",
					"setResponseCode", 0);
			digester.addCallMethod("transaction/amount",
					"setTransactionAmount", 0);
			digester
					.addCallMethod("transaction/reasontext", "setReasontext", 0);
			digester
					.addCallMethod("transaction/reasoncode", "setReasonCode", 0);

			reader = new StringReader(finalresult);

			digester.parse(reader);

			pe.setTransactionId(trx.getTransactionDetails()
					.getMerchantPaymentGwOrderid());// BECAUSE NOT RETURNED FROM
													// AUTHORIZE NET !!!!

			return this.parseResponse(PaymentConstants.CAPTURE, sx.toString(),
					finalresult, pe, order, order.getTotal());

		} catch (Exception e) {
			if (e instanceof TransactionException) {
				throw (TransactionException) e;
			}
			log.error(e);

			TransactionException te = new TransactionException(
					"AuthorizeNet Gateway error ", e);
			throw te;
		} finally {
			if (httppost != null) {
				httppost.releaseConnection();
			}
		}

	}

	public GatewayTransactionVO refundTransaction(IntegrationKeys keys, IntegrationProperties props,
			MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService cis, BigDecimal amount) throws TransactionException {

		PostMethod httppost = null;
		try {


			String host = cis.getCoreModuleServiceProdDomain();
			String protocol = cis.getCoreModuleServiceProdProtocol();
			String port = cis.getCoreModuleServiceProdPort();
			String url = cis.getCoreModuleServiceProdEnv();
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				host = cis.getCoreModuleServiceDevDomain();
				protocol = cis.getCoreModuleServiceDevProtocol();
				port = cis.getCoreModuleServiceDevPort();
				url = cis.getCoreModuleServiceDevEnv();
			}

			// parse sent elements required
			AuthorizeNetParsedElements pe = null;
			pe = new AuthorizeNetParsedElements();
			Digester digester = new Digester();
			digester.push(pe);

			digester
					.addCallMethod("transaction/x_card_num", "setCardnumber", 0);
			digester.addCallMethod("transaction/approvalcode",
					"setApprovalCode", 0);
			digester
					.addCallMethod("transaction/x_exp_date", "setExpiration", 0);
			digester.addCallMethod("transaction/x_amount",
					"setTransactionAmount", 0);
			digester.addCallMethod("transaction/x_card_code", "setCvv", 0);

			Reader reader = new StringReader(trx.getTransactionDetails()
					.getGatewaySentDecrypted().trim());

			digester.parse(reader);

			StringBuffer sb = new StringBuffer();

			// mandatory name/value pairs for all AIM CC transactions
			// as well as some "good to have" values
			sb.append("x_login=").append(keys.getUserid()).append("&"); // replace
																		// with
																		// your
																		// own
			sb.append("x_tran_key=").append(keys.getTransactionKey()).append("&"); // replace
																					// with
																					// your
																					// own
			sb.append("x_version=3.1&");// set to required in the specs

			// if dev
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				sb.append("x_test_request=TRUE&"); // for testing
			}

			sb.append("x_method=CC&");// required, CC or ECHECK
			sb.append("x_type=CREDIT&");

			sb.append("x_card_num=").append(pe.getCardnumber()).append("&");// at
																			// least
																			// 4
																			// last
																			// card
																			// digit
			sb.append("x_exp_date=").append(pe.getExpiration()).append("&");
			sb.append("x_amount=").append(amount.toString()).append("&");
			if (pe.getCvv() != null) {
				sb.append("x_card_code=").append(pe.getCvv()).append("&");
			}
			sb.append("x_delim_data=TRUE&");
			sb.append("x_delim_char=|&");
			sb.append("x_relay_response=FALSE&");
			sb.append("x_trans_id=").append(trx.getTransactionID()).append("&");

			
			
			/** debug **/
			StringBuffer sblog = new StringBuffer();

			// mandatory name/value pairs for all AIM CC transactions
			// as well as some "good to have" values
			sblog.append("x_login=").append(keys.getUserid()).append("&"); // replace
																		// with
																		// your
																		// own
			sblog.append("x_tran_key=").append(keys.getTransactionKey()).append("&"); // replace																	// own
			sblog.append("x_version=3.1&");// set to required in the specs
			// if dev
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				sblog.append("x_test_request=TRUE&"); // for testing
			}
			sblog.append("x_method=CC&");// required, CC or ECHECK
			sblog.append("x_type=CREDIT&");
			sblog.append("x_card_num=").append(pe.getCardnumber()).append("&");// at
																			// least
																			// 4
																			// last
																			// card
																			// digit
			sblog.append("x_exp_date=").append(pe.getExpiration()).append("&");
			sblog.append("x_amount=").append(amount.toString()).append("&");
			if (pe.getCvv() != null) {
				sblog.append("x_card_code=").append(pe.getCvv()).append("&");
			}
			sblog.append("x_delim_data=TRUE&");
			sblog.append("x_delim_char=|&");
			sblog.append("x_relay_response=FALSE&");
			sblog.append("x_trans_id=").append(trx.getTransactionID()).append("&");

			log.debug("Transaction sent -> " + sblog.toString());
			/** debug **/
			
			
			
			
			// sx builds the xml for persisting local
			StringBuffer sx = new StringBuffer();

			sx.append("<transaction>");
			sx.append("<x_login>").append(keys.getUserid()).append("</x_login>"); // replace
																				// with
																				// your
																				// own
			sx.append("<x_tran_key>").append(keys.getTransactionKey()).append(
					"</x_tran_key>"); // replace with your own
			sx.append("<x_version>3.1</x_version>");// set to required in the
													// specs

			// if dev
			if (props.getProperties2().equals(
					String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
				sx.append("<x_test_request>TRUE</x_test_request>"); // for
																	// testing
			}

			sx.append("<x_method>CC</x_method>");// required, CC or ECHECK
			sx.append("<x_type>CREDIT</x_type>");

			// need ccard number
			sx.append("<x_card_num>").append(pe.getCardnumber()).append(
					"</x_card_num>");// at least 4 last card digit
			sx.append("<x_exp_date>").append(pe.getExpiration()).append(
					"</x_exp_date>");
			sx.append("<x_amount>").append(pe.getTransactionAmount()).append(
					"</x_amount>");
			if (pe.getCvv() != null) {
				sx.append("<x_card_code>").append(pe.getCvv()).append(
						"</x_card_code>");
			}

			sx.append("<x_delim_data>TRUE</x_delim_data>");
			sx.append("<x_delim_char>|</x_delim_char>");
			sx.append("<x_relay_response>FALSE</x_relay_response>");
			sx.append("<x_trans_id>").append(trx.getTransactionID()).append(
					"</x_trans_id>");
			sx.append("</transaction>");

			HttpClient client = new HttpClient();

			httppost = new PostMethod(protocol + "://" + host + ":" + port
					+ url);
			RequestEntity entity = new StringRequestEntity(sb.toString(),
					"text/plain", "UTF-8");
			httppost.setRequestEntity(entity);

			String stringresult = null;

			int result = client.executeMethod(httppost);
			if (result != 200) {
				log.error("Communication Error with Authorizenet " + protocol
						+ "://" + host + ":" + port + url);
				throw new Exception("Communication Error with Authorizenet "
						+ protocol + "://" + host + ":" + port + url);
			}
			stringresult = httppost.getResponseBodyAsString();
			log.debug("AuthorizeNet response " + stringresult);

			StringBuffer appendresult = new StringBuffer().append(
					order.getTotal()).append("|").append(stringresult);

			// Format to xml
			String finalresult = this
					.readresponse("|", appendresult.toString());

			pe = new AuthorizeNetParsedElements();
			digester = new Digester();
			digester.push(pe);

			digester.addCallMethod("transaction/transactionid",
					"setTransactionId", 0);
			digester.addCallMethod("transaction/approvalcode",
					"setApprovalCode", 0);
			digester.addCallMethod("transaction/responsecode",
					"setResponseCode", 0);
			digester.addCallMethod("transaction/amount",
					"setTransactionAmount", 0);
			digester
					.addCallMethod("transaction/reasontext", "setReasontext", 0);
			digester
					.addCallMethod("transaction/reasoncode", "setReasonCode", 0);

			reader = new StringReader(finalresult);

			digester.parse(reader);

			pe.setTransactionId(trx.getTransactionDetails()
					.getMerchantPaymentGwOrderid());// BECAUSE NOT RETURNED FROM
													// AUTHORIZE NET !!!!

			return this.parseResponse(PaymentConstants.REFUND, sx.toString(),
					finalresult, pe, order, amount);

		} catch (Exception e) {
			if (e instanceof TransactionException) {
				throw (TransactionException) e;
			}
			log.error(e);

			TransactionException te = new TransactionException(
					"AuthorizeNet Gateway error ", e);
			te.setErrorcode("01");
			throw te;
		} finally {
			if (httppost != null) {
				httppost.releaseConnection();
			}
		}

	}

	private String readresponse(String pattern, String in) throws Exception {

		StringBuffer responsebuffer = new StringBuffer();
		responsebuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		responsebuffer.append("<transaction>");
		// responsebuffer.append("<amount>").append(order.getTotal()).append("</amount>");
		StringTokenizer st = new StringTokenizer(in, pattern);
		int i = 0;

		while (st.hasMoreTokens()) {
			String token = st.nextToken();

			if (i == 0) {
				responsebuffer.append("<amount>").append(token).append(
						"</amount>");
			}
			if (i == 1) {
				responsebuffer.append("<responsecode>").append(token).append(
						"</responsecode>");
			}
			if (i == 2) {
				responsebuffer.append("<responsesubcode>").append(token)
						.append("</responsesubcode>");
			}
			if (i == 3) {
				responsebuffer.append("<reasoncode>").append(token).append(
						"</reasoncode>");
			}
			if (i == 4) {
				responsebuffer.append("<reasontext>").append(token).append(
						"</reasontext>");
			}
			if (i == 5) {
				responsebuffer.append("<approvalcode>").append(token).append(
						"</approvalcode>");
			}
			if (i == 7) {
				responsebuffer.append("<transactionid>").append(token).append(
						"</transactionid>");
				break;
			}

			// $xmlresponse = '<?xml version="1.0"
			// encoding="UTF-8"?><transaction><amount>' .
			// number_format($order->info['total'], 2) .
			// '</amount><responsecode>' . $response[0] .
			// '</responsecode><responsesubcode>' . $response[1] .
			// '</responsesubcode><reasoncode>' . $response[2] .
			// '</reasoncode><reasontext>' . $response[3] .
			// '</reasontext><approvalcode>' . $response[4] .
			// '</approvalcode><transactionid>' . $response[6] .
			// '</transactionid><originalresponse><![CDATA[' . $response_list .
			// ']]></originalresponse></transaction>';

			i++;
		}

		responsebuffer.append("<originalresponse><![CDATA[").append(in).append(
				"]]></originalresponse>");
		responsebuffer.append("</transaction>");
		return responsebuffer.toString();
	}

	private GatewayTransactionVO parseResponse(int transactiontype,
			String request, String response,
			AuthorizeNetParsedElements elements, Order order, BigDecimal amount)
			throws Exception {
		MerchantPaymentGatewayTrx gtrx = null;
		// check if error

		if (elements.getResponseCode() == null) {
			log
					.debug("Can't process AuthorizeNet transaction, did not received a response code for orderid "
							+ order.getOrderId());
		}

		if (!elements.getResponseCode().equals("1")) {// 1 is aproved, 2 is
														// declined, 3 is error

			if (elements.getResponseCode().equals("3")) {
				log.info("Can't process AuthorizeNet message "
						+ elements.getReasontext());
				LogMerchantUtil.log(order.getMerchantId(),
						"Can't process AuthorizeNet, an error occured (code 3) "
								+ elements.getReasontext() + " code="
								+ elements.getReasonCode());
			}

			if (elements.getResponseCode().equals("2")) {
				LogMerchantUtil.log(order.getMerchantId(),
						"Can't process AuthorizeNet, the transaction was declined (code 2) "
								+ elements.getReasontext() + " code="
								+ elements.getReasonCode());
				log
						.debug("Can't process AuthorizeNet, an error occured (code 2) "
								+ elements.getReasontext());

			}

			TransactionException te = new TransactionException(
					"Can't process AuthorizeNet, message "
							+ elements.getReasontext() + " code="
							+ elements.getReasonCode());
			te.setErrorcode("02");
			te.setReason(elements.getReasontext());
			throw te;
			// End user should see the message
		}

		try {

			PaymentService pservice = (PaymentService) ServiceFactory
					.getService(ServiceFactory.PaymentService);

			gtrx = new MerchantPaymentGatewayTrx();
			gtrx.setMerchantId(order.getMerchantId());
			gtrx.setCustomerid(order.getCustomerId());
			gtrx.setOrderId(order.getOrderId());
			gtrx.setAmount(amount);
			gtrx.setMerchantPaymentGwMethod(order.getPaymentModuleCode());
			gtrx.setMerchantPaymentGwRespcode(elements.getResponseCode());
			gtrx.setMerchantPaymentGwOrderid(elements.getTransactionId());// created
																			// by
																			// sm
			gtrx.setMerchantPaymentGwTrxid(elements.getApprovalCode());// internal
																		// id
			gtrx.setMerchantPaymentGwAuthtype(String.valueOf(transactiontype));
			gtrx.setMerchantPaymentGwSession("");

			String cryptedvalue = EncryptionUtil.encrypt(EncryptionUtil
					.generatekey(String.valueOf(order.getMerchantId())),
					request);
			gtrx.setMerchantPaymentGwSent(cryptedvalue);
			gtrx.setMerchantPaymentGwReceived(response);
			gtrx.setDateAdded(new Date(new Date().getTime()));
			gtrx.setAmount(amount);

			pservice.saveMerchantPaymentGatewayTrx(gtrx);

		} catch (Exception e) {

			TransactionException te = new TransactionException(
					"Can't persist MerchantPaymentGatewayTrx internal id (orderid)"
							+ order.getOrderId(), e);
			te.setErrorcode("01");
			throw te;
		}

		GatewayTransactionVO vo = new GatewayTransactionVO();
		vo.setAmount(order.getTotal());
		vo.setCreditcard(order.getCardType());
		vo.setCreditcardtransaction(true);
		vo.setExpirydate(order.getCcExpires());
		vo.setInternalGatewayOrderId(elements.getApprovalCode());
		vo.setTransactionDetails(gtrx);
		return vo;
	}

	public List<SalesManagerTransactionVO> retreiveTransactions(int merchantid,
			Order order) throws Exception {

		long orderid = order.getOrderId();
		TransactionHelper trxhelper = new TransactionHelper();
		List trxs = trxhelper.getSentData(merchantid, orderid);

		if (trxs == null) {
			return null;
		}

		Iterator i = trxs.iterator();

		List returnlist = new ArrayList();

		while (i.hasNext()) {

			MerchantPaymentGatewayTrx trx = (MerchantPaymentGatewayTrx) i
					.next();

			AuthorizeNetParsedElements elems = new AuthorizeNetParsedElements();

			// $xmlresponse = '<?xml version="1.0"
			// encoding="UTF-8"?><transaction><responsecode>' . $response[0] .
			// '</responsecode><responsesubcode>' . $response[1] .
			// '</responsesubcode><reasoncode>' . $response[2] .
			// '</reasoncode><reasontext>' . $response[3] .
			// '</reasontext><approvalcode>' . $response[4] .
			// '</approvalcode><transactionid>' . $response[6] .
			// '</transactionid><transaction>';

			// we want to set
			// Response Sub Code
			// Approval Code

			Digester digester = new Digester();
			digester.push(elems);

			// <?xml version="1.0"
			// encoding="UTF-8"?><transaction><amount>58.00</amount><responsecode>1</responsecode><responsesubcode>1</responsesubcode><reasoncode>1</reasoncode><reasontext>(TESTMODE)
			// This transaction has been
			// approved.</reasontext><approvalcode>000000</approvalcode><transactionid>0</transactionid><originalresponse><![CDATA[1=1&2=1&3=1&4=%28TESTMODE%29+This+transaction+has+been+approved.&5=000000&6=P&7=0&8=43W2X558321209516732&9=Montreal+Canadian+T-Shirt%28qty%3A+1%29&10=58.00&11=CC&12=auth_only&13=43&14=dqwcqwdcqwdcqwc+AN+PA&15=dcqecwqedc&16=&17=368+Du+Languedoc&18=Boucherville&19=Quebec&20=j4b8j9&21=Canada&22=4445556666&23=&24=carl%40csticonsulting.com&25=dqwcqwdcqwdcqwc+AN+PA&26=dcqecwqedc&27=&28=368+Du+Languedoc&29=Boucherville&30=Quebec&31=j4b8j9&32=Canada&33=&34=&35=&36=&37=&38=44655B6E5FB3B458A37B93130899374E&39=&40=&41=&42=&43=&44=&45=&46=&47=&48=&49=&50=&51=&52=&53=&54=&55=&56=&57=&58=&59=&60=&61=&62=&63=&64=&65=&66=&67=&68=&69=April+29+2008+8%3A52+pm&70=127.0.0.1&71=cdd69664aea1f46998818d83f9b5a87a&]]></originalresponse></transaction>

			digester.addCallMethod("transaction/transactionid",
					"setTransactionId", 0);
			digester.addCallMethod("transaction/approvalcode",
					"setApprovalCode", 0);
			digester.addCallMethod("transaction/responsecode",
					"setResponseCode", 0);
			digester.addCallMethod("transaction/amount",
					"setTransactionAmount", 0);
			digester
					.addCallMethod("transaction/reasontext", "setReasontext", 0);

			Reader reader = new StringReader(trx.getMerchantPaymentGwReceived());

			digester.parse(reader);

			GatewayTransactionVO mtrx = new GatewayTransactionVO();

			mtrx.setOrderID(String.valueOf(orderid));
			mtrx.setInternalGatewayOrderId(elems.getApprovalCode());// returned
																	// by
																	// authorize
																	// net
			mtrx.setTransactionID(elems.getTransactionId());// created by sales
															// manager
			mtrx.setTransactionDetails(trx);
			mtrx.setType(Integer.parseInt(trx.getMerchantPaymentGwAuthtype()));
			mtrx.setAmount(new BigDecimal(elems.getTransactionAmount()));
			returnlist.add(mtrx);

		}
		return returnlist;
	}

	public ConfigurationResponse getConfiguration(
			MerchantConfiguration configurations, ConfigurationResponse vo) {

		try {

			String decryptedvalue = EncryptionUtil.decrypt(
					EncryptionUtil.generatekey(String.valueOf(configurations
							.getMerchantId())), configurations
							.getConfigurationValue());
			IntegrationKeys ik = ModuleManagerImpl
					.stripCredentials(decryptedvalue);
			vo.addConfiguration("keys", ik);

			IntegrationProperties props = ModuleManagerImpl
					.stripProperties(configurations.getConfigurationValue2());
			vo.addConfiguration("properties", props);

			vo.addConfiguration(PaymentConstants.PAYMENT_AUTHORIZENETNAME,
					configurations);
		} catch (Exception e) {
			log.error("Can't understand MerchantConfiguration"
					+ configurations.getConfigurationId());
		}

		vo.addMerchantConfiguration(configurations);
		return vo;
	}

	public void storeConfiguration(int merchantid, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {

		// Managed in PaymentauthorizenetAction (Central)


	}

}

class AuthorizeNetParsedElements {

	private String transactionId;
	private String approvalCode;
	private String transactionAmount;
	private String responseCode;
	private String reasontext;
	private String cardnumber;
	private String expiration;
	private String cvv;
	private String reasonCode;

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getReasontext() {
		return reasontext;
	}

	public void setReasontext(String reasontext) {
		this.reasontext = reasontext;
	}

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

}
