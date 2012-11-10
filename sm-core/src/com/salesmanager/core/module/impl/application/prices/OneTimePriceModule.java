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
package com.salesmanager.core.module.impl.application.prices;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.entity.catalog.ProductPrice;
import com.salesmanager.core.entity.catalog.ProductPriceSpecial;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductPrice;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.orders.OrderTotalLine;
import com.salesmanager.core.module.model.application.PriceModule;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.ProductUtil;

public class OneTimePriceModule implements PriceModule {

	public OrderTotalSummary calculateOrderPrice(Order order,
			OrderTotalSummary orderSummary, OrderProduct orderProduct,
			OrderProductPrice productPrice, String currency) {
		// TODO Auto-generated method stub
		return orderSummary;
	}

	public OrderTotalSummary calculateOrderPrice(Order order,
			OrderTotalSummary orderSummary, OrderProduct orderProduct,
			OrderProductPrice productPrice, String currency, Locale locale) {
		// TODO Auto-generated method stub


		/**
		 * activation price goes in the oneTime fees
		 */

		BigDecimal finalPrice = null;
		// BigDecimal discountPrice=null;

		// order price this type of price needs an upfront payment
		BigDecimal otprice = orderSummary.getOneTimeSubTotal();
		if (otprice == null) {
			otprice = new BigDecimal(0);
		}

		// the final price is in the product price
		finalPrice = productPrice.getProductPriceAmount();
		int quantity = orderProduct.getProductQuantity();
		finalPrice = finalPrice.multiply(new BigDecimal(quantity));

		otprice = otprice.add(finalPrice);
		orderSummary.setOneTimeSubTotal(otprice);

		ProductPriceSpecial pps = productPrice.getSpecial();

		// Build text

		StringBuffer notes = new StringBuffer();
		notes.append(quantity).append(" x ");
		notes.append(orderProduct.getProductName());
		notes.append(" ");
		notes.append(CurrencyUtil.displayFormatedAmountWithCurrency(
				productPrice.getProductPriceAmount(), currency));
		notes.append(" ");

		notes.append(productPrice.getProductPriceName());

		BigDecimal originalPrice = orderProduct.getOriginalProductPrice();
		if (!productPrice.isDefaultPrice()) {
			originalPrice = productPrice.getProductPriceAmount();
		}

		if (pps != null) {
			if (pps.getProductPriceSpecialStartDate() != null
					&& pps.getProductPriceSpecialEndDate() != null) {
				if (pps.getProductPriceSpecialStartDate().before(
						order.getDatePurchased())
						&& pps.getProductPriceSpecialEndDate().after(
								order.getDatePurchased())) {

					BigDecimal dPrice = new BigDecimal(ProductUtil
							.determinePrice(productPrice).floatValue());

					if (dPrice.floatValue() < productPrice
							.getProductPriceAmount().floatValue()) {

						BigDecimal subTotal = originalPrice
								.multiply(new BigDecimal(orderProduct
										.getProductQuantity()));
						BigDecimal creditSubTotal = pps
								.getProductPriceSpecialAmount().multiply(
										new BigDecimal(orderProduct
												.getProductQuantity()));

						BigDecimal credit = subTotal.subtract(creditSubTotal);

						StringBuffer spacialNote = new StringBuffer();
						spacialNote.append("<font color=\"red\">[");

						spacialNote.append(productPrice.getProductPriceName());

						// spacialNote.append(getPriceText(currency,locale));

						spacialNote.append(" ");
						spacialNote.append(CurrencyUtil
								.displayFormatedAmountWithCurrency(credit,
										currency));

						spacialNote.append("]</font>");

						if (productPrice.getProductPriceAmount().doubleValue() > pps
								.getProductPriceSpecialAmount().doubleValue()) {

							OrderTotalLine line = new OrderTotalLine();
							line.setText(spacialNote.toString());
							line.setCost(credit);
							line.setCostFormated(CurrencyUtil
									.displayFormatedAmountWithCurrency(credit,
											currency));
							orderSummary.addDueNowCredits(line);

							BigDecimal oneTimeCredit = orderProduct
									.getApplicableCreditOneTimeCharge();
							oneTimeCredit = oneTimeCredit.add(credit);
							orderProduct
									.setApplicableCreditOneTimeCharge(oneTimeCredit);
						}
					}

				} else if (pps.getProductPriceSpecialDurationDays() > -1) {

					Date dt = new Date();

					int numDays = pps.getProductPriceSpecialDurationDays();
					Date purchased = order.getDatePurchased();
					Calendar c = Calendar.getInstance();
					c.setTime(dt);
					c.add(Calendar.DATE, numDays);

					BigDecimal dPrice = new BigDecimal(ProductUtil
							.determinePrice(productPrice).floatValue());

					if (dt.before(c.getTime())
							&& dPrice.floatValue() < productPrice
									.getProductPriceAmount().floatValue()) {


						BigDecimal subTotal = originalPrice
								.multiply(new BigDecimal(orderProduct
										.getProductQuantity()));
						BigDecimal creditSubTotal = pps
								.getProductPriceSpecialAmount().multiply(
										new BigDecimal(orderProduct
												.getProductQuantity()));

						BigDecimal credit = subTotal.subtract(creditSubTotal);

						StringBuffer spacialNote = new StringBuffer();
						spacialNote.append("<font color=\"red\">[");

						spacialNote.append(productPrice.getProductPriceName());

						// spacialNote.append(getPriceText(currency,locale));
						spacialNote.append(" ");
						spacialNote.append(CurrencyUtil
								.displayFormatedAmountWithCurrency(credit,
										currency));

						spacialNote.append("]</font>");

						if (productPrice.getProductPriceAmount().doubleValue() > pps
								.getProductPriceSpecialAmount().doubleValue()) {

							OrderTotalLine line = new OrderTotalLine();

							line.setText(spacialNote.toString());
							line.setCost(credit);
							line.setCostFormated(CurrencyUtil
									.displayFormatedAmountWithCurrency(credit,
											currency));
							orderSummary.addDueNowCredits(line);

							BigDecimal oneTimeCredit = orderProduct
									.getApplicableCreditOneTimeCharge();
							oneTimeCredit = oneTimeCredit.add(credit);
							orderProduct
									.setApplicableCreditOneTimeCharge(oneTimeCredit);

						}

					}

				}
			}

		}

		if (!productPrice.isDefaultPrice()) {

			// add a price description
			OrderTotalLine scl = new OrderTotalLine();
			scl.setText(notes.toString());
			scl.setCost(finalPrice);
			scl.setCostFormated(CurrencyUtil.displayFormatedAmountWithCurrency(
					finalPrice, currency));
			orderSummary.addOtherDueNowPrice(scl);
		}

		return orderSummary;

	}

	public boolean isTaxApplicable() {
		return true;
	}

	public BigDecimal getPrice(ProductPrice productPrice, String currency) {
		// TODO Auto-generated method stub
		return ProductUtil.determinePrice(productPrice);
	}

	public String getHtmlPriceFormated(String prefix,
			ProductPrice productPrice, Locale locale, String currency) {

		if (locale == null)
			locale = LocaleUtil.getDefaultLocale();

		if (currency == null)
			currency = CurrencyUtil.getDefaultCurrency();

		StringBuffer p = new StringBuffer();
		p.append("<div class='product-price product-onetime-price'>");
		if (!ProductUtil.hasDiscount(productPrice)) {
			p.append("<div class='product-price-price' style='width:50%;float:left;'>");
			if (!StringUtils.isBlank(prefix)) {
				p.append("<div class='product-price-text'><strong>").append(prefix).append(" : </strong></div>");
			}
			p.append(CurrencyUtil.displayFormatedAmountWithCurrency(
					productPrice.getProductPriceAmount(), currency));
			p.append(getPriceSuffixText(currency, locale));
			p.append("</div>");
			p.append("<div class='product-line'>&nbsp;</div>");
		} else {

			double arith = productPrice.getSpecial()
					.getProductPriceSpecialAmount().doubleValue()
					/ productPrice.getProductPriceAmount().doubleValue();
			double fsdiscount = 100 - arith * 100;
			Float percentagediscount = new Float(fsdiscount);
			String savediscount = String.valueOf(percentagediscount.intValue());

			p.append("<div class='product-price-price' style='width:50%;float:left;'>");
			if (!StringUtils.isBlank(prefix)) {
				p.append("<div class='product-price-text'><strong>").append(prefix).append(" : </strong></div>");
			}
			p.append("<strike>").append(
					CurrencyUtil.displayFormatedAmountWithCurrency(productPrice
							.getProductPriceAmount(), currency)).append(
					getPriceSuffixText(currency, locale)).append("</strike>")
					.append("</div>").append(
							"<div style='width:50%;float:right;'>").append(
							"<font color='red'>").append(
							CurrencyUtil.displayFormatedAmountWithCurrency(
									ProductUtil.determinePrice(productPrice),
									currency)).append(
							getPriceSuffixText(currency, locale)).append(
							"</font>").append("<br>").append(
							"<font color='red' style='font-size:75%;'>")
					.append(
							LabelUtil.getInstance().getText(locale,
									"label.generic.save")).append(" :").append(
							savediscount).append(
							LabelUtil.getInstance().getText(locale,
									"label.generic.percentsign")).append(" ")
					.append(
							LabelUtil.getInstance().getText(locale,
									"label.generic.off")).append("</font>");

			Date discountEndDate = ProductUtil.getDiscountEndDate(productPrice);

			if (discountEndDate != null) {
				p.append("<br>").append(" <font style='font-size:65%;'>")
						.append(
								LabelUtil.getInstance().getText(locale,
										"label.generic.until"))
						.append("&nbsp;").append(
								DateUtil.formatDate(discountEndDate)).append(
								"</font>");
			}

			p.append("</div>");
		}
		p.append("</div>");
		return p.toString();

	}

	public String getPricePrefixText(String currency, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPriceSuffixText(String currency, Locale locale) {
		// TODO Auto-generated method stub
		return "";
	}

	public String getPriceText(String currency, Locale locale) {
		// TODO Auto-generated method stub
		if (locale == null)
			locale = LocaleUtil.getDefaultLocale();

		return LabelUtil.getInstance().getText(locale,
				"module.description.onetime-price");

	}

}
