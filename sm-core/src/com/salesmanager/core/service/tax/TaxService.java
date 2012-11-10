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
package com.salesmanager.core.service.tax;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.constants.TaxConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductPrice;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.orders.OrderTotalLine;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.GeoZone;
import com.salesmanager.core.entity.reference.ZoneToGeoZone;
import com.salesmanager.core.entity.tax.TaxClass;
import com.salesmanager.core.entity.tax.TaxRate;
import com.salesmanager.core.entity.tax.TaxRateDescription;
import com.salesmanager.core.entity.tax.TaxRateDescriptionId;
import com.salesmanager.core.entity.tax.TaxRateDescriptionTaxTemplate;
import com.salesmanager.core.entity.tax.TaxRateDescriptionTaxTemplateId;
import com.salesmanager.core.entity.tax.TaxRateTaxTemplate;
import com.salesmanager.core.module.model.application.TaxModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductDao;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.impl.dao.IGeoZoneDao;
import com.salesmanager.core.service.reference.impl.dao.IZoneToGeoZoneDao;
import com.salesmanager.core.service.shipping.ShippingService;
import com.salesmanager.core.service.tax.impl.dao.ITaxClassDao;
import com.salesmanager.core.service.tax.impl.dao.ITaxRateDao;
import com.salesmanager.core.service.tax.impl.dao.ITaxRateDescriptionDao;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.SpringUtil;

@Service
public class TaxService {

	@Autowired
	private ITaxClassDao taxClassDao;

	@Autowired
	private IGeoZoneDao geoZoneDao;

	@Autowired
	private IZoneToGeoZoneDao zoneToGeoZoneDao;

	@Autowired
	private ITaxRateDao taxRateDao;

	@Autowired
	private ITaxRateDescriptionDao taxRateDescriptionDao;

	@Autowired
	private IProductDao productDao;

	@Transactional
	public Collection<TaxRateTaxTemplate> findByGeoZoneCountryId(int countryId)
			throws Exception {
		return taxRateDao.findByZoneCountryId(countryId);
	}

	@Transactional
	public Collection<TaxRateTaxTemplate> findBySchemeId(int schemeId)
			throws Exception {
		return taxRateDao.findBySchemeId(schemeId);
	}

	@Transactional
	public Collection<TaxRate> findByCountryIdZoneIdAndClassId(int countryId,
			int zoneId, long taxClassId, int merchantId) throws Exception {

		return taxRateDao.findByCountryIdZoneIdAndClassId(countryId, zoneId,
				taxClassId, merchantId);
	}

	/**
	 * Calculates tax on a BigDecimal price, returns the price with tax
	 * 
	 * @param amount
	 * @param customer
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BigDecimal calculateTax(BigDecimal amount, long taxClassId,
			Customer customer, int merchantId) throws Exception {

		// no tax calculation id taxClassId==-1
		if (taxClassId == -1) {
			return amount;
		}

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		ConfigurationRequest request = new ConfigurationRequest(merchantId,
				ShippingConstants.MODULE_TAX_BASIS);
		ConfigurationResponse response = mservice.getConfiguration(request);

		String taxBasis = TaxConstants.SHIPPING_TAX_BASIS;

		// get tax basis
		MerchantConfiguration taxConf = response
				.getMerchantConfiguration(TaxConstants.MODULE_TAX_BASIS);
		if (taxConf != null
				&& !StringUtils.isBlank(taxConf.getConfigurationValue())) {// tax
																			// basis
			taxBasis = taxConf.getConfigurationValue();
		}

		Collection taxCollection = null;
		if (taxBasis.equals(TaxConstants.SHIPPING_TAX_BASIS)) {
			taxCollection = taxRateDao.findByCountryIdZoneIdAndClassId(customer
					.getCustomerCountryId(), customer.getCustomerZoneId(),
					taxClassId, merchantId);
		} else {
			taxCollection = taxRateDao.findByCountryIdZoneIdAndClassId(customer
					.getCustomerBillingCountryId(), customer
					.getCustomerBillingZoneId(), taxClassId, merchantId);
		}

		BigDecimal currentAmount = new BigDecimal(0);
		currentAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		if (taxCollection != null) {

			Iterator i = taxCollection.iterator();
			while (i.hasNext()) {

				TaxRate trv = (TaxRate) i.next();
				BigDecimal amountForCalculation = amount;
				if (trv.isPiggyback()) {
					amountForCalculation = amountForCalculation
							.add(currentAmount);
				}

				double value = ((trv.getTaxRate().doubleValue() * amountForCalculation
						.doubleValue()) / 100)
						+ amountForCalculation.doubleValue();
				currentAmount = currentAmount.add(new BigDecimal(value));

			}

		}

		return currentAmount;

	}

	/**
	 * Returns a Collection of TaxRate for a given zone information
	 * 
	 * @param taxClassId
	 * @param customerCountryId
	 * @param customerZoneId
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<TaxRate> getTax(long taxClassId, int customerCountryId,
			int customerZoneId, int merchantId) throws Exception {
		return taxRateDao.findByCountryIdZoneIdAndClassId(customerCountryId,
				customerZoneId, taxClassId, merchantId);
	}

	/**
	 * Calculates tax on an OrderTotalSummary object (products applicable,
	 * shipping...), creates and set the shopping cart lines. Returns the amount
	 * with tax
	 * 
	 * @param summary
	 * @param amount
	 * @param customer
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public OrderTotalSummary calculateTax(OrderTotalSummary summary,
			Collection<OrderProduct> products, Customer customer,
			int merchantId, Locale locale, String currency) throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		MerchantStore store = mservice.getMerchantStore(merchantId);

		Map productsTax = new HashMap();

		//rounding definition
		BigDecimal totalTaxAmount = new BigDecimal(0);
		//totalTaxAmount.setScale(2, BigDecimal.ROUND_DOWN);

		// check if tax is applicable and build a map
		// of tax class - product
		if (products != null) {
			Iterator prodIt = products.iterator();
			while (prodIt.hasNext()) {
				OrderProduct prod = (OrderProduct) prodIt.next();
				if (prod.getTaxClassId() > -1) {

					BigDecimal groupBeforeTaxAmount = (BigDecimal) productsTax
							.get(prod.getTaxClassId());

					if (groupBeforeTaxAmount == null) {
						groupBeforeTaxAmount = new BigDecimal("0");
					}

					BigDecimal finalPrice = prod.getFinalPrice();// unit price +
																	// attribute
																	// * qty
					// finalPrice = finalPrice.multiply(new
					// BigDecimal(prod.getProductQuantity()));

					groupBeforeTaxAmount = groupBeforeTaxAmount.add(finalPrice);

					// getPrices
					Set prices = prod.getPrices();
					// List prices = prod.getRelatedPrices();
					if (prices != null) {
						Iterator ppriceIter = prices.iterator();
						while (ppriceIter.hasNext()) {
							OrderProductPrice pprice = (OrderProductPrice) ppriceIter
									.next();
							if (!pprice.isDefaultPrice()) {// related price
															// activation...
								// PriceModule module =
								// (PriceModule)SpringUtil.getBean(pprice.getProductPriceModuleName());
								// if(module.isTaxApplicable()) {//related price
								// becomes taxeable
								// if(pprice.isProductHasTax()) {
								// groupBeforeTaxAmount =
								// groupBeforeTaxAmount.add(ProductUtil.determinePrice(pprice));

								BigDecimal ppPrice = pprice
										.getProductPriceAmount();
								ppPrice = ppPrice.multiply(new BigDecimal(prod
										.getProductQuantity()));

								groupBeforeTaxAmount = groupBeforeTaxAmount
										.add(ppPrice);
								// }
							}
						}
					}

					BigDecimal credits = prod
							.getApplicableCreditOneTimeCharge();
					groupBeforeTaxAmount = groupBeforeTaxAmount
							.subtract(credits);

					productsTax.put(prod.getTaxClassId(), groupBeforeTaxAmount);

				}
			}
		}

		if (productsTax.size() == 0) {
			return summary;
		}

		// determine if tax applies on billing or shipping address

		// get shipping & tax informations
		ConfigurationRequest request = new ConfigurationRequest(merchantId);
		ConfigurationResponse response = mservice.getConfiguration(request);

		String taxBasis = TaxConstants.SHIPPING_TAX_BASIS;

		// get tax basis
		MerchantConfiguration taxConf = response
				.getMerchantConfiguration(TaxConstants.MODULE_TAX_BASIS);
		if (taxConf != null
				&& !StringUtils.isBlank(taxConf.getConfigurationValue())) {// tax
																			// basis
			taxBasis = taxConf.getConfigurationValue();
		}

		// tax on shipping
		if (summary.getShippingTotal() != null
				&& summary.getShippingTotal().floatValue() > 0) {
			MerchantConfiguration shippingTaxConf = response
					.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_TAX_CLASS);
			if (shippingTaxConf != null
					&& !StringUtils.isBlank(shippingTaxConf
							.getConfigurationValue())) {// tax on shipping

				long taxClass = Long.parseLong(shippingTaxConf
						.getConfigurationValue());
				BigDecimal groupSubTotal = (BigDecimal) productsTax
						.get(taxClass);
				if (groupSubTotal == null) {
					groupSubTotal = new BigDecimal("0");
					productsTax.put(taxClass, groupSubTotal);
				}
				groupSubTotal = groupSubTotal.add(summary.getShippingTotal());
				productsTax.put(taxClass, groupSubTotal);
			}
		}

		Map taxDescriptionsHolder = new TreeMap();

		Iterator taxMapIter = productsTax.keySet().iterator();
		while (taxMapIter.hasNext()) {// get each tax class

			long key = (Long) taxMapIter.next();
			// List taxClassGroup = (List)productsTax.get(key);

			int countryId = 0;

			Collection taxCollection = null;
			if (taxBasis.equals(TaxConstants.SHIPPING_TAX_BASIS)) {

				if (store.getCountry() != customer.getCustomerCountryId()) {
					return summary;
				}

				taxCollection = taxRateDao.findByCountryIdZoneIdAndClassId(
						customer.getCustomerCountryId(), customer
								.getCustomerZoneId(), key, merchantId);
				countryId = customer.getCustomerCountryId();
			} else { // BILLING

				if (store.getCountry() != customer
						.getCustomerBillingCountryId()) {
					return summary;
				}

				taxCollection = taxRateDao.findByCountryIdZoneIdAndClassId(
						customer.getCustomerBillingCountryId(), customer
								.getCustomerBillingZoneId(), key, merchantId);
				countryId = customer.getCustomerBillingCountryId();
			}

			if (taxCollection == null || taxCollection.size() == 0) {// no tax
				continue;
			}

			Map countries = RefCache.getCountriesMap();
			Country c = (Country) countries.get(countryId);

			if (c != null) {// tax adjustment rules
				TaxModule module = (TaxModule) SpringUtil.getBean(c
						.getCountryIsoCode2());
				if (module != null) {
					taxCollection = module.adjustTaxRate(taxCollection, store);
				}
			}

			//BigDecimal beforeTaxAmount = new BigDecimal("0");
			//beforeTaxAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal groupSubTotal = (BigDecimal) productsTax.get(key);

			//beforeTaxAmount = beforeTaxAmount.add(groupSubTotal);
			BigDecimal beforeTaxAmount = groupSubTotal;
			beforeTaxAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

			// iterate through tax collection and calculate tax lines
			if (taxCollection != null) {

				Iterator i = taxCollection.iterator();
				while (i.hasNext()) {

					TaxRate trv = (TaxRate) i.next();
					// double value = ((trv.getTaxRate().doubleValue() *
					// beforeTaxAmount.doubleValue())/100)+beforeTaxAmount.doubleValue();
					double trDouble = trv.getTaxRate().doubleValue();

					// if piggy back, add tax to subtotal
					BigDecimal amount = beforeTaxAmount;
					if (trv.isPiggyback()) {
						// add previous calculated tax on top of subtotal
						amount = amount.add(totalTaxAmount);
					}

					// commented for piggyback
					// double beforeTaxDouble = beforeTaxAmount.doubleValue();
					double beforeTaxDouble = amount.doubleValue();
					

					double value = ((trDouble * beforeTaxDouble) / 100);
					
					BigDecimal nValue = BigDecimal.valueOf(value);

					//BigDecimal nValue = new BigDecimal(value);
					nValue.setScale(2, BigDecimal.ROUND_HALF_UP);


					//nValue = nValue.add(new BigDecimal(value));

					// commented for piggyback
					// beforeTaxAmount = beforeTaxAmount.add(nValue);

					//BigDecimal bdValue = nValue;
				    String am = CurrencyUtil.getAmount(nValue, store.getCurrency());
				    
				    
				    /** this one **/
					totalTaxAmount = totalTaxAmount.add(new BigDecimal(am));

					String name = LabelUtil.getInstance().getText(locale,
							"label.generic.tax");

					OrderTotalLine line = (OrderTotalLine) taxDescriptionsHolder
							.get(trv.getZoneToGeoZone().getGeoZoneId());

					if (line == null) {
						// tax description
						line = new OrderTotalLine();
						Set descriptionsSet = trv.getDescriptions();
						if (descriptionsSet != null) {
							Iterator li = descriptionsSet.iterator();
							while (li.hasNext()) {
								TaxRateDescription description = (TaxRateDescription) li
										.next();
								if (description.getId().getLanguageId() == LanguageUtil
										.getLanguageNumberCode(locale
												.getLanguage())) {
									name = description.getTaxDescription();
									break;
								}
							}
						}

						line.setText(name);
						line.setCost(nValue);
						line.setCostFormated(CurrencyUtil
								.displayFormatedAmountWithCurrency(nValue,
										currency));
						taxDescriptionsHolder.put(trv.getZoneToGeoZone()
								.getGeoZoneId(), line);
					} else {// needs to re-use the same shopping cart line
						BigDecimal cost = line.getCost();
						cost = cost.add(nValue);
						line.setCostFormated(CurrencyUtil
								.displayFormatedAmountWithCurrency(cost,
										currency));
					}

					// now set tax on producs
					Iterator prodIt = products.iterator();
					while (prodIt.hasNext()) {
						OrderProduct prod = (OrderProduct) prodIt.next();
						if (prod.getTaxClassId() == key) {
							// calculate tax for this product
							BigDecimal price = prod.getProductPrice();
							BigDecimal productTax = prod.getProductTax();
							if (productTax == null) {
								productTax = new BigDecimal("0");
							}
							price = price.add(productTax);
							double pTax = ((trDouble * price.doubleValue()) / 100);

							prod.setProductTax(new BigDecimal(pTax));

						}
					}

				}// end while

			}

			Iterator titer = taxDescriptionsHolder.keySet().iterator();
			while (titer.hasNext()) {
				long lineKey = (Long) titer.next();
				OrderTotalLine line = (OrderTotalLine) taxDescriptionsHolder
						.get(lineKey);
				summary.addTaxPrice(line);
			}

		}

		summary.setTaxTotal(totalTaxAmount);

		return summary;

	}

	/**
	 * Returns a Collection of tax class for a given merchant id. It will
	 * retreive also entries where merchantid = 0
	 */
	@Transactional
	public List<TaxClass> getTaxClasses(int merchantid) throws Exception {

		return taxClassDao.findByMerchantId(merchantid);
	}

	@Transactional
	public TaxClass getTaxClass(long taxclassId) throws Exception {

		return taxClassDao.findById(taxclassId);
	}

	/**
	 * Persist a new TaxClass or update an existing one
	 * 
	 * @param taxClass
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateTaxClass(TaxClass taxClass) throws Exception {

		if (taxClass.getTaxClassId() == 0) {
			taxClassDao.persist(taxClass);
		} else {
			taxClassDao.saveOrUpdate(taxClass);
		}
	}

	@Transactional
	public void deleteTaxClass(TaxClass taxClass) throws Exception {

		// need to update all taxrates to basic tax class

		List taxrates = taxRateDao.findByTaxClassId(taxClass.getTaxClassId());

		if (taxrates != null) {
			Iterator i = taxrates.iterator();
			while (i.hasNext()) {
				TaxRate tr = (TaxRate) i.next();
				tr.setTaxClassId(TaxConstants.DEFAULT_TAX_CLASS_ID);
				taxRateDao.saveOrUpdate(tr);
			}
		}

		// products
		Collection products = productDao.findByTaxClassId(taxClass
				.getTaxClassId());
		if (products != null) {
			Iterator i = products.iterator();
			while (i.hasNext()) {
				Product p = (Product) i.next();
				p.setProductTaxClassId(TaxConstants.DEFAULT_TAX_CLASS_ID);
				productDao.saveOrUpdate(p);
			}
		}

		// update shipping tax class
		ShippingService service = (ShippingService) ServiceFactory
				.getService(ServiceFactory.ShippingService);
		service.setShippingTaxClass(TaxConstants.DEFAULT_TAX_CLASS_ID, taxClass
				.getMerchantId());

		taxClassDao.delete(taxClass);

	}

	@Transactional
	public void deleteTaxConfiguration(int merchantid) throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		mservice.cleanConfigurationLikeKey("MODULE_TAX_", merchantid);

		Collection zones = geoZoneDao.findByMerchantId(merchantid);
		if (zones != null) {
			geoZoneDao.deleteAll(zones);
		}

		Collection geozones = zoneToGeoZoneDao.findByMerchantId(merchantid);
		if (geozones != null) {
			zoneToGeoZoneDao.deleteAll(geozones);
		}

		List taxRateIdList = null;

		Collection taxrates = taxRateDao.findByMerchantId(merchantid);
		if (taxrates != null) {

			taxRateIdList = new ArrayList();

			Iterator i = taxrates.iterator();
			while (i.hasNext()) {
				TaxRate tr = (TaxRate) i.next();
				long id = tr.getTaxRateId();

				Collection taxratesdesc = taxRateDescriptionDao
						.findByTaxRateId(id);
				taxRateDescriptionDao.deleteAll(taxratesdesc);
			}

			taxRateDao.deleteAll(taxrates);

		}

		Collection taxclass = taxClassDao.findByOwnerMerchantId(merchantid);
		if (taxclass != null) {

			taxClassDao.deleteAll(taxclass);

		}

	}

	@Transactional
	public void deleteTaxRate(TaxRate taxRate) throws Exception {

		Set descriptions = taxRate.getDescriptions();
		if (descriptions != null) {
			taxRateDescriptionDao.deleteAll(descriptions);
		}

		taxRateDao.delete(taxRate);
	}

	/**
	 * Returns a TaxRate entity based on the taxrateid
	 * 
	 * @param taxRateId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public TaxRate getTaxRate(long taxRateId) throws Exception {

		TaxRate rate = taxRateDao.findById(taxRateId);

		if (rate != null) {
			Set descriptions = taxRateDescriptionDao.findByTaxRateId(taxRateId);
			rate.setDescriptions(descriptions);
		}

		return rate;

	}

	/**
	 * Load all taxes for a given merchantId as well as all collections
	 * 
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<TaxRate> getTaxRates(int merchantId) throws Exception {
		return taxRateDao.findByMerchantId(merchantId);
	}

	@Transactional
	public void saveOrUpdateTaxRate(TaxRate taxRate, int countryId, int zoneId,
			int merchantId) throws Exception {

		if (taxRate.getTaxRateId() > 0) {// update

			taxRateDao.saveOrUpdate(taxRate);
			if (taxRate.getDescriptions() != null) {

				taxRateDescriptionDao
						.saveOrUpdateAll(taxRate.getDescriptions());

			}

		} else { // create
			GeoZone geoZone = new GeoZone();
			geoZone.setMerchantId(merchantId);
			Map countries = RefCache.getAllcountriesmap(Constants.ENGLISH);
			Country country = (Country) countries.get(countryId);
			if (country == null) {
				throw new Exception("Cannoy retreive a country for countryId "
						+ countryId);
			}
			geoZone.setGeoZoneName(country.getCountryName() + "-TAX");
			geoZoneDao.persist(geoZone);

			ZoneToGeoZone ztGeoZone = new ZoneToGeoZone();
			ztGeoZone.setMerchantId(merchantId);
			ztGeoZone.setGeoZoneId(geoZone.getGeoZoneId());
			ztGeoZone.setZoneId(zoneId);
			ztGeoZone.setZoneCountryId(countryId);
			zoneToGeoZoneDao.persist(ztGeoZone);

			Integer priority = taxRate.getTaxPriority();
			if (priority == null) {
				priority = 0;
			}
			Collection existingRates = taxRateDao.findByCountryId(countryId,
					merchantId);
			if (existingRates != null && existingRates.size() > 0) {
				Iterator erit = existingRates.iterator();
				while (erit.hasNext()) {
					TaxRate trv = (TaxRate) erit.next();
					if (trv.getZoneToGeoZone() != null
							&& trv.getZoneToGeoZone().getZoneId() == zoneId) {
						Integer p = trv.getTaxPriority();
						if (p != null && p > priority) {
							priority = p + 1;
						}
					}
				}
			}

			taxRate.setTaxPriority(priority);

			taxRate.setTaxZoneId(geoZone.getGeoZoneId());
			taxRate.setMerchantId(merchantId);
			taxRateDao.persist(taxRate);

			if (taxRate.getDescriptions() != null) {
				Iterator it = taxRate.getDescriptions().iterator();
				while (it.hasNext()) {
					TaxRateDescription desc = (TaxRateDescription) it.next();
					TaxRateDescriptionId id = desc.getId();
					if (id == null || id.getLanguageId() == 0) {
						throw new Exception(
								"Need to configure a taxdescriptionid or the languageid for each description");
					}
					id.setTaxRateId(taxRate.getTaxRateId());
				}

				taxRateDescriptionDao
						.saveOrUpdateAll(taxRate.getDescriptions());
			}

		}
	}

	@Transactional
	public void createTaxRates(int schemeId, int merchantId, int countryId,
			int zoneId) throws Exception {

		/**
		 * if tax rate == 0 this means a new entry, so it will require a new -
		 * GEO_ZONE - ZONE_TO_GEO_ZONE - TAX_RATE - TAX_RATE_DESCRIPTION
		 **/

		Collection txs = this.findBySchemeId(schemeId);

		if (txs != null) {
			Iterator i = txs.iterator();
			while (i.hasNext()) {
				TaxRateTaxTemplate t = (TaxRateTaxTemplate) i.next();

				TaxRate taxRate = new TaxRate();
				taxRate.setTaxClassId(t.getTaxClassId());
				taxRate.setTaxPriority(t.getTaxPriority());
				taxRate.setTaxZoneId(t.getZoneToGeoZone().getGeoZoneId());
				taxRate.setTaxRate(t.getTaxRate());
				taxRate.setMerchantId(merchantId);
				taxRate.setPiggyback(t.isPiggyback());

				
				Set descriptions = t.getDescriptions();
				if (descriptions != null) {
					HashSet descriptionset = new HashSet();
					Iterator desci = descriptions.iterator();
					while (desci.hasNext()) {
						TaxRateDescriptionTaxTemplate trt = (TaxRateDescriptionTaxTemplate) desci
								.next();
						TaxRateDescriptionTaxTemplateId id = trt.getId();

						TaxRateDescriptionId newId = new TaxRateDescriptionId();
						newId.setLanguageId(id.getLanguageId());
						newId.setTaxRateId(id.getTaxRateId());

						TaxRateDescription newDesc = new TaxRateDescription();
						newDesc.setTaxDescription(trt.getTaxDescription());
						newDesc.setId(newId);
						descriptionset.add(newDesc);
					}
					taxRate.setDescriptions(descriptionset);

				}

				this.saveOrUpdateTaxRate(taxRate, countryId, t
						.getZoneToGeoZone().getZoneId(), merchantId);

			}

		}

	}

}
