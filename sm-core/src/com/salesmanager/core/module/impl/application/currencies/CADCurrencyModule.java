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

import com.salesmanager.core.entity.reference.Currency;

public class CADCurrencyModule extends USDCurrencyModule {

	protected final static char DOLLAR = '\u0024';

	private Currency currency;

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public BigDecimal getAmount(String amount) throws Exception {
		// same as US currency
		return super.getAmount(amount);
	}

	public String getFormatedAmount(BigDecimal amount) throws Exception {
		// same as USD
		return super.getFormatedAmount(amount);
	}

	public String getFormatedAmountWithCurrency(BigDecimal amount)
			throws Exception {
		// same as USD
		return super.getFormatedAmountWithCurrency(amount);
	}

	public String getCurrencySymbol() {
		return Character.toString(DOLLAR);
	}

}
