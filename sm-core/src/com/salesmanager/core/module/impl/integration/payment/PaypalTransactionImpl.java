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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.MerchantPaymentGatewayTrx;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.module.model.application.CurrencyModule;
import com.salesmanager.core.module.model.integration.CreditCardPaymentModule;
import com.salesmanager.core.module.model.integration.PaymentModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.impl.ModuleManagerImpl;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.payment.GatewayTransactionVO;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.payment.SalesManagerTransactionVO;
import com.salesmanager.core.service.payment.TransactionException;
import com.salesmanager.core.service.payment.impl.TransactionHelper;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.EncryptionUtil;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LogMerchantUtil;
import com.salesmanager.core.util.MerchantConfigurationUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.StringUtil;

/**
 * Papal Express Checkout implementation
 * 
 * @author Carl Samson
 * 
 */
public class PaypalTransactionImpl extends CreditCardGatewayTransactionImpl {

	private static Logger log = Logger.getLogger(PaypalTransactionImpl.class);
	private static Configuration conf = PropertiesUtil.getConfiguration();

	
	public GatewayTransactionVO authorizeTransaction(
			IntegrationKeys keys, IntegrationProperties properties,
			MerchantStore store, Order order, Customer customer, CoreModuleService cms) throws TransactionException {
		throw new TransactionException("Not implemented");
	}
	
	
	public GatewayTransactionVO authorizeAndCapture(
			IntegrationKeys keys, IntegrationProperties properties,
			MerchantStore store, Order order, Customer customer, CoreModuleService cms) throws TransactionException {
			throw new TransactionException("Not implemented");
	}
	
	//overwrite super method
	public SalesManagerTransactionVO processTransaction(CoreModuleService serviceDefinition,
			PaymentMethod paymentMethod, Order order, Customer customer)
			throws TransactionException {

		ConfigurationRequest vo = new ConfigurationRequest(order
				.getMerchantId());
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		IntegrationProperties properties;

		String nvpstr = "";

		try {

			ConfigurationResponse config = mservice.getConfigurationByModule(
					vo, PaymentConstants.PAYMENT_PAYPALNAME);
			if (config == null) {
				throw new TransactionException("Payment module "
						+ PaymentConstants.PAYMENT_PAYPALNAME
						+ " cannot be retreived from MerchantConfiguration");
			}
			properties = (IntegrationProperties) config
					.getConfiguration("properties");

		} catch (Exception e) {
			throw new TransactionException(e);
		}

		if (properties == null) {
			throw new TransactionException(
					"Integration properties are null for merchantId "
							+ order.getMerchantId());
		}

		/*
		 * '------------------------------------ ' The currencyCodeType and
		 * paymentType ' are set to the selections made on the Integration
		 * Assistant '------------------------------------
		 */



		String paymentType = paymentMethod.getInfo("PAYMENTTYPE");
		int paymentTypeCode = PaymentConstants.SALE;// Sale
		if (paymentType == null) {
			throw new TransactionException("Paypal paymentMethod is null");
		}

		if (paymentType.equals("Sale")) {// sale
			// here the code should be placed in authorizeAndCapture method
			order.setOrderStatus(OrderConstants.STATUSDELIVERED);
		} else {
			// here the code should be placed in authorize method
			paymentTypeCode = PaymentConstants.PREAUTH;// Authorization
			order.setOrderStatus(OrderConstants.STATUSPROCESSING);
		}

		/*
		 * '------------------------------------ ' The currencyCodeType and
		 * paymentType ' are set to the selections made on the Integration
		 * Assistant '------------------------------------
		 */
		String currencyCodeType = order.getCurrency();

		String token = (String) paymentMethod.getInfo("TOKEN");
		String payerID = (String) paymentMethod.getInfo("PAYERID");

		if (token == null) {
			throw new TransactionException(
					"processTransaction Paypal TOKEN is null in PaymentMethod");
		}

		if (payerID == null) {
			throw new TransactionException(
					"processTransaction Paypal PAYERID is null in PaymentMethod");
		}

		String amount = CurrencyUtil.displayFormatedAmountNoCurrency(order
				.getTotal(), Constants.CURRENCY_CODE_USD);
		if (order.getTotal().toString().equals(
				new BigDecimal("0.00").toString())) {
			// check if recuring
			if (order.getRecursiveAmount().floatValue() > 0) {
				amount = CurrencyUtil.displayFormatedAmountNoCurrency(order
						.getRecursiveAmount(), order.getCurrency());
			} else {
				amount = "0";
			}
		}

		try {

			// InetAddress addr = InetAddress.getLocalHost();

			// Get IP Address
			// byte[] ipAddr = addr.getAddress();

			// String ip = new String(ipAddr);

			// IPN
			// String ipnUrl = ReferenceUtil.buildSecureServerUrl() +
			// (String)conf.getString("core.salesmanager.checkout.paypalIpn");

			nvpstr = "&TOKEN=" + token + "&PAYERID=" + payerID
					+ "&PAYMENTACTION=" + paymentType + "&AMT=" + amount
					+ "&INVNUM=" + order.getOrderId();

			// nvpstr = nvpstr + "&CURRENCYCODE=" + currencyCodeType +
			// "&IPADDRESS=" + ip.toString();
			nvpstr = nvpstr + "&CURRENCYCODE=" + currencyCodeType;

			Map nvp = httpcall(properties, serviceDefinition,
					"DoExpressCheckoutPayment", nvpstr);
			String strAck = nvp.get("ACK").toString();
			
			GatewayTransactionVO rvo = new GatewayTransactionVO();
			rvo.setOrderID(String.valueOf(order.getOrderId()));
			
			if (strAck != null && strAck.equalsIgnoreCase("Success")) {
				// return nvp;
				// log transaction

				// transactionid TRANSACTIONID, CORRELATIONID
				String transactionId = (String) nvp.get("TRANSACTIONID");
				String correlationId = (String) nvp.get("CORRELATIONID");

				try {

					Iterator it = nvp.keySet().iterator();
					StringBuffer valueBuffer = new StringBuffer();
					while (it.hasNext()) {
						String key = (String) it.next();
						valueBuffer.append("[").append(key).append("=").append(
								(String) nvp.get(key)).append("]");
					}

					MerchantPaymentGatewayTrx gtrx = new MerchantPaymentGatewayTrx();
					gtrx.setMerchantId(order.getMerchantId());
					gtrx.setCustomerid(order.getCustomerId());
					gtrx.setOrderId(order.getOrderId());
					gtrx.setAmount(order.getTotal());
					gtrx.setMerchantPaymentGwMethod(order
							.getPaymentModuleCode());
					gtrx.setMerchantPaymentGwRespcode(strAck);
					gtrx.setMerchantPaymentGwOrderid(correlationId);// CORRELATIONID
					gtrx.setMerchantPaymentGwTrxid(transactionId);// TRANSACTIONID
					gtrx.setMerchantPaymentGwAuthtype(String
							.valueOf(paymentTypeCode));
					gtrx.setMerchantPaymentGwSession("");
					
					rvo.setTransactionID(transactionId);

					// String cryptedvalue =
					// EncryptionUtil.encrypt(EncryptionUtil.generatekey(String.valueOf(order.getMerchantId())),
					// nvpstr);
					gtrx.setMerchantPaymentGwSent(nvpstr);
					gtrx.setMerchantPaymentGwReceived(valueBuffer.toString());
					gtrx.setDateAdded(new Date(new Date().getTime()));
					
					rvo.setTransactionDetails(gtrx);

					PaymentService pservice = (PaymentService) ServiceFactory
							.getService(ServiceFactory.PaymentService);
					pservice.saveMerchantPaymentGatewayTrx(gtrx);

					// insert an off system gateway transaction
					// for IPN usage

					// OffsystemPendingOrder pending = new
					// OffsystemPendingOrder();
					// pending.setOrderId(order.getOrderId());
					// pending.setTransactionId(payerID);
					// pending.setPayerEmail(order.getCustomerEmailAddress());
					// pservice.saveOrUpdateOffsystemPendingOrder(pending,
					// order);

				} catch (Exception e) {
					TransactionException te = new TransactionException(
							"Can't persist MerchantPaymentGatewayTrx internal id (orderid)"
									+ order.getOrderId(), e);
					te.setErrorcode("01");
					throw te;
				}

			} else {

				String ErrorCode = nvp.get("L_ERRORCODE0").toString();
				String ErrorShortMsg = nvp.get("L_SHORTMESSAGE0").toString();
				String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
				String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0")
						.toString();

				TransactionException te = new TransactionException(
						"Paypal transaction refused " + ErrorLongMsg);
				te.setErrorType(ErrorConstants.PAYMENT_TRANSACTION_ERROR);
				LogMerchantUtil.log(order.getMerchantId(),
						"Paypal transaction error code[" + ErrorCode + "] "
								+ ErrorLongMsg);
				if (ErrorCode.equals("10415")) {// transaction already submited
					te = new TransactionException("Paypal transaction refused "
							+ ErrorLongMsg);
					te
							.setErrorType(ErrorConstants.PAYMENT_DUPLICATE_TRANSACTION);
				}

				throw te;
			}
			
			return rvo;


		} catch (Exception e) {
			if (e instanceof TransactionException) {
				throw (TransactionException) e;
			}
			throw new TransactionException(e);
		}

	}



	public Map httpcall(IntegrationProperties keys, CoreModuleService cms,
			String methodName, String nvpStr) throws Exception {

		// return null;

		boolean bSandbox = false;
		if (keys.getProperties5().equals("2")) {// sandbox
			bSandbox = true;
		}

		String gv_APIEndpoint = "";
		String PAYPAL_URL = "";
		String gv_Version = "3.3";

		if (bSandbox == true) {
			gv_APIEndpoint = "https://api-3t.sandbox.paypal.com/nvp";
			PAYPAL_URL = new StringBuffer().append(
					cms.getCoreModuleServiceDevProtocol()).append("://")
					.append(cms.getCoreModuleServiceDevDomain()).append(
							cms.getCoreModuleServiceDevEnv()).append(
							"?cmd=_express-checkout&token=").toString();
			// PAYPAL_URL =
			// "https://www.sandbox.paypal.com/webscr?cmd=_express-checkout&token=";
		} else {
			gv_APIEndpoint = "https://api-3t.paypal.com/nvp";
			PAYPAL_URL = new StringBuffer().append(
					cms.getCoreModuleServiceProdProtocol()).append("://")
					.append(cms.getCoreModuleServiceProdDomain()).append(
							cms.getCoreModuleServiceProdEnv()).append(
							"?cmd=_express-checkout&token=").toString();
			// PAYPAL_URL =
			// "https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=";
		}

		String agent = "Mozilla/4.0";
		String respText = "";
		Map nvp = null;

		// deformatNVP( nvpStr );
		String encodedData = "METHOD=" + methodName + "&VERSION=" + gv_Version
				+ "&PWD=" + keys.getProperties2() + "&USER="
				+ keys.getProperties1() + "&SIGNATURE=" + keys.getProperties3()
				+ nvpStr + "&BUTTONSOURCE=" + "PP-ECWizard";
		log.debug("REQUEST SENT TO PAYPAL -> " + encodedData);

		HttpURLConnection conn = null;
		DataOutputStream output = null;
		DataInputStream in = null;
		BufferedReader is = null;
		try {
			URL postURL = new URL(gv_APIEndpoint);
			conn = (HttpURLConnection) postURL.openConnection();

			// Set connection parameters. We need to perform input and output,
			// so set both as true.
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Set the content type we are POSTing. We impersonate it as
			// encoded form data
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", agent);

			// conn.setRequestProperty( "Content-Type", type );
			conn.setRequestProperty("Content-Length", String
					.valueOf(encodedData.length()));
			conn.setRequestMethod("POST");

			// get the output stream to POST to.
			output = new DataOutputStream(conn.getOutputStream());
			output.writeBytes(encodedData);
			output.flush();
			// output.close ();

			// Read input from the input stream.
			in = new DataInputStream(conn.getInputStream());
			int rc = conn.getResponseCode();
			if (rc != -1) {
				is = new BufferedReader(new InputStreamReader(conn
						.getInputStream()));
				String _line = null;
				while (((_line = is.readLine()) != null)) {
					log.debug("Response line from Paypal -> " + _line);
					respText = respText + _line;
				}
				nvp = StringUtil.deformatUrlResponse(respText);
			} else {
				throw new Exception("Invalid response from paypal, return code is " + rc);
			}

			nvp.put("PAYPAL_URL", PAYPAL_URL);
			nvp.put("NVP_URL", gv_APIEndpoint);

			return nvp;

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (output != null) {
				try {
					output.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}
		}
	}

	/**
	 * Marks SetExpressCheckout
	 */
	public Map<String, String> initTransaction(
			CoreModuleService serviceDefinition, Order order)
			throws TransactionException {

		ConfigurationRequest vo = new ConfigurationRequest(order
				.getMerchantId());
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		String paymentType = "Sale";

		IntegrationProperties properties;

		try {

			ConfigurationResponse config = mservice.getConfigurationByModule(
					vo, PaymentConstants.PAYMENT_PAYPALNAME);
			if (config == null) {
				throw new TransactionException("Payment module "
						+ PaymentConstants.PAYMENT_PAYPALNAME
						+ " cannot be retreived from MerchantConfiguration");
			}
			properties = (IntegrationProperties) config
					.getConfiguration("properties");

		} catch (Exception e) {
			throw new TransactionException(e);
		}

		if (properties == null) {
			throw new TransactionException(
					"Integration properties are null for merchantId "
							+ order.getMerchantId());
		}

		/*
		 * '------------------------------------ ' The currencyCodeType and
		 * paymentType ' are set to the selections made on the Integration
		 * Assistant '------------------------------------
		 */

		// @todo may support order
		if (properties.getProperties4().equals("1")) {
			paymentType = "Authorization";
		}

		/*
		 * Construct the parameter string that describes the PayPal payment the
		 * varialbes were set in the web form, and the resulting string is
		 * stored in $nvpstr
		 */

		String nvpstr = null;

		// always pass using USD format
		String amount = CurrencyUtil.displayFormatedAmountNoCurrency(order
				.getTotal(), Constants.CURRENCY_CODE_USD);
		if (order.getTotal().toString().equals(
				new BigDecimal("0.00").toString())) {
			// check if recuring
			if (order.getRecursiveAmount().floatValue() > 0) {
				amount = CurrencyUtil.displayFormatedAmountNoCurrency(order
						.getRecursiveAmount(), order.getCurrency());
			} else {
				amount = "0";
			}
		}

		Locale locale = order.getLocale();
		String sLocale = "US";// default to US english

		/**
		 * supports AU DE FR GB IT ES JP US
		 **/

		if (locale != null) {
			/*
			 * String language = locale.getLanguage(); int lang =
			 * LanguageUtil.getLanguageNumberCode(language);
			 * if(lang==Constants.FRENCH) { sLocale = "FR"; }
			 */
			String country = locale.getCountry();
			if ("AU".equals(country)) {
				sLocale = "AU";
			} else if ("DE".equals(country)) {
				sLocale = "DE";
			} else if ("FR".equals(country)) {
				sLocale = "FR";
			} else if ("GB".equals(country)) {
				sLocale = "GB";
			} else if ("IT".equals(country)) {
				sLocale = "IT";
			} else if ("ES".equals(country)) {
				sLocale = "ES";
			} else if ("JP".equals(country)) {
				sLocale = "JP";
			}
		}

		try {

			MerchantStore store = mservice.getMerchantStore(order
					.getMerchantId());
			
			
			String cancelUrl = "";

			String returnUrl = URLEncoder
					.encode(
							ReferenceUtil.buildCheckoutUri(store)
									+ (String) conf
											.getString("core.salesmanager.checkout.paypalReturnAction"),
							"UTF-8");
			
				   cancelUrl = URLEncoder.encode(ReferenceUtil
							.buildCheckoutShowCartUrl(store), "UTF-8");
					

			if (order.getChannel() == OrderConstants.INVOICE_CHANNEL) {
				returnUrl = URLEncoder
						.encode(
								ReferenceUtil.buildCheckoutUri(store)
										+ (String) conf
												.getString("core.salesmanager.checkout.paypalInvoiceReturnAction"),
								"UTF-8");
				
				
				
				CustomerService customerService = (CustomerService)ServiceFactory.getService(ServiceFactory.CustomerService);
				Customer customer = customerService.getCustomer(order.getCustomerId());
				

				
				cancelUrl = URLEncoder.encode(new StringBuilder().append(FileUtil.getInvoiceUrl(order, customer)).append(customer.getCustomerLang()).append("_")
						.append(locale.getCountry()).toString(), "UTF-8");
				
			}

			nvpstr = "&METHOD=SetExpressCheckout&Amt="
					+ amount
					+ "&PAYMENTACTION="
					+ paymentType
					+ "&LOCALECODE="
					+ sLocale
					+ "&ReturnUrl="
					+ returnUrl
					+ "&CANCELURL="
					+ cancelUrl
					+ "&CURRENCYCODE=" + order.getCurrency();
		} catch (Exception e) {
			log.error(e);
			throw new TransactionException(e);
		}

		/*
		 * Make the call to PayPal to get the Express Checkout token If the API
		 * call succeded, then redirect the buyer to PayPal to begin to
		 * authorize payment. If an error occured, show the resulting errors
		 */

		Map nvp = null;

		try {
			nvp = httpcall(properties, serviceDefinition, "SetExpressCheckout",
					nvpstr);
		} catch (Exception e) {
			log.error(e);
			throw new TransactionException(e);
		}

		String strAck = nvp.get("ACK").toString();
		if (strAck != null && strAck.equalsIgnoreCase("Success")) {
			nvp.put("PAYMENTTYPE", paymentType);
			return nvp;
		} else {
			LogMerchantUtil.log(order.getMerchantId(), (String) nvp
					.get("L_LONGMESSAGE0"));
			log.error("Error with paypal transaction "
					+ (String) nvp.get("L_LONGMESSAGE0"));
			return null;
		}

	}

	public Map getShippingDetails(String token, Order order,
			CoreModuleService cms) throws Exception {

		ConfigurationRequest vo = new ConfigurationRequest(order
				.getMerchantId());
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		IntegrationProperties properties;

		try {

			ConfigurationResponse config = mservice.getConfigurationByModule(
					vo, PaymentConstants.PAYMENT_PAYPALNAME);
			if (config == null) {
				throw new TransactionException("Payment module "
						+ PaymentConstants.PAYMENT_PAYPALNAME
						+ " cannot be retreived from MerchantConfiguration");
			}
			properties = (IntegrationProperties) config
					.getConfiguration("properties");

		} catch (Exception e) {
			throw new TransactionException(e);
		}

		Map nvp = null;

		String nvpstr = "&TOKEN=" + token;

		try {
			nvp = httpcall(properties, cms, "GetExpressCheckoutDetails", nvpstr);
		} catch (Exception e) {
			log.error(e);
			throw new TransactionException(e);
		}

		String strAck = nvp.get("ACK").toString();
		if (strAck != null && strAck.equalsIgnoreCase("Success")
				|| strAck.equalsIgnoreCase("SuccessWithWarning")) {
			return nvp;
		} else {
			String ErrorCode = nvp.get("L_ERRORCODE0").toString();
			String ErrorShortMsg = nvp.get("L_SHORTMESSAGE0").toString();
			String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
			String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0").toString();

			log.error("Paypal address error code " + ErrorCode
					+ " description " + ErrorLongMsg);
			return null;
		}

	}

	public Order postTransaction(Order order) throws TransactionException {
		return null;
	}

	/** This can be invoked after a pre-authorize transaction **/
	//public GatewayTransactionVO captureTransaction(MerchantStore store,
	//		Order order) throws TransactionException {
		
	public GatewayTransactionVO captureTransaction(
				IntegrationKeys keys, IntegrationProperties properties,
				MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService serviceDefinition) throws TransactionException {

		try {

			/*
			 * '------------------------------------ ' The currencyCodeType and
			 * paymentType ' are set to the selections made on the Integration
			 * Assistant '------------------------------------
			 */


			if (serviceDefinition == null) {
				// throw new
				// Exception("Central integration services not configured for "
				// + PaymentConstants.PAYMENT_LINKPOINTNAME + " and country id "
				// + origincountryid);
				log.error("Central integration services not configured for "
						+ PaymentConstants.PAYMENT_PAYPALNAME
						+ " and country id " + store.getCountry());
				TransactionException te = new TransactionException(
						"Central integration services not configured for "
								+ PaymentConstants.PAYMENT_PAYPALNAME
								+ " and country id " + store.getCountry());
				te.setErrorcode("01");
				throw te;
			}

			String authorizationId = trx.getTransactionID();

			if (authorizationId == null) {
				throw new TransactionException(
						"capture Paypal authorizationId is null in GatewayTransaction");
			}

			String amount = CurrencyUtil.displayFormatedAmountNoCurrency(trx
					.getAmount(), Constants.CURRENCY_CODE_USD);

			// InetAddress addr = InetAddress.getLocalHost();

			// Get IP Address
			// byte[] ipAddr = addr.getAddress();

			// String ip = new String(ipAddr);

			// REQUEST
			// requiredSecurityParameters]&METHOD=DoCapture&AUTHORIZATIONID=01987219673867
			// &AMT=99.12&COMPLETETYPE=Complete

			// IPN


			// String nvpstr = "&AUTHORIZATIONID=" + authorizationId + "&AMT=" +
			// amount + "&COMPLETETYPE=Complete&IPADDRESS=" + ip.toString();
			String nvpstr = "&AUTHORIZATIONID=" + authorizationId + "&AMT="
					+ amount + "&CURRENCYCODE=" + order.getCurrency() + "&COMPLETETYPE=Complete";

			Map nvp = httpcall(properties, serviceDefinition, "DoCapture", nvpstr);
			String strAck = nvp.get("ACK").toString();
			if (strAck != null && strAck.equalsIgnoreCase("Success")) {

				/**
				 * RESPONSE
				 * [successResponseFields]&AUTHORIZATIONID=01987219673867
				 * &TRANSACTIONID
				 * =7JZ9679864YA2699519&PARENTTRANSACTIONID=01987219673867
				 * &RECEIPTID
				 * =5151-0525-2028-5336&TRANSACTIONTYPE=express-checkout
				 * &PAYMENTTYPE=instant&ORDERTIME=2006-08-15T17:31:38Z&AMT=99.12
				 * &CURRENCYCODE=USD&FEEAMT=3.29&TAXAMT=0.00&PAYMENTSTATUS=
				 * Completed &PENDINGREASON=None&REASONCODE=None
				 **/

				String responseTransactionId = (String) nvp
						.get("TRANSACTIONID");
				String responseAuthorizationId = (String) nvp
						.get("AUTHORIZATIONID");

				try {

					Iterator it = nvp.keySet().iterator();
					StringBuffer valueBuffer = new StringBuffer();
					while (it.hasNext()) {
						String key = (String) it.next();
						valueBuffer.append("[").append(key).append("=").append(
								(String) nvp.get(key)).append("]");
					}

					MerchantPaymentGatewayTrx gtrx = new MerchantPaymentGatewayTrx();
					gtrx.setMerchantId(order.getMerchantId());
					gtrx.setCustomerid(order.getCustomerId());
					gtrx.setAmount(order.getTotal());
					gtrx.setOrderId(order.getOrderId());
					gtrx.setMerchantPaymentGwMethod(order
							.getPaymentModuleCode());
					gtrx.setMerchantPaymentGwRespcode(strAck);
					gtrx.setMerchantPaymentGwOrderid(responseAuthorizationId);// AUTHORIZATIONID
					gtrx.setMerchantPaymentGwTrxid(responseTransactionId);// TRANSACTIONID
					gtrx.setMerchantPaymentGwAuthtype(String
							.valueOf(PaymentConstants.CAPTURE));
					gtrx.setMerchantPaymentGwSession("");

					// String cryptedvalue =
					// EncryptionUtil.encrypt(EncryptionUtil.generatekey(String.valueOf(order.getMerchantId())),
					// nvpstr);
					gtrx.setMerchantPaymentGwSent(nvpstr);
					gtrx.setMerchantPaymentGwReceived(valueBuffer.toString());
					gtrx.setDateAdded(new Date(new Date().getTime()));

					PaymentService pservice = (PaymentService) ServiceFactory
							.getService(ServiceFactory.PaymentService);
					pservice.saveMerchantPaymentGatewayTrx(gtrx);

					GatewayTransactionVO returnVo = new GatewayTransactionVO();
					returnVo.setAmount(order.getTotal());
					returnVo.setCreditcard("");
					returnVo.setCreditcardtransaction(false);
					returnVo.setExpirydate("");
					returnVo.setInternalGatewayOrderId(responseTransactionId);
					returnVo.setTransactionDetails(gtrx);
					returnVo.setOrderID(String.valueOf(order.getOrderId()));
					
					returnVo.setTransactionID(responseTransactionId);
					
					returnVo.setTransactionMessage((String) nvp.get("RESPONSE"));
					
					return returnVo;

					// insert an off system gateway transaction
					// for IPN usage

					// OffsystemPendingOrder pending = new
					// OffsystemPendingOrder();
					// pending.setOrderId(order.getOrderId());
					// pending.setTransactionId(payerID);
					// pending.setPayerEmail(order.getCustomerEmailAddress());
					// pservice.saveOrUpdateOffsystemPendingOrder(pending,
					// order);

				} catch (Exception e) {
					TransactionException te = new TransactionException(
							"Can't persist MerchantPaymentGatewayTrx internal id (orderid)"
									+ order.getOrderId(), e);
					te.setErrorcode("01");
					throw te;
				}

			} else {

				String ErrorCode = nvp.get("L_ERRORCODE0").toString();
				String ErrorShortMsg = nvp.get("L_SHORTMESSAGE0").toString();
				String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
				String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0")
						.toString();

				TransactionException te = new TransactionException(
						"Paypal transaction refused " + ErrorLongMsg);
				te.setErrorType(ErrorConstants.PAYMENT_TRANSACTION_ERROR);
				LogMerchantUtil.log(order.getMerchantId(),
						"Paypal transaction error code[" + ErrorCode + "] "
								+ ErrorLongMsg);
				if (ErrorCode.equals("10415")) {// transaction already submited
					te = new TransactionException("Paypal transaction refused "
							+ ErrorLongMsg);
					te
							.setErrorType(ErrorConstants.PAYMENT_DUPLICATE_TRANSACTION);
				}

				throw te;
			}

		} catch (Exception e) {
			throw new TransactionException(e);
		}

	}

	/** This can be invoked after a sale transaction **/
	//public GatewayTransactionVO refundTransaction(MerchantStore store,
	//		Order order, BigDecimal amnt) throws TransactionException {
		
		
		
	public GatewayTransactionVO refundTransaction(
				IntegrationKeys keys, IntegrationProperties properties,
				MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService serviceDefinition, BigDecimal amnt) throws TransactionException {
		// TODO Auto-generated method stub

		try {

			/*
			 * '------------------------------------ ' The currencyCodeType and
			 * paymentType ' are set to the selections made on the Integration
			 * Assistant '------------------------------------
			 */

			if (serviceDefinition == null) {
				// throw new
				// Exception("Central integration services not configured for "
				// + PaymentConstants.PAYMENT_LINKPOINTNAME + " and country id "
				// + origincountryid);
				log.error("Central integration services not configured for "
						+ PaymentConstants.PAYMENT_PAYPALNAME
						+ " and country id " + store.getCountry());
				TransactionException te = new TransactionException(
						"Central integration services not configured for "
								+ PaymentConstants.PAYMENT_PAYPALNAME
								+ " and country id " + store.getCountry());
				te.setErrorcode("01");
				throw te;
			}

			String transactionId = trx.getTransactionID();

			if (transactionId == null) {
				throw new TransactionException(
						"capture Paypal authorizationId is null in GatewayTransaction");
			}

			String amount = CurrencyUtil.displayFormatedAmountNoCurrency(amnt,
					Constants.CURRENCY_CODE_USD);

			// InetAddress addr = InetAddress.getLocalHost();

			// Get IP Address
			// byte[] ipAddr = addr.getAddress();

			// String ip = new String(ipAddr);

			// REQUEST
			// requiredSecurityParameters]&METHOD=RefundTransaction&AUTHORIZATIONID=
			// &AMT=99.12&REFUNDTYPE=Full|Partial

			// IPN
			// String ipnUrl = ReferenceUtil.buildSecureServerUrl() +
			// (String)conf.getString("core.salesmanager.checkout.paypalIpn");

			String refundType = "Full";
			boolean partial = false;
			if (amnt.doubleValue() < order.getTotal().doubleValue()) {
				partial = true;
			}
			if (partial) {
				refundType = "Partial";
			}

			// String nvpstr = "&TRANSACTIONID=" + transactionId +
			// "&REFUNDTYPE=" + refundType + "&IPADDRESS=" + ip.toString();
			String nvpstr = "&TRANSACTIONID=" + transactionId + "&REFUNDTYPE="
					+ refundType + "&CURRENCYCODE=" + order.getCurrency() + "&IPADDRESS=";

			if (amnt.floatValue() < order.getTotal().floatValue()) {
				partial = true;
			}

			if (partial) {
				nvpstr = nvpstr + "&AMT=" + amount + "&NOTE=Partial refund";
			}

			Map nvp = httpcall(properties, serviceDefinition, "RefundTransaction",
					nvpstr);
			String strAck = nvp.get("ACK").toString();
			if (strAck != null && strAck.equalsIgnoreCase("Success")) {

				/**
				 * RESPONSE
				 * [successResponseFields]&AUTHORIZATIONID=
				 * &TRANSACTIONID
				 * =&PARENTTRANSACTIONID=
				 * &RECEIPTID
				 * =&TRANSACTIONTYPE=express-checkout
				 * &PAYMENTTYPE=instant&ORDERTIME=2006-08-15T17:31:38Z&AMT=99.12
				 * &CURRENCYCODE=USD&FEEAMT=3.29&TAXAMT=0.00&PAYMENTSTATUS=
				 * Completed &PENDINGREASON=None&REASONCODE=None
				 **/

				String responseTransactionId = (String) nvp
						.get("REFUNDTRANSACTIONID");
				String responseAuthorizationId = (String) nvp
						.get("REFUNDTRANSACTIONID");

				try {

					Iterator it = nvp.keySet().iterator();
					StringBuffer valueBuffer = new StringBuffer();
					while (it.hasNext()) {
						String key = (String) it.next();
						valueBuffer.append("[").append(key).append("=").append(
								(String) nvp.get(key)).append("]");
					}

					MerchantPaymentGatewayTrx gtrx = new MerchantPaymentGatewayTrx();
					gtrx.setMerchantId(order.getMerchantId());
					gtrx.setCustomerid(order.getCustomerId());
					gtrx.setOrderId(order.getOrderId());
					gtrx.setAmount(amnt);
					gtrx.setMerchantPaymentGwMethod(order
							.getPaymentModuleCode());
					gtrx.setMerchantPaymentGwRespcode(strAck);
					gtrx.setMerchantPaymentGwOrderid(responseAuthorizationId);// AUTHORIZATIONID
					gtrx.setMerchantPaymentGwTrxid(responseTransactionId);// TRANSACTIONID
					gtrx.setMerchantPaymentGwAuthtype(String
							.valueOf(PaymentConstants.REFUND));
					gtrx.setMerchantPaymentGwSession("");

					// String cryptedvalue =
					// EncryptionUtil.encrypt(EncryptionUtil.generatekey(String.valueOf(order.getMerchantId())),
					// nvpstr);
					gtrx.setMerchantPaymentGwSent(nvpstr);
					gtrx.setMerchantPaymentGwReceived(valueBuffer.toString());
					gtrx.setDateAdded(new Date(new Date().getTime()));

					PaymentService pservice = (PaymentService) ServiceFactory
							.getService(ServiceFactory.PaymentService);
					pservice.saveMerchantPaymentGatewayTrx(gtrx);

					GatewayTransactionVO returnVo = new GatewayTransactionVO();
					returnVo.setAmount(order.getTotal());
					returnVo.setCreditcard("");
					returnVo.setCreditcardtransaction(false);
					returnVo.setExpirydate("");
					returnVo.setInternalGatewayOrderId(responseTransactionId);
					returnVo.setTransactionDetails(gtrx);
					returnVo.setOrderID(String.valueOf(order.getOrderId()));
					returnVo.setTransactionID(responseTransactionId);
					
					returnVo.setTransactionMessage((String) nvp.get("RESPONSE"));
					
					return returnVo;

					// insert an off system gateway transaction
					// for IPN usage

					// OffsystemPendingOrder pending = new
					// OffsystemPendingOrder();
					// pending.setOrderId(order.getOrderId());
					// pending.setTransactionId(payerID);
					// pending.setPayerEmail(order.getCustomerEmailAddress());
					// pservice.saveOrUpdateOffsystemPendingOrder(pending,
					// order);

				} catch (Exception e) {
					TransactionException te = new TransactionException(
							"Can't persist MerchantPaymentGatewayTrx internal id (orderid)"
									+ order.getOrderId(), e);
					te.setErrorcode("01");
					throw te;
				}

			} else {

				String ErrorCode = nvp.get("L_ERRORCODE0").toString();
				String ErrorShortMsg = nvp.get("L_SHORTMESSAGE0").toString();
				String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
				String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0")
						.toString();

				TransactionException te = new TransactionException(
						"Paypal transaction refused " + ErrorLongMsg);
				te.setErrorType(ErrorConstants.PAYMENT_TRANSACTION_ERROR);
				LogMerchantUtil.log(order.getMerchantId(),
						"Paypal transaction error code[" + ErrorCode + "] "
								+ ErrorLongMsg);
				if (ErrorCode.equals("10415")) {// transaction already submited
					te = new TransactionException("Paypal transaction refused "
							+ ErrorLongMsg);
					te
							.setErrorType(ErrorConstants.PAYMENT_DUPLICATE_TRANSACTION);
				}

				throw te;
			}

		} catch (Exception e) {
			if (e instanceof TransactionException) {
				throw (TransactionException) e;
			}
			throw new TransactionException(e);
		}

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

			String nvpString = trx.getGatewaySentDecrypted();
			Map nvp = StringUtil.deformatUrlResponse(nvpString);

			CurrencyModule module = (CurrencyModule) SpringUtil.getBean(order
					.getCurrency());

			BigDecimal amount = module.getAmount((String) nvp.get("AMT"));

			// digester.parse(reader);

			GatewayTransactionVO mtrx = new GatewayTransactionVO();

			mtrx.setOrderID(String.valueOf(orderid));
			mtrx.setInternalGatewayOrderId(trx.getMerchantPaymentGwOrderid());// correlation
																				// id
			mtrx.setTransactionID(trx.getMerchantPaymentGwTrxid());// transaction
																	// id
			mtrx.setTransactionDetails(trx);
			mtrx.setType(Integer.parseInt(trx.getMerchantPaymentGwAuthtype()));
			// mtrx.setAmount(new BigDecimal((String)nvp.get("AMT")));
			mtrx.setAmount(amount);
			returnlist.add(mtrx);

		}
		return returnlist;
	}

	public ConfigurationResponse getConfiguration(
			MerchantConfiguration configurations, ConfigurationResponse vo)
			throws Exception {

		if (vo == null) {
			vo = new ConfigurationResponse();
		}
		if (configurations == null)
			return vo;

		vo
				.addConfiguration(PaymentConstants.PAYMENT_PAYPALNAME,
						configurations);

		IntegrationProperties keys = MerchantConfigurationUtil
				.getIntegrationProperties(configurations
						.getConfigurationValue(), "|");
		keys.setProperties5(configurations.getConfigurationValue1());// environment
		
		
		vo.addConfiguration(PaymentConstants.PAYMENT_PAYPALNAME,
				configurations);
		
		/**
		 * IntegrationProperties keys = new IntegrationProperties();
		 * keys.setProperties1(;//userid keys.setProperties2(;//password
		 * keys.setProperties3(;//signature keys.setProperties4(;//authorization
		 * sale keys.setProperties5(;//environment
		 **/

		vo.addConfiguration("properties", keys);



		return vo;
	}

	public void storeConfiguration(int merchantid, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {
		// Done in PaymentpaypalAction

	}

}
