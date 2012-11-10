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
package com.salesmanager.core.module.model.application;

import java.math.BigDecimal;

import com.salesmanager.core.entity.reference.Currency;

public interface CurrencyModule {

	/**
	 * Format a measure unit
	 * 
	 * @param measure
	 * @param currencycode
	 * @return
	 * @throws Exception
	 */
	public String getMeasure(BigDecimal measure, String currencycode)
			throws Exception;

	/**
	 * This method validates that the String amount complies with the currency
	 * and returns a BigDecimal for the given amount
	 * 
	 * @param amount
	 * @return
	 */
	public BigDecimal getAmount(String amount) throws Exception;

	/**
	 * Will format the amount based on the appropriate currency
	 * 
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public String getFormatedAmount(BigDecimal amount) throws Exception;

	/**
	 * Will format the amount and add the currency symbol where appropriate
	 * 
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public String getFormatedAmountWithCurrency(BigDecimal amount)
			throws Exception;

	/**
	 * Can be used to receive an HTML formated amount that can format the amount
	 * 
	 * @param amount
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public String getFormatedAmountWithCurrency(BigDecimal amount,
			String amountCssClassName) throws Exception;

	public String getCurrencySymbol();

	/**
	 * Set currency entity when instantiating objects
	 * 
	 * @param currency
	 */
	public void setCurrency(Currency currency);

}
