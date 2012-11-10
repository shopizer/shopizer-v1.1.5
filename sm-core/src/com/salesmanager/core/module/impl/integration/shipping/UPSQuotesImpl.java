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

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.digester.Digester;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.entity.shipping.PackageDetail;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.module.model.integration.ShippingQuotesModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.common.CommonService;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LogMerchantUtil;

public class UPSQuotesImpl implements ShippingQuotesModule {

	private Logger log = Logger.getLogger(UPSQuotesImpl.class);

	public String getShippingMethodDescription(Locale locale) {
		return LabelUtil.getInstance().getText(locale, "module.upsxml");
	}

	private String getPackageCode(String codeId) {

		if (StringUtils.isBlank(codeId)) {
			log.warn("codeId is blank or null, will return standard overnight");

		}

		/**
		 *'00' (unknown), '01' (UPS letter), '02' (customer supplied package),
		 * '03'(tube), '04' (PAK), '21' (UPS express box), '2a' (UPS small
		 * express box), '2b' (UPS medium express box), '2c' (UPS large express
		 * box), '24' (UPS 25KG box), '25' (UPS 10KG box), '2a' (small express
		 * box), '2b' (medium express box), '2c' (large express box), or '30'
		 * (pallet)
		 */

		return null;

	}

	protected String getHeader(int merchantid, ConfigurationResponse vo)
			throws Exception {

		IntegrationKeys sk = (IntegrationKeys) vo
				.getConfiguration("upsxml-keys");

		if (sk == null) {
			throw new Exception(
					"Integration keys are null from configuration request upsxml");
		}

		StringBuffer xmlreqbuffer = new StringBuffer();
		xmlreqbuffer.append("<?xml version=\"1.0\"?>");
		xmlreqbuffer.append("<AccessRequest>");
		xmlreqbuffer.append("<AccessLicenseNumber>");
		xmlreqbuffer.append(sk.getKey1());
		xmlreqbuffer.append("</AccessLicenseNumber>");
		xmlreqbuffer.append("<UserId>");
		xmlreqbuffer.append(sk.getUserid());
		xmlreqbuffer.append("</UserId>");
		xmlreqbuffer.append("<Password>");
		xmlreqbuffer.append(sk.getPassword());
		xmlreqbuffer.append("</Password>");
		xmlreqbuffer.append("</AccessRequest>");

		return xmlreqbuffer.toString();

	}

	public Collection<ShippingOption> getShippingQuote(
			ConfigurationResponse config, BigDecimal orderTotal,
			Collection<PackageDetail> packages, Customer customer,
			MerchantStore store, Locale locale) {

		CoreModuleService cis = null;

		StringBuffer xmlbuffer = new StringBuffer();
		BufferedReader reader = null;
		PostMethod httppost = null;

		try {

			CommonService cservice = (CommonService) ServiceFactory
					.getService(ServiceFactory.CommonService);

			String countrycode = CountryUtil.getCountryIsoCodeById(store
					.getCountry());
			cis = cservice.getModule(countrycode, "upsxml");

			if (cis == null) {
				log.error("Can't retreive an integration service [countryid "
						+ store.getCountry() + " ups subtype 1]");
				// throw new
				// Exception("UPS getQuote Can't retreive an integration service");
			}

			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			ConfigurationRequest request_prefs = new ConfigurationRequest(store
					.getMerchantId(),
					ShippingConstants.MODULE_SHIPPING_RT_PKG_DOM_INT);
			ConfigurationResponse vo_prefs = service
					.getConfiguration(request_prefs);

			String pack = (String) vo_prefs.getConfiguration("package-upsxml");
			if (pack == null) {
				log
						.debug("Will assign packaging type 02 to UPS shipping for merchantid "
								+ store.getMerchantId());
				pack = "02";
			}

			ConfigurationRequest request = new ConfigurationRequest(store
					.getMerchantId(), ShippingConstants.MODULE_SHIPPING_RT_CRED);
			ConfigurationResponse vo = service.getConfiguration(request);

			if (vo == null) {
				throw new Exception("ConfigurationVO is null upsxml");
			}

			String xmlhead = getHeader(store.getMerchantId(), vo);

			String weightCode = store.getWeightunitcode();
			String measureCode = store.getSeizeunitcode();

			if (weightCode.equals("KG")) {
				weightCode = "KGS";
			} else {
				weightCode = "LBS";
			}

			String xml = "<?xml version=\"1.0\"?><RatingServiceSelectionRequest><Request><TransactionReference><CustomerContext>SalesManager Data</CustomerContext><XpciVersion>1.0001</XpciVersion></TransactionReference><RequestAction>Rate</RequestAction><RequestOption>Shop</RequestOption></Request>";
			StringBuffer xmldatabuffer = new StringBuffer();

			/**
			 * <Shipment>
			 * 
			 * <Shipper> <Address> <City></City>
			 * <StateProvinceCode>QC</StateProvinceCode>
			 * <CountryCode>CA</CountryCode> <PostalCode></PostalCode>
			 * </Address> </Shipper>
			 * 
			 * <ShipTo> <Address> <City>Redwood Shores</City>
			 * <StateProvinceCode>CA</StateProvinceCode>
			 * <CountryCode>US</CountryCode> <PostalCode></PostalCode>
			 * <ResidentialAddressIndicator/> </Address> </ShipTo>
			 * 
			 * <Package> <PackagingType> <Code>21</Code> </PackagingType>
			 * <PackageWeight> <UnitOfMeasurement> <Code>LBS</Code>
			 * </UnitOfMeasurement> <Weight>1.1</Weight> </PackageWeight>
			 * <PackageServiceOptions> <InsuredValue>
			 * <CurrencyCode>CAD</CurrencyCode>
			 * <MonetaryValue>100</MonetaryValue> </InsuredValue>
			 * </PackageServiceOptions> </Package>
			 * 
			 * 
			 * </Shipment>
			 * 
			 * <CustomerClassification> <Code>03</Code>
			 * </CustomerClassification> </RatingServiceSelectionRequest>
			 * **/

			Map countriesMap = (Map) RefCache.getAllcountriesmap(LanguageUtil
					.getLanguageNumberCode(locale.getLanguage()));
			Map zonesMap = (Map) RefCache.getAllZonesmap(LanguageUtil
					.getLanguageNumberCode(locale.getLanguage()));

			Country storeCountry = (Country) countriesMap.get(store
					.getCountry());

			Country customerCountry = (Country) countriesMap.get(customer
					.getCustomerCountryId());

			int sZone = -1;
			try {
				sZone = Integer.parseInt(store.getZone());
			} catch (Exception e) {
				// TODO: handle exception
			}

			Zone storeZone = (Zone) zonesMap.get(sZone);
			Zone customerZone = (Zone) zonesMap.get(customer
					.getCustomerZoneId());

			xmldatabuffer.append("<PickupType><Code>03</Code></PickupType>");
			// xmldatabuffer.append("<Description>Daily Pickup</Description>");
			xmldatabuffer.append("<Shipment><Shipper>");
			xmldatabuffer.append("<Address>");
			xmldatabuffer.append("<City>");
			xmldatabuffer.append(store.getStorecity());
			xmldatabuffer.append("</City>");
			// if(!StringUtils.isBlank(store.getStorestateprovince())) {
			if (storeZone != null) {
				xmldatabuffer.append("<StateProvinceCode>");
				xmldatabuffer.append(storeZone.getZoneCode());// zone code
				xmldatabuffer.append("</StateProvinceCode>");
			}
			xmldatabuffer.append("<CountryCode>");
			xmldatabuffer.append(storeCountry.getCountryIsoCode2());
			xmldatabuffer.append("</CountryCode>");
			xmldatabuffer.append("<PostalCode>");
			xmldatabuffer.append(com.salesmanager.core.util.ShippingUtil
					.trimPostalCode(store.getStorepostalcode()));
			xmldatabuffer.append("</PostalCode></Address></Shipper>");

			// ship to
			xmldatabuffer.append("<ShipTo>");
			xmldatabuffer.append("<Address>");
			xmldatabuffer.append("<City>");
			xmldatabuffer.append(customer.getCustomerCity());
			xmldatabuffer.append("</City>");
			// if(!StringUtils.isBlank(customer.getCustomerState())) {
			if (customerZone != null) {
				xmldatabuffer.append("<StateProvinceCode>");
				xmldatabuffer.append(customerZone.getZoneCode());// zone code
				xmldatabuffer.append("</StateProvinceCode>");
			}
			xmldatabuffer.append("<CountryCode>");
			xmldatabuffer.append(customerCountry.getCountryIsoCode2());
			xmldatabuffer.append("</CountryCode>");
			xmldatabuffer.append("<PostalCode>");
			xmldatabuffer.append(com.salesmanager.core.util.ShippingUtil
					.trimPostalCode(customer.getCustomerPostalCode()));
			xmldatabuffer.append("</PostalCode></Address></ShipTo>");
			// xmldatabuffer.append("<Service><Code>11</Code></Service>");

			Iterator packagesIterator = packages.iterator();
			while (packagesIterator.hasNext()) {

				PackageDetail detail = (PackageDetail) packagesIterator.next();
				xmldatabuffer.append("<Package>");
				xmldatabuffer.append("<PackagingType>");
				xmldatabuffer.append("<Code>");
				xmldatabuffer.append(pack);
				xmldatabuffer.append("</Code>");
				xmldatabuffer.append("</PackagingType>");

				// weight
				xmldatabuffer.append("<PackageWeight>");
				xmldatabuffer.append("<UnitOfMeasurement>");
				xmldatabuffer.append("<Code>");
				xmldatabuffer.append(weightCode);
				xmldatabuffer.append("</Code>");
				xmldatabuffer.append("</UnitOfMeasurement>");
				xmldatabuffer.append("<Weight>");
				xmldatabuffer.append(new BigDecimal(detail.getShippingWeight())
						.setScale(1, BigDecimal.ROUND_HALF_UP));
				xmldatabuffer.append("</Weight>");
				xmldatabuffer.append("</PackageWeight>");

				// dimension
				xmldatabuffer.append("<Dimensions>");
				xmldatabuffer.append("<UnitOfMeasurement>");
				xmldatabuffer.append("<Code>");
				xmldatabuffer.append(measureCode);
				xmldatabuffer.append("</Code>");
				xmldatabuffer.append("</UnitOfMeasurement>");
				xmldatabuffer.append("<Length>");
				xmldatabuffer.append(new BigDecimal(detail.getShippingLength())
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				xmldatabuffer.append("</Length>");
				xmldatabuffer.append("<Width>");
				xmldatabuffer.append(new BigDecimal(detail.getShippingWidth())
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				xmldatabuffer.append("</Width>");
				xmldatabuffer.append("<Height>");
				xmldatabuffer.append(new BigDecimal(detail.getShippingHeight())
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				xmldatabuffer.append("</Height>");
				xmldatabuffer.append("</Dimensions>");
				xmldatabuffer.append("</Package>");

			}

			xmldatabuffer.append("</Shipment>");
			xmldatabuffer.append("</RatingServiceSelectionRequest>");

			xmlbuffer.append(xmlhead).append(xml).append(
					xmldatabuffer.toString());

			log.debug("UPS QUOTE REQUEST " + xmlbuffer.toString());

			String data = "";

			IntegrationKeys keys = (IntegrationKeys) config
					.getConfiguration("upsxml-keys");

			IntegrationProperties props = (IntegrationProperties) config
					.getConfiguration("upsxml-properties");

			String host = cis.getCoreModuleServiceProdDomain();
			String protocol = cis.getCoreModuleServiceProdProtocol();
			String port = cis.getCoreModuleServiceProdPort();
			String uri = cis.getCoreModuleServiceProdEnv();

			if (props.getProperties1().equals(
					String.valueOf(ShippingConstants.TEST_ENVIRONMENT))) {
				host = cis.getCoreModuleServiceDevDomain();
				protocol = cis.getCoreModuleServiceDevProtocol();
				port = cis.getCoreModuleServiceDevPort();
				uri = cis.getCoreModuleServiceDevEnv();
			}

			HttpClient client = new HttpClient();
			httppost = new PostMethod(protocol + "://" + host + ":" + port
					+ uri);
			RequestEntity entity = new StringRequestEntity(
					xmlbuffer.toString(), "text/plain", "UTF-8");
			httppost.setRequestEntity(entity);

			int result = client.executeMethod(httppost);
			if (result != 200) {
				log.error("Communication Error with ups quote " + result + " "
						+ protocol + "://" + host + ":" + port + uri);
				throw new Exception("UPS quote communication error " + result);
			}
			data = httppost.getResponseBodyAsString();
			log.debug("ups quote response " + data);

			UPSParsedElements parsed = new UPSParsedElements();

			Digester digester = new Digester();
			digester.push(parsed);
			digester.addCallMethod(
					"RatingServiceSelectionResponse/Response/Error",
					"setErrorCode", 0);
			digester.addCallMethod(
					"RatingServiceSelectionResponse/Response/ErrorDescriprion",
					"setError", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/Response/ResponseStatusCode",
							"setStatusCode", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/Response/ResponseStatusDescription",
							"setStatusMessage", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/Response/Error/ErrorDescription",
							"setError", 0);

			digester.addObjectCreate(
					"RatingServiceSelectionResponse/RatedShipment",
					com.salesmanager.core.entity.shipping.ShippingOption.class);
			// digester.addSetProperties(
			// "RatingServiceSelectionResponse/RatedShipment", "sequence",
			// "optionId" );
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/RatedShipment/Service/Code",
							"setOptionId", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/RatedShipment/TotalCharges/MonetaryValue",
							"setOptionPriceText", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/RatedShipment/TotalCharges/CurrencyCode",
							"setCurrency", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/RatedShipment/Service/Code",
							"setOptionCode", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/RatedShipment/GuaranteedDaysToDelivery",
							"setEstimatedNumberOfDays", 0);
			digester.addSetNext("RatingServiceSelectionResponse/RatedShipment",
					"addOption");

			// <?xml
			// version="1.0"?><AddressValidationResponse><Response><TransactionReference><CustomerContext>SalesManager
			// Data</CustomerContext><XpciVersion>1.0</XpciVersion></TransactionReference><ResponseStatusCode>0</ResponseStatusCode><ResponseStatusDescription>Failure</ResponseStatusDescription><Error><ErrorSeverity>Hard</ErrorSeverity><ErrorCode>10002</ErrorCode><ErrorDescription>The
			// XML document is well formed but the document is not
			// valid</ErrorDescription><ErrorLocation><ErrorLocationElementName>AddressValidationRequest</ErrorLocationElementName></ErrorLocation></Error></Response></AddressValidationResponse>

			Reader xmlreader = new StringReader(data);

			digester.parse(xmlreader);

			if (!StringUtils.isBlank(parsed.getErrorCode())) {
				log.error("Can't process UPS statusCode="
						+ parsed.getErrorCode() + " message= "
						+ parsed.getError());
				return null;
			}
			if (!StringUtils.isBlank(parsed.getStatusCode())
					&& !parsed.getStatusCode().equals("1")) {
				LogMerchantUtil.log(store.getMerchantId(),
						"Can't process UPS statusCode="
								+ parsed.getStatusCode() + " message= "
								+ parsed.getError());
				log.error("Can't process UPS statusCode="
						+ parsed.getStatusCode() + " message= "
						+ parsed.getError());
				return null;
			}

			if (parsed.getOptions() == null || parsed.getOptions().size() == 0) {
				log.warn("No options returned from UPS");
				return null;
			}

			String carrier = getShippingMethodDescription(locale);
			// cost is in CAD, need to do conversion

			/*
			 * boolean requiresCurrencyConversion = false; String storeCurrency
			 * = store.getCurrency();
			 * if(!storeCurrency.equals(Constants.CURRENCY_CODE_CAD)) {
			 * requiresCurrencyConversion = true; }
			 */

			LabelUtil labelUtil = LabelUtil.getInstance();
			Map serviceMap = com.salesmanager.core.util.ShippingUtil
					.buildServiceMap("upsxml", locale);

			/** Details on whit RT quote information to display **/
			MerchantConfiguration rtdetails = config
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

			Collection returnColl = null;

			List options = parsed.getOptions();
			if (options != null) {

				Map selectedintlservices = (Map) config
						.getConfiguration("service-global-upsxml");

				Iterator i = options.iterator();
				while (i.hasNext()) {
					ShippingOption option = (ShippingOption) i.next();
					// option.setCurrency(store.getCurrency());
					StringBuffer description = new StringBuffer();

					String code = option.getOptionCode();
					option.setOptionCode(code);
					// get description
					String label = (String) serviceMap.get(code);
					if (label == null) {
						log
								.warn("UPSXML cannot find description for service code "
										+ code);
					}

					option.setOptionName(label);

					description.append(option.getOptionName());
					if (displayQuoteDeliveryTime == ShippingConstants.DISPLAY_RT_QUOTE_TIME) {
						if (!StringUtils.isBlank(option
								.getEstimatedNumberOfDays())) {
							description.append(" (").append(
									option.getEstimatedNumberOfDays()).append(
									" ").append(
									labelUtil.getText(locale,
											"label.generic.days.lowercase"))
									.append(")");
						}
					}
					option.setDescription(description.toString());

					// get currency
					if (!option.getCurrency().equals(store.getCurrency())) {
						option.setOptionPrice(CurrencyUtil.convertToCurrency(
								option.getOptionPrice(), option.getCurrency(),
								store.getCurrency()));
					}

					if (!selectedintlservices.containsKey(option
							.getOptionCode())) {
						if (returnColl == null) {
							returnColl = new ArrayList();
						}
						returnColl.add(option);
						// options.remove(option);
					}

				}

				if (options.size() == 0) {
					LogMerchantUtil
							.log(
									store.getMerchantId(),
									" none of the service code returned by UPS ["
											+ selectedintlservices
													.keySet()
													.toArray(
															new String[selectedintlservices
																	.size()])
											+ "] for this shipping is in your selection list");
				}
			}



			return returnColl;

		} catch (Exception e1) {
			log.error(e1);
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception ignore) {
				}
			}

			if (httppost != null) {
				httppost.releaseConnection();
			}
		}

	}

	public void storeConfiguration(int merchantid, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		// stored in ShippingupsxmlAction class

	}

	public ConfigurationResponse getConfiguration(
			MerchantConfiguration configurations, ConfigurationResponse vo)
			throws Exception {

		// what is the MerchantConfiguration ??

		if (configurations.getConfigurationKey().equals(
				ShippingConstants.MODULE_SHIPPING_RT_CRED)) {// handle
																// credentials

			if (!StringUtils.isBlank(configurations.getConfigurationValue1())) {

				IntegrationKeys keys = ShippingUtil.getKeys(configurations
						.getConfigurationValue1());
				vo.addConfiguration("upsxml-keys", keys);

			}

			if (!StringUtils.isBlank(configurations.getConfigurationValue2())) {

				IntegrationProperties props = ShippingUtil
						.getProperties(configurations.getConfigurationValue2());
				vo.addConfiguration("upsxml-properties", props);
			}

		}

		if (configurations.getConfigurationKey().equals(
				ShippingConstants.MODULE_SHIPPING_RT_PKG_DOM_INT)) {// handle
																	// packages
																	// &
																	// services
			Map domesticmap = null;
			Map globalmap = null;
			// PKGOPTIONS
			if (!StringUtils.isBlank(configurations.getConfigurationValue())) {
				vo.addConfiguration("package-upsxml", configurations
						.getConfigurationValue());
			}
			// Global
			if (!StringUtils.isBlank(configurations.getConfigurationValue2())) {
				globalmap = new HashMap();
				String intl = configurations.getConfigurationValue2();
				StringTokenizer st = new StringTokenizer(intl, ";");
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					globalmap.put(token, token);
				}
				vo.addConfiguration("service-global-upsxml", globalmap);
			}
		}

		vo.addMerchantConfiguration(configurations);
		return vo;

	}

	private String responsecode = null;
	private String responsetext = null;

	public String getResponsecode() {
		return responsecode;
	}

	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}

	public String getResponsetext() {
		return responsetext;
	}

	public void setResponsetext(String responsetext) {
		this.responsetext = responsetext;
	}

}

class UPSParsedElements {

	private String statusCode;
	private String statusMessage;
	private String error = "";
	private String errorCode = "";
	private List options = new ArrayList();

	public void addOption(ShippingOption option) {
		options.add(option);
	}

	public List getOptions() {
		return options;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
