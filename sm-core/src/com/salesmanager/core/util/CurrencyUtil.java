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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.validator.ValidationException;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Currency;
import com.salesmanager.core.module.impl.application.currencies.GenericCurrencyModule;
import com.salesmanager.core.module.model.application.CurrencyModule;
import com.salesmanager.core.service.cache.RefCache;

public class CurrencyUtil {

	private final static char EURO = '\u20AC';
	private final static char POUND = '\u00A3';
	private final static char DOLLAR = '\u0024';
	private static Logger log = Logger.getLogger(CurrencyUtil.class);

	private static Map currencyMap = new HashMap();

	static {

		try {

			Map currencies = RefCache.getCurrenciesListWithCodes();
			if (currencies != null) {
				Iterator i = currencies.keySet().iterator();
				while (i.hasNext()) {
					String code = (String) i.next();

					try {

						CurrencyModule module = (CurrencyModule) SpringUtil
								.getBean(code);
						Currency cur = (Currency) currencies.get(code);
						if (module != null) {
							module.setCurrency(cur);
							currencyMap.put(code, module);
						} else {
							log
									.warn("Currency "
											+ code
											+ " is not supported by a Spring module, using GenericCurrency");

							GenericCurrencyModule currency = new GenericCurrencyModule();
							currency.setCurrency(cur);
							currencyMap.put(code, currency);
						}

					} catch (Exception e) {
						log
								.warn("Currency "
										+ code
										+ " is not supported by a Spring module, using GenericCurrency");
						Currency cur = (Currency) currencies.get(code);
						GenericCurrencyModule currency = new GenericCurrencyModule();
						currency.setCurrency(cur);
						currencyMap.put(code, currency);
					}

				}
			}

		} catch (Exception e) {
			log.error(e);
		}

	}

	public static BigDecimal validateMeasure(String measure, String currencycode)
			throws ValidationException {

		try {

			if (currencycode == null) {
				currencycode = getDefaultCurrency();
			}

			log.debug("Trying to validate " + measure + " for currency "
					+ currencycode);

			CurrencyModule module = (CurrencyModule) currencyMap
					.get(currencycode);

			if (module == null) {
				throw new ValidationException(
						"There is no CurrencyModule defined for currency "
								+ currencycode
								+ " in module/impl/application/currencies");
			}

			BigDecimal returnMeasure = module.getAmount(measure);
			returnMeasure.setScale(1, BigDecimal.ROUND_HALF_UP);
			return returnMeasure;

		} catch (Exception e) {
			if (e instanceof ValidationException)
				throw (ValidationException) e;
			throw new ValidationException(e.getMessage());
		}

	}

	public static BigDecimal validateCurrency(String amount, String currencycode)
			throws ValidationException {

		try {

			if (currencycode == null) {
				currencycode = getDefaultCurrency();
			}

			log.debug("Trying to validate " + amount + " for currency "
					+ currencycode);

			CurrencyModule module = (CurrencyModule) currencyMap
					.get(currencycode);

			if (module == null) {
				throw new ValidationException(
						"There is no CurrencyModule defined for currency "
								+ currencycode
								+ " in module/impl/application/currencies");
			}

			return module.getAmount(amount);

		} catch (Exception e) {
			if (e instanceof ValidationException)
				throw (ValidationException) e;
			throw new ValidationException(e.getMessage());
		}
	}

	/**
	 * Get the measure according to the appropriate measure base. If the measure
	 * configured in store is LB and it needs KG then the appropriate
	 * calculation is done
	 * 
	 * @param weight
	 * @param store
	 * @param base
	 * @return
	 */
	public static double getWeight(double weight, MerchantStore store,
			String base) {

		double weightConstant = 2.2;
		if (base.equals(Constants.LB_WEIGHT_UNIT)) {
			if (store.getWeightunitcode().equals(Constants.LB_WEIGHT_UNIT)) {
				return new BigDecimal(String.valueOf(weight)).setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
			} else {// pound = kilogram
				double answer = weight * weightConstant;
				BigDecimal w = new BigDecimal(answer);
				return w.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		} else {// need KG
			if (store.getWeightunitcode().equals(Constants.KG_WEIGHT_UNIT)) {
				return new BigDecimal(String.valueOf(weight)).setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
			} else {

				double answer = weight / weightConstant;
				BigDecimal w = new BigDecimal(answer);
				return w.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			}
		}
	}

	/**
	 * Converts a BigDecimal amount from a given currency to another currency
	 * 
	 * @param amount
	 * @param originCurrency
	 * @param toCurrency
	 * @return
	 */
	public static BigDecimal convertToCurrency(BigDecimal amount,
			String originCurrency, String toCurrency) {

		try {

			// get originCurrency
			Map currencies = RefCache.getCurrenciesListWithCodes();
			double returnAmount = amount.doubleValue();
			com.salesmanager.core.entity.reference.Currency origin = (com.salesmanager.core.entity.reference.Currency) currencies
					.get(originCurrency);
			com.salesmanager.core.entity.reference.Currency convert = (com.salesmanager.core.entity.reference.Currency) currencies
					.get(toCurrency);
			if (origin == null) {
				log.error("Origin currency " + originCurrency + " not found");
				return amount;
			}

			if (convert == null) {
				log.error("Convert currency " + toCurrency + " not found");
				return amount;
			}

			returnAmount = returnAmount / origin.getValue().doubleValue();

			returnAmount = returnAmount * convert.getValue().doubleValue();

			return new BigDecimal(returnAmount)
					.setScale(2, BigDecimal.ROUND_UP);

		} catch (Exception e) {
			log.equals(e);
			return amount;
		}
	}

	/**
	 * Get the measure according to the appropriate measure base. If the measure
	 * configured in store is IN and it needs CM or vise versa then the
	 * appropriate calculation is done
	 * 
	 * @param weight
	 * @param store
	 * @param base
	 * @return
	 */
	public static double getMeasure(double measure, MerchantStore store,
			String base) {

		if (base.equals(Constants.INCH_SIZE_UNIT)) {
			if (store.getSeizeunitcode().equals(Constants.INCH_SIZE_UNIT)) {
				return new BigDecimal(String.valueOf(measure)).setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
			} else {// centimeter (inch to centimeter)
				double measureConstant = 2.54;

				double answer = measure * measureConstant;
				BigDecimal w = new BigDecimal(answer);
				return w.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			}
		} else {// need CM
			if (store.getSeizeunitcode().equals(Constants.CM_SIZE_UNIT)) {
				return new BigDecimal(String.valueOf(measure)).setScale(2)
						.doubleValue();
			} else {// in (centimeter to inch)
				double measureConstant = 0.39;

				double answer = measure * measureConstant;
				BigDecimal w = new BigDecimal(answer);
				return w.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			}
		}

	}

	/**
	 * For displaying weight, length, height and width
	 * 
	 * @param measure
	 * @param currncycode
	 * @return
	 */

	public static String displayMeasure(BigDecimal measure, String currencycode) {

		try {

			if (measure == null) {
				return "";
			}

			if (currencycode == null) {
				currencycode = getDefaultCurrency();
			}

			CurrencyModule module = (CurrencyModule) currencyMap
					.get(currencycode);

			if (module == null) {
				log.error("There is no CurrencyModule defined for currency "
						+ currencycode
						+ " in module/impl/application/currencies");
				return measure.toString();
			}

			return module.getMeasure(measure, currencycode);

		} catch (Exception e) {
			log.error("Cannot format measure " + measure.toString()
					+ " for currency " + currencycode);
			return measure.toString();
		}
	}

	private static String displayFormatedAmount(BigDecimal amount,
			String currencycode) {

		try {

			if (amount == null) {
				return "";
			}

			if (currencycode == null) {
				currencycode = getDefaultCurrency();
			}

			CurrencyModule module = (CurrencyModule) currencyMap
					.get(currencycode);

			if (module == null) {
				log.error("There is no CurrencyModule defined for currency "
						+ currencycode
						+ " in module/impl/application/currencies");
				return amount.toString();
			}

			return module.getFormatedAmount(amount);

		} catch (Exception e) {
			log.error("Cannot format amount " + amount.toString()
					+ " for currency " + currencycode);
			return amount.toString();
		}

	}

	public static String displayFormatedAmountWithCurrency(BigDecimal amount,
			String currencycode) {

		try {

			if (currencycode == null) {
				currencycode = getDefaultCurrency();
			}

			CurrencyModule module = (CurrencyModule) currencyMap
					.get(currencycode);

			if (module == null) {
				log.error("There is no CurrencyModule defined for currency "
						+ currencycode
						+ " in module/impl/application/currencies");
				return amount.toString();
			}

			return module.getFormatedAmountWithCurrency(amount);

		} catch (Exception e) {
			log.error("Cannot format amount " + amount.toString()
					+ " for currency " + currencycode);
			return amount.toString();
		}
	}

	public static String displayFormatedCssAmountWithCurrency(
			BigDecimal amount, String currencycode) {

		try {

			if (currencycode == null) {
				currencycode = getDefaultCurrency();
			}

			CurrencyModule module = (CurrencyModule) currencyMap
					.get(currencycode);

			if (module == null) {
				log.error("There is no CurrencyModule defined for currency "
						+ currencycode
						+ " in module/impl/application/currencies");
				return amount.toString();
			}

			return module
					.getFormatedAmountWithCurrency(amount, "product-value");

		} catch (Exception e) {
			log.error("Cannot format amount " + amount.toString()
					+ " for currency " + currencycode);
			return amount.toString();
		}
	}

	public static String displayFormatedAmountNoCurrency(BigDecimal amount,
			String currencycode) {

		if (currencycode == null) {
			currencycode = getDefaultCurrency();
		}

		return displayFormatedAmount(amount, currencycode);

	}

	public static String getAmount(BigDecimal amount, String currencycode) {

		if (currencycode == null) {
			currencycode = getDefaultCurrency();
		}

		return displayFormatedAmount(amount, currencycode);

	}

	public static BigDecimal getAmount(String amount, String currencyCode)
			throws ValidationException {

		CurrencyModule module = (CurrencyModule) currencyMap.get(currencyCode);

		if (module == null) {
			throw new ValidationException(
					"There is no CurrencyModule defined for currency "
							+ currencyCode
							+ " in module/impl/application/currencies");
		}

		try {

			return module.getAmount(amount);

		} catch (Exception e) {
			log.error("Cannot format amount " + amount + " for currency "
					+ currencyCode);
			return null;
		}

	}

	public static String displayEditablePriceWithCurrency(String textname,
			int textsize, boolean displaycurrency, BigDecimal amount,
			String currencycode, String appender) {

		if (currencycode == null) {
			currencycode = getDefaultCurrency();
		}

		StringBuffer formatedfieldbuffer = new StringBuffer();

		CurrencyModule module = (CurrencyModule) currencyMap.get(currencycode);

		if (module == null) {
			log.error("There is no CurrencyModule defined for currency "
					+ currencycode + " in module/impl/application/currencies");
			return amount.toString();
		}

		String returnamount = "";
		try {
			returnamount = module.getFormatedAmount(amount);
		} catch (Exception e) {
			log.error("Cannot format amount " + amount.toString()
					+ " for currency " + currencycode);
			returnamount = amount.toString();
		}

		String display = module.getCurrencySymbol();

		return new StringBuffer().append(display).append(" ").append(
				"<input type=\"text\" name=\"").append(textname).append("\"")
				.append(" id=\"").append(textname).append("\"").append(
						" value=\"").append(returnamount).append("\"").append(
						" size=\"").append(textsize).append("\"").append(
						appender != null ? " " + appender : "").append(">")
				.toString();
	}

	public static String getDefaultCurrency() {

		Configuration conf = PropertiesUtil.getConfiguration();
		String def = conf.getString("core.system.defaultcurrency");
		if (def == null) {
			def = Constants.CURRENCY_CODE_USD;
		}

		return def;
	}

}
