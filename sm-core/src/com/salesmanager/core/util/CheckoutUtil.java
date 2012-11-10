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
package com.salesmanager.core.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductAttribute;
import com.salesmanager.core.entity.catalog.ProductAttributeDownload;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductOption;
import com.salesmanager.core.entity.catalog.ProductOptionDescription;
import com.salesmanager.core.entity.catalog.ProductOptionValue;
import com.salesmanager.core.entity.catalog.ProductOptionValueDescription;
import com.salesmanager.core.entity.catalog.ProductPrice;
import com.salesmanager.core.entity.catalog.ProductPriceDescription;
import com.salesmanager.core.entity.catalog.ProductPriceSpecial;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductAttribute;
import com.salesmanager.core.entity.orders.OrderProductDownload;
import com.salesmanager.core.entity.orders.OrderProductPrice;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.www.SessionUtil;

public class CheckoutUtil {

	/**
	 * Creates an order product (OrderProduct) from a Product entity. An
	 * OrderProduct is the entity used during checkout and persisted with the
	 * order
	 * 
	 * @param productId
	 * @param locale
	 * @param currency
	 * @return
	 * @throws Exception
	 */
	public static OrderProduct createOrderProduct(long productId,
			Locale locale, String currency) throws Exception {

		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);
		Product p = cservice.getProduct(productId);

		OrderProduct scp = new OrderProduct();

		if (p != null) {

			scp.setMerchantId(p.getMerchantId());

			// check if attributes exist
			Collection attrs = cservice.getProductAttributes(p.getProductId(),
					locale.getLanguage());

			boolean hasPricedAttributes = false;
			if (attrs != null && attrs.size() > 0) {
				Iterator i = attrs.iterator();
				while (i.hasNext()) {
					ProductAttribute pa = (ProductAttribute) i.next();
					if (!pa.isAttributeDisplayOnly()) {
						hasPricedAttributes = true;
					}
				}
			}

			if (hasPricedAttributes) {
				scp.setAttributes(true);
			}

			scp.setProductName("");
			// name
			Set descriptions = p.getDescriptions();
			if (descriptions != null) {
				Iterator i = descriptions.iterator();
				while (i.hasNext()) {
					ProductDescription pd = (ProductDescription) i.next();
					if (pd.getId().getLanguageId() == LanguageUtil
							.getLanguageNumberCode(locale.getLanguage())) {
						scp.setProductName(pd.getProductName());
						scp.setProductDescription(pd.getProductDescription());
						break;
					}
				}
			}

			scp.setProductId(p.getProductId());
			scp.setTaxClassId(p.getProductTaxClassId());

			// get download
			ProductAttributeDownload download = cservice.getProductDownload(p
					.getProductId());

			if (download != null) {
				OrderProductDownload opd = new OrderProductDownload();
				opd.setDownloadCount(0);
				opd.setDownloadMaxdays(download.getProductAttributeMaxdays());
				opd.setDownloadMaxdays(download.getProductAttributeMaxdays());
				opd.setOrderProductFilename(download
						.getProductAttributeFilename());
				opd.setFileId(download.getProductAttributeId());
				Set downloads = new HashSet();
				downloads.add(opd);
				scp.setDownloads(downloads);
			}

			// now determine price
			BigDecimal price = ProductUtil.determinePriceNoDiscount(p, locale,
					currency);

			scp.setFinalPrice(price);
			scp.setProductPrice(price);
			scp.setCurrency(currency);
			scp.setProductQuantityOrderMax(p.getProductQuantityOrderMax());
			if (p.getSpecial() != null) {
				scp.setProductSpecialNewPrice(p.getSpecial()
						.getSpecialNewProductPrice());
				scp.setProductSpecialDateAvailable(p.getSpecial()
						.getSpecialDateAvailable());
				scp
						.setProductSpecialDateExpire(p.getSpecial()
								.getExpiresDate());
			}
			scp.setPriceText(CurrencyUtil.displayFormatedAmountNoCurrency(
					price, currency));
			scp.setPriceFormated(CurrencyUtil
					.displayFormatedAmountWithCurrency(price, currency));

			// original price
			scp.setOriginalProductPrice(price);

			scp.setProductImage(p.getProductImage());
			scp.setProductType(p.getProductType());
			scp.setProductVirtual(p.isProductVirtual());
			scp.setProductWidth(p.getProductWidth());
			scp.setProductWeight(p.getProductWeight());
			scp.setProductHeight(p.getProductHeight());
			scp.setProductLength(p.getProductLength());
			scp.setProductId(p.getProductId());

			Set pricesSet = p.getPrices();
			if (pricesSet != null) {
				Set productSet = new HashSet();
				Iterator i = pricesSet.iterator();
				while (i.hasNext()) {
					ProductPrice pp = (ProductPrice) i.next();

					ProductPriceSpecial pps = pp.getSpecial();
					if (pps != null) {
						pps.setOriginalPriceAmount(pp.getProductPriceAmount());
					}
					OrderProductPrice opp = new OrderProductPrice();
					opp.setDefaultPrice(pp.isDefaultPrice());
					opp.setProductPriceAmount(pp.getProductPriceAmount());
					opp.setProductPriceModuleName(pp
							.getProductPriceModuleName());
					opp.setProductPriceTypeId(pp.getProductPriceTypeId());
					opp.setSpecial(pps);

					Set priceDescriptions = pp.getPriceDescriptions();
					if (priceDescriptions != null
							&& priceDescriptions.size() > 0) {
						String priceDescription = "";
						for (Object o : priceDescriptions) {
							ProductPriceDescription ppd = (ProductPriceDescription) o;
							if (ppd.getId().getLanguageId() == LanguageUtil
									.getLanguageNumberCode(locale.getLanguage())) {
								priceDescription = ppd.getProductPriceName();
								break;
							}
						}
						opp.setProductPriceName(priceDescription);
					}

					productSet.add(opp);
				}
				scp.setPrices(productSet);
			}

			if (cservice.isProductSubscribtion(p)) {
				scp.setProductSubscribtion(true);
			}

			if (!p.isProductVirtual()) {
				scp.setShipping(true);
			}

		}

		return scp;

	}

	public static String getAttributesLine(OrderProduct product) {

		Set attributes = product.getOrderattributes();

		if (attributes != null) {

			StringBuffer attributesLine = null;

			attributesLine = new StringBuffer();
			int count = 0;
			Iterator i = attributes.iterator();
			while (i.hasNext()) {
				OrderProductAttribute opa = (OrderProductAttribute) i.next();

				if (count == 0) {
					attributesLine.append("[ ");
				}
				attributesLine.append(opa.getProductOption()).append(" -> ")
						.append(opa.getProductOptionValue());
				if (count + 1 == attributes.size()) {
					attributesLine.append("]");
				} else {
					attributesLine.append(", ");
				}
				count++;
			}

			return attributesLine.toString();

		} else {
			return null;
		}

	}

	/**
	 * Add attributes to an OrderProduct and calculates the OrderProductPrice
	 * accordingly
	 * 
	 * @param attributes
	 * @param product
	 * @param currency
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	public static OrderProduct addAttributesToProduct(
			List<OrderProductAttribute> attributes, OrderProduct product,
			String currency, Locale locale) throws Exception {

		Locale loc = locale;
		String lang = loc.getLanguage();

		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);

		BigDecimal sumPrice = null;

		// get attributes for this product
		Collection productAttributes = cservice.getProductAttributes(product
				.getProductId(), locale.getLanguage());
		Map mapAttributes = new HashMap();
		if (productAttributes != null) {

			Iterator i = productAttributes.iterator();
			while (i.hasNext()) {
				ProductAttribute p = (ProductAttribute) i.next();
				mapAttributes.put(p.getOptionValueId(), p);
			}
		}

		StringBuffer attributesLine = null;

		if (attributes != null) {
			attributesLine = new StringBuffer();
			int count = 0;
			Iterator i = attributes.iterator();
			while (i.hasNext()) {
				OrderProductAttribute opa = (OrderProductAttribute) i.next();
				String attrPriceText = opa.getPrice();
				BigDecimal attrPrice = null;
				if (attrPriceText != null) {
					attrPrice = CurrencyUtil.validateCurrency(attrPriceText,
							currency);
				} else {
					attrPrice = opa.getOptionValuePrice();
				}
				// get all information from the attribute
				ProductAttribute pa = (ProductAttribute) mapAttributes.get(opa
						.getProductOptionValueId());
				if (pa != null) {
					if (attrPrice == null) {
						attrPrice = pa.getOptionValuePrice();
					}
				}
				if (attrPrice != null) {
					opa.setOptionValuePrice(attrPrice);
					opa.setPrice(CurrencyUtil.displayFormatedAmountNoCurrency(
							attrPrice, currency));
					if (sumPrice == null) {
						// try {
						// sumPrice= new
						// BigDecimal(attrPrice.doubleValue()).setScale(BigDecimal.ROUND_UNNECESSARY);
						sumPrice = attrPrice;
						// } catch (Exception e) {
						// }

					} else {
						BigDecimal currentPrice = sumPrice;
						sumPrice = currentPrice.add(attrPrice);
					}
				}
				opa.setOrderProductId(product.getProductId());
				opa.setProductAttributeIsFree(pa.isProductAttributeIsFree());

				opa.setProductOption("");
				if (StringUtils.isBlank(opa.getProductOptionValue())) {
					opa.setProductOptionValue("");
				}

				ProductOption po = pa.getProductOption();
				Set poDescriptions = po.getDescriptions();
				if (poDescriptions != null) {
					Iterator pi = poDescriptions.iterator();
					while (pi.hasNext()) {
						ProductOptionDescription pod = (ProductOptionDescription) pi
								.next();
						if (pod.getId().getLanguageId() == LanguageUtil
								.getLanguageNumberCode(lang)) {
							opa.setProductOption(pod.getProductOptionName());
							break;
						}
					}
				}

				if (StringUtils.isBlank(opa.getProductOptionValue())) {
					ProductOptionValue pov = pa.getProductOptionValue();
					if (pov != null) {
						Set povDescriptions = pov.getDescriptions();
						if (povDescriptions != null) {
							Iterator povi = povDescriptions.iterator();
							while (povi.hasNext()) {
								ProductOptionValueDescription povd = (ProductOptionValueDescription) povi
										.next();
								if (povd.getId().getLanguageId() == LanguageUtil
										.getLanguageNumberCode(lang)) {
									opa.setProductOptionValue(povd
											.getProductOptionValueName());
									break;
								}
							}
						}
					}
				}
				opa.setProductAttributeWeight(pa.getProductAttributeWeight());
				if (count == 0) {
					attributesLine.append("[ ");
				}
				attributesLine.append(opa.getProductOption()).append(" -> ")
						.append(opa.getProductOptionValue());
				if (count + 1 == attributes.size()) {
					attributesLine.append("]");
				} else {
					attributesLine.append(", ");
				}
				count++;
			}
		}

		// add attribute price to productprice
		if (sumPrice != null) {

			// get product price
			BigDecimal productPrice = product.getProductPrice();
			productPrice = productPrice.add(sumPrice);

			// added
			product.setProductPrice(productPrice);

			BigDecimal finalPrice = productPrice.multiply(new BigDecimal(
					product.getProductQuantity()));

			product.setPriceText(CurrencyUtil.displayFormatedAmountNoCurrency(
					productPrice, currency));
			product.setPriceFormated(CurrencyUtil
					.displayFormatedAmountWithCurrency(finalPrice, currency));
		}

		if (attributesLine != null) {
			product.setAttributesLine(attributesLine.toString());
		}

		Set attributesSet = new HashSet(attributes);

		product.setOrderattributes(attributesSet);

		return product;
	}

	public String buildAttributesLine(
			Collection<OrderProductAttribute> attributes,
			OrderProduct orderProduct) {
		return null;
	}

	/**
	 * OrderProductAttribute is configured from javascript This code needs to
	 * invoke catalog objects because it requires getProductOptionValueId, will
	 * not change any price Add attributes to product, add attribute offset
	 * price to original product price
	 * 
	 * @param attributes
	 * @param lineId
	 * @param currency
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static OrderProduct addAttributesFromRawObjects(
			List<OrderProductAttribute> attributes, long productId,
			String lineId, String currency, HttpServletRequest request)
			throws Exception {

		Locale loc = request.getLocale();
		String lang = loc.getLanguage();

		HttpSession session = request.getSession();

		Map cartLines = SessionUtil.getOrderProducts(request);

		if (cartLines == null) {
			throw new Exception(
					"No OrderProduct exixt yet, cannot assign attributes");
		}

		OrderProduct scp = (OrderProduct) cartLines.get(lineId);

		if (scp == null) {
			throw new Exception("No OrderProduct exixt for lineId " + lineId);
		}

		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);

		BigDecimal sumPrice = null;

		// make sure OrderProduct and productId match
		if (scp.getProductId() == productId) {

			Locale locale = (Locale) request.getSession().getAttribute(
					"WW_TRANS_I18N_LOCALE");
			if (locale == null)
				locale = request.getLocale();

			// get attributes for this product
			Collection productAttributes = cservice.getProductAttributes(
					productId, locale.getLanguage());
			Map mapAttributes = new HashMap();
			if (productAttributes != null) {

				Iterator i = productAttributes.iterator();
				while (i.hasNext()) {
					ProductAttribute p = (ProductAttribute) i.next();
					mapAttributes.put(p.getOptionValueId(), p);
				}

			}

			if (scp != null) {

				StringBuffer attributesLine = null;

				if (attributes != null) {
					attributesLine = new StringBuffer();
					int count = 0;
					Iterator i = attributes.iterator();
					while (i.hasNext()) {
						OrderProductAttribute opa = (OrderProductAttribute) i
								.next();
						String attrPriceText = opa.getPrice();
						BigDecimal attrPrice = null;
						if (attrPriceText != null) {
							attrPrice = CurrencyUtil.validateCurrency(
									attrPriceText, currency);
						} else {
							attrPrice = opa.getOptionValuePrice();
						}
						// get all information from the attribute
						ProductAttribute pa = (ProductAttribute) mapAttributes
								.get(opa.getProductOptionValueId());
						if (pa != null) {
							if (attrPrice == null) {
								attrPrice = pa.getOptionValuePrice();
							}
						}
						if (attrPrice != null) {
							opa.setOptionValuePrice(attrPrice);
							opa.setPrice(CurrencyUtil
									.displayFormatedAmountNoCurrency(attrPrice,
											currency));
							if (sumPrice == null) {
								sumPrice = new BigDecimal(attrPrice
										.doubleValue()).setScale(2);
							} else {
								// double pr = sumPrice.doubleValue() +
								// attrPrice.doubleValue();
								// sumPrice = new
								// BigDecimal(sumPrice.doubleValue() +
								// attrPrice.doubleValue());
								BigDecimal currentPrice = sumPrice;
								sumPrice = currentPrice.add(attrPrice);
							}
						}
						// opa.setOrderId(Long.parseLong(orderId));
						opa.setOrderProductId(productId);
						opa.setProductAttributeIsFree(pa
								.isProductAttributeIsFree());

						opa.setProductOption("");
						if (StringUtils.isBlank(opa.getProductOptionValue())) {
							opa.setProductOptionValue("");
						}

						ProductOption po = pa.getProductOption();
						Set poDescriptions = po.getDescriptions();
						if (poDescriptions != null) {
							Iterator pi = poDescriptions.iterator();
							while (pi.hasNext()) {
								ProductOptionDescription pod = (ProductOptionDescription) pi
										.next();
								if (pod.getId().getLanguageId() == LanguageUtil
										.getLanguageNumberCode(lang)) {
									opa.setProductOption(pod
											.getProductOptionName());
									break;
								}
							}
						}

						if (StringUtils.isBlank(opa.getProductOptionValue())) {
							ProductOptionValue pov = pa.getProductOptionValue();
							if (pov != null) {
								Set povDescriptions = pov.getDescriptions();
								if (povDescriptions != null) {
									Iterator povi = povDescriptions.iterator();
									while (povi.hasNext()) {
										ProductOptionValueDescription povd = (ProductOptionValueDescription) povi
												.next();
										if (povd.getId().getLanguageId() == LanguageUtil
												.getLanguageNumberCode(lang)) {
											opa
													.setProductOptionValue(povd
															.getProductOptionValueName());
											break;
										}
									}
								}
							}
						}
						opa.setProductAttributeWeight(pa
								.getProductAttributeWeight());
						if (count == 0) {
							attributesLine.append("[ ");
						}
						attributesLine.append(opa.getProductOption()).append(
								" -> ").append(opa.getProductOptionValue());
						if (count + 1 == attributes.size()) {
							attributesLine.append("]");
						} else {
							attributesLine.append(", ");
						}
						count++;
					}
				}

				// add attribute price to productprice
				if (sumPrice != null) {
					// sumPrice = sumPrice.multiply(new
					// BigDecimal(scp.getProductQuantity()));

					scp.setAttributeAdditionalCost(sumPrice);// add additional
																// attribute
																// price

					// get product price
					BigDecimal productPrice = scp.getProductPrice();
					productPrice = productPrice.add(sumPrice);

					// added
					scp.setProductPrice(productPrice);

					// BigDecimal finalPrice = scp.getFinalPrice();
					BigDecimal finalPrice = productPrice
							.multiply(new BigDecimal(scp.getProductQuantity()));
					// finalPrice = finalPrice.add(sumPrice);
					// BigDecimal cost = scp.get

					scp.setPriceText(CurrencyUtil
							.displayFormatedAmountNoCurrency(productPrice,
									currency));
					scp.setPriceFormated(CurrencyUtil
							.displayFormatedAmountWithCurrency(finalPrice,
									currency));
				}

				if (attributesLine != null) {
					scp.setAttributesLine(attributesLine.toString());
				}

				Set attributesSet = new HashSet(attributes);

				scp.setOrderattributes(attributesSet);



			}

		}

		return scp;
	}

	/**
	 * OrderProductAttribute is configured from javascript This code needs to
	 * invoke catalog objects require getProductOptionValueId
	 * 
	 * @param attributes
	 * @param lineId
	 * @param currency
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static OrderProduct addAttributesFromRawObjects(
			List<OrderProductAttribute> attributes, OrderProduct scp,
			String currency, HttpServletRequest request) throws Exception {

		Locale loc = request.getLocale();
		String lang = loc.getLanguage();

		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);

		BigDecimal sumPrice = null;

		Locale locale = (Locale) request.getSession().getAttribute(
				"WW_TRANS_I18N_LOCALE");
		if (locale == null)
			locale = request.getLocale();

		// get attributes for this product
		Collection productAttributes = cservice.getProductAttributes(scp
				.getProductId(), locale.getLanguage());
		Map mapAttributes = new HashMap();
		if (productAttributes != null) {

			Iterator i = productAttributes.iterator();
			while (i.hasNext()) {
				ProductAttribute p = (ProductAttribute) i.next();
				mapAttributes.put(p.getOptionValueId(), p);
			}

		}

		if (scp != null) {

			StringBuffer attributesLine = null;

			if (attributes != null) {
				attributesLine = new StringBuffer();
				int count = 0;
				Iterator i = attributes.iterator();
				while (i.hasNext()) {
					OrderProductAttribute opa = (OrderProductAttribute) i
							.next();
					String attrPriceText = opa.getPrice();
					BigDecimal attrPrice = null;
					if (attrPriceText != null) {
						attrPrice = CurrencyUtil.validateCurrency(
								attrPriceText, currency);
					} else {
						attrPrice = opa.getOptionValuePrice();
					}
					// get all information from the attribute
					ProductAttribute pa = (ProductAttribute) mapAttributes
							.get(opa.getProductOptionValueId());
					if (pa != null) {
						if (attrPrice == null) {
							attrPrice = pa.getOptionValuePrice();
						}
					}
					if (attrPrice != null) {
						opa.setOptionValuePrice(attrPrice);
						opa.setPrice(CurrencyUtil
								.displayFormatedAmountNoCurrency(attrPrice,
										currency));
						if (sumPrice == null) {
							sumPrice = new BigDecimal(attrPrice.doubleValue())
									.setScale(2);
						} else {
		
							BigDecimal currentPrice = sumPrice;
							sumPrice = currentPrice.add(attrPrice);
						}
					}
					// opa.setOrderId(Long.parseLong(orderId));
					opa.setOrderProductId(scp.getProductId());
					opa
							.setProductAttributeIsFree(pa
									.isProductAttributeIsFree());

					opa.setProductOption("");
					if (StringUtils.isBlank(opa.getProductOptionValue())) {
						opa.setProductOptionValue("");
					}

					ProductOption po = pa.getProductOption();
					Set poDescriptions = po.getDescriptions();
					if (poDescriptions != null) {
						Iterator pi = poDescriptions.iterator();
						while (pi.hasNext()) {
							ProductOptionDescription pod = (ProductOptionDescription) pi
									.next();
							if (pod.getId().getLanguageId() == LanguageUtil
									.getLanguageNumberCode(lang)) {
								opa
										.setProductOption(pod
												.getProductOptionName());
								break;
							}
						}
					}

					if (StringUtils.isBlank(opa.getProductOptionValue())) {
						ProductOptionValue pov = pa.getProductOptionValue();
						if (pov != null) {
							Set povDescriptions = pov.getDescriptions();
							if (povDescriptions != null) {
								Iterator povi = povDescriptions.iterator();
								while (povi.hasNext()) {
									ProductOptionValueDescription povd = (ProductOptionValueDescription) povi
											.next();
									if (povd.getId().getLanguageId() == LanguageUtil
											.getLanguageNumberCode(lang)) {
										opa.setProductOptionValue(povd
												.getProductOptionValueName());
										break;
									}
								}
							}
						}
					}
					opa.setProductAttributeWeight(pa
							.getProductAttributeWeight());
					if (count == 0) {
						attributesLine.append("[ ");
					}
					attributesLine.append(opa.getProductOption())
							.append(" -> ").append(opa.getProductOptionValue());
					if (count + 1 == attributes.size()) {
						attributesLine.append("]");
					} else {
						attributesLine.append(", ");
					}
					count++;
				}
			}

			// add attribute price to productprice
			if (sumPrice != null) {
				// sumPrice = sumPrice.multiply(new
				// BigDecimal(scp.getProductQuantity()));

				// get product price
				BigDecimal productPrice = scp.getProductPrice();
				productPrice = productPrice.add(sumPrice);

				// added
				scp.setProductPrice(productPrice);

				// BigDecimal finalPrice = scp.getFinalPrice();
				BigDecimal finalPrice = productPrice.multiply(new BigDecimal(
						scp.getProductQuantity()));
				// finalPrice = finalPrice.add(sumPrice);
				// BigDecimal cost = scp.get

				scp.setPriceText(CurrencyUtil.displayFormatedAmountNoCurrency(
						productPrice, currency));
				scp
						.setPriceFormated(CurrencyUtil
								.displayFormatedAmountWithCurrency(finalPrice,
										currency));
			}

			if (attributesLine != null) {
				scp.setAttributesLine(attributesLine.toString());
			}

			Set attributesSet = new HashSet(attributes);

			scp.setOrderattributes(attributesSet);

		}

		return scp;
	}

}
