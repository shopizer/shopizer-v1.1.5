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
package com.salesmanager.core.module.impl.application.currencies;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;

import com.opensymphony.xwork2.validator.ValidationException;
import com.salesmanager.core.entity.reference.Currency;
import com.salesmanager.core.module.impl.common.CurrencyModuleUtil;
import com.salesmanager.core.module.model.application.CurrencyModule;

/**
 * Needed to implement other currencies
 * 
 * @author Carl Samson
 * 
 */
public class GenericCurrencyModule implements CurrencyModule {

	private Currency currency;
	private String s = "";
	private char decimalCount = '2';
	private char decimalPoint = '.';
	private char thousandPoint = ',';
	private String suffix;

	public String getSuffix() {
		return suffix;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
		s = currency.getSymbol();
		if (!StringUtils.isBlank(currency.getSuffix())) {
			suffix = currency.getSuffix();
		}
		suffix = currency.getSuffix();
		decimalCount = currency.getDecimalPlaces();
		decimalPoint = currency.getDecimalPoint();
		thousandPoint = currency.getThousandsPoint();
	}

	public BigDecimal getAmount(String amount) throws Exception {

		// validations
		/**
		 * 1) remove decimal and thousand
		 * 
		 * String.replaceAll(decimalPoint, ""); String.replaceAll(thousandPoint,
		 * "");
		 * 
		 * Should be able to parse to Integer
		 */
		StringBuffer newAmount = new StringBuffer();
		for (int i = 0; i < amount.length(); i++) {
			if (amount.charAt(i) != decimalPoint
					&& amount.charAt(i) != thousandPoint) {
				newAmount.append(amount.charAt(i));
			}
		}

		try {
			Integer.parseInt(newAmount.toString());
		} catch (Exception e) {
			throw new ValidationException("Cannot parse " + amount);
		}

		if (!amount.contains(Character.toString(decimalPoint))
				&& !amount.contains(Character.toString(thousandPoint))
				&& !amount.contains(" ")) {

			if (CurrencyModuleUtil.matchPositiveInteger(amount)) {
				BigDecimalValidator validator = CurrencyValidator.getInstance();
				BigDecimal bdamount = validator.validate(amount, Locale.US);
				if (bdamount == null) {
					throw new ValidationException("Cannot parse " + amount);
				} else {
					return bdamount;
				}
			} else {
				throw new ValidationException("Not a positive integer "
						+ amount);
			}

		} else {

			StringBuffer pat = new StringBuffer();

			if (!StringUtils.isBlank(Character.toString(thousandPoint))) {
				pat.append("\\d{1,3}(" + thousandPoint + "?\\d{3})*");
			}

			pat.append("(\\" + decimalPoint + "\\d{1," + decimalCount + "})");

			Pattern pattern = Pattern.compile(pat.toString());

			Matcher matcher = pattern.matcher(amount);

			if (matcher.matches()) {

				Locale locale = Locale.US;

				if (this.decimalPoint == ',') {
					locale = Locale.GERMAN;
				}

				BigDecimalValidator validator = CurrencyValidator.getInstance();
				BigDecimal bdamount = validator.validate(amount, locale);

				return bdamount;
			} else {
				throw new ValidationException("Cannot parse " + amount);
			}
		}

	}

	public String getCurrencySymbol() {
		// TODO Auto-generated method stub
		int i = 0, len = s.length();
		char c;
		StringBuffer sb = new StringBuffer(len);

		while (i < len) {
			c = s.charAt(i++);
			if (c == '\\') {
				if (i < len) {
					c = s.charAt(i++);
					if (c == 'u') {
						c = (char) Integer.parseInt(s.substring(i, i + 4), 16);
						i += 4;
					} // add other cases here as desired...
				}
			} // fall through: \ escapes itself, quotes any character but u
			sb.append(c);
		}
		return sb.toString();
	}

	public String getFormatedAmount(BigDecimal amount) throws Exception {
		// TODO Auto-generated method stub
		NumberFormat nf = null;

		Locale locale = Locale.US;

		if (this.decimalPoint == ',') {
			locale = Locale.GERMAN;
		}

		nf = NumberFormat.getInstance(locale);

		nf.setMaximumFractionDigits(Integer.parseInt(Character
				.toString(decimalCount)));
		nf.setMinimumFractionDigits(Integer.parseInt(Character
				.toString(decimalCount)));

		return nf.format(amount);
	}

	public String getFormatedAmountWithCurrency(BigDecimal amount)
			throws Exception {
		// TODO Auto-generated method stub
		String returnamount = getFormatedAmount(amount);

		StringBuffer ret = new StringBuffer().append(this.getCurrencySymbol())
				.append("").append(returnamount);
		if (this.getSuffix() != null) {
			ret.append(" ").append(this.getSuffix());
		}
		return ret.toString();

	}

	public String getFormatedAmountWithCurrency(BigDecimal amount,
			String amountCssClassName) throws Exception {
		// TODO Auto-generated method stub
		String returnamount = getFormatedAmount(amount);

		StringBuffer ret = new StringBuffer().append(this.getCurrencySymbol())
				.append("<font class='").append(amountCssClassName)
				.append("'>").append(returnamount);
		if (this.getSuffix() != null) {
			ret.append(" ").append(this.getSuffix());
		}

		ret.append("</font>");
		return ret.toString();
	}

	public String getMeasure(BigDecimal measure, String currencycode)
			throws Exception {

		NumberFormat nf = null;

		Locale locale = Locale.US;

		if (this.decimalPoint == ',') {
			locale = Locale.GERMAN;
		}

		nf = NumberFormat.getInstance(locale);

		nf.setMaximumFractionDigits(1);
		nf.setMinimumFractionDigits(1);

		measure.setScale(1, BigDecimal.ROUND_HALF_UP);

		return nf.format(measure);
	}

}
