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

public class USDCurrencyModule implements CurrencyModule {

	protected final static char DOLLAR = '\u0024';

	private static Pattern pattern = Pattern
			.compile("\\d{1,3}(,?\\d{3})*(\\.\\d{1,2})");

	private Currency currency;
	private String suffix;

	public String getSuffix() {
		return suffix;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
		if (!StringUtils.isBlank(currency.getSuffix())) {
			suffix = currency.getSuffix();
		}
	}

	public String getMeasure(BigDecimal measure, String currencycode)
			throws Exception {

		NumberFormat nf = null;

		nf = NumberFormat.getInstance(Locale.US);

		nf.setMaximumFractionDigits(1);
		nf.setMinimumFractionDigits(1);

		measure.setScale(1, BigDecimal.ROUND_HALF_UP);

		return nf.format(measure);

	}

	public BigDecimal getAmount(String amount) throws Exception {

		if (!amount.contains(",") && !amount.contains(".")
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
				throw new ValidationException("Cannot parse " + amount);
			}

		} else {

			Matcher matcher = pattern.matcher(amount);

			if (matcher.matches()) {
				BigDecimalValidator validator = CurrencyValidator.getInstance();
				BigDecimal bdamount = validator.validate(amount, Locale.US);

				return bdamount;
			} else {
				throw new ValidationException("Cannot parse " + amount);
			}
		}
	}

	public String getFormatedAmount(BigDecimal amount) throws Exception {

		NumberFormat nf = null;

		nf = NumberFormat.getInstance(Locale.US);

		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		return nf.format(amount);

	}

	public String getFormatedAmountWithCurrency(BigDecimal amount)
			throws Exception {

		String returnamount = getFormatedAmount(amount);

		char display = DOLLAR;

		StringBuffer ret = new StringBuffer().append(display).append("")
				.append(returnamount);
		if (this.getSuffix() != null) {
			ret.append(" ").append(this.getSuffix());
		}
		return ret.toString();
	}

	public String getFormatedAmountWithCurrency(BigDecimal amount,
			String amountCssClassName) throws Exception {

		String returnamount = getFormatedAmount(amount);

		char display = DOLLAR;

		StringBuffer ret = new StringBuffer().append(display).append(
				"<font class='").append(amountCssClassName).append("'>")
				.append(returnamount);
		if (this.getSuffix() != null) {
			ret.append(" ").append(this.getSuffix());
		}

		ret.append("</font>");
		return ret.toString();
	}

	public String getCurrencySymbol() {
		return Character.toString(DOLLAR);
	}

}
