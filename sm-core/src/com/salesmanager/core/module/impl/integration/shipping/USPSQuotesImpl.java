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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.digester.Digester;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.Country;
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
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LogMerchantUtil;

public class USPSQuotesImpl implements ShippingQuotesModule {

	private Logger log = Logger.getLogger(USPSQuotesImpl.class);

	public Collection<ShippingOption> getShippingQuote(
			ConfigurationResponse config, BigDecimal orderTotal,
			Collection<PackageDetail> packages, Customer customer,
			MerchantStore store, Locale locale) {

		CoreModuleService cis = null;

		StringBuffer xmlbuffer = new StringBuffer();
		BufferedReader reader = null;

		GetMethod httpget = null;

		try {

			CommonService cservice = (CommonService) ServiceFactory
					.getService(ServiceFactory.CommonService);

			String countrycode = CountryUtil.getCountryIsoCodeById(store
					.getCountry());
			cis = cservice.getModule(countrycode, "usps");

			if (cis == null) {
				log.error("Can't retreive an integration service [countryid "
						+ store.getCountry() + " usps subtype 1]");
			}

			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			ConfigurationRequest request_prefs = new ConfigurationRequest(store
					.getMerchantId(),
					ShippingConstants.MODULE_SHIPPING_RT_PKG_DOM_INT);
			ConfigurationResponse vo_prefs = service
					.getConfiguration(request_prefs);

			String pack = (String) vo_prefs.getConfiguration("package-usps");
			if (pack == null) {
				log
						.debug("Will assign packaging type 02 to USPS shipping for merchantid "
								+ store.getMerchantId());
				pack = "PARCEL";
			}

			ConfigurationRequest request = new ConfigurationRequest(store
					.getMerchantId(), ShippingConstants.MODULE_SHIPPING_RT_CRED);
			ConfigurationResponse vo = service.getConfiguration(request);

			if (vo == null) {
				throw new Exception("ConfigurationVO is null upsxml");
			}

			// if not shipping to USA
			boolean domestic = true;
			int shippingCountryId = customer.getCustomerCountryId();
			if (shippingCountryId != Constants.US_COUNTRY_ID) {
				domestic = false;
			}

			IntegrationKeys keys = (IntegrationKeys) vo
					.getConfiguration("usps-keys");

			if (keys == null) {
				throw new Exception(
						"Integration keys are null from configuration request usps");
			}

			String xmlheader = "<RateV3Request USERID=\"" + keys.getUserid()
					+ "\">";
			if (!domestic) {
				xmlheader = "<IntlRateRequest USERID=\"" + keys.getUserid()
						+ "\">";
			}

			StringBuffer xmldatabuffer = new StringBuffer();

			Map countriesMap = (Map) RefCache.getAllcountriesmap(LanguageUtil
					.getLanguageNumberCode(locale.getLanguage()));
			Country customerCountry = (Country) countriesMap.get(customer
					.getCustomerCountryId());

			Iterator packagesIterator = packages.iterator();

			double totalW = 0;
			double totalH = 0;
			double totalL = 0;
			double totalG = 0;
			double totalP = 0;

			while (packagesIterator.hasNext()) {

				PackageDetail detail = (PackageDetail) packagesIterator.next();

				// need size in inch
				double w = CurrencyUtil.getMeasure(detail.getShippingWidth(),
						store, Constants.INCH_SIZE_UNIT);
				double h = CurrencyUtil.getMeasure(detail.getShippingHeight(),
						store, Constants.INCH_SIZE_UNIT);
				double l = CurrencyUtil.getMeasure(detail.getShippingLength(),
						store, Constants.INCH_SIZE_UNIT);

				totalW = totalW + w;
				totalH = totalH + h;
				totalL = totalL + l;

				// Girth = Length + (Width x 2) + (Height x 2)
				double girth = l + (w * 2) + (h * 2);

				totalG = totalG + girth;

				// need weight in pounds
				double p = CurrencyUtil.getWeight(detail.getShippingWeight(),
						store, Constants.LB_WEIGHT_UNIT);

				totalP = totalP + p;

			}

			BigDecimal convertedOrderTotal = CurrencyUtil.convertToCurrency(
					orderTotal, store.getCurrency(),
					Constants.CURRENCY_CODE_USD);

			// calculate total shipping volume

			// ship date is 3 days from here
			Date newDate = DateUtil.addDaysToCurrentDate(3);
			String shipDate = DateUtil.formatDateMonthString(newDate);

			int i = 1;

			// need pounds and ounces
			int pounds = (int) totalP;
			String ouncesString = String.valueOf(totalP - pounds);
			int ouncesIndex = ouncesString.indexOf(".");
			String ounces = "00";
			if (ouncesIndex > -1) {
				ounces = ouncesString.substring(ouncesIndex + 1);
			}

			String size = "REGULAR";

			if (totalL + totalG <= 64) {
				size = "REGULAR";
			} else if (totalL + totalG <= 108) {
				size = "LARGE";
			} else {
				size = "OVERSIZE";
			}

			/**
			 * Domestic <Package ID="1ST"> <Service>ALL</Service>
			 * <ZipOrigination>90210</ZipOrigination>
			 * <ZipDestination>96698</ZipDestination> <Pounds>8</Pounds>
			 * <Ounces>32</Ounces> <Container/> <Size>REGULAR</Size>
			 * <Machinable>true</Machinable> </Package>
			 * 
			 * //MAXWEIGHT=70 lbs
			 * 
			 * 
			 * //domestic container default=VARIABLE whiteSpace=collapse
			 * enumeration=VARIABLE enumeration=FLAT RATE BOX enumeration=FLAT
			 * RATE ENVELOPE enumeration=LG FLAT RATE BOX
			 * enumeration=RECTANGULAR enumeration=NONRECTANGULAR
			 * 
			 * //INTL enumeration=Package enumeration=Postcards or aerogrammes
			 * enumeration=Matter for the blind enumeration=Envelope
			 * 
			 * Size May be left blank in situations that do not Size. Defined as
			 * follows: REGULAR: package plus girth is 84 inches or less; LARGE:
			 * package length plus girth measure more than 84 inches not more
			 * than 108 inches; OVERSIZE: package length plus girth is more than
			 * 108 but not 130 inches. For example: <Size>REGULAR</Size>
			 * 
			 * International <Package ID="1ST"> <Machinable>true</Machinable>
			 * <MailType>Envelope</MailType> <Country>Canada</Country>
			 * <Length>0</Length> <Width>0</Width> <Height>0</Height>
			 * <ValueOfContents>250</ValueOfContents> </Package>
			 * 
			 * <Package ID="2ND"> <Pounds>4</Pounds> <Ounces>3</Ounces>
			 * <MailType>Package</MailType> <GXG> <Length>46</Length>
			 * <Width>14</Width> <Height>15</Height> <POBoxFlag>N</POBoxFlag>
			 * <GiftFlag>N</GiftFlag> </GXG>
			 * <ValueOfContents>250</ValueOfContents> <Country>Japan</Country>
			 * </Package>
			 */

			xmldatabuffer.append("<Package ID=\"").append(i).append("\">");
			// if domestic

			// user selected services
			// Map selectedintlservices =
			// (Map)config.getConfiguration("service-global-usps");

			// now get corresponding code
			// Map allservices =
			// com.salesmanager.core.util.ShippingUtil.buildServiceMapLabelByCode("usps",locale);
			// TreeBidiMap bidiMap = new TreeBidiMap(allservices);
			// Map invertedservicesmap = (Map)bidiMap.inverseBidiMap();

			// Iterator selServices = selectedintlservices.keySet().iterator();
			// while(selServices.hasNext()) {

			// String serviceId = (String)selServices.next();

			// String svc = (String)invertedservicesmap.get(serviceId);

			if (domestic) {

				xmldatabuffer.append("<Service>");
				xmldatabuffer.append("ALL");
				xmldatabuffer.append("</Service>");
				xmldatabuffer.append("<ZipOrigination>");
				xmldatabuffer.append(com.salesmanager.core.util.ShippingUtil
						.trimPostalCode(store.getStorepostalcode()));
				xmldatabuffer.append("</ZipOrigination>");
				xmldatabuffer.append("<ZipDestination>");
				xmldatabuffer.append(com.salesmanager.core.util.ShippingUtil
						.trimPostalCode(customer.getCustomerPostalCode()));
				xmldatabuffer.append("</ZipDestination>");
				xmldatabuffer.append("<Pounds>");
				xmldatabuffer.append(pounds);
				xmldatabuffer.append("</Pounds>");
				xmldatabuffer.append("<Ounces>");
				xmldatabuffer.append(ounces);
				xmldatabuffer.append("</Ounces>");
				xmldatabuffer.append("<Container>");
				xmldatabuffer.append(pack);
				xmldatabuffer.append("</Container>");
				xmldatabuffer.append("<Size>");
				xmldatabuffer.append(size);
				xmldatabuffer.append("</Size>");
				xmldatabuffer.append("<ShipDate>");
				xmldatabuffer.append(shipDate);
				xmldatabuffer.append("</ShipDate>");
			} else {
				// if international
				xmldatabuffer.append("<Pounds>");
				xmldatabuffer.append(pounds);
				xmldatabuffer.append("</Pounds>");
				xmldatabuffer.append("<Ounces>");
				xmldatabuffer.append(ounces);
				xmldatabuffer.append("</Ounces>");
				xmldatabuffer.append("<MailType>");
				xmldatabuffer.append("Package");
				xmldatabuffer.append("</MailType>");
				xmldatabuffer.append("<ValueOfContents>");
				xmldatabuffer.append(convertedOrderTotal);
				xmldatabuffer.append("</ValueOfContents>");
				xmldatabuffer.append("<Country>");
				xmldatabuffer.append(customerCountry.getCountryName());
				xmldatabuffer.append("</Country>");
			}

			// }

			// if international & CXG
			/*
			 * xmldatabuffer.append("<CXG>"); xmldatabuffer.append("<Length>");
			 * xmldatabuffer.append(""); xmldatabuffer.append("</Length>");
			 * xmldatabuffer.append("<Width>"); xmldatabuffer.append("");
			 * xmldatabuffer.append("</Width>");
			 * xmldatabuffer.append("<Height>"); xmldatabuffer.append("");
			 * xmldatabuffer.append("</Height>");
			 * xmldatabuffer.append("<POBoxFlag>"); xmldatabuffer.append("");
			 * xmldatabuffer.append("</POBoxFlag>");
			 * xmldatabuffer.append("<GiftFlag>"); xmldatabuffer.append("");
			 * xmldatabuffer.append("</GiftFlag>");
			 * xmldatabuffer.append("</CXG>");
			 */

			/*
			 * xmldatabuffer.append("<Width>"); xmldatabuffer.append(totalW);
			 * xmldatabuffer.append("</Width>");
			 * xmldatabuffer.append("<Length>"); xmldatabuffer.append(totalL);
			 * xmldatabuffer.append("</Length>");
			 * xmldatabuffer.append("<Height>"); xmldatabuffer.append(totalH);
			 * xmldatabuffer.append("</Height>");
			 * xmldatabuffer.append("<Girth>"); xmldatabuffer.append(totalG);
			 * xmldatabuffer.append("</Girth>");
			 */

			xmldatabuffer.append("</Package>");

			String xmlfooter = "</RateRequest>";
			if (!domestic) {
				xmlfooter = "</IntlRateRequest>";
			}

			xmlbuffer.append(xmlheader.toString()).append(
					xmldatabuffer.toString()).append(xmlfooter.toString());

			log.debug("USPS QUOTE REQUEST " + xmlbuffer.toString());

			String data = "";

			IntegrationProperties props = (IntegrationProperties) config
					.getConfiguration("usps-properties");

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

			String encoded = java.net.URLEncoder.encode(xmlbuffer.toString());

			String completeUri = uri + "?API=RateV3&XML=" + encoded;
			if (!domestic) {
				completeUri = uri + "?API=IntlRate&XML=" + encoded;
			}

			// ?API=RateV3

			httpget = new GetMethod(protocol + "://" + host + ":" + port
					+ completeUri);
			// RequestEntity entity = new
			// StringRequestEntity(xmlbuffer.toString(),"text/plain","UTF-8");
			// httpget.setRequestEntity(entity);

			int result = client.executeMethod(httpget);
			if (result != 200) {
				log.error("Communication Error with usps quote " + result + " "
						+ protocol + "://" + host + ":" + port + uri);
				throw new Exception("USPS quote communication error " + result);
			}
			data = httpget.getResponseBodyAsString();
			log.debug("usps quote response " + data);

			UPSParsedElements parsed = new UPSParsedElements();

			/**
			 * <RateV3Response> <Package ID="1ST">
			 * <ZipOrigination>44106</ZipOrigination>
			 * <ZipDestination>20770</ZipDestination>
			 */

			Digester digester = new Digester();
			digester.push(parsed);

			if (domestic) {

				digester.addCallMethod("RateV3Response/Package/Error",
						"setError", 0);
				digester
						.addObjectCreate(
								"RateV3Response/Package/Postage",
								com.salesmanager.core.entity.shipping.ShippingOption.class);
				digester.addSetProperties("RateV3Response/Package/Postage",
						"CLASSID", "optionId");
				digester.addCallMethod(
						"RateV3Response/Package/Postage/MailService",
						"optionName", 0);
				digester.addCallMethod(
						"RateV3Response/Package/Postage/MailService",
						"optionCode", 0);
				digester.addCallMethod("RateV3Response/Package/Postage/Rate",
						"optionPrice", 0);
				digester
						.addCallMethod(
								"RateV3Response/Package/Postage/Commitment/CommitmentDate",
								"estimatedNumberOfDays", 0);
				digester.addSetNext("RateV3Response/Package/Postage",
						"addOption");

			} else {

				digester.addCallMethod("IntlRateResponse/Package/Error",
						"setError", 0);
				digester
						.addObjectCreate(
								"IntlRateResponse/Package/Service",
								com.salesmanager.core.entity.shipping.ShippingOption.class);
				digester.addSetProperties("IntlRateResponse/Package/Service",
						"ID", "optionId");
				digester.addCallMethod(
						"IntlRateResponse/Package/Service/SvcDescription",
						"setOptionName", 0);
				digester.addCallMethod(
						"IntlRateResponse/Package/Service/SvcDescription",
						"setOptionCode", 0);
				digester.addCallMethod(
						"IntlRateResponse/Package/Service/Postage",
						"setOptionPriceText", 0);
				digester.addCallMethod(
						"IntlRateResponse/Package/Service/SvcCommitments",
						"setEstimatedNumberOfDays", 0);
				digester.addSetNext("IntlRateResponse/Package/Service",
						"addOption");

			}

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
			// cost is in USD, need to do conversion

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

			LabelUtil labelUtil = LabelUtil.getInstance();
			// Map serviceMap =
			// com.salesmanager.core.util.ShippingUtil.buildServiceMap("usps",locale);

			List options = parsed.getOptions();

			Collection returnColl = null;

			if (options != null && options.size() > 0) {

				returnColl = new ArrayList();
				// Map selectedintlservices =
				// (Map)config.getConfiguration("service-global-usps");
				// need to create a Map of LABEL - LABLEL
				// Iterator servicesIterator =
				// selectedintlservices.keySet().iterator();
				// Map services = new HashMap();

				// ResourceBundle bundle = ResourceBundle.getBundle("usps",
				// locale);

				// while(servicesIterator.hasNext()) {
				// String key = (String)servicesIterator.next();
				// String value =
				// bundle.getString("shipping.quote.services.label." + key);
				// services.put(value, key);
				// }

				Iterator it = options.iterator();
				while (it.hasNext()) {
					ShippingOption option = (ShippingOption) it.next();
					option.setCurrency(Constants.CURRENCY_CODE_USD);

					StringBuffer description = new StringBuffer();
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

					// if(!services.containsKey(option.getOptionCode())) {
					// if(returnColl==null) {
					// returnColl = new ArrayList();
					// }
					// returnColl.add(option);
					// }
					returnColl.add(option);
				}

				// if(options.size()==0) {
				// CommonService.logServiceMessage(store.getMerchantId(),
				// " none of the service code returned by UPS [" +
				// selectedintlservices.keySet().toArray(new
				// String[selectedintlservices.size()]) +
				// "] for this shipping is in your selection list");
				// }

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
			if (httpget != null) {
				httpget.releaseConnection();
			}
		}

	}

	public String getShippingMethodDescription(Locale locale) {
		return LabelUtil.getInstance().getText(locale, "module.usps");
	}

	public ConfigurationResponse getConfiguration(
			MerchantConfiguration configurations, ConfigurationResponse vo)
			throws Exception {
		if (configurations.getConfigurationKey().equals(
				ShippingConstants.MODULE_SHIPPING_RT_CRED)) {// handle
																// credentials

			if (!StringUtils.isBlank(configurations.getConfigurationValue1())) {

				IntegrationKeys keys = ShippingUtil.getKeys(configurations
						.getConfigurationValue1());
				vo.addConfiguration("usps-keys", keys);

			}

			if (!StringUtils.isBlank(configurations.getConfigurationValue2())) {

				IntegrationProperties props = ShippingUtil
						.getProperties(configurations.getConfigurationValue2());
				vo.addConfiguration("usps-properties", props);

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
				vo.addConfiguration("package-usps", configurations
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
				vo.addConfiguration("service-global-usps", globalmap);
			}
		}

		vo.addMerchantConfiguration(configurations);
		return vo;
	}

	public void storeConfiguration(int merchantid, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {
		// implemented in ShippinguspsAction

	}

}

class USPSParsedElements {

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
