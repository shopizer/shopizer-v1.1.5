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
package com.salesmanager.core.service.shipping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductAttribute;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.shipping.PackageDetail;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.entity.shipping.ShippingMethod;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.entity.shipping.ShippingPriceRegion;
import com.salesmanager.core.module.model.application.CalculatePackingModule;
import com.salesmanager.core.module.model.integration.ShippingQuotesModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.common.impl.ServicesUtil;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantException;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantConfigurationDao;
import com.salesmanager.core.service.shipping.impl.AddressValidationContextVO;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.LogMerchantUtil;
import com.salesmanager.core.util.ShippingUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.StringUtil;

@Service
public class ShippingService {

	private static Logger log = Logger.getLogger(ShippingService.class);

	@Autowired
	private IMerchantConfigurationDao merchantConfigurationDao;

	@Transactional
	public boolean isShippingInternational(int merchantId) throws Exception {

		MerchantConfiguration config = merchantConfigurationDao.findByKey(
				ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING, merchantId);

		if (config == null) {
			return true;
		}

		String shippingType = config.getConfigurationValue();
		if (ShippingConstants.INTERNATIONAL_SHIPPING.equals(shippingType)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Calculates shipping cost based on shipping modules configured Will return
	 * ShippingOptions
	 */
	@Transactional
	public ShippingInformation getShippingQuote(
			Collection<OrderProduct> orderProducts, Customer customer,
			int merchantId, Locale locale, String currency) throws Exception {

		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(locale);

		ShippingInformation shippingInfo = new ShippingInformation();

		String shippingType = ShippingConstants.DOMESTIC_SHIPPING;

		Collection<ShippingMethod> shippingMethods = null;

		double shippingWeight = 0;
		int quoteMethodCount = 0;

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		MerchantStore store = mservice.getMerchantStore(merchantId);

		Map allCountries = RefCache.getCountriesMap();
		// origin country
		Country c = (Country) allCountries.get(store.getCountry());
		if (c == null) {
			LogMerchantUtil
					.log(
							merchantId,
							"Cannot identify country id "
									+ store.getCountry()
									+ " please make sure it is configured in Store menu option.");
			log.error("Cannot identify origin countryId " + store.getCountry());
			String message = label.getText(locale, "error.cart.origincountry");
			shippingInfo.setMessage(message);
			return shippingInfo;
		}

		int customerCountryId = customer.getCustomerCountryId();
		// destination
		Country customerCountry = (Country) allCountries.get(customerCountryId);
		if (customerCountry == null) {
			log.error("Cannot identify destination countryId "
					+ customerCountryId);
			String message = label.getText(locale,
					"error.cart.destinationcountry");
			shippingInfo.setMessage(message);
			return shippingInfo;
		}

		if (store.getCountry() != customer.getCustomerCountryId()) {
			shippingType = ShippingConstants.INTERNATIONAL_SHIPPING;
		}

		// get shipping informations
		ConfigurationRequest request = new ConfigurationRequest(merchantId,
				true, "SHP_");
		ConfigurationResponse response = mservice.getConfiguration(request);

		// are there any module configured
		MerchantConfiguration rtconf = response
				.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_RT_MODULE_INDIC_NAME);
		MerchantConfiguration custconf = response
				.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_INDIC_COUNTRIES_COSTS);

		if ((rtconf == null && custconf == null)
				|| (rtconf != null
						&& !StringUtils.isBlank(rtconf.getConfigurationValue()) && rtconf
						.getConfigurationValue().equals("false"))
				&& (custconf != null
						&& !StringUtils.isBlank(custconf
								.getConfigurationValue()) && custconf != null && custconf
						.getConfigurationValue().equals("false"))) {// no module
																	// configured
			LogMerchantUtil
					.log(merchantId, "No shipping module configured yet");
			String message = label.getText(locale,
					"error.cart.noshippingconfigured");
			shippingInfo.setMessage(message);
			return shippingInfo;
		}

		// get shipping type
		// national or international
		MerchantConfiguration shippingTypeConf = response
				.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING);
		if (shippingType != null
				&& StringUtils
						.isBlank(shippingTypeConf.getConfigurationValue())) {
			String sType = shippingTypeConf.getConfigurationValue();

			if (sType.equals(ShippingConstants.DOMESTIC_SHIPPING)
					&& store.getCountry() != customer.getCustomerCountryId()) {
				// set shipping message
				String message = label.getText(locale,
						"error.cart.noshippinginternational");
				shippingInfo.setMessage(message);
				return shippingInfo;
			}

		}

		// is the shipping country from the exclusion list
		MerchantConfiguration excluconf = response
				.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_ZONES_SKIPPED);
		if (excluconf != null
				&& !StringUtils.isBlank(excluconf.getConfigurationValue())) {
			Map excludeMap = StringUtil.parseTokenLine(excluconf
					.getConfigurationValue(), ";");

			if (excludeMap.containsKey(customerCountry.getCountryIsoCode2())) {
				String message = label.getText(locale,
						"error.cart.noshippinginternational");
				shippingInfo.setMessage(message);
				return shippingInfo;
			}
		}

		// Calculate orderTotal for items to be shipped
		BigDecimal orderTotal = new BigDecimal("0");
		Iterator orderProductsIterator = orderProducts.iterator();
		while (orderProductsIterator.hasNext()) {
			OrderProduct op = (OrderProduct) orderProductsIterator.next();
			if (op.isShipping()) {
				BigDecimal finalPrice = op.getFinalPrice();
				BigDecimal priceTax = op.getProductTax();
				orderTotal = orderTotal.add(finalPrice).add(priceTax);
			}
		}

		// invoke packing module for getting details on the packages
		Collection<PackageDetail> packages = null;

		MerchantConfiguration conf = response
				.getMerchantConfiguration(ShippingConstants.PACKING_CONFIGURATION_KEY);
		if (conf != null) {

			String packingModule = conf.getConfigurationValue();
			if (!StringUtils.isBlank(packingModule)) {
				CalculatePackingModule module = (CalculatePackingModule) SpringUtil
						.getBean(packingModule);
				try {
					packages = module.calculatePacking(orderProducts, conf,
							merchantId);
				} catch (Exception e) {

					// use standard packing
					if (!packingModule
							.equals(ShippingConstants.DEFAULT_PACKING_MODULE)) {
						module = (CalculatePackingModule) SpringUtil
								.getBean(ShippingConstants.DEFAULT_PACKING_MODULE);
						packages = module.calculatePacking(orderProducts, conf,
								merchantId);
					}
				}
			}
		}

		if (packages == null) {// calculate packages per item
			packages = new ArrayList();
			orderProductsIterator = orderProducts.iterator();
			while (orderProductsIterator.hasNext()) {
				OrderProduct op = (OrderProduct) orderProductsIterator.next();

				if (!op.isShipping()) {
					continue;
				}

				BigDecimal weight = op.getProductWeight();
				Set attributes = op.getOrderattributes();
				if (attributes != null && attributes.size() > 0) {
					Iterator attributesIterator = attributes.iterator();
					OrderProductAttribute opa = (OrderProductAttribute) attributesIterator
							.next();
					weight = weight.add(opa.getProductAttributeWeight());
				}

				if (op.getProductQuantity() == 1) {
					PackageDetail detail = new PackageDetail();

					detail.setCurrency(currency);
					detail.setShippingHeight(op.getProductHeight()
							.doubleValue());
					detail.setShippingLength(op.getProductLength()
							.doubleValue());
					detail.setShippingWeight(weight.doubleValue());
					detail.setShippingWidth(op.getProductWidth().doubleValue());
					detail.setShippingQuantity(op.getProductQuantity());
					detail.setProductName(op.getProductName());
					packages.add(detail);
				} else if (op.getProductQuantity() > 1) {
					for (int i = 0; i < op.getProductQuantity(); i++) {
						PackageDetail inner = new PackageDetail();
						inner.setCurrency(currency);
						inner.setShippingHeight(op.getProductHeight()
								.doubleValue());
						inner.setShippingLength(op.getProductLength()
								.doubleValue());
						inner.setShippingWeight(weight.doubleValue());
						inner.setShippingWidth(op.getProductWidth()
								.doubleValue());
						inner.setShippingQuantity(op.getProductQuantity());
						inner.setProductName(op.getProductName());
						packages.add(inner);
					}
				}
			}
		}

		if (packages != null) {
			Iterator packIter = packages.iterator();
			while (packIter.hasNext()) {
				PackageDetail detail = (PackageDetail) packIter.next();
				detail.setProductName("N/A");
				shippingWeight = shippingWeight + detail.getShippingWeight();
			}
		}

		// tax
		MerchantConfiguration taxconf = response
				.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_TAX_CLASS);
		if (taxconf != null
				&& !StringUtils.isBlank(taxconf.getConfigurationValue())) {

			long taxClassId = Long.parseLong(taxconf.getConfigurationValue());
			shippingInfo.setTaxClass(taxClassId);

		}

		// handling fee
		BigDecimal handling = new BigDecimal("0");
		MerchantConfiguration handlingconf = response
				.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_HANDLING_FEES);
		if (handlingconf != null
				&& !StringUtils.isBlank(handlingconf.getConfigurationValue())) {

			String shandling = handlingconf.getConfigurationValue();
			try {
				handling = new BigDecimal(shandling);
				if (handling.doubleValue() > 0) {
					shippingInfo.setHandlingCost(handling);
					shippingInfo.setHandlingCostText(CurrencyUtil
							.displayFormatedAmountWithCurrency(handling,
									currency));
				}
			} catch (Exception e) {
				log.error("Cannot parse handling fee to BigDecimal "
						+ shandling);
			}

		}

		// invoke configured modules
		List configList = response.getMerchantConfigurationList();
		Iterator configListIterator = configList.iterator();
		while (configListIterator.hasNext()) {

			MerchantConfiguration gatewayconf = (MerchantConfiguration) configListIterator
					.next();
			if (gatewayconf.getConfigurationKey().equals(
					ShippingConstants.MODULE_SHIPPING_RT_MODULE_INDIC_NAME)) {
				if (gatewayconf != null
						&& !StringUtils.isBlank(gatewayconf
								.getConfigurationValue())
						&& gatewayconf.getConfigurationValue().equals("true")) {

					String module = gatewayconf.getConfigurationValue1();
					if (StringUtils.isBlank(module)) {
						log
								.warn("RT Shipping module is not configured appropriatly, the column module should contain module name, see configuration id "
										+ gatewayconf.getConfigurationId());
					} else {
						ShippingQuotesModule shippingModule = (ShippingQuotesModule) SpringUtil
								.getBean(module);
						if (shippingModule == null) {
							log.error("Shipping module " + module
									+ " is not defined in sm-modules.xml");
						} else {

							// check if module can be applied to store
							CoreModuleService cms = getRealTimeQuoteShippingService(
									CountryUtil.getCountryIsoCodeById(store
											.getCountry()), module);

							if (cms != null) {// this shipping method cannot be
												// used

								Collection rtOptions = shippingModule
										.getShippingQuote(response, orderTotal,
												packages, customer, store,
												locale);

								if (rtOptions != null) {

									// check for quote comparaison
									MerchantConfiguration rtdetails = response
											.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_DISPLAY_REALTIME_QUOTES);
									int quoteDisplayType = ShippingConstants.ALL_QUOTES_DISPLAYED;
									if (rtdetails != null) {

										if (!StringUtils.isBlank(rtdetails
												.getConfigurationValue2())) {// display
																				// or
																				// not
																				// quotes
											try {
												quoteDisplayType = Integer
														.parseInt(rtdetails
																.getConfigurationValue2());

											} catch (Exception e) {
												log
														.error("Display quote types is not an integer value ["
																+ rtdetails
																		.getConfigurationValue2()
																+ "]");
											}
										}
									}

									if (quoteDisplayType == ShippingConstants.LESS_EXPENSIVE_QUOTE_DISPLAYED
											|| quoteDisplayType == ShippingConstants.MAX_EXPENSIVE_QUOTE_DISPLAYED) {

										Iterator rtOptionsIterator = rtOptions
												.iterator();
										ShippingOption currentOption = null;
										while (rtOptionsIterator.hasNext()) {
											ShippingOption option = (ShippingOption) rtOptionsIterator
													.next();
											if (currentOption == null) {
												currentOption = option;
											}
											if (quoteDisplayType == ShippingConstants.LESS_EXPENSIVE_QUOTE_DISPLAYED) {
												if (option.getOptionPrice()
														.longValue() < currentOption
														.getOptionPrice()
														.longValue()) {
													currentOption = option;
												}
											} else if (quoteDisplayType == ShippingConstants.MAX_EXPENSIVE_QUOTE_DISPLAYED) {
												if (option.getOptionPrice()
														.longValue() > currentOption
														.getOptionPrice()
														.longValue()) {
													currentOption = option;
												}
											}

										}
										rtOptions = new ArrayList();
										rtOptions.add(currentOption);
									}

									ShippingMethod method = new ShippingMethod();
									method.setShippingModule(module);
									method
											.setShippingMethodName(shippingModule
													.getShippingMethodDescription(locale));

									quoteMethodCount++;
									if (shippingMethods == null) {
										shippingMethods = new ArrayList();
									}
									method.setOptions(rtOptions);
									method.setImage(cms
											.getCoreModuleServiceLogoPath());
									method.setPriority(0);

									shippingMethods.add(method);
								}

							}
						}
					}
				}
			}
		}

		// invoke custom module
		MerchantConfiguration customconf = response
				.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_INDIC_COUNTRIES_COSTS);
		if (customconf != null
				&& !StringUtils.isBlank(customconf.getConfigurationValue())
				&& customconf.getConfigurationValue().equals("true")) {

			// parse first column
			Map cs = StringUtil.parseTokenLine(customconf
					.getConfigurationValue1(), "|");
			
			Map countries = new HashMap();
			
			if(cs!=null && cs.size()>0) {
				
				
				for(Object o: cs.keySet()) {
					String k = (String)o;
					if(!k.contains(";")) {
						countries.put(k,(Integer)cs.get(k));
						continue;
					}
					StringTokenizer st = new StringTokenizer(k,";");
					while(st.hasMoreTokens()) {
						String t = st.nextToken();
						countries.put(t, (Integer)cs.get(k));
					}
				}
			}

			if (countries.containsKey(customerCountry.getCountryIsoCode2())) {

				// get estimate
				String shippingEstimateLine = null;
				String shippingCostLine = customconf.getConfigurationValue1();
				MerchantConfiguration estimateCountryConf = response
						.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_ESTIMATE_BYCOUNTRY);
				if (estimateCountryConf!=null && !StringUtils.isBlank(estimateCountryConf
						.getConfigurationValue1())) {
					shippingEstimateLine = estimateCountryConf
							.getConfigurationValue1();
				}
				// contains index - ShippingPriceRegion
				Map priceTimeMap = ShippingUtil.buildShippingPriceRegionMap("",
						shippingCostLine, shippingEstimateLine);

				// determine index
				int index = -1;
				if (!StringUtils.isBlank(shippingCostLine)) {
					StringTokenizer cvtk = new StringTokenizer(
							shippingCostLine, "|");
					int count = 1;
					while (cvtk.hasMoreTokens()) {
						if (index != -1) {
							break;
						}
						String countryline = cvtk.nextToken();// maxpound:price,maxpound:price...|
						if (!countryline.equals("*")) {
							StringTokenizer countrystk = new StringTokenizer(
									countryline, ";");
							String country = null;
							while (countrystk.hasMoreTokens()) {
								country = countrystk.nextToken();
								if (customerCountry.getCountryIsoCode2() != null
										&& country.equals(customerCountry
												.getCountryIsoCode2())) {
									index = count;
									break;
								}
							}
						}
						count++;
					}
				}

				int iposition = (Integer) countries.get(customerCountry
						.getCountryIsoCode2());// index
				// need to get the prices / pounds
				Map data = StringUtil.parseTokenLine(customconf
						.getConfigurationValue2(), "|");
				Map swapedData = ((BidiMap) data).inverseBidiMap();
				String countryData = (String) swapedData.get(iposition);

				// this line is in the form [MAX_POUND:PRICE;MAX_POUND2:PRICE]
				// get boundaries
				ShippingMethod method = new ShippingMethod();
				method.setShippingModule("custom");
				String description = label.getText(locale,
						"message.cart.sellershippingcost")
						+ " "
						+ customerCountry.getCountryName()
						+ " "
						+ customer.getCustomerPostalCode();
				method.setPriority(1);

				if (shippingMethods == null || shippingMethods.size() == 0) {
					method.setPriority(0);
				}

				method.setShippingMethodName(description);
				StringTokenizer st = new StringTokenizer(countryData, ";");
				int lowBoundary = 0;
				while (st.hasMoreTokens()) {
					String token = st.nextToken();// should have MAX_POUND:PRICE
					try {
						int position = token.indexOf(":");
						if (position > -1) {
							String spound = token.substring(0, token
									.indexOf(":"));
							if (spound != null) {
								int highBoundary = Integer.parseInt(spound);
								if (lowBoundary <= shippingWeight
										&& highBoundary >= shippingWeight) {
									String sprice = token.substring(
											position + 1, token.length());
									ShippingOption option = new ShippingOption();
									option.setCurrency(store.getCurrency());
									option.setOptionCode("custom");
									option.setOptionId("custom-" + lowBoundary);
									option
											.setOptionPrice(new BigDecimal(
													sprice));
									option.setDescription(description);
									method.addOption(option);
									if (shippingMethods == null) {
										shippingMethods = new ArrayList();
									}
									shippingMethods.add(method);

									// get the estimate
									if (priceTimeMap != null
											&& priceTimeMap.size() > 0) {

										ShippingPriceRegion spr = (ShippingPriceRegion) priceTimeMap
												.get(index);
										if (spr != null
												&& spr.getMinDays() != -1
												&& spr.getMaxDays() != -1) {
											String returnText = "";

											if (locale == null) {
												locale = LocaleUtil
														.getDefaultLocale();
											}

											try {

												if (spr.getMinDays() == spr
														.getMaxDays()) {
													List parameters = new ArrayList();
													parameters
															.add(customerCountry
																	.getCountryIsoCode2());
													parameters.add(spr
															.getMaxDays());
													returnText = label
															.getText(
																	locale,
																	"message.delivery.estimate.precise",
																	parameters);
												} else {
													List parameters = new ArrayList();
													parameters
															.add(customerCountry
																	.getCountryIsoCode2());
													parameters.add(spr
															.getMinDays());
													parameters.add(spr
															.getMaxDays());
													returnText = label
															.getText(
																	locale,
																	"message.delivery.estimate.range",
																	parameters);
												}
												option
														.setEstimatedNumberOfDays(returnText);

											} catch (Exception e) {
												log
														.error("Error assigning parameters for shipping");
											}

										}
									}

									quoteMethodCount++;
									break;
								}
								lowBoundary = highBoundary;
							}
						}
					} catch (Exception e) {
						log.error("Price Pound parsing error " + e);
					}
				}
			}

		}

		if (quoteMethodCount == 0) {
			String message = LabelUtil.getInstance().getText(locale,
					"error.cart.noshippingconfigured");
			shippingInfo.setMessage(message);
			return shippingInfo;
		}

		if (quoteMethodCount > 0) {

			// is it free ?
			MerchantConfiguration freeconf = response
					.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_FREE_IND_DEST_AMNT);
			if (freeconf != null
					&& !StringUtils.isBlank(freeconf.getConfigurationValue())
					&& freeconf.getConfigurationValue().equals("true")) {
				// get national or international
				String freeShippingType = freeconf.getConfigurationValue1();
				if (!StringUtils.isBlank(freeShippingType)) {
					// if shipping is domestic and free ship is domestic, then
					// ok
					// if shipping is domestic and free ship is intl, then ok
					// if shipping is intl and free ship is intl, then ok
					if (shippingType
							.equals(ShippingConstants.DOMESTIC_SHIPPING)
							|| freeShippingType.equals(shippingType)) {
						// get amount
						if (!StringUtils.isBlank(freeconf
								.getConfigurationValue2())) {
							BigDecimal freeAmount = new BigDecimal(freeconf
									.getConfigurationValue2()).setScale(2);
							if (orderTotal.floatValue()
									- freeAmount.floatValue() > 0) {

								// remove live quotes
								shippingInfo.setShippingMethods(null);

								String message = LabelUtil.getInstance()
										.getText(locale,
												"message.cart.freeshipping")
										+ " "
										+ CurrencyUtil
												.displayFormatedAmountWithCurrency(
														freeAmount, currency);
								shippingInfo.setMessage(message);
								BigDecimal freeShippingCost = new BigDecimal(
										"0");

								List optList = new ArrayList();

								ShippingMethod method = new ShippingMethod();
								method.setShippingMethodName(LabelUtil
										.getInstance().getText(locale,
												"label.cart.freeshipping"));
								method.setShippingModule("free");
								method.setPriority(0);
								ShippingOption option = new ShippingOption();
								option.setOptionId("free");
								option.setCurrency(currency);
								option.setOptionCode("free");
								option.setModule("free");
								option.setOptionPrice(new BigDecimal("0"));
								option.setDescription(LabelUtil.getInstance()
										.getText(locale,
												"label.cart.freeshipping")
										+ " "
										+ customerCountry.getCountryName()
										+ " "
										+ customer.getCustomerPostalCode());
								optList.add(option);
								method.setOptions(optList);

								shippingMethods = new ArrayList();

								shippingMethods.add(method);

								shippingInfo.setShippingCost(freeShippingCost);
								shippingInfo.setShippingCostText(CurrencyUtil
										.displayFormatedAmountWithCurrency(
												freeShippingCost, currency));
								shippingInfo.setFreeShipping(true);

								// no handling fee
								shippingInfo
										.setHandlingCost(new BigDecimal("0"));

							}
						}

					}
				}

			}

		}

		if (shippingMethods != null) {
			shippingInfo.setShippingMethods(shippingMethods);
		}

		return shippingInfo;

	}

	@Transactional
	public void setShippingTaxClass(long taxClassId, int merchantId)
			throws Exception {

		ConfigurationRequest request = new ConfigurationRequest(merchantId,
				false, "SHP_TAX_CLASS");

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		ConfigurationResponse configuration = mservice
				.getConfiguration(request);

		if (configuration != null) {
			MerchantConfiguration config = configuration
					.getMerchantConfiguration("SHP_TAX_CLASS");
			if (config != null) {
				config.setConfigurationValue("SHP_TAX_CLASS");
				merchantConfigurationDao.saveOrUpdate(config);
			}
		}

	}

	/**
	 * Retreives a list of shipping module name configured through central admin
	 * tool
	 * 
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	public Collection<String> getShippingModulesNamesConfigured(int merchantId)
			throws Exception {

		ConfigurationRequest request = new ConfigurationRequest(merchantId,
				true, "SHP_");
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		ConfigurationResponse response = mservice.getConfiguration(request);

		List modules = new ArrayList();

		if (response != null) {
			List config = response.getMerchantConfigurationList();
			if (config != null) {

				// Iterator it = config.iterator();
				Iterator it = config.iterator();
				while (it.hasNext()) {

					MerchantConfiguration c = (MerchantConfiguration) it.next();
					String key = c.getConfigurationKey();

					// custom configured
					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_INDIC_COUNTRIES_COSTS)) {// Custom
																								// costs
						if (!StringUtils.isBlank(c.getConfigurationValue())
								&& c.getConfigurationValue().equals("true")) {
							modules.add("custom");
						}
					}

					// real time
					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_RT_MODULE_INDIC_NAME)) {

						if (!StringUtils.isBlank(c.getConfigurationValue())
								&& c.getConfigurationValue().equals("true")) {
							modules.add(c.getConfigurationValue1());
						}
					}

				}
			}

		}
		return modules;
	}

	/**
	 * Get a List of CentralIntegrationService containing RT Shipping Quote
	 * integration systems given the country code
	 * 
	 * @return
	 * @throws ShippingException
	 */
	public List<CoreModuleService> getRealTimeQuoteShippingServices(
			String countryIsoCode) throws ShippingException {

		return ServicesUtil.getShippingRealTimeQuotesMethods(countryIsoCode);

	}

	/**
	 * Get a CoreModuleService entity for a given countryCode
	 * 
	 * @param countryIsoCode
	 * @param moduleName
	 * @return
	 * @throws Exception
	 */
	public CoreModuleService getRealTimeQuoteShippingService(
			String countryIsoCode, String moduleName) throws Exception {

		return ServicesUtil.getModule(countryIsoCode, moduleName);
	}

	/**
	 * Will update current user shipping zones and costs define in Merchant
	 * Configuration for beinng domestic. This method will remove all configured
	 * costs and zones that are not domestic from an existing configuration
	 */
	@Transactional
	public void updateShippingZonesAndCostsForDomestic(int merchantId,
			int countryId) throws MerchantException {
		// Get countries and costs
		MerchantConfiguration countriescosts = merchantConfigurationDao
				.findByKey(
						ShippingConstants.MODULE_SHIPPING_INDIC_COUNTRIES_COSTS,
						merchantId);

		// Get the current user country (national)
		Map countries = RefCache.getCountriesMap();
		Country currentcountry = (Country) countries.get(countryId);
		java.util.Date dt = new java.util.Date();

		if (countriescosts != null) {// countries and cost are defined

			String value1 = countriescosts.getConfigurationValue1();
			String value2 = countriescosts.getConfigurationValue2();
			StringTokenizer st = new StringTokenizer(value1, "|");
			int pos = 0;
			boolean foundcountry = false;
			// Take care of changing the country
			while (st.hasMoreTokens() && !foundcountry) {
				String token = st.nextToken();
				StringTokenizer subst = new StringTokenizer(token, ";");
				if (subst.hasMoreTokens()) {
					while (subst.hasMoreTokens() && !foundcountry) {
						String subtoken = subst.nextToken();
						if (subtoken
								.equals(currentcountry.getCountryIsoCode2())) {
							foundcountry = true;
							break;
						}
					}
				} else {
					if (subst.equals(currentcountry.getCountryIsoCode2())) {
						foundcountry = true;
						break;
					}
				}
				if (!foundcountry)
					pos++;
			}

			// Take care of changing the price
			if (value2 != null) {
				if (foundcountry) {
					// - overwrite domestic price in MODULE_SHIPPING_ZONES_COST
					int cpos = 0;
					StringTokenizer cvtk = new StringTokenizer(value2, "|");
					String costtokenvalue = null;
					while (cvtk.hasMoreTokens()) {
						costtokenvalue = cvtk.nextToken();
						if (cpos == pos) {
							break;
						}
						cpos++;
					}

					// overwrite everything with
					if (costtokenvalue != null) {
						countriescosts
								.setConfigurationValue1(new StringBuffer()
										.append(
												currentcountry
														.getCountryIsoCode2())
										.append("|*|*|*|*").toString());
						countriescosts.setConfigurationValue2(costtokenvalue);
						countriescosts.setLastModified(new java.util.Date(
								new Date().getTime()));
						merchantConfigurationDao.saveOrUpdate(countriescosts);
					}
				} else {// no country found
					countriescosts.setConfigurationValue1(new StringBuffer()
							.append(currentcountry.getCountryIsoCode2())
							.append("|*|*|*|*").toString());
					countriescosts.setLastModified(new java.util.Date(
							new Date().getTime()));
					countriescosts
							.setConfigurationValue2("5:0;10:0;99:0|*|*|*|*");
					merchantConfigurationDao.saveOrUpdate(countriescosts);

				}
			} else {// No price yet defined
				countriescosts.setConfigurationValue1(new StringBuffer()
						.append(currentcountry.getCountryIsoCode2()).append(
								"|*|*|*|*").toString());
				countriescosts.setConfigurationValue2("5:0;10:0;99:0|*|*|*|*");
				countriescosts.setLastModified(new java.util.Date(new Date()
						.getTime()));
				countriescosts.setConfigurationModule("");
				countriescosts.setMerchantId(merchantId);
				countriescosts
						.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_INDIC_COUNTRIES_COSTS);
				merchantConfigurationDao.saveOrUpdate(countriescosts);
			}
		} else {// First time you come in

			merchantConfigurationDao.deleteLike("SHP_ZONES_", merchantId);
			MerchantConfiguration zones = new MerchantConfiguration();
			zones.setConfigurationValue1(new StringBuffer().append(
					currentcountry.getCountryIsoCode2()).append("|*|*|*|*")
					.toString());
			zones.setConfigurationValue2("5:0;10:0;99:0|*|*|*|*");
			zones.setMerchantId(merchantId);
			zones
					.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_INDIC_COUNTRIES_COSTS);
			zones.setConfigurationModule("");
			zones.setDateAdded(new java.util.Date(dt.getTime()));
			zones.setLastModified(new java.util.Date(dt.getTime()));
			merchantConfigurationDao.persist(zones);

		}

		// Add or update MerchantConfiguration for shipping domestic
		MerchantConfiguration conf = merchantConfigurationDao.findByKey(
				ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING, merchantId);
		if (conf != null
				&& !conf.getConfigurationValue().equals(
						ShippingConstants.DOMESTIC_SHIPPING)) {// already
																// configured
			conf.setConfigurationValue(ShippingConstants.DOMESTIC_SHIPPING);
			conf.setLastModified(new java.util.Date(dt.getTime()));
		} else {
			conf = new MerchantConfiguration();
			conf.setConfigurationModule("");
			conf
					.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING);
			conf.setConfigurationValue(ShippingConstants.DOMESTIC_SHIPPING);
			conf.setDateAdded(new java.util.Date(dt.getTime()));
			conf.setMerchantId(merchantId);
			conf.setLastModified(new java.util.Date(dt.getTime()));
		}
		merchantConfigurationDao.saveOrUpdate(conf);

	}

}
