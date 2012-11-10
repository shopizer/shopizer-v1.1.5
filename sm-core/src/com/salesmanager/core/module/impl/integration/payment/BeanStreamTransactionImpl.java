package com.salesmanager.core.module.impl.integration.payment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.MerchantPaymentGatewayTrx;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.payment.GatewayTransactionVO;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.payment.SalesManagerTransactionVO;
import com.salesmanager.core.service.payment.TransactionException;
import com.salesmanager.core.service.payment.impl.TransactionHelper;
import com.salesmanager.core.util.CreditCardUtil;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.EncryptionUtil;
import com.salesmanager.core.util.LogMerchantUtil;
import com.salesmanager.core.util.MerchantConfigurationUtil;
import com.salesmanager.core.util.StringUtil;

/**
 * Manages credit card transactions with BeanStream API
 * @author Carl Samson
 *
 */
@Component("beanstream")
public class BeanStreamTransactionImpl extends CreditCardGatewayTransactionImpl {

	
	private static Logger log = Logger.getLogger(BeanStreamTransactionImpl.class);
	
	@Override
	public GatewayTransactionVO authorizeAndCapture(IntegrationKeys keys,
			IntegrationProperties properties, MerchantStore store, Order order, Customer customer, CoreModuleService cms)
			throws TransactionException {
		// TODO Auto-generated method stub
		return makeTransaction("P", keys, properties, store, order, customer, cms);
	}

	@Override
	public GatewayTransactionVO authorizeTransaction(IntegrationKeys keys,
			IntegrationProperties properties, MerchantStore store, Order order, Customer customer, CoreModuleService cms)
			throws TransactionException {
		// TODO Auto-generated method stub
		return makeTransaction("PA", keys, properties, store, order, customer, cms);
	}

	/**
	 * Invoked from admin panel to capture after an authorization 
	 */
	public GatewayTransactionVO captureTransaction(IntegrationKeys ik, IntegrationProperties props,
			MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService cis) throws TransactionException {
		// TODO Auto-generated method stub
		

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
		
		StringBuffer server = new StringBuffer();
		if(!StringUtils.isBlank(protocol)) {
			server.append(protocol);
			server.append("://");
		}
		if(!StringUtils.isBlank(host)) {
			server.append(host);
		}
		if(!StringUtils.isBlank(port)) {
			server.append(":");
			server.append(port);
		}
		if(!StringUtils.isBlank(url)) {
			server.append(url);
		}

		String trnID = trx.getTransactionDetails().getMerchantPaymentGwTrxid();
		
		String amount = CurrencyUtil.displayFormatedAmountNoCurrency(order.getTotal(), order.getCurrency());
		
		/**
		merchant_id=123456789&requestType=BACKEND
		&trnType=PAC&username=user1234&password=pass1234&trnID=1000
		2115 --> requires also adjId [not documented]
		**/
		
		StringBuffer messageString = new StringBuffer();
		messageString.append("requestType=BACKEND&");
		messageString.append("merchant_id=").append(ik.getTransactionKey()).append("&");
		messageString.append("trnType=").append("PAC").append("&");
		messageString.append("username=").append(ik.getUserid()).append("&");
		messageString.append("password=").append(ik.getPassword()).append("&");
		messageString.append("trnAmount=").append(amount).append("&");
		messageString.append("adjId=").append(trnID).append("&");
		messageString.append("trnID=").append(trnID);
		
		log.debug("REQUEST SENT TO BEANSTREAM -> " + messageString.toString());

		
		HttpURLConnection conn = null;

		try {
			
			URL postURL = new URL(server.toString());
			conn = (HttpURLConnection) postURL.openConnection();
			

			GatewayTransactionVO response = this.sendTransaction(messageString.toString(), "PAC", order, conn);
			
			return response;
			
		} catch(Exception e) {
			
			if(e instanceof TransactionException)
				throw (TransactionException)e;
			throw new TransactionException("Error while processing BeanStream transaction",e);

		} finally {
			
			
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
	 * no need to initialize a transaction for this gateway
	 */
	public Map<String, String> initTransaction(
			CoreModuleService serviceDefinition, Order order)
			throws TransactionException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * no need to invoke any 'post transaction' url once completed
	 */
	public Order postTransaction(Order order) throws TransactionException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Invoked from admin panel to refund after a capture 
	 */
	public 	GatewayTransactionVO refundTransaction(IntegrationKeys keys, IntegrationProperties props,
			MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService cis, BigDecimal amount) throws TransactionException {
		// TODO Auto-generated method stub
		
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
		
		StringBuffer server = new StringBuffer();
		if(!StringUtils.isBlank(protocol)) {
			server.append(protocol);
			server.append("://");
		}
		if(!StringUtils.isBlank(host)) {
			server.append(host);
		}
		if(!StringUtils.isBlank(port)) {
			server.append(":");
			server.append(port);
		}
		if(!StringUtils.isBlank(url)) {
			server.append(url);
		}
		
		
		String orderTansactionNumber = trx.getInternalGatewayOrderId();
		
		/**
			merchant_id=123456789&requestType=BACKEND
			&trnType=R&username=user1234&password=pass1234
			&trnOrderNumber=1234&trnAmount=1.00&adjId=1000
			2115
		**/
		
		String amnt = CurrencyUtil.displayFormatedAmountNoCurrency(amount, order.getCurrency());
		String trn = trx.getTransactionDetails().getMerchantPaymentGwTrxid();
		
		StringBuffer messageString = new StringBuffer();
		messageString.append("requestType=BACKEND&");
		messageString.append("merchant_id=").append(keys.getTransactionKey()).append("&");
		messageString.append("trnType=").append("R").append("&");
		messageString.append("username=").append(keys.getUserid()).append("&");
		messageString.append("password=").append(keys.getPassword()).append("&");
		messageString.append("trnOrderNumber=").append(orderTansactionNumber).append("&");
		messageString.append("trnAmount=").append(amnt).append("&");
		messageString.append("adjId=").append(trn);
		
		
		log.debug("REQUEST SENT TO BEANSTREAM -> " + messageString.toString());

		
		HttpURLConnection conn = null;

		try {
			
			URL postURL = new URL(server.toString());
			conn = (HttpURLConnection) postURL.openConnection();
			

			GatewayTransactionVO response = this.sendTransaction(messageString.toString(), "R", order, conn);
			
			return response;
			
		} catch(Exception e) {
			if(e instanceof TransactionException)
				throw (TransactionException)e;
			
			throw new TransactionException("Error while processing BeanStream transaction",e);

		} finally {
			
			
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
	 * Retrieve transaction history
	 */
	public List<SalesManagerTransactionVO> retreiveTransactions(int merchantid,
			Order order) throws Exception {
		// TODO Auto-generated method stub
		
		
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

			GatewayTransactionVO mtrx = new GatewayTransactionVO();


			mtrx.setOrderID(String.valueOf(order.getOrderId()));
			
			mtrx.setInternalGatewayOrderId(trx.getMerchantPaymentGwOrderid());
			mtrx.setTransactionID(trx.getMerchantPaymentGwTrxid());
			
			mtrx.setTransactionDetails(trx);
			
			mtrx.setType(Integer.parseInt(trx.getMerchantPaymentGwAuthtype()));
			
			mtrx.setAmount(trx.getAmount());
			returnlist.add(mtrx);

		}
		return returnlist;
		
	}
	
	
	private GatewayTransactionVO makeTransaction(String type,
			IntegrationKeys ik, IntegrationProperties props,
			MerchantStore store, Order order, Customer customer, CoreModuleService cms) throws TransactionException {
		

		

		//determine environment
		// determine production - test environment
		String host = cms.getCoreModuleServiceProdDomain();
		String protocol = cms.getCoreModuleServiceProdProtocol();
		String port = cms.getCoreModuleServiceProdPort();
		String url = cms.getCoreModuleServiceProdEnv();
		if (props.getProperties2().equals(
				String.valueOf(PaymentConstants.TEST_ENVIRONMENT))) {
			host = cms.getCoreModuleServiceDevDomain();
			protocol = cms.getCoreModuleServiceDevProtocol();
			port = cms.getCoreModuleServiceDevPort();
			url = cms.getCoreModuleServiceDevEnv();
		}
		
		StringBuffer server = new StringBuffer();
		if(!StringUtils.isBlank(protocol)) {
			server.append(protocol);
			server.append("://");
		}
		if(!StringUtils.isBlank(host)) {
			server.append(host);
		}
		if(!StringUtils.isBlank(port)) {
			server.append(":");
			server.append(port);
		}
		if(!StringUtils.isBlank(url)) {
			server.append(url);
		}

		
		String orderNumber = order.getOrderId()+DateUtil.generateTimeStamp();
		
		StringBuffer messageString = new StringBuffer();
		
		messageString.append("requestType=BACKEND&");
		messageString.append("merchant_id=").append(ik.getTransactionKey()).append("&");
		messageString.append("trnType=").append(type).append("&");
		messageString.append("orderNumber=").append(orderNumber).append("&");
		messageString.append("trnCardOwner=").append(order.getCcOwner()).append("&");
		messageString.append("trnCardNumber=").append(order.getCcNumber()).append("&");
		messageString.append("trnExpMonth=").append(order.getCcExpires().substring(0, 2)).append("&");
		messageString.append("trnExpYear=").append(order.getCcExpires().substring(2,
				order.getCcExpires().length())).append("&");
		if (props.getProperties3().equals("2")) {
			messageString.append("trnCardCvd=").append(order.getCcCvv()).append("&");
		}
		messageString.append("trnAmount=").append(CurrencyUtil.displayFormatedAmountNoCurrency(order.getTotal(), order.getCurrency())).append("&");
		messageString.append("ordName=").append(customer.getCustomerBillingFirstName() + " " + customer.getCustomerBillingLastName()).append("&");
		messageString.append("ordAddress1=").append(customer.getCustomerBillingStreetAddress()).append("&");
		messageString.append("ordCity=").append(customer.getCustomerBillingCity()).append("&");
		
		Map zones = RefCache.getAllZonesmap(1);
		String zone = "--";
		if(zones!=null) {
			Zone z = (Zone)zones.get(customer.getCustomerBillingZoneId());
			if(z!=null) {
				zone = z.getZoneCode();
			}
		}
		
		Map countries = RefCache.getAllcountriesmap(1);
		Country c = (Country)countries.get(customer.getCustomerBillingCountryId());
		
		if(c==null) {
			log.error("Country is null for c " + customer.getCustomerCountryId());
			throw new TransactionException("Invalid country id " + customer.getCustomerBillingCountryId());
		}
		
		messageString.append("ordProvince=").append(zone).append("&");
		messageString.append("ordPostalCode=").append(customer.getCustomerBillingPostalCode()).append("&");
		messageString.append("ordCountry=").append(c.getCountryIsoCode2()).append("&");
		messageString.append("ordPhoneNumber=").append(customer.getCustomerTelephone()).append("&");
		messageString.append("ordEmailAddress=").append(customer.getCustomerEmailAddress());
		
		
		
		
		/**
		 * 	purchase (P)
		 *  -----------
				REQUEST -> merchant_id=123456789&requestType=BACKEND&trnType=P&trnOrderNumber=1234TEST&trnAmount=5.00&trnCardOwner=Joe+Test&trnCardNumber=4030000010001234&trnExpMonth=10&trnExpYear=10&ordName=Joe+Test&ordAddress1=123+Test+Street&ordCity=Victoria&ordProvince=BC&ordCountry=CA&ordPostalCode=V8T2E7&ordPhoneNumber=5555555555&ordEmailAddress=joe%40testemail.com
				RESPONSE-> trnApproved=1&trnId=10003067&messageId=1&messageText=Approved&trnOrderNumber=E40089&authCode=TEST&errorType=N&errorFields=&responseType=T&trnAmount=10%2E00&trnDate=1%2F17%2F2008+11%3A36%3A34+AM&avsProcessed=0&avsId=0&avsResult=0&avsAddrMatch=0&avsPostalMatch=0&avsMessage=Address+Verification+not+performed+for+this+transaction%2E&rspCodeCav=0&rspCavResult=0&rspCodeCredit1=0&rspCodeCredit2=0&rspCodeCredit3=0&rspCodeCredit4=0&rspCodeAddr1=0&rspCodeAddr2=0&rspCodeAddr3=0&rspCodeAddr4=0&rspCodeDob=0&rspCustomerDec=&trnType=P&paymentMethod=CC&ref1=&ref2=&ref3=&ref4=&ref5=
		
			pre authorization (PA)
			----------------------

			Prior to processing a pre-authorization through the API, you must modify the transaction settings in your Beanstream merchant member area to allow for this transaction type.
			- Log in to the Beanstream online member area at www.beanstream.com/admin/sDefault.asp.
			- Navigate to administration - account admin - order settings in the left menu.
			Under the heading �Restrict Internet Transaction Processing Types,� select either of the last two options. The �Purchases or Pre-Authorization Only� option will allow you to process both types of transaction through your web interface. De-selecting the �Restrict Internet Transaction Processing Types� checkbox will allow you to process all types of transactions including returns, voids and pre-auth completions.
		
			capture (PAC) -> requires trnId
			-------------
		
			refund (R)
			-------------
				REQUEST -> merchant_id=123456789&requestType=BACKEND&trnType=R&username=user1234&password=pass1234&trnOrderNumber=1234&trnAmount=1.00&adjId=10002115
				RESPONSE-> trnApproved=1&trnId=10002118&messageId=1&messageText=Approved&trnOrderNumber=1234R&authCode=TEST&errorType=N&errorFields=&responseType=T&trnAmount=1%2E00&trnDate=8%2F17%2F2009+1%3A44%3A56+PM&avsProcessed=0&avsId=0&avsResult=0&avsAddrMatch=0&avsPostalMatch=0&avsMessage=Address+Verification+not+performed+for+this+transaction%2E&cardType=VI&trnType=R&paymentMethod=CC&ref1=&ref2=&ref3=&ref4=&ref5=
		

			//notes
			//On receipt of the transaction response, the merchant must display order amount, transaction ID number, bank authorization code (authCode), currency, date and �messageText� to the customer on a confirmation page.
		*/
		

		//String agent = "Mozilla/4.0";
		//String respText = "";
		//Map nvp = null;
		
		
		/** debug **/
		
		try {
			

		StringBuffer messageLogString = new StringBuffer();
		
		messageLogString.append("requestType=BACKEND&");
		messageLogString.append("merchant_id=").append(ik.getTransactionKey()).append("&");
		messageLogString.append("trnType=").append(type).append("&");
		messageLogString.append("orderNumber=").append(orderNumber).append("&");
		messageLogString.append("trnCardOwner=").append(order.getCcOwner()).append("&");
		messageLogString.append("trnCardNumber=").append(CreditCardUtil.maskCardNumber(order.getCcNumber())).append("&");
		messageLogString.append("trnExpMonth=").append(order.getCcExpires().substring(0, 2)).append("&");
		messageLogString.append("trnExpYear=").append(order.getCcExpires().substring(2,
				order.getCcExpires().length())).append("&");
		if (props.getProperties3().equals("2")) {
			messageLogString.append("trnCardCvd=").append(order.getCcCvv()).append("&");
		}
		messageLogString.append("trnAmount=").append(CurrencyUtil.displayFormatedAmountNoCurrency(order.getTotal(), order.getCurrency())).append("&");
		messageLogString.append("ordName=").append(customer.getCustomerBillingFirstName() + " " + customer.getCustomerBillingLastName()).append("&");
		messageLogString.append("ordAddress1=").append(customer.getCustomerBillingStreetAddress()).append("&");
		messageLogString.append("ordCity=").append(customer.getCustomerBillingCity()).append("&");


		messageLogString.append("ordProvince=").append(zone).append("&");
		messageLogString.append("ordPostalCode=").append(customer.getCustomerBillingPostalCode()).append("&");
		messageLogString.append("ordCountry=").append(c.getCountryIsoCode2()).append("&");
		messageLogString.append("ordPhoneNumber=").append(customer.getCustomerTelephone()).append("&");
		messageLogString.append("ordEmailAddress=").append(customer.getCustomerEmailAddress());

		/** debug **/


		log.debug("REQUEST SENT TO BEANSTREAM -> " + messageLogString.toString());

		
		} catch (Exception e) {
			log.error("cannot log debug transaction");
		}
		
		HttpURLConnection conn = null;
		//DataOutputStream output = null;
		//DataInputStream in = null;
		//BufferedReader is = null;
		try {
			
			URL postURL = new URL(server.toString());
			conn = (HttpURLConnection) postURL.openConnection();
			
			GatewayTransactionVO response = this.sendTransaction(messageString.toString(), type, order, conn);
			
			return response;


			
		} catch(Exception e) {
			
			if(e instanceof TransactionException) {
				throw (TransactionException)e;
			}
			
			throw new TransactionException("Error while processing BeanStream transaction",e);

		} finally {


			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}
		}

	}
	
	
	private GatewayTransactionVO sendTransaction(String transaction, String type, Order order, HttpURLConnection conn) throws TransactionException {
		
		String agent = "Mozilla/4.0";
		String respText = "";
		Map nvp = null;
		DataOutputStream output = null;
		DataInputStream in = null;
		BufferedReader is = null;
		try {
			

			// Set connection parameters. We need to perform input and output,
			// so set both as true.
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Set the content type we are POSTing. We impersonate it as
			// encoded form data
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", agent);

			conn.setRequestProperty("Content-Length", String
					.valueOf(transaction.length()));
			conn.setRequestMethod("POST");

			// get the output stream to POST to.
			output = new DataOutputStream(conn.getOutputStream());
			output.writeBytes(transaction);
			output.flush();


			// Read input from the input stream.
			in = new DataInputStream(conn.getInputStream());
			int rc = conn.getResponseCode();
			if (rc != -1) {
				is = new BufferedReader(new InputStreamReader(conn
						.getInputStream()));
				String _line = null;
				while (((_line = is.readLine()) != null)) {
					respText = respText + _line;
				}
				
				log.debug("BeanStream response -> " + respText.trim());
				
				nvp = StringUtil.deformatUrlResponse(respText.trim());
			} else {
				throw new TransactionException("Invalid response from BeanStream, return code is " + rc);
			}
			
			//check
			//trnApproved=1&trnId=10003067&messageId=1&messageText=Approved&trnOrderNumber=E40089&authCode=TEST&errorType=N&errorFields=

			String transactionApproved = (String)nvp.get("TRNAPPROVED");
			String transactionId = (String)nvp.get("TRNID");
			String messageId = (String)nvp.get("MESSAGEID");
			String messageText = (String)nvp.get("MESSAGETEXT");
			String orderId = (String)nvp.get("TRNORDERNUMBER");
			String authCode = (String)nvp.get("AUTHCODE");
			String errorType = (String)nvp.get("ERRORTYPE");
			String errorFields = (String)nvp.get("ERRORFIELDS");
			
			
			if(StringUtils.isBlank(transactionApproved)) {
				throw new TransactionException("Required field transactionApproved missing from BeanStream response");
			}
			
			//errors
			if(transactionApproved.equals("0")) {
				LogMerchantUtil.log(order.getMerchantId(),
						"Can't process BeanStream message " + messageText + " return code id " + messageId);
				log.debug("Can't process BeanStream message " + messageText);
	
				TransactionException te = new TransactionException(
						"Can't process BeanStream message " + messageText);
				te.setErrorcode("02");
				te.setReason(messageText);
				throw te;
			}
			
			//create transaction object

			return parseResponse(type,transaction,respText,nvp,order);
			
			
		} catch(Exception e) {
			if(e instanceof TransactionException) {
				throw (TransactionException)e;
			}
			
			throw new TransactionException("Error while processing BeanStream transaction",e);

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

		}

		
	}
	
	private GatewayTransactionVO parseResponse(String transactionType,
			String request, String response, Map nvp,
			Order order) throws Exception {
		
		MerchantPaymentGatewayTrx gtrx = null;
		
		try {


			PaymentService pservice = (PaymentService) ServiceFactory
					.getService(ServiceFactory.PaymentService);

			gtrx = new MerchantPaymentGatewayTrx();
			gtrx.setMerchantId(order.getMerchantId());
			gtrx.setCustomerid(order.getCustomerId());
			gtrx.setOrderId(order.getOrderId());
			gtrx.setAmount(order.getTotal());
			gtrx.setMerchantPaymentGwMethod(order.getPaymentModuleCode());
			gtrx.setMerchantPaymentGwRespcode((String)nvp.get("TRNAPPROVED"));//transactionApproved
			gtrx.setMerchantPaymentGwOrderid((String)nvp.get("TRNORDERNUMBER"));//trnOrderNumber [required for refund]
			gtrx.setMerchantPaymentGwTrxid((String)nvp.get("TRNID"));//transactionId
			if(transactionType.equals("PA")) {//pre-auth
				gtrx.setMerchantPaymentGwAuthtype(String.valueOf(PaymentConstants.PREAUTH));
			} else if(transactionType.equals("PAC")) {//capture
				gtrx.setMerchantPaymentGwAuthtype(String.valueOf(PaymentConstants.CAPTURE));
			} else if(transactionType.equals("P")) {//capture
				gtrx.setMerchantPaymentGwAuthtype(String.valueOf(PaymentConstants.CAPTURE));
			} else if(transactionType.equals("R")) {//refund
				gtrx.setMerchantPaymentGwAuthtype(String.valueOf(PaymentConstants.REFUND));
			}
			
			gtrx.setMerchantPaymentGwSession("");

			String cryptedvalue = EncryptionUtil.encrypt(EncryptionUtil
					.generatekey(String.valueOf(order.getMerchantId())),
					request);
			gtrx.setMerchantPaymentGwSent(cryptedvalue);
			gtrx.setMerchantPaymentGwReceived(response);
			gtrx.setDateAdded(new Date(new Date().getTime()));
			gtrx.setAmount(order.getTotal());

			pservice.saveMerchantPaymentGatewayTrx(gtrx);


		} catch (Exception e) {

			TransactionException te = new TransactionException(
					"Can't persist MerchantPaymentGatewayTrx for order id"
							+ order.getOrderId(), e);
			te.setErrorcode("01");
			throw te;
		}

		GatewayTransactionVO vo = new GatewayTransactionVO();
		vo.setAmount(order.getTotal());
		vo.setCreditcard(order.getCardType());
		vo.setCreditcardtransaction(true);
		vo.setExpirydate(order.getCcExpires());
		vo.setInternalGatewayOrderId((String)nvp.get("TRNORDERNUMBER"));
		vo.setTransactionDetails(gtrx);
		
		vo.setTransactionID((String)nvp.get("TRNORDERNUMBER"));
		vo.setTransactionMessage((String)nvp.get("MESSAGETEXT"));
		
		return vo;
		
	}
	

	public ConfigurationResponse getConfiguration(
			MerchantConfiguration configurations, ConfigurationResponse vo)
			throws Exception {
		//get payment gatemay configuration from MERCHANT_CONFIGURATION table
		
		//merchantId, userName, password
		
		try {
			// vo.addConfiguration("paymentmethod",
			// configurations.getConfigurationValue());

			String decryptedvalue = EncryptionUtil.decrypt(
					EncryptionUtil.generatekey(String.valueOf(configurations
							.getMerchantId())), configurations
							.getConfigurationValue());
			IntegrationKeys ik = MerchantConfigurationUtil.getIntegrationKeys(decryptedvalue,";");
			vo.addConfiguration("keys", ik);

			IntegrationProperties props = MerchantConfigurationUtil.getIntegrationProperties(configurations
					.getConfigurationValue2(),";");
			vo.addConfiguration("properties", props);

			vo.addConfiguration("beanstream",
					configurations);
			
			
		} catch (Exception e) {
			log.error("Can't understand MerchantConfiguration"
					+ configurations.getConfigurationId());
		}
		
		return vo;
	}

	public void storeConfiguration(int merchantid, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {
		//
		
		//key -> MD_PAY_GW_beanstream
		//module -> beanstream
		//configurationValue -> encrypt(merchantId;userName;password)
		//configurationValue2 -> transactionType(auth/capture);environnement(1=PROD/2=TEST);useCvv(1=NO/2=YES)

		String transactionType = (String)request.getAttribute("properties.properties1");
		String environment = (String)request.getAttribute("properties.properties2");
		String useCvv = (String)request.getAttribute("properties.properties3");
		
		
		String merchantId = (String)request.getAttribute("keys.transactionKey");
		String userName = (String)request.getAttribute("keys.userid");
		String password = (String)request.getAttribute("keys.password");
		
		
		
		//we assume here that everything has been validated in the action class
		//and that no object will be null !
		
		
		String key = EncryptionUtil.generatekey(String.valueOf(merchantid));
		// keep this order userid,password,transactionkey
		String credentials = new StringBuffer().append(userName)
				.append(";").append(password).append(";").append(
						merchantId).toString();

		String encrypted = EncryptionUtil.encrypt(key, credentials);

		String props = new StringBuffer().append(
				transactionType).append(";").append(
						environment).append(";").append(
								useCvv).toString();
		
		

		MerchantConfiguration conf = (MerchantConfiguration)vo.getConfiguration(PaymentConstants.MODULE_PAYMENT_GATEWAY + "beanstream");
		if(conf==null) {

				conf = new MerchantConfiguration();
				conf.setConfigurationKey(PaymentConstants.MODULE_PAYMENT_GATEWAY + "beanstream");
				conf.setConfigurationModule("beanstream");
				conf.setMerchantId(merchantid);

		}
		conf.setLastModified(new Date());
		conf.setConfigurationValue(encrypted);
		conf.setConfigurationValue2(props);
		
		
		MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
		mservice.saveOrUpdateMerchantConfiguration(conf);
		

		
		
	}

}
