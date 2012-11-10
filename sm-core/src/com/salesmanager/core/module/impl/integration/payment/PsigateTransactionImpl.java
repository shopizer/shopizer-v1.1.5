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
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.EncryptionUtil;
import com.salesmanager.core.util.LogMerchantUtil;

public class PsigateTransactionImpl extends CreditCardGatewayTransactionImpl {

	private Logger log = Logger.getLogger(PsigateTransactionImpl.class);

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
		return makeTransaction("1", keys, properties, store, order, customer, cms);
	}

	public GatewayTransactionVO authorizeAndCapture(IntegrationKeys keys,
			IntegrationProperties properties, MerchantStore store, Order order, Customer customer, CoreModuleService cms)
			throws TransactionException {
		return makeTransaction("0", keys, properties, store, order, customer, cms);
	}

	private GatewayTransactionVO makeTransaction(String type,
			IntegrationKeys ik, IntegrationProperties props,
			MerchantStore store, Order order, Customer customer, CoreModuleService cis) throws TransactionException {

		PostMethod httppost = null;

		try {



			// determine production - test environment
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

			HttpClient client = new HttpClient();

			String xml = "<?xml version=\"1.0\"?><AddressValidationRequest xml:lang=\"en-US\"><Request><TransactionReference><CustomerContext>SalesManager Data</CustomerContext><XpciVersion>1.0001</XpciVersion></TransactionReference><RequestAction>AV</RequestAction></Request>";
			StringBuffer xmldatabuffer = new StringBuffer();
			xmldatabuffer.append("<Order>");
			xmldatabuffer.append("<StoreID>").append(ik.getUserid()).append(
					"</StoreID>");
			xmldatabuffer.append("<Passphrase>").append(ik.getTransactionKey())
					.append("</Passphrase>");
			xmldatabuffer.append("<PaymentType>").append("CC").append(
					"</PaymentType>");
			xmldatabuffer.append("<Subtotal>").append(
					order.getTotal().toString()).append("</Subtotal>");

			// 0=Sale, 1=PreAuth, 2=PostAuth, 3=Credit, 4=Forced PostAuth

			xmldatabuffer.append("<CardAction>").append(type).append(
					"</CardAction>");

			xmldatabuffer.append("<CardNumber>").append(order.getCcNumber())
					.append("</CardNumber>");
			xmldatabuffer.append("<CardExpMonth>").append(
					order.getCcExpires().substring(0, 2)).append(
					"</CardExpMonth>");
			xmldatabuffer.append("<CardExpYear>").append(
					order.getCcExpires().substring(2,
							order.getCcExpires().length())).append(
					"</CardExpYear>");
			// CVV
			if (props.getProperties3().equals("2")) {
				xmldatabuffer.append("<CustomerIP>").append(
						order.getIpAddress()).append("</CustomerIP>");
				xmldatabuffer.append("<CardIDNumber>").append("").append(
						"</CardIDNumber>");
			}
			xmldatabuffer.append("</Order>");
			
			
			
			/** log data **/
			StringBuffer xmllogbuffer = new StringBuffer();
			xmllogbuffer.append("<Order>");
			xmllogbuffer.append("<StoreID>").append(ik.getUserid()).append(
					"</StoreID>");
			xmllogbuffer.append("<Passphrase>").append(ik.getTransactionKey())
					.append("</Passphrase>");
			xmllogbuffer.append("<PaymentType>").append("CC").append(
					"</PaymentType>");
			xmllogbuffer.append("<Subtotal>").append(
					order.getTotal().toString()).append("</Subtotal>");
			xmllogbuffer.append("<CardAction>").append(type).append(
					"</CardAction>");
			xmllogbuffer.append("<CardNumber>").append(CreditCardUtil.maskCardNumber(order.getCcNumber()))
					.append("</CardNumber>");
			xmllogbuffer.append("<CardExpMonth>").append(
					order.getCcExpires().substring(0, 2)).append(
					"</CardExpMonth>");
			xmllogbuffer.append("<CardExpYear>").append(
					order.getCcExpires().substring(2,
							order.getCcExpires().length())).append(
					"</CardExpYear>");
			if (props.getProperties3().equals("2")) {
				xmllogbuffer.append("<CustomerIP>").append(
						order.getIpAddress()).append("</CustomerIP>");
				xmllogbuffer.append("<CardIDNumber>").append("").append(
						"</CardIDNumber>");
			}
			xmllogbuffer.append("</Order>");
			
			log.debug("Psigate request " + xmllogbuffer.toString());
			
			/** log **/

			

			httppost = new PostMethod(protocol + "://" + host + ":" + port
					+ url);
			RequestEntity entity = new StringRequestEntity(xmldatabuffer
					.toString(), "text/plain", "UTF-8");
			httppost.setRequestEntity(entity);

			PsigateParsedElements pe = null;
			String stringresult = null;

			int result = client.executeMethod(httppost);
			if (result != 200) {
				log.error("Communication Error with psigate " + protocol
						+ "://" + host + ":" + port + url);
				throw new Exception("Communication Error with psigate "
						+ protocol + "://" + host + ":" + port + url);
			}
			stringresult = httppost.getResponseBodyAsString();
			log.debug("Psigate response " + stringresult);

			pe = new PsigateParsedElements();
			Digester digester = new Digester();
			digester.push(pe);

			digester.addCallMethod("Result/OrderID", "setOrderID", 0);
			digester.addCallMethod("Result/Approved", "setApproved", 0);
			digester.addCallMethod("Result/ErrMsg", "setErrMsg", 0);
			digester.addCallMethod("Result/ReturnCode", "setReturnCode", 0);
			digester.addCallMethod("Result/TransRefNumber",
					"setTransRefNumber", 0);
			digester.addCallMethod("Result/CardType", "setCardType", 0);

			Reader reader = new StringReader(stringresult);

			digester.parse(reader);

			if (type.equals("0")) {

				return this.parseResponse(PaymentConstants.CAPTURE,
						xmldatabuffer.toString(), stringresult, pe, order,
						order.getTotal());

			} else {

				return this.parseResponse(PaymentConstants.PREAUTH,
						xmldatabuffer.toString(), stringresult, pe, order,
						order.getTotal());

			}

		} catch (Exception e) {
			if (e instanceof TransactionException) {
				throw (TransactionException) e;
			}
			log.error(e);
			TransactionException te = new TransactionException(
					"Psigate Gateway error ", e);
			te.setErrorcode("01");
			throw te;
		} finally {
			if (httppost != null)
				httppost.releaseConnection();
		}

	}

	public GatewayTransactionVO captureTransaction(IntegrationKeys ik, IntegrationProperties props,
			MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService cis) throws TransactionException {

		// Get capturable transaction
		PostMethod httppost = null;

		try {


			// determine production - test environment

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

			// Protocol easyhttps = new Protocol("https", new
			// EasySSLProtocolSocketFactory(), 443);
			HttpClient client = new HttpClient();

			String xml = "<?xml version=\"1.0\"?><AddressValidationRequest xml:lang=\"en-US\"><Request><TransactionReference><CustomerContext>SalesManager Data</CustomerContext><XpciVersion>1.0001</XpciVersion></TransactionReference><RequestAction>AV</RequestAction></Request>";
			StringBuffer xmldatabuffer = new StringBuffer();
			xmldatabuffer.append("<Order>");
			xmldatabuffer.append("<StoreID>").append(ik.getUserid()).append(
					"</StoreID>");
			xmldatabuffer.append("<Passphrase>").append(ik.getTransactionKey())
					.append("</Passphrase>");
			xmldatabuffer.append("<PaymentType>").append("CC").append(
					"</PaymentType>");

			// 0=Sale, 1=PreAuth, 2=PostAuth, 3=Credit, 4=Forced PostAuth

			xmldatabuffer.append("<CardAction>").append("2").append(
					"</CardAction>");
			// For postauth only
			xmldatabuffer.append("<OrderID>").append(
					trx.getInternalGatewayOrderId()).append("</OrderID>");
			xmldatabuffer.append("</Order>");
			/**
			 * xmldatabuffer.append("<CardNumber>").append("").append(
			 * "</CardNumber>");
			 * xmldatabuffer.append("<CardExpMonth>").append(""
			 * ).append("</CardExpMonth>");
			 * xmldatabuffer.append("<CardExpYear>")
			 * .append("").append("</CardExpYear>"); //CVV
			 * xmldatabuffer.append("<CustomerIP>"
			 * ).append("").append("</CustomerIP>");
			 * xmldatabuffer.append("<CardIDNumber>"
			 * ).append("").append("</CardIDNumber>");
			 * xmldatabuffer.append("</Order>");
			 **/

			log.debug("Psigate request " + xmldatabuffer.toString());

			httppost = new PostMethod(protocol + "://" + host + ":" + port
					+ url);
			RequestEntity entity = new StringRequestEntity(xmldatabuffer
					.toString(), "text/plain", "UTF-8");
			httppost.setRequestEntity(entity);

			PsigateParsedElements pe = null;
			String stringresult = null;

			int result = client.executeMethod(httppost);
			if (result != 200) {
				log.error("Communication Error with psigate " + protocol
						+ "://" + host + ":" + port + url);
				throw new Exception("Communication Error with psigate "
						+ protocol + "://" + host + ":" + port + url);
			}
			stringresult = httppost.getResponseBodyAsString();
			log.debug("Psigate response " + stringresult);

			pe = new PsigateParsedElements();
			Digester digester = new Digester();
			digester.push(pe);

			digester.addCallMethod("Result/OrderID", "setOrderID", 0);
			digester.addCallMethod("Result/Approved", "setApproved", 0);
			digester.addCallMethod("Result/ErrMsg", "setErrMsg", 0);
			digester.addCallMethod("Result/ReturnCode", "setReturnCode", 0);
			digester.addCallMethod("Result/TransRefNumber",
					"setTransRefNumber", 0);
			digester.addCallMethod("Result/CardType", "setCardType", 0);

			Reader reader = new StringReader(stringresult);

			digester.parse(reader);

			return this.parseResponse(PaymentConstants.CAPTURE, xmldatabuffer
					.toString(), stringresult, pe, order, order.getTotal());

		} catch (Exception e) {
			if (e instanceof TransactionException) {
				throw (TransactionException) e;
			}
			log.error(e);
			TransactionException te = new TransactionException(
					"Psigate Gateway error ", e);
			te.setErrorcode("01");
			throw te;
		} finally {
			if (httppost != null)
				httppost.releaseConnection();
		}

	}

	public GatewayTransactionVO refundTransaction(IntegrationKeys keys, IntegrationProperties props,
			MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService cis, BigDecimal amount) throws TransactionException {

		// Get refundable transaction

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

			// String total = CurrencyUtil.getAmount(order.getTotal(),
			// order.getCurrency());
			String total = CurrencyUtil.getAmount(amount, order.getCurrency());

			HttpClient client = new HttpClient();


			String xml = "<?xml version=\"1.0\"?><AddressValidationRequest xml:lang=\"en-US\"><Request><TransactionReference><CustomerContext>SalesManager Data</CustomerContext><XpciVersion>1.0001</XpciVersion></TransactionReference><RequestAction>AV</RequestAction></Request>";
			StringBuffer xmldatabuffer = new StringBuffer();
			xmldatabuffer.append("<Order>");
			xmldatabuffer.append("<StoreID>").append(keys.getUserid()).append(
					"</StoreID>");
			xmldatabuffer.append("<Passphrase>").append(keys.getTransactionKey())
					.append("</Passphrase>");
			xmldatabuffer.append("<PaymentType>").append("CC").append(
					"</PaymentType>");

			// 0=Sale, 1=PreAuth, 2=PostAuth, 3=Credit, 4=Forced PostAuth

			xmldatabuffer.append("<CardAction>").append("3").append(
					"</CardAction>");
			// For postauth only
			xmldatabuffer.append("<OrderID>").append(
					trx.getInternalGatewayOrderId()).append("</OrderID>");
			xmldatabuffer.append("<SubTotal>").append(total).append(
					"</SubTotal>");
			xmldatabuffer.append("</Order>");
			/**
			 * xmldatabuffer.append("<CardNumber>").append("").append(
			 * "</CardNumber>");
			 * xmldatabuffer.append("<CardExpMonth>").append(""
			 * ).append("</CardExpMonth>");
			 * xmldatabuffer.append("<CardExpYear>")
			 * .append("").append("</CardExpYear>"); //CVV
			 * xmldatabuffer.append("<CustomerIP>"
			 * ).append("").append("</CustomerIP>");
			 * xmldatabuffer.append("<CardIDNumber>"
			 * ).append("").append("</CardIDNumber>");
			 * xmldatabuffer.append("</Order>");
			 **/

			log.debug("Psigate request " + xmldatabuffer.toString());

			httppost = new PostMethod(protocol + "://" + host + ":" + port
					+ url);
			RequestEntity entity = new StringRequestEntity(xmldatabuffer
					.toString(), "text/plain", "UTF-8");
			httppost.setRequestEntity(entity);

			PsigateParsedElements pe = null;
			String stringresult = null;

			int result = client.executeMethod(httppost);
			if (result != 200) {
				log.error("Communication Error with psigate " + protocol
						+ "://" + host + ":" + port + url);
				// throw new Exception("Psigate Gateway error ");
				TransactionException te = new TransactionException(
						"Communication Error with psigate " + protocol + "://"
								+ host + ":" + port + url);
				te.setErrorcode("01");
				throw te;
			}
			stringresult = httppost.getResponseBodyAsString();
			log.debug("Psigate response " + stringresult);

			pe = new PsigateParsedElements();
			Digester digester = new Digester();
			digester.push(pe);

			digester.addCallMethod("Result/OrderID", "setOrderID", 0);
			digester.addCallMethod("Result/Approved", "setApproved", 0);
			digester.addCallMethod("Result/ErrMsg", "setErrMsg", 0);
			digester.addCallMethod("Result/ReturnCode", "setReturnCode", 0);
			digester.addCallMethod("Result/TransRefNumber",
					"setTransRefNumber", 0);
			digester.addCallMethod("Result/CardType", "setCardType", 0);

			Reader reader = new StringReader(stringresult);

			digester.parse(reader);

			return this.parseResponse(PaymentConstants.REFUND, xmldatabuffer
					.toString(), stringresult, pe, order, amount);

		} catch (Exception e) {
			if (e instanceof TransactionException) {
				throw (TransactionException) e;
			}
			log.error(e);
			// throw new
			// Exception("Exception occured while calling Psigate gateway " +
			// e);
			TransactionException te = new TransactionException(
					"Communication Error with psigate ", e);
			te.setErrorcode("01");
			throw te;
		} finally {
			if (httppost != null)
				httppost.releaseConnection();
		}

	}

	private GatewayTransactionVO parseResponse(int transactiontype,
			String request, String xmlresponse, PsigateParsedElements response,
			Order order, BigDecimal amount) throws Exception {

		MerchantPaymentGatewayTrx gtrx = null;
		// check if error
		if (response.getErrMsg() != null
				&& !response.getErrMsg().trim().equals("")) {
			LogMerchantUtil.log(order.getMerchantId(),
					"Can't process Psigate message " + response.getErrMsg());
			log.debug("Can't process Psigate message " + response.getErrMsg());
			// throw new Exception("Can't process Psigate message " +
			// response.getErrMsg());
			TransactionException te = new TransactionException(
					"Can't process Psigate message " + response.getErrMsg());
			te.setErrorcode("02");
			te.setReason(response.getErrMsg());
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
			gtrx.setMerchantPaymentGwRespcode(response.getReturnCode());
			gtrx.setMerchantPaymentGwOrderid(response.getOrderID());
			gtrx.setMerchantPaymentGwTrxid(response.getTransRefNumber());
			gtrx.setMerchantPaymentGwAuthtype(String.valueOf(transactiontype));
			gtrx.setMerchantPaymentGwSession("");

			String cryptedvalue = EncryptionUtil.encrypt(EncryptionUtil
					.generatekey(String.valueOf(order.getMerchantId())),
					request);
			gtrx.setMerchantPaymentGwSent(cryptedvalue);
			gtrx.setMerchantPaymentGwReceived(xmlresponse);
			gtrx.setDateAdded(new Date(new Date().getTime()));
			gtrx.setAmount(amount);

			pservice.saveMerchantPaymentGatewayTrx(gtrx);


		} catch (Exception e) {

			TransactionException te = new TransactionException(
					"Can't persist MerchantPaymentGatewayTrx internal id (orderid)"
							+ response.getOrderID(), e);
			te.setErrorcode("01");
			throw te;
		}

		GatewayTransactionVO vo = new GatewayTransactionVO();
		vo.setAmount(order.getTotal());
		vo.setCreditcard(response.getCardType());
		vo.setCreditcardtransaction(true);
		vo.setExpirydate(order.getCcExpires());
		vo.setInternalGatewayOrderId(response.getOrderID());
		vo.setTransactionDetails(gtrx);
		return vo;
	}

	public List<SalesManagerTransactionVO> retreiveTransactions(int merchantid,
			Order order) throws Exception {
		TransactionHelper trxhelper = new TransactionHelper();
		List trxs = trxhelper.getSentData(merchantid, order.getOrderId());

		if (trxs == null) {
			return null;
		}

		Iterator i = trxs.iterator();

		List returnlist = new ArrayList();

		while (i.hasNext()) {

			MerchantPaymentGatewayTrx trx = (MerchantPaymentGatewayTrx) i
					.next();

			PsigateParsedElements pe = new PsigateParsedElements();
			Digester digester = new Digester();
			digester.push(pe);

			digester.addCallMethod("Result/OrderID", "setOrderID", 0);
			// digester.addCallMethod("Result/Approved", "setApproved",0);
			// digester.addCallMethod("Result/ErrMsg", "setErrMsg",0);
			digester.addCallMethod("Result/TransRefNumber",
					"setTransRefNumber", 0);
			// digester.addCallMethod("Result/CardType", "setCardType",0);
			digester.addCallMethod("Result/FullTotal", "setFullTotal", 0);

			Reader reader = new StringReader(trx.getMerchantPaymentGwReceived());

			digester.parse(reader);

			GatewayTransactionVO mtrx = new GatewayTransactionVO();

			if (pe.getOrderID() == null) {
				log.error("Can't parse transaction for orderid "
						+ order.getOrderId());
				throw new Exception(
						"Psigate (retreiveTransaction) can't parse XML");
			}

			// GatewayTransaction gt = new GatewayTransaction();
			mtrx.setOrderID(String.valueOf(order.getOrderId()));
			mtrx.setInternalGatewayOrderId(pe.getOrderID());
			mtrx.setTransactionID(pe.getTransRefNumber());
			mtrx.setTransactionDetails(trx);
			mtrx.setType(Integer.parseInt(trx.getMerchantPaymentGwAuthtype()));
			mtrx.setAmount(new BigDecimal(pe.getFullTotal()));
			returnlist.add(mtrx);

		}
		return returnlist;
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

			IntegrationProperties props = this.stripProperties(configurations
					.getConfigurationValue2());
			vo.addConfiguration("properties", props);

			vo.addConfiguration(PaymentConstants.PAYMENT_PSIGATENAME,
					configurations);
		} catch (Exception e) {
			log.error("Can't understand MerchantConfiguration"
					+ configurations.getConfigurationId());
		}

		vo.addMerchantConfiguration(configurations);
		return vo;
	}

	/**
	 * key1 = storeid, transactionkey = password
	 * 
	 * @param configvalue
	 * @return
	 * @throws Exception
	 */
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

	public void storeConfiguration(int merchantid, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {

		// managed in Central PaymentpsigateAction

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

class PsigateParsedElements {

	private String approved;
	private String returnCode;
	private String errMsg;
	private String orderID;
	private String transRefNumber;
	private String cardType;
	private String fullTotal;

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getTransRefNumber() {
		return transRefNumber;
	}

	public void setTransRefNumber(String transRefNumber) {
		this.transRefNumber = transRefNumber;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getFullTotal() {
		return fullTotal;
	}

	public void setFullTotal(String fullTotal) {
		this.fullTotal = fullTotal;
	}

}
