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
import org.apache.log4j.Logger;

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

/**
 * Designed for handling monthly recursive prices
 * 
 * @author Administrator
 * 
 */
public class MonthlyPriceModule implements PriceModule {

	private Logger log = Logger.getLogger(MonthlyPriceModule.class);

	public OrderTotalSummary calculateOrderPrice(Order order,
			OrderTotalSummary orderSummary, OrderProduct orderProduct,
			OrderProductPrice productPrice, String currency, Locale locale) {

		/**
		 * Monthly price goes in the oneTime fees as well as in the upcoming
		 * recursive fees
		 */

		BigDecimal finalPrice = null;
		BigDecimal discountPrice = null;

		BigDecimal originalPrice = orderProduct.getOriginalProductPrice();
		if (!productPrice.isDefaultPrice()) {
			originalPrice = productPrice.getProductPriceAmount();
		}

		int quantity = orderProduct.getProductQuantity();

		// the real price is the price submited
		finalPrice = orderProduct.getProductPrice();
		finalPrice = finalPrice.multiply(new BigDecimal(quantity));

		// the final price is the product price * quantity

		if (finalPrice == null) {// pick it from the productPrice
			finalPrice = productPrice.getProductPriceAmount();
			finalPrice = finalPrice.multiply(new BigDecimal(quantity));
		}

		// this type of price needs an upfront payment
		BigDecimal otprice = orderSummary.getOneTimeSubTotal();
		if (otprice == null) {
			otprice = new BigDecimal(0);
		}

		otprice = otprice.add(finalPrice);
		orderSummary.setOneTimeSubTotal(otprice);

		ProductPriceSpecial pps = productPrice.getSpecial();

		// Build text
		StringBuffer notes = new StringBuffer();
		notes.append(quantity).append(" x ");
		notes.append(orderProduct.getProductName());
		notes.append(" ");
		if (!productPrice.isDefaultPrice()) {
			notes.append(CurrencyUtil.displayFormatedAmountWithCurrency(
					productPrice.getProductPriceAmount(), currency));
		} else {
			notes.append(CurrencyUtil.displayFormatedAmountWithCurrency(
					orderProduct.getProductPrice(), currency));
		}
		notes.append(" ");
		notes.append(this.getPriceSuffixText(currency, locale));

		if (pps != null) {
			if (pps.getProductPriceSpecialStartDate() != null
					&& pps.getProductPriceSpecialEndDate() != null) {
				if (pps.getProductPriceSpecialStartDate().before(
						order.getDatePurchased())
						&& pps.getProductPriceSpecialEndDate().after(
								order.getDatePurchased())) {

					BigDecimal dPrice = new BigDecimal(ProductUtil
							.determinePrice(productPrice).floatValue());

					BigDecimal subTotal = originalPrice
							.multiply(new BigDecimal(orderProduct
									.getProductQuantity()));
					BigDecimal creditSubTotal = pps
							.getProductPriceSpecialAmount().multiply(
									new BigDecimal(orderProduct
											.getProductQuantity()));
					BigDecimal credit = subTotal.subtract(creditSubTotal);

					if (dPrice.floatValue() < productPrice
							.getProductPriceAmount().floatValue()) {

						discountPrice = productPrice.getProductPriceAmount()
								.subtract(dPrice);

						BigDecimal newPrice = orderProduct.getProductPrice();

						if (!productPrice.isDefaultPrice()) {
							newPrice = productPrice.getProductPriceAmount();
						} else {
							newPrice = newPrice.add(discountPrice);
						}

						StringBuffer spacialNote = new StringBuffer();
						spacialNote.append("<font color=\"red\">[");
						spacialNote.append(orderProduct.getProductName());
						spacialNote.append(" ");
						spacialNote.append(CurrencyUtil
								.displayFormatedAmountWithCurrency(credit,
										currency));
						spacialNote.append(" ");
						spacialNote.append(LabelUtil.getInstance().getText(
								locale, "label.generic.rebate"));
						spacialNote.append(" ");
						spacialNote.append(LabelUtil.getInstance().getText(
								locale, "label.generic.until"));

						spacialNote.append(" ");
						spacialNote.append(DateUtil.formatDate(pps
								.getProductPriceSpecialEndDate()));
						spacialNote.append("]</font>");

						OrderTotalLine line = new OrderTotalLine();
						// BigDecimal credit = discountPrice;
						line.setText(spacialNote.toString());
						line.setCost(credit);
						line.setCostFormated(CurrencyUtil
								.displayFormatedAmountWithCurrency(credit,
										currency));
						orderSummary.addDueNowCredits(line);
						orderSummary.addRecursiveCredits(line);

						BigDecimal oneTimeCredit = orderProduct
								.getApplicableCreditOneTimeCharge();
						oneTimeCredit = oneTimeCredit.add(credit);
						orderProduct
								.setApplicableCreditOneTimeCharge(oneTimeCredit);

					}

				} else if (pps.getProductPriceSpecialDurationDays() > -1) {

					Date dt = new Date(new Date().getTime());

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

						discountPrice = productPrice.getProductPriceAmount()
								.subtract(dPrice);

						BigDecimal newPrice = orderProduct.getProductPrice();


						BigDecimal subTotal = originalPrice
								.multiply(new BigDecimal(orderProduct
										.getProductQuantity()));
						BigDecimal creditSubTotal = pps
								.getProductPriceSpecialAmount().multiply(
										new BigDecimal(orderProduct
												.getProductQuantity()));
						BigDecimal credit = subTotal.subtract(creditSubTotal);

						if (!productPrice.isDefaultPrice()) {
							newPrice = productPrice.getProductPriceAmount();
						} else {
							newPrice = newPrice.add(discountPrice);
						}

						StringBuffer spacialNote = new StringBuffer();

						spacialNote.append("<font color=\"red\">[");
						spacialNote.append(orderProduct.getProductName());
						spacialNote.append(" ");
						spacialNote.append(CurrencyUtil
								.displayFormatedAmountWithCurrency(credit,
										currency));
						spacialNote.append(" ");
						spacialNote.append(LabelUtil.getInstance().getText(
								locale, "label.generic.rebate"));
						spacialNote.append(" ");
						spacialNote.append(LabelUtil.getInstance().getText(
								locale, "label.generic.until"));

						spacialNote.append(" ");
						spacialNote.append(DateUtil.formatDate(c.getTime()));
						spacialNote.append("]</font>");


						OrderTotalLine line = new OrderTotalLine();


						line.setText(spacialNote.toString());
						line.setCost(credit);
						line.setCostFormated(CurrencyUtil
								.displayFormatedAmountWithCurrency(credit,
										currency));
						orderSummary.addDueNowCredits(line);
						if (numDays > 30) {
							orderSummary.addRecursiveCredits(line);
						}

						BigDecimal oneTimeCredit = orderProduct
								.getApplicableCreditOneTimeCharge();
						oneTimeCredit = oneTimeCredit.add(credit);
						orderProduct
								.setApplicableCreditOneTimeCharge(oneTimeCredit);

						// }

					}

				}

			}

		}


		BigDecimal newPrice = orderProduct.getProductPrice();
		if (!productPrice.isDefaultPrice()) {
			newPrice = productPrice.getProductPriceAmount();
		}

		newPrice = newPrice.multiply(new BigDecimal(quantity));

		// Recursive sub total
		BigDecimal rprice = orderSummary.getRecursiveSubTotal();
		if (rprice == null) {
			rprice = new BigDecimal(0);
		}

		// recursive always contain full price
		rprice = rprice.add(newPrice);
		orderSummary.setRecursiveSubTotal(rprice);

		// recursive price
		OrderTotalLine scl = new OrderTotalLine();
		scl.setText(notes.toString());
		scl.setCost(newPrice);
		scl.setCostFormated(CurrencyUtil.displayFormatedAmountWithCurrency(
				newPrice, currency));
		orderSummary.addRecursivePrice(scl);

		return orderSummary;

	}

	public OrderTotalSummary calculateOrderPrice(Order order,
			OrderTotalSummary orderSummary, OrderProduct orderProduct,
			OrderProductPrice productPrice, String currency) {

		return null;
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
		p.append("<div class='product-price product-monthly-price'>");
		if (!ProductUtil.hasDiscount(productPrice)) {
			p.append("<div style='width:50%;float:left;'>");
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

			p.append("<div class='product-price product-monthly-price'>");
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

	public String getPriceText(String currency, Locale locale) {
		// TODO Auto-generated method stub

		if (locale == null)
			locale = LocaleUtil.getDefaultLocale();

		return LabelUtil.getInstance().getText(locale,
				"module.description.monthly-price");
	}

	public String getPricePrefixText(String currency, Locale locale) {
		return "";
	}

	public boolean isTaxApplicable() {
		return true;
	}

	public String getPriceSuffixText(String currency, Locale locale) {

		String desc = "";
		try {

			if (locale != null) {
				desc = LabelUtil.getInstance().getText(locale,
						"module.suffix.recursive-monthly");
			} else {
				desc = LabelUtil.getInstance().getText(
						"module.suffix.recursive-monthly");
			}

		} catch (Exception e) {
			log.error(e);
		}

		return desc;

	}

}
