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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.fedex.rate.stub.RatedShipmentDetail;
import com.fedex.rate.stub.RequestedPackage;
import com.fedex.rate.stub.RequestedPackageDetailType;
import com.fedex.rate.stub.RequestedShipment;
import com.fedex.rate.stub.ReturnedRateType;
import com.fedex.rate.stub.ServiceType;
import com.fedex.rate.stub.ShipmentRateDetail;
import com.fedex.rate.stub.TransactionDetail;
import com.fedex.rate.stub.VersionId;
import com.fedex.rate.stub.WebAuthenticationCredential;
import com.fedex.rate.stub.WebAuthenticationDetail;
import com.fedex.rate.stub.Weight;
import com.fedex.rate.stub.WeightUnits;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.entity.shipping.PackageDetail;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.shipping.ShippingService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LogMerchantUtil;

public class FedexRequestQuotesImpl {

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
		request.setClientDetail(createClientDetail(module, vo));
		request.setWebAuthenticationDetail(createWebAuthenticationDetail(
				module, vo));

		Collection returnColl = null;
		int icountry = store.getCountry();
		String country = CountryUtil.getCountryIsoCodeById(icountry);

		IntegrationProperties props = (IntegrationProperties) vo
				.getConfiguration(module + "-properties");

		ShippingService sservice = (ShippingService) ServiceFactory
				.getService(ServiceFactory.ShippingService);
		CoreModuleService cms = sservice.getRealTimeQuoteShippingService(
				country, module);

		/** Details on whit RT quote information to display **/
		MerchantConfiguration rtdetails = vo
				.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_DISPLAY_REALTIME_QUOTES);
		int displayQuoteDeliveryTime = ShippingConstants.NO_DISPLAY_RT_QUOTE_TIME;
		if (rtdetails != null) {

			if (!StringUtils.isBlank(rtdetails.getConfigurationValue1())) {// display
																			// or
																			// not
																			// quotes
				try {
					displayQuoteDeliveryTime = Integer.parseInt(rtdetails
							.getConfigurationValue1());

				} catch (Exception e) {
					log.error("Display quote is not an integer value ["
							+ rtdetails.getConfigurationValue1() + "]");
				}
			}
		}
		/**/

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
		String protocol = cms.getCoreModuleServiceProdProtocol();
		String port = cms.getCoreModuleServiceProdPort();
		String url = cms.getCoreModuleServiceProdEnv();
		if (props.getProperties1().equals(
				String.valueOf(ShippingConstants.TEST_ENVIRONMENT))) {
			host = cms.getCoreModuleServiceDevDomain();
			protocol = cms.getCoreModuleServiceDevProtocol();
			port = cms.getCoreModuleServiceDevPort();
			url = cms.getCoreModuleServiceDevEnv();
		}

		//
		TransactionDetail transactionDetail = new TransactionDetail();
		transactionDetail.setCustomerTransactionId("Shopizer salesmanager"); // The
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

		requestedShipment.setShipTimestamp(Calendar.getInstance());
		requestedShipment.setDropoffType(DropoffType.REGULAR_PICKUP);

		MerchantConfiguration packageServices = vo
				.getMerchantConfiguration(module + "-"
						+ ShippingConstants.MODULE_SHIPPING_RT_PKG_DOM_INT);
		String packageOption = packageServices.getConfigurationValue();

		PackagingType pType = getPackagingType(packageOption);

		if (deliveryType != null) {
			// requestedShipment.setServiceType(sType);
			ServiceType sType = getServiceType(deliveryType);
		}

		// if (! getAllRatesFlag) {
		// requestedShipment.setServiceType(ServiceType.INTERNATIONAL_PRIORITY);
		// requestedShipment.setServiceType(sType);
		requestedShipment.setPackagingType(pType);
		// }

		//

		Map countriesMap = (Map) RefCache.getAllcountriesmap(LanguageUtil
				.getLanguageNumberCode(locale.getLanguage()));
		Country c = (Country) countriesMap.get(store.getCountry());

		Map zonesMap = (Map) RefCache.getAllZonesmap(LanguageUtil
				.getLanguageNumberCode(locale.getLanguage()));
		// Zone z = CountryUtil.getZoneCodeByName(store.getZone(),
		// LanguageUtil.getLanguageNumberCode(locale.getCountry()));

		Zone zone = (Zone) zonesMap.get(Integer.parseInt(store.getZone()));
		String zoneName = null;
		if (zone == null) {
			zoneName = "N/A";
		} else {
			zoneName = zone.getZoneCode();
		}

		/*
		 * String originZone = zoneName; if(originZone.endsWith("QC")) {
		 * originZone = "PQ"; }
		 */

		// origin
		Party shipper = new Party();
		Address shipperAddress = new Address(); // Origin information
		shipperAddress.setStreetLines(new String[] { store.getStoreaddress() });
		shipperAddress.setCity(store.getStorecity());
		shipperAddress.setStateOrProvinceCode(zoneName);
		shipperAddress.setPostalCode(com.salesmanager.core.util.ShippingUtil
				.trimPostalCode(store.getStorepostalcode()));
		shipperAddress.setCountryCode(c.getCountryIsoCode2());
		shipper.setAddress(shipperAddress);
		requestedShipment.setShipper(shipper);

		Zone customerZone = (Zone) zonesMap.get(customer.getCustomerZoneId());
		Country customerCountry = (Country) countriesMap.get(customer
				.getCustomerCountryId());
		String customerZoneName = null;
		if (zone == null) {
			customerZoneName = "N/A";
		} else {
			customerZoneName = customerZone.getZoneCode();
		}

		/*
		 * String destZone = customerZoneName; if(destZone.endsWith("QC")) {
		 * destZone = "PQ"; }
		 */

		// destination
		Party recipient = new Party();
		Address recipientAddress = new Address(); // Destination information
		recipientAddress.setStreetLines(new String[] { customer
				.getCustomerStreetAddress() });
		recipientAddress.setCity(customer.getCustomerCity());
		recipientAddress.setStateOrProvinceCode(customerZoneName);
		recipientAddress.setPostalCode(com.salesmanager.core.util.ShippingUtil
				.trimPostalCode(customer.getCustomerPostalCode()));
		recipientAddress.setCountryCode(customerCountry.getCountryIsoCode2());

		recipient.setAddress(recipientAddress);
		requestedShipment.setRecipient(recipient);

		//
		Payment shippingChargesPayment = new Payment();
		shippingChargesPayment.setPaymentType(PaymentType.SENDER);
		requestedShipment.setShippingChargesPayment(shippingChargesPayment);

		// build packages

		RequestedPackage[] rpArray = new RequestedPackage[packages.size()];

		Iterator packagesIterator = packages.iterator();
		int i = 0;
		while (packagesIterator.hasNext()) {

			PackageDetail detail = (PackageDetail) packagesIterator.next();
			RequestedPackage rp = new RequestedPackage();

			Weight w = null;
			if (store.getWeightunitcode().equals(Constants.LB_WEIGHT_UNIT)) {
				w = new Weight(WeightUnits.LB, new BigDecimal(detail
						.getShippingWeight()));
			} else {
				w = new Weight(WeightUnits.KG, new BigDecimal(detail
						.getShippingWeight()));
			}

			Dimensions d = null;

			BigDecimal h = new BigDecimal(detail.getShippingHeight());
			h.setScale(2, BigDecimal.ROUND_UP);
			int ih = h.intValue();

			BigDecimal sw = new BigDecimal(detail.getShippingWeight());
			sw.setScale(2, BigDecimal.ROUND_UP);
			int isw = sw.intValue();

			BigDecimal sw2 = new BigDecimal(detail.getShippingWidth());
			sw2.setScale(2, BigDecimal.ROUND_UP);
			int isw2 = sw2.intValue();

			if (store.getSeizeunitcode().equals(Constants.INCH_SIZE_UNIT)) {
				d = new Dimensions(new NonNegativeInteger(String.valueOf(ih)),
						new NonNegativeInteger(String.valueOf(isw)),
						new NonNegativeInteger(String.valueOf(isw2)),
						LinearUnits.IN);
			} else {
				d = new Dimensions(new NonNegativeInteger(String.valueOf(ih)),
						new NonNegativeInteger(String.valueOf(isw)),
						new NonNegativeInteger(String.valueOf(isw2)),
						LinearUnits.CM);
			}

			rp.setWeight(w);
			// insurance
			// rp.setInsuredValue(new Money(store.getCurrency(), new
			// BigDecimal("100.00")));
			//
			rp.setDimensions(d);
			PackageSpecialServicesRequested pssr = new PackageSpecialServicesRequested();
			rp.setSpecialServicesRequested(pssr);

			rpArray[i] = rp;
			i++;

		}
		requestedShipment.setRequestedPackages(rpArray);

		requestedShipment.setPackageCount(new NonNegativeInteger(String
				.valueOf(packages.size())));
		requestedShipment
				.setRateRequestTypes(new RateRequestType[] { RateRequestType.ACCOUNT });
		requestedShipment
				.setPackageDetail(RequestedPackageDetailType.INDIVIDUAL_PACKAGES);
		request.setRequestedShipment(requestedShipment);

		//
		try {
			// Initialize the service
			RateServiceLocator service;
			RatePortType p;
			//
			service = new RateServiceLocator();

			// updateEndPoint(service);
			String endPointUrl = protocol + "://" + host + ":" + port + url;

			service.setRateServicePortEndpointAddress(endPointUrl);

			p = service.getRateServicePort();
			// This is the call to the web service passing in a RateRequest and
			// returning a RateReply
			RateReply reply = p.getRates(request); // Service call
			if (isResponseOk(reply.getHighestSeverity())) {
				returnColl = writeServiceOutput(displayQuoteDeliveryTime,
						reply, module, carrier, locale, store.getCurrency());
			}
			printNotifications(store, reply.getNotifications());

		} catch (RuntimeException e) {
			e.printStackTrace();
			log.error(e);
		} catch (Exception ex) {
			log.error(ex);
		}

		return returnColl;
	}

	public Collection<ShippingOption> writeServiceOutput(int displayType,
			RateReply reply, String module, String carrier, Locale locale,
			String currency) throws Exception {

		// get all shipping type labels
		Map svs = com.salesmanager.core.util.ShippingUtil.buildServiceMap(
				module, locale);

		List options = new ArrayList();
		RateReplyDetail[] rrds = reply.getRateReplyDetails();
		
		if(rrds==null) {
			return options;
		}

		for (int i = 0; i < rrds.length; i++) {
			RateReplyDetail rrd = rrds[i];
			ShippingOption option = new ShippingOption();

			ServiceType sType = rrd.getServiceType();
			String serviceType = (String) svs.get(sType.getValue());
			StringBuffer description = new StringBuffer();
			description.append(serviceType);

			Calendar deliveryDate = rrd.getDeliveryTimestamp();

			if (displayType == ShippingConstants.DISPLAY_RT_QUOTE_TIME) {
				if (deliveryDate != null) {
					description.append(" (").append(
							DateUtil.formatDate(deliveryDate.getTime()))
							.append(")");
				}
			}

			option.setDescription(description.toString());
			option.setOptionId(String.valueOf(i + 1));
			option.setOptionCode(sType.getValue());
			RatedShipmentDetail[] rsds = rrd.getRatedShipmentDetails();
			boolean found = false;
			for (int j = 0; j < rsds.length && !found; j++) {
				// print("RatedShipmentDetail " + j, "");
				RatedShipmentDetail rsd = rsds[j];
				ShipmentRateDetail srd = rsd.getShipmentRateDetail();
				// print("  Rate type", srd.getRateType());//payor_account,
				// rated_account
				// printWeight("  Total Billing weight",
				// srd.getTotalBillingWeight());
				// printMoney("  Total surcharges", srd.getTotalSurcharges());
				// printMoney("  Total net charge", srd.getTotalNetCharge());
				ReturnedRateType rrt = srd.getRateType();

				if (rrt.getValue().equals("PAYOR_ACCOUNT")) {
					Money m = srd.getTotalNetCharge();
					String amount = CurrencyUtil
							.displayFormatedAmountWithCurrency(m.getAmount(),
									currency);

					option.setCurrency(currency);
					option.setOptionPrice(m.getAmount());
					options.add(option);
					found = true;
				} else if (j + 1 == rsds.length) {
					Money m = srd.getTotalNetCharge();
					String amount = CurrencyUtil
							.displayFormatedAmountWithCurrency(m.getAmount(),
									currency);

					option.setCurrency(currency);
					option.setOptionPrice(m.getAmount());
					options.add(option);
				}

				/*
				 * RatedPackageDetail[] rpds = rsd.getRatedPackages(); if (rpds
				 * != null && rpds.length > 0) { print("  RatedPackageDetails",
				 * ""); for (int k = 0; k < rpds.length; k++) {
				 * print("  RatedPackageDetail " + i, ""); RatedPackageDetail
				 * rpd = rpds[k]; PackageRateDetail prd =
				 * rpd.getPackageRateDetail(); //if (prd != null) { //
				 * printWeight("    Billing weight", prd.getBillingWeight()); //
				 * printMoney("    Base charge", prd.getBaseCharge()); //
				 * Surcharge[] surcharges = prd.getSurcharges(); //if
				 * (surcharges != null && surcharges.length > 0) { // for (int m
				 * = 0; m < surcharges.length; m++) { // Surcharge surcharge =
				 * surcharges[m]; // printMoney("    " +
				 * surcharge.getDescription() + " surcharge",
				 * surcharge.getAmount()); // } //} //} } }
				 */
			}
		}

		return options;
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

	private void printNotifications(MerchantStore store,
			Notification[] notifications) {
		if (notifications == null || notifications.length == 0) {
			return;
		}
		for (int i = 0; i < notifications.length; i++) {
			Notification n = notifications[i];

			NotificationSeverityType notificationSeverityType = n.getSeverity();
			// if(notificationSeverityType.equals(NotificationSeverityType.WARNING)||
			// notificationSeverityType.equals(NotificationSeverityType.NOTE) )
			// {

			LogMerchantUtil.log(store.getMerchantId(),
					"Notification from Fedex Code=[" + n.getCode()
							+ "] message[" + n.getMessage() + "]");

			if (notificationSeverityType.equals(NotificationSeverityType.ERROR)
					|| notificationSeverityType
							.equals(NotificationSeverityType.FAILURE)) {
				log.error("FEDEX  Severity: "
						+ (notificationSeverityType == null ? "null"
								: notificationSeverityType.getValue()));
				log.error("FEDEX  Code: " + n.getCode());
				log.error("FEDEX  Message: " + n.getMessage());
				log.error("FEDEX  Source: " + n.getSource());
			}

			// }
		}
	}

	private boolean isResponseOk(
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

}
