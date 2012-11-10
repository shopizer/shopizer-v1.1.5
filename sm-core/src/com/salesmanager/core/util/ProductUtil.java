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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductAttribute;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductPrice;
import com.salesmanager.core.entity.catalog.ProductPriceSpecial;
import com.salesmanager.core.entity.catalog.Special;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductPrice;
import com.salesmanager.core.entity.orders.OrderProductPriceSpecial;
import com.salesmanager.core.entity.reference.Currency;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.order.OrderService;

public class ProductUtil {

	private static Logger log = Logger.getLogger(ProductUtil.class);

	/**
	 * Set product information to Order product
	 * @param orderProduct
	 * @param currency
	 * @return
	 */
	public static OrderProduct initOrderProduct(OrderProduct orderProduct,
			String currency) {
		try {

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			
			CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);
			Product product = cservice.getProduct(orderProduct.getProductId());
			
			int maxOrderQuantity = CatalogConstants.DEFAULT_MAX_ORDER_BY_PRODUCT;
			if(product!=null) {
				maxOrderQuantity = product.getProductQuantityOrderMax();
			}
			
			orderProduct.setProductQuantityOrderMax(maxOrderQuantity);
			orderProduct.setPriceFormated(CurrencyUtil
					.displayFormatedAmountWithCurrency(orderProduct
							.getProductPrice(), currency));
			double finalPrice = orderProduct.getProductPrice().doubleValue()
					* orderProduct.getProductQuantity();
			//revise precision
			BigDecimal bdFinalPrice = new BigDecimal(finalPrice);
			orderProduct.setCostText(CurrencyUtil
					.displayFormatedAmountWithCurrency(bdFinalPrice, currency));
			orderProduct.setPriceText(CurrencyUtil
					.displayFormatedAmountNoCurrency(orderProduct
							.getProductPrice(), currency));
			orderProduct.setFinalPrice(bdFinalPrice);
			orderProduct.setQuantityText(String.valueOf(orderProduct
					.getProductQuantity()));
			orderProduct.setPriceText(CurrencyUtil
					.displayFormatedAmountNoCurrency(orderProduct
							.getProductPrice(), currency));
			if (orderProduct.getOrderattributes() != null
					&& orderProduct.getOrderattributes().size() > 0) {
				orderProduct.setAttributes(true);
				orderProduct.setAttributesLine(CheckoutUtil
						.getAttributesLine(orderProduct));
			}

			// get all discounts associated with prices
			Set prices = orderProduct.getPrices();
			if (prices != null) {
				Iterator i = prices.iterator();
				while (i.hasNext()) {
					OrderProductPrice opp = (OrderProductPrice) i.next();
					// get specials
					OrderProductPriceSpecial opps = oservice
							.getOrderProductPriceSpecial(opp
									.getOrderProductPrice());
					if (opps != null) {
						ProductPriceSpecial pps = new ProductPriceSpecial();
						pps.setOriginalPriceAmount(opps.getOriginalPrice());
						pps.setProductPriceSpecialAmount(opps
								.getSpecialNewProductPrice());
						pps.setProductPriceSpecialDurationDays(opps
								.getOrderProductSpecialDurationDays());
						pps.setProductPriceSpecialEndDate(opps
								.getOrderProductSpecialEndDate());
						pps.setProductPriceSpecialStartDate(opps
								.getOrderProductPriceSpecialStartDate());
						opp.setSpecial(pps);
					}

				}
			}

		} catch (Exception e) {
			log.error(e);
		}
		return orderProduct;
	}

	public static String getProductName(Product product, Locale locale) {

		try {

			Set descriptionset = product.getDescriptions();
			int lang = LanguageUtil.getLanguageNumberCode(locale.getLanguage());
			if (descriptionset != null) {
				Iterator i = descriptionset.iterator();
				while (i.hasNext()) {
					ProductDescription desc = (ProductDescription) i.next();
					if (desc.getId().getLanguageId() == lang) {
						return desc.getProductName();
					}
				}
			}

		} catch (Exception e) {
			log.error(e);
		}

		return "";

	}

	/**
	 * Format like <blue>[if discount striked]SYMBOL BASEAMOUNT</blue> [if
	 * discount <red>SYMBOL DISCOUNTAMOUNT</red>] [if discount <red>Save:
	 * PERCENTAGE AMOUNT</red>] [if qty discount <red>Buy QTY save [if price qty
	 * discount AMOUNT] [if percent qty discount PERCENT]</red>]
	 * 
	 * @param ctx
	 * @param view
	 * @return
	 */
	public static String formatHTMLProductPriceWithAttributes(Locale locale,
			String currency, Product view, Collection attributes,
			boolean showDiscountDate) {

		if (currency == null) {
			log.error("Currency is null ...");
			return "-N/A-";
		}

		int decimalPlace = 2;

		String prefix = "";
		String suffix = "";

		Map currenciesmap = RefCache.getCurrenciesListWithCodes();
		Currency c = (Currency) currenciesmap.get(currency);

		// regular price
		BigDecimal bdprodprice = view.getProductPrice();

		// determine any properties prices
		BigDecimal attributesPrice = null;

		if (attributes != null) {
			Iterator i = attributes.iterator();
			while (i.hasNext()) {
				ProductAttribute attr = (ProductAttribute) i.next();
				if (!attr.isAttributeDisplayOnly()
						&& attr.getOptionValuePrice().longValue() > 0) {
					if (attributesPrice == null) {
						attributesPrice = new BigDecimal(0);
					}
					attributesPrice = attributesPrice.add(attr
							.getOptionValuePrice());
				}
			}
		}

		if (attributesPrice != null) {
			bdprodprice = bdprodprice.add(attributesPrice);// new price with
															// properties
		}

		// discount price
		java.util.Date spdate = null;
		java.util.Date spenddate = null;
		BigDecimal bddiscountprice = null;
		Special special = view.getSpecial();
		if (special != null) {
			bddiscountprice = special.getSpecialNewProductPrice();
			if (attributesPrice != null) {
				bddiscountprice = bddiscountprice.add(attributesPrice);// new
																		// price
																		// with
																		// properties
			}
			spdate = special.getSpecialDateAvailable();
			spenddate = special.getExpiresDate();
		}

		// all other prices
		Set prices = view.getPrices();
		if (prices != null) {
			Iterator pit = prices.iterator();
			while (pit.hasNext()) {
				ProductPrice pprice = (ProductPrice) pit.next();
				if (pprice.isDefaultPrice()) {
					pprice.setLocale(locale);
					suffix = pprice.getPriceSuffix();
					bddiscountprice = null;
					spdate = null;
					spenddate = null;
					bdprodprice = pprice.getProductPriceAmount();
					if (attributesPrice != null) {
						bdprodprice = bdprodprice.add(attributesPrice);// new
																		// price
																		// with
																		// properties
					}
					ProductPriceSpecial ppspecial = pprice.getSpecial();
					if (ppspecial != null) {
						if (ppspecial.getProductPriceSpecialStartDate() != null
								&& ppspecial.getProductPriceSpecialEndDate() != null) {
							spdate = ppspecial
									.getProductPriceSpecialStartDate();
							spenddate = ppspecial
									.getProductPriceSpecialEndDate();
						}
						bddiscountprice = ppspecial
								.getProductPriceSpecialAmount();
						if (bddiscountprice != null && attributesPrice != null) {
							bddiscountprice = bddiscountprice
									.add(attributesPrice);// new price with
															// properties
						}
					}
					break;
				}
			}
		}

		double fprodprice = 0;
		;
		if (bdprodprice != null) {
			fprodprice = bdprodprice.setScale(decimalPlace,
					BigDecimal.ROUND_HALF_UP).doubleValue();
		}

		// regular price String
		String regularprice = CurrencyUtil
				.displayFormatedCssAmountWithCurrency(bdprodprice, currency);

		Date dt = new Date();

		// discount price String
		String discountprice = null;
		String savediscount = null;
		if (bddiscountprice != null
				&& (spdate != null && spdate.before(new Date(dt.getTime())) && spenddate
						.after(new Date(dt.getTime())))) {

			double fdiscountprice = bddiscountprice.setScale(decimalPlace,
					BigDecimal.ROUND_HALF_UP).doubleValue();

			discountprice = CurrencyUtil.displayFormatedAmountWithCurrency(
					bddiscountprice, currency);

			double arith = fdiscountprice / fprodprice;
			double fsdiscount = 100 - arith * 100;

			Float percentagediscount = new Float(fsdiscount);

			savediscount = String.valueOf(percentagediscount.intValue());

		}

		StringBuffer p = new StringBuffer();
		p.append("<div class='product-price'>");
		if (discountprice == null) {
			p.append("<div class='product-price-price' style='width:50%;float:left;'>");
			p.append(regularprice);
			if (!StringUtils.isBlank(suffix)) {
				p.append(suffix).append(" ");
			}
			p.append("</div>");
			p.append("<div class='product-line'>&nbsp;</div>");
		} else {
			p.append("<div style='width:50%;float:left;'>");
			p.append("<strike>").append(regularprice);
			if (!StringUtils.isBlank(suffix)) {
				p.append(suffix).append(" ");
			}
			p.append("</strike>");
			p.append("</div>");
			p.append("<div style='width:50%;float:right;'>");
			p.append("<font color='red'>").append(discountprice);
			if (!StringUtils.isBlank(suffix)) {
				p.append(suffix).append(" ");
			}
			p.append("</font>").append("<br>").append(
					"<font color='red' style='font-size:75%;'>").append(
					LabelUtil.getInstance().getText(locale,
							"label.generic.save")).append(": ").append(
					savediscount).append(
					LabelUtil.getInstance().getText(locale,
							"label.generic.percentsign")).append(" ").append(
					LabelUtil.getInstance()
							.getText(locale, "label.generic.off")).append(
					"</font>");

			if (showDiscountDate && spenddate != null) {
				p.append("<br>").append(" <font style='font-size:65%;'>")
						.append(
								LabelUtil.getInstance().getText(locale,
										"label.generic.until"))
						.append("&nbsp;")
						.append(DateUtil.formatDate(spenddate)).append(
								"</font>");
			}

			p.append("</div>").toString();
		}
		p.append("</div>");
		return p.toString();

	}

	public static String formatHTMLProductPrice(Locale locale, String currency,
			Product view, boolean showDiscountDate, boolean shortDiscountFormat) {

		if (currency == null) {
			log.error("Currency is null ...");
			return "-N/A-";
		}

		int decimalPlace = 2;

		String prefix = "";
		String suffix = "";

		Map currenciesmap = RefCache.getCurrenciesListWithCodes();
		Currency c = (Currency) currenciesmap.get(currency);

		// regular price
		BigDecimal bdprodprice = view.getProductPrice();

		Date dt = new Date();

		// discount price
		java.util.Date spdate = null;
		java.util.Date spenddate = null;
		BigDecimal bddiscountprice = null;
		Special special = view.getSpecial();
		if (special != null) {
			spdate = special.getSpecialDateAvailable();
			spenddate = special.getExpiresDate();
			if (spdate.before(new Date(dt.getTime()))
					&& spenddate.after(new Date(dt.getTime()))) {
				bddiscountprice = special.getSpecialNewProductPrice();
			}
		}

		// all other prices
		Set prices = view.getPrices();
		if (prices != null) {
			Iterator pit = prices.iterator();
			while (pit.hasNext()) {
				ProductPrice pprice = (ProductPrice) pit.next();
				if (pprice.isDefaultPrice()) {
					pprice.setLocale(locale);
					suffix = pprice.getPriceSuffix();
					bddiscountprice = null;
					spdate = null;
					spenddate = null;
					bdprodprice = pprice.getProductPriceAmount();
					ProductPriceSpecial ppspecial = pprice.getSpecial();
					if (ppspecial != null) {
						if (ppspecial.getProductPriceSpecialStartDate() != null
								&& ppspecial.getProductPriceSpecialEndDate() != null) {
							spdate = ppspecial
									.getProductPriceSpecialStartDate();
							spenddate = ppspecial
									.getProductPriceSpecialEndDate();
						}
						bddiscountprice = ppspecial
								.getProductPriceSpecialAmount();
					}
					break;
				}
			}
		}

		double fprodprice = 0;
		;
		if (bdprodprice != null) {
			fprodprice = bdprodprice.setScale(decimalPlace,
					BigDecimal.ROUND_HALF_UP).doubleValue();
		}

		// regular price String
		String regularprice = CurrencyUtil
				.displayFormatedCssAmountWithCurrency(bdprodprice, currency);

		// discount price String
		String discountprice = null;
		String savediscount = null;

		if (bddiscountprice != null
				&& (spdate != null && spdate.before(new Date(dt.getTime())) && spenddate
						.after(new Date(dt.getTime())))) {

			double fdiscountprice = bddiscountprice.setScale(decimalPlace,
					BigDecimal.ROUND_HALF_UP).doubleValue();

			discountprice = CurrencyUtil.displayFormatedAmountWithCurrency(
					bddiscountprice, currency);

			double arith = fdiscountprice / fprodprice;
			double fsdiscount = 100 - arith * 100;

			Float percentagediscount = new Float(fsdiscount);

			savediscount = String.valueOf(percentagediscount.intValue());

		}

		StringBuffer p = new StringBuffer();
		p.append("<div class='product-price'>");
		if (discountprice == null) {
			p.append("<div class='product-price-price' style='width:50%;float:left;'>");
			p.append(regularprice);
			if (!StringUtils.isBlank(suffix)) {
				p.append(suffix).append(" ");
			}
			p.append("</div>");
			p.append("<div class='product-line'>&nbsp;</div>");
		} else {
			p.append("<div style='width:50%;float:left;'>");
			p.append("<strike>").append(regularprice);
			if (!StringUtils.isBlank(suffix)) {
				p.append(suffix).append(" ");
			}
			p.append("</strike>");
			p.append("</div>");
			p.append("<div style='width:50%;float:right;'>");
			p.append("<font color='red'>").append(discountprice);
			if (!StringUtils.isBlank(suffix)) {
				p.append(suffix).append(" ");
			}
			p.append("</font>");
			if(!shortDiscountFormat) {
					p.append("<br>").append(
					"<font color='red' style='font-size:75%;'>").append(
					LabelUtil.getInstance().getText(locale,
							"label.generic.save")).append(": ").append(
					savediscount).append(
					LabelUtil.getInstance().getText(locale,
							"label.generic.percentsign")).append(" ").append(
					LabelUtil.getInstance()
							.getText(locale, "label.generic.off")).append(
					"</font>");
			}

			if (showDiscountDate && spenddate != null) {
				p.append("<br>").append(" <font style='font-size:65%;'>")
						.append(
								LabelUtil.getInstance().getText(locale,
										"label.generic.until"))
						.append("&nbsp;")
						.append(DateUtil.formatDate(spenddate)).append(
								"</font>");
			}

			p.append("</div>").toString();
		}
		p.append("</div>");
		return p.toString();

	}

	public static BigDecimal determinePriceNoDiscount(Product product,
			Locale locale, String currency) {

		BigDecimal price = product.getProductPrice();

		// all other prices
		Set prices = product.getPrices();
		if (prices != null) {
			Iterator pit = prices.iterator();
			while (pit.hasNext()) {
				ProductPrice pprice = (ProductPrice) pit.next();
				pprice.setLocale(locale);

				if (pprice.isDefaultPrice()) {// overwrites default price
					price = pprice.getProductPriceAmount();
				}
			}
		}

		return price;

	}

	public static BigDecimal determinePriceNoDiscountWithAttributes(
			Product product, Collection<Long> attributes, Locale locale,
			String currency) {

		BigDecimal price = product.getProductPrice();

		// all other prices
		Set prices = product.getPrices();
		if (prices != null) {
			Iterator pit = prices.iterator();
			while (pit.hasNext()) {
				ProductPrice pprice = (ProductPrice) pit.next();
				pprice.setLocale(locale);

				if (pprice.isDefaultPrice()) {// overwrites default price
					price = pprice.getProductPriceAmount();
				}
			}
		}

		BigDecimal attributesPrice = null;

		if (attributes != null) {
			Iterator i = attributes.iterator();

			while (i.hasNext()) {
				ProductAttribute attr = (ProductAttribute) i.next();
				if (!attr.isAttributeDisplayOnly()
						&& attr.getOptionValuePrice().longValue() > 0) {
					if (attributesPrice == null) {
						attributesPrice = new BigDecimal(0);
					}
					attributesPrice = attributesPrice.add(attr
							.getOptionValuePrice());
				}
			}
		}

		if (attributesPrice != null) {
			price = price.add(attributesPrice);
		}

		return price;

	}

	public static BigDecimal determinePriceWithAttributes(Product product,
			Collection attributes, Locale locale, String currency) {

		int decimalPlace = 2;

		Map currenciesmap = RefCache.getCurrenciesListWithCodes();
		Currency c = (Currency) currenciesmap.get(currency);

		// prices
		BigDecimal bdprodprice = product.getProductPrice();
		BigDecimal bddiscountprice = null;

		// discount price
		Special special = product.getSpecial();

		Date dt = new Date();

		java.util.Date spdate = null;
		java.util.Date spenddate = null;

		if (special != null) {
			spdate = special.getSpecialDateAvailable();
			spenddate = special.getExpiresDate();
			if (spdate.before(new Date(dt.getTime()))
					&& spenddate.after(new Date(dt.getTime()))) {
				bddiscountprice = special.getSpecialNewProductPrice();
			}
		}

		// all other prices
		Set prices = product.getPrices();
		if (prices != null) {
			Iterator pit = prices.iterator();
			while (pit.hasNext()) {
				ProductPrice pprice = (ProductPrice) pit.next();
				pprice.setLocale(locale);

				if (pprice.isDefaultPrice()) {// overwrites default price
					bddiscountprice = null;
					spdate = null;
					spenddate = null;
					bdprodprice = pprice.getProductPriceAmount();
					ProductPriceSpecial ppspecial = pprice.getSpecial();
					if (ppspecial != null) {
						if (ppspecial.getProductPriceSpecialStartDate() != null
								&& ppspecial.getProductPriceSpecialEndDate() != null

								&& ppspecial.getProductPriceSpecialStartDate()
										.before(new Date(dt.getTime()))
								&& ppspecial.getProductPriceSpecialEndDate()
										.after(new Date(dt.getTime()))) {

							bddiscountprice = ppspecial
									.getProductPriceSpecialAmount();

						} else if (ppspecial
								.getProductPriceSpecialDurationDays() > -1) {

							bddiscountprice = ppspecial
									.getProductPriceSpecialAmount();
						}
					}
					break;
				}
			}
		}

		double fprodprice = 0;
		;
		if (bdprodprice != null) {
			fprodprice = bdprodprice.setScale(decimalPlace,
					BigDecimal.ROUND_HALF_UP).doubleValue();
		}

		// determine any properties prices
		BigDecimal attributesPrice = null;

		if (attributes != null) {
			Iterator i = attributes.iterator();

			while (i.hasNext()) {
				ProductAttribute attr = (ProductAttribute) i.next();
				if (!attr.isAttributeDisplayOnly()
						&& attr.getOptionValuePrice().longValue() > 0) {
					if (attributesPrice == null) {
						attributesPrice = new BigDecimal(0);
					}
					attributesPrice = attributesPrice.add(attr
							.getOptionValuePrice());
				}
			}
		}

		if (bddiscountprice != null) {

			if (attributesPrice != null) {
				bddiscountprice = bddiscountprice.add(attributesPrice);
			}

			return bddiscountprice;

		} else {

			if (attributesPrice != null) {
				bdprodprice = bdprodprice.add(attributesPrice);
			}

			return bdprodprice;
		}

	}

	public static BigDecimal determinePrice(Product product, Locale locale,
			String currency) {

		int decimalPlace = 2;

		Map currenciesmap = RefCache.getCurrenciesListWithCodes();
		Currency c = (Currency) currenciesmap.get(currency);

		// prices
		BigDecimal bdprodprice = product.getProductPrice();
		BigDecimal bddiscountprice = null;

		// discount price
		Special special = product.getSpecial();

		java.util.Date spdate = null;
		java.util.Date spenddate = null;

		if (special != null) {
			bddiscountprice = special.getSpecialNewProductPrice();
			spdate = special.getSpecialDateAvailable();
			spenddate = special.getExpiresDate();
		}

		Date dt = new Date();

		// all other prices
		Set prices = product.getPrices();
		if (prices != null) {
			Iterator pit = prices.iterator();
			while (pit.hasNext()) {
				ProductPrice pprice = (ProductPrice) pit.next();
				pprice.setLocale(locale);

				if (pprice.isDefaultPrice()) {// overwrites default price
					bddiscountprice = null;
					spdate = null;
					spenddate = null;
					bdprodprice = pprice.getProductPriceAmount();
					ProductPriceSpecial ppspecial = pprice.getSpecial();
					if (ppspecial != null) {
						if (ppspecial.getProductPriceSpecialStartDate() != null
								&& ppspecial.getProductPriceSpecialEndDate() != null

								&& ppspecial.getProductPriceSpecialStartDate()
										.before(new Date(dt.getTime()))
								&& ppspecial.getProductPriceSpecialEndDate()
										.after(new Date(dt.getTime()))) {

							bddiscountprice = ppspecial
									.getProductPriceSpecialAmount();

						} else if (ppspecial
								.getProductPriceSpecialDurationDays() > -1) {

							bddiscountprice = ppspecial
									.getProductPriceSpecialAmount();

						}
					}
					break;
				}
			}
		}

		double fprodprice = 0;

		if (bdprodprice != null) {
			fprodprice = bdprodprice.setScale(decimalPlace,
					BigDecimal.ROUND_HALF_UP).doubleValue();
		}

		if (bddiscountprice != null) {

			return bddiscountprice;

		} else {
			return bdprodprice;
		}

	}

	public static Date getDiscountEndDate(ProductPrice productPrice) {
		Date dt = new Date();
		BigDecimal price = productPrice.getProductPriceAmount();
		ProductPriceSpecial ppspecial = productPrice.getSpecial();
		if (ppspecial != null) {
			// this type of discount supercedes
			if (ppspecial.getProductPriceSpecialStartDate() != null
					&& ppspecial.getProductPriceSpecialEndDate() != null) {
				if (ppspecial.getProductPriceSpecialStartDate().before(
						new Date(dt.getTime()))
						&& ppspecial.getProductPriceSpecialEndDate().after(
								new Date(dt.getTime()))) {

					return ppspecial.getProductPriceSpecialEndDate();

				} else if (ppspecial.getProductPriceSpecialDurationDays() > -1) {

					Date startDate = ppspecial
							.getProductPriceSpecialStartDate();

					int numDays = ppspecial
							.getProductPriceSpecialDurationDays();
					Date purchased = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(dt);
					c.add(Calendar.DATE, numDays);

					if (dt.before(c.getTime())
							&& ppspecial.getProductPriceSpecialAmount()
									.floatValue() < productPrice
									.getProductPriceAmount().floatValue()) {
						return c.getTime();
					} else {
						return null;
					}
				}

			}
		}
		return null;
	}
	
	
	/**
	 * Detects a discount on base price
	 * @param product
	 * @return
	 */
	public static boolean hasDiscount(Product product) {
		


		// discount price
		java.util.Date spdate = null;
		java.util.Date spenddate = null;
		BigDecimal bddiscountprice = null;
		Special special = product.getSpecial();
		
		
		boolean hasDiscount = false;
		
		if (special != null) {
			bddiscountprice = special.getSpecialNewProductPrice();
			spdate = special.getSpecialDateAvailable();
			spenddate = special.getExpiresDate();
			
			if (special.getSpecialDateAvailable() != null
					&& special.getExpiresDate() != null) {
				if (special.getSpecialDateAvailable().before(
						new Date())
						&& special.getExpiresDate().after(
								new Date())) {
					
					hasDiscount = true;

				} 
			}
			
			if(product.getPrices()!=null && product.getPrices().size()>0) {
				
				for(Object o: product.getPrices()) {
					
					ProductPrice pp = (ProductPrice)o;
					if(pp.isDefaultPrice()) {
						hasDiscount = false;
						hasDiscount = hasDiscount(pp);
						if(hasDiscount) {
							break;
						}
					}
				}
			} 
		}
		
		return hasDiscount;
		
		
	}

	public static boolean hasDiscount(ProductPrice productPrice) {
		Date dt = new Date();
		BigDecimal price = productPrice.getProductPriceAmount();
		ProductPriceSpecial ppspecial = productPrice.getSpecial();
		if (ppspecial != null) {
			// this type of discount supercedes
			if (ppspecial.getProductPriceSpecialStartDate() != null
					&& ppspecial.getProductPriceSpecialEndDate() != null) {
				if (ppspecial.getProductPriceSpecialStartDate().before(
						new Date(dt.getTime()))
						&& ppspecial.getProductPriceSpecialEndDate().after(
								new Date(dt.getTime()))) {

					return true;

				} else if (ppspecial.getProductPriceSpecialDurationDays() > -1) {

					Date startDate = ppspecial
							.getProductPriceSpecialStartDate();

					int numDays = ppspecial
							.getProductPriceSpecialDurationDays();
					Date purchased = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(dt);
					c.add(Calendar.DATE, numDays);
					// if(dt.before(c.getTime())) {

					if (dt.before(c.getTime())
							&& ppspecial.getProductPriceSpecialAmount()
									.floatValue() < productPrice
									.getProductPriceAmount().floatValue()) {
						return true;
					} else {
						return false;
					}
				}

			}
		}
		return false;
	}

	public static BigDecimal determinePrice(ProductPrice productPrice) {
		Date dt = new Date();
		BigDecimal price = productPrice.getProductPriceAmount();
		ProductPriceSpecial ppspecial = productPrice.getSpecial();
		if (ppspecial != null) {
			// this type of discount supercedes
			if (ppspecial.getProductPriceSpecialStartDate() != null
					&& ppspecial.getProductPriceSpecialEndDate() != null) {
				if (ppspecial.getProductPriceSpecialStartDate().before(
						new Date(dt.getTime()))
						&& ppspecial.getProductPriceSpecialEndDate().after(
								new Date(dt.getTime()))) {

					price = ppspecial.getProductPriceSpecialAmount();

				} else if (ppspecial.getProductPriceSpecialDurationDays() > -1) {

					Date startDate = ppspecial
							.getProductPriceSpecialStartDate();

					int numDays = ppspecial
							.getProductPriceSpecialDurationDays();
					Date purchased = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(dt);
					c.add(Calendar.DATE, numDays);
					// if(dt.before(c.getTime())) {

					if (dt.before(c.getTime())
							&& ppspecial.getProductPriceSpecialAmount()
									.floatValue() < productPrice
									.getProductPriceAmount().floatValue()) {
						price = ppspecial.getProductPriceSpecialAmount();
					} else {
						price = productPrice.getProductPriceAmount();
					}

				}

			}
		}

		return price;
	}

	public static BigDecimal determinePrice(OrderProductPrice productPrice) {
		Date dt = new Date();
		BigDecimal price = productPrice.getProductPriceAmount();
		ProductPriceSpecial ppspecial = productPrice.getSpecial();
		if (ppspecial != null) {
			// this type of discount supercedes
			if (ppspecial.getProductPriceSpecialStartDate() != null
					&& ppspecial.getProductPriceSpecialEndDate() != null) {
				if (ppspecial.getProductPriceSpecialStartDate().before(
						new Date(dt.getTime()))
						&& ppspecial.getProductPriceSpecialEndDate().after(
								new Date(dt.getTime()))) {

					price = ppspecial.getProductPriceSpecialAmount();

				} else if (ppspecial.getProductPriceSpecialDurationDays() > -1) {

					price = ppspecial.getProductPriceSpecialAmount();

				}

			}

		}

		return price;

	}



}
