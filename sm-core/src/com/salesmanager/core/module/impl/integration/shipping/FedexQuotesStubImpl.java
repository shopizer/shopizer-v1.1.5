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
package com.salesmanager.core.module.impl.integration.shipping;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import org.apache.axis.types.NonNegativeInteger;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fedex.rate.stub.Address;
import com.fedex.rate.stub.ClientDetail;
import com.fedex.rate.stub.Dimensions;
import com.fedex.rate.stub.DropoffType;
import com.fedex.rate.stub.LinearUnits;
import com.fedex.rate.stub.Money;
import com.fedex.rate.stub.Notification;
import com.fedex.rate.stub.NotificationSeverityType;
import com.fedex.rate.stub.PackageRateDetail;
import com.fedex.rate.stub.PackageSpecialServicesRequested;
import com.fedex.rate.stub.PackagingType;
import com.fedex.rate.stub.Party;
import com.fedex.rate.stub.Payment;
import com.fedex.rate.stub.PaymentType;
import com.fedex.rate.stub.RatePortType;
import com.fedex.rate.stub.RateReply;
import com.fedex.rate.stub.RateReplyDetail;
import com.fedex.rate.stub.RateRequest;
import com.fedex.rate.stub.RateRequestType;
import com.fedex.rate.stub.RateServiceLocator;
import com.fedex.rate.stub.RatedPackageDetail;
import com.fedex.rate.stub.RatedShipmentDetail;
import com.fedex.rate.stub.RequestedPackage;
import com.fedex.rate.stub.RequestedPackageDetailType;
import com.fedex.rate.stub.RequestedShipment;
import com.fedex.rate.stub.ServiceType;
import com.fedex.rate.stub.ShipmentRateDetail;
import com.fedex.rate.stub.Surcharge;
import com.fedex.rate.stub.TransactionDetail;
import com.fedex.rate.stub.VersionId;
import com.fedex.rate.stub.WebAuthenticationCredential;
import com.fedex.rate.stub.WebAuthenticationDetail;
import com.fedex.rate.stub.Weight;
import com.fedex.rate.stub.WeightUnits;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.shipping.PackageDetail;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.shipping.ShippingService;
import com.salesmanager.core.util.CountryUtil;

public class FedexQuotesStubImpl {

	private Logger log = Logger.getLogger(FedexRequestQuotesImpl.class);

	private ServiceType getServiceType(String serviceTypeId) {

		if (StringUtils.isBlank(serviceTypeId)) {
			log
					.warn("ServiceType is blank or null, will return standard overnight");
			return ServiceType.STANDARD_OVERNIGHT;
		}

		ServiceType sType = null;

		if (serviceTypeId.equals("STANDARD_OVERNIGHT")) {
			sType = ServiceType.STANDARD_OVERNIGHT;
		} else if (serviceTypeId.equals("FEDEX_1_DAY_FREIGHT")) {
			sType = ServiceType.FEDEX_1_DAY_FREIGHT;
		} else if (serviceTypeId.equals("FEDEX_2_DAY_FREIGHT")) {
			sType = ServiceType.FEDEX_2_DAY_FREIGHT;
		} else if (serviceTypeId.equals("FEDEX_3_DAY_FREIGHT")) {
			sType = ServiceType.FEDEX_3_DAY_FREIGHT;
		} else if (serviceTypeId.equals("FEDEX_2_DAY")) {
			sType = ServiceType.FEDEX_2_DAY;
		} else if (serviceTypeId.equals("FEDEX_EXPRESS_SAVER")) {
			sType = ServiceType.FEDEX_EXPRESS_SAVER;
		} else if (serviceTypeId.equals("FEDEX_GROUND")) {
			sType = ServiceType.FEDEX_GROUND;
		} else if (serviceTypeId.equals("FIRST_OVERNIGHT")) {
			sType = ServiceType.FIRST_OVERNIGHT;
		} else if (serviceTypeId.equals("GROUND_HOME_DELIVERY")) {
			sType = ServiceType.GROUND_HOME_DELIVERY;
		} else if (serviceTypeId.equals("INTERNATIONAL_ECONOMY")) {
			sType = ServiceType.INTERNATIONAL_ECONOMY;
		} else if (serviceTypeId.equals("INTERNATIONAL_ECONOMY_FREIGHT")) {
			sType = ServiceType.INTERNATIONAL_ECONOMY_FREIGHT;
		} else if (serviceTypeId.equals("INTERNATIONAL_FIRST")) {
			sType = ServiceType.INTERNATIONAL_FIRST;
		} else if (serviceTypeId.equals("INTERNATIONAL_PRIORITY")) {
			sType = ServiceType.INTERNATIONAL_PRIORITY;
		} else if (serviceTypeId.equals("INTERNATIONAL_PRIORITY_FREIGHT")) {
			sType = ServiceType.INTERNATIONAL_PRIORITY_FREIGHT;
		} else if (serviceTypeId.equals("PRIORITY_OVERNIGHT")) {
			sType = ServiceType.PRIORITY_OVERNIGHT;
		} else {
			sType = ServiceType.STANDARD_OVERNIGHT;
		}

		return sType;

	}

	/**
	 * Needs to be updated for any changes made to fedexground.properties &
	 * fedexexpress.properties
	 * 
	 * @param optionId
	 * @return
	 */
	private PackagingType getPackagingType(String optionId) {

		if (StringUtils.isBlank(optionId)) {
			log
					.warn("PackageOption is blank or null, will return YourPackaging");
			return PackagingType.YOUR_PACKAGING;
		}

		PackagingType pType = null;

		if (optionId.equals("1")) {
			pType = PackagingType.FEDEX_TUBE;
		} else if (optionId.equals("2")) {
			pType = PackagingType.FEDEX_10KG_BOX;
		} else if (optionId.equals("3")) {
			pType = PackagingType.FEDEX_25KG_BOX;
		} else if (optionId.equals("4")) {
			pType = PackagingType.FEDEX_BOX;
		} else if (optionId.equals("5")) {
			pType = PackagingType.FEDEX_PAK;
		} else if (optionId.equals("6")) {
			pType = PackagingType.FEDEX_ENVELOPE;
		} else if (optionId.equals("7")) {
			pType = PackagingType.YOUR_PACKAGING;
		} else {
			pType = PackagingType.YOUR_PACKAGING;
		}

		return pType;

	}

	public Collection<ShippingOption> getQuote(String carrier,
			String deliveryType, String module,
			Collection<PackageDetail> packages, BigDecimal orderTotal,
			ConfigurationResponse vo, MerchantStore store, Customer customer,
			Locale locale) throws Exception {
		// Build a RateRequest request object
		boolean getAllRatesFlag = true; // set to true to get the rates for
										// different service types
		RateRequest request = new RateRequest();

		// request.setClientDetail(createClientDetail());
		// request.setWebAuthenticationDetail(createWebAuthenticationDetail());
		request.setClientDetail(createClientDetail(module, vo));
		request.setWebAuthenticationDetail(createWebAuthenticationDetail(
				module, vo));

		MerchantConfiguration packageServices = vo
				.getMerchantConfiguration(module + "-"
						+ ShippingConstants.MODULE_SHIPPING_RT_PKG_DOM_INT);
		String packageOption = packageServices.getConfigurationValue();

		PackagingType pType = getPackagingType(packageOption);

		ServiceType sType = getServiceType(deliveryType);

		int icountry = store.getCountry();
		String country = CountryUtil.getCountryIsoCodeById(icountry);

		IntegrationProperties props = (IntegrationProperties) vo
				.getConfiguration(module + "-properties");

		ShippingService sservice = (ShippingService) ServiceFactory
				.getService(ServiceFactory.ShippingService);
		CoreModuleService cms = sservice.getRealTimeQuoteShippingService(
				country, module);

		if (cms == null) {
			// throw new
			// Exception("Central integration services not configured for " +
			// PaymentConstants.PAYMENT_PSIGATENAME + " and country id " +
			// origincountryid);
			log.error("CoreModuleService not configured for " + carrier
					+ " and country id " + icountry);
			return null;
		}

		String host = cms.getCoreModuleServiceProdDomain();
		String prot = cms.getCoreModuleServiceProdProtocol();
		String pt = cms.getCoreModuleServiceProdPort();
		String url = cms.getCoreModuleServiceProdEnv();
		if (props.getProperties1().equals(
				String.valueOf(ShippingConstants.TEST_ENVIRONMENT))) {
			host = cms.getCoreModuleServiceDevDomain();
			prot = cms.getCoreModuleServiceDevProtocol();
			pt = cms.getCoreModuleServiceDevPort();
			url = cms.getCoreModuleServiceDevEnv();
		}

		//
		TransactionDetail transactionDetail = new TransactionDetail();
		transactionDetail
				.setCustomerTransactionId("java sample - Rate Request"); // The
																			// client
																			// will
																			// get
																			// the
																			// same
																			// value
																			// back
																			// in
																			// the
																			// response
		request.setTransactionDetail(transactionDetail);

		//
		VersionId versionId = new VersionId("crs", 5, 0, 0);
		request.setVersion(versionId);

		//
		RequestedShipment requestedShipment = new RequestedShipment();

		requestedShipment.setServiceType(sType);
		requestedShipment.setPackagingType(pType);

		requestedShipment.setShipTimestamp(Calendar.getInstance());
		requestedShipment.setDropoffType(DropoffType.REGULAR_PICKUP);
		// if (! getAllRatesFlag) {
		// requestedShipment.setServiceType(ServiceType.INTERNATIONAL_PRIORITY);
		// requestedShipment.setPackagingType(PackagingType.YOUR_PACKAGING);
		// }

		//
		Party shipper = new Party();
		Address shipperAddress = new Address(); // Origin information
		shipperAddress.setStreetLines(new String[] { "Address Line 1" });
		shipperAddress.setCity("City Name");
		shipperAddress.setStateOrProvinceCode("TN");
		shipperAddress.setPostalCode("38115");
		shipperAddress.setCountryCode("US");
		shipper.setAddress(shipperAddress);
		requestedShipment.setShipper(shipper);

		//
		Party recipient = new Party();
		Address recipientAddress = new Address(); // Destination information
		recipientAddress.setStreetLines(new String[] { "Address Line 1" });
		recipientAddress.setCity("City Name");
		recipientAddress.setStateOrProvinceCode("QC");
		recipientAddress.setPostalCode("H1E1A1");
		recipientAddress.setCountryCode("CA");

		recipient.setAddress(recipientAddress);
		requestedShipment.setRecipient(recipient);

		//
		Payment shippingChargesPayment = new Payment();
		shippingChargesPayment.setPaymentType(PaymentType.SENDER);
		requestedShipment.setShippingChargesPayment(shippingChargesPayment);

		RequestedPackage rp = new RequestedPackage();
		rp.setWeight(new Weight(WeightUnits.LB, new BigDecimal(15.0)));
		//
		rp.setInsuredValue(new Money("USD", new BigDecimal("100.00")));
		//
		rp.setDimensions(new Dimensions(new NonNegativeInteger("1"),
				new NonNegativeInteger("1"), new NonNegativeInteger("1"),
				LinearUnits.IN));
		PackageSpecialServicesRequested pssr = new PackageSpecialServicesRequested();
		rp.setSpecialServicesRequested(pssr);
		requestedShipment.setRequestedPackages(new RequestedPackage[] { rp });

		requestedShipment.setPackageCount(new NonNegativeInteger("1"));
		requestedShipment
				.setRateRequestTypes(new RateRequestType[] { RateRequestType.ACCOUNT });
		requestedShipment
				.setPackageDetail(RequestedPackageDetailType.INDIVIDUAL_PACKAGES);
		request.setRequestedShipment(requestedShipment);

		//
		try {
			// Initialize the service
			RateServiceLocator service;
			RatePortType port;
			//

			// updateEndPoint(service);
			String endPointUrl = prot + "://" + host + ":" + pt + url;

			service = new RateServiceLocator();
			// updateEndPoint(service);
			service.setRateServicePortEndpointAddress(endPointUrl);

			port = service.getRateServicePort();
			// This is the call to the web service passing in a RateRequest and
			// returning a RateReply
			RateReply reply = port.getRates(request); // Service call
			if (isResponseOk(reply.getHighestSeverity())) {
				writeServiceOutput(reply);
			}
			printNotifications(reply.getNotifications());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void writeServiceOutput(RateReply reply) {
		RateReplyDetail[] rrds = reply.getRateReplyDetails();
		for (int i = 0; i < rrds.length; i++) {
			RateReplyDetail rrd = rrds[i];
			print("\nService type", rrd.getServiceType());
			print("Packaging type", rrd.getPackagingType());
			RatedShipmentDetail[] rsds = rrd.getRatedShipmentDetails();
			for (int j = 0; j < rsds.length; j++) {
				print("RatedShipmentDetail " + j, "");
				RatedShipmentDetail rsd = rsds[j];
				ShipmentRateDetail srd = rsd.getShipmentRateDetail();
				print("  Rate type", srd.getRateType());
				printWeight("  Total Billing weight", srd
						.getTotalBillingWeight());
				printMoney("  Total surcharges", srd.getTotalSurcharges());
				printMoney("  Total net charge", srd.getTotalNetCharge());

				RatedPackageDetail[] rpds = rsd.getRatedPackages();
				if (rpds != null && rpds.length > 0) {
					print("  RatedPackageDetails", "");
					for (int k = 0; k < rpds.length; k++) {
						print("  RatedPackageDetail " + i, "");
						RatedPackageDetail rpd = rpds[k];
						PackageRateDetail prd = rpd.getPackageRateDetail();
						if (prd != null) {
							printWeight("    Billing weight", prd
									.getBillingWeight());
							printMoney("    Base charge", prd.getBaseCharge());
							Surcharge[] surcharges = prd.getSurcharges();
							if (surcharges != null && surcharges.length > 0) {
								for (int m = 0; m < surcharges.length; m++) {
									Surcharge surcharge = surcharges[m];
									printMoney("    "
											+ surcharge.getDescription()
											+ " surcharge", surcharge
											.getAmount());
								}
							}
						}
					}
				}
			}
		}
	}

	private ClientDetail createClientDetail(String carrier,
			ConfigurationResponse vo) throws Exception {
		ClientDetail clientDetail = new ClientDetail();

		IntegrationKeys keys = (IntegrationKeys) vo.getConfiguration(carrier
				+ "-keys");

		String accountNumber = keys.getUserid();
		String meterNumber = keys.getKey2();

		//
		// See if the accountNumber and meterNumber properties are set,
		// if set use those values, otherwise default them to "XXX"
		//
		if (accountNumber == null) {
			throw new Exception("FedexQuotesImpl, missing accountNumber");
		}
		if (meterNumber == null) {
			throw new Exception("FedexQuotesImpl, missing meterNumber");
		}
		clientDetail.setAccountNumber(accountNumber);
		clientDetail.setMeterNumber(meterNumber);
		return clientDetail;
	}

	private WebAuthenticationDetail createWebAuthenticationDetail(
			String carrier, ConfigurationResponse vo) throws Exception {

		IntegrationKeys keys = (IntegrationKeys) vo.getConfiguration(carrier
				+ "-keys");

		WebAuthenticationCredential wac = new WebAuthenticationCredential();

		String key = keys.getKey1();
		String password = keys.getPassword();

		// String key = System.getProperty("key");
		// String password = System.getProperty("password");

		//
		// See if the key and password properties are set,
		// if set use those values, otherwise default them to "XXX"
		//
		if (key == null) {
			throw new Exception("FedexQuotesImpl, missing key");
		}
		if (password == null) {
			throw new Exception("FedexQuotesImpl, missing password");
		}
		wac.setKey(key);
		wac.setPassword(password);
		return new WebAuthenticationDetail(wac);
	}

	private static ClientDetail createClientDetail() {
		ClientDetail clientDetail = new ClientDetail();
		String accountNumber = System.getProperty("accountNumber");
		String meterNumber = System.getProperty("meterNumber");

		//
		// See if the accountNumber and meterNumber properties are set,
		// if set use those values, otherwise default them to "XXX"
		//
		if (accountNumber == null) {
			accountNumber = "510087062"; // Replace "XXX" with clients account
											// number
		}
		if (meterNumber == null) {
			meterNumber = "1197831"; // Replace "XXX" with clients meter number
		}
		clientDetail.setAccountNumber(accountNumber);
		clientDetail.setMeterNumber(meterNumber);
		return clientDetail;
	}

	private static WebAuthenticationDetail createWebAuthenticationDetail() {
		WebAuthenticationCredential wac = new WebAuthenticationCredential();
		String key = System.getProperty("key");
		String password = System.getProperty("password");

		//
		// See if the key and password properties are set,
		// if set use those values, otherwise default them to "XXX"
		//
		if (key == null) {
			key = "hiJVrsQHdYVtykw5"; // Replace "XXX" with clients key
		}
		if (password == null) {
			password = "N1a2daqFr3QUfz9MmaZrUpJ2p"; // Replace "XXX" with
													// clients password
		}
		wac.setKey(key);
		wac.setPassword(password);
		return new WebAuthenticationDetail(wac);
	}

	private static void printNotifications(Notification[] notifications) {
		System.out.println("Notifications:");
		if (notifications == null || notifications.length == 0) {
			System.out.println("  No notifications returned");
		}
		for (int i = 0; i < notifications.length; i++) {
			Notification n = notifications[i];
			System.out.print("  Notification no. " + i + ": ");
			if (n == null) {
				System.out.println("null");
				continue;
			} else {
				System.out.println("");
			}
			NotificationSeverityType nst = n.getSeverity();

			System.out.println("    Severity: "
					+ (nst == null ? "null" : nst.getValue()));
			System.out.println("    Code: " + n.getCode());
			System.out.println("    Message: " + n.getMessage());
			System.out.println("    Source: " + n.getSource());
		}
	}

	private static boolean isResponseOk(
			NotificationSeverityType notificationSeverityType) {
		if (notificationSeverityType == null) {
			return false;
		}
		if (notificationSeverityType.equals(NotificationSeverityType.WARNING)
				|| notificationSeverityType
						.equals(NotificationSeverityType.NOTE)
				|| notificationSeverityType
						.equals(NotificationSeverityType.SUCCESS)) {
			return true;
		}
		return false;
	}

	private static void print(String msg, Object obj) {
		if (msg == null || obj == null) {
			return;
		}
		System.out.println(msg + ": " + obj.toString());
	}

	private static void printMoney(String msg, Money money) {
		if (msg == null || money == null) {
			return;
		}
		System.out.println(msg + ": " + money.getAmount() + " "
				+ money.getCurrency());
	}

	private static void printWeight(String msg, Weight weight) {
		if (msg == null || weight == null) {
			return;
		}
		System.out.println(msg + ": " + weight.getValue() + " "
				+ weight.getUnits());
	}

	private static void updateEndPoint(RateServiceLocator serviceLocator) {
		// String endPoint = System.getProperty("endPoint");
		String endPoint = "https://gatewaybeta.fedex.com:443/web-services";
		if (endPoint != null) {
			serviceLocator.setRateServicePortEndpointAddress(endPoint);
		}
	}

}
