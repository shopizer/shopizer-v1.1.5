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
package com.salesmanager.core.entity.payment;

import java.io.Serializable;
import java.util.Locale;

import com.salesmanager.core.util.CreditCardUtil;
import com.salesmanager.core.util.LabelUtil;

public class CreditCard implements Serializable {

	private String cardNumber;
	private int creditCardCode = -1;
	private String expirationYear;
	private String expirationMonth;
	private String cvv;
	private String cardOwner;
	private Locale locale;

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getCreditCardCode() {
		return creditCardCode;
	}

	public void setCreditCardCode(int creditCardCode) {
		this.creditCardCode = creditCardCode;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public String getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}

	public String getEncryptedCreditCardNumber() {
		CreditCardUtil util = new CreditCardUtil();
		try {
			String enc = util.maskCardNumber(this.getCardNumber());
			return enc;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public String getCreditCardName() {
		switch (this.getCreditCardCode()) {
		case CreditCardUtil.VISA:
			return LabelUtil.getInstance().getText(locale,
					"label.payment.creditcard.visa");
		case CreditCardUtil.AMEX:
			return LabelUtil.getInstance().getText(locale,
					"label.payment.creditcard.amex");
		case CreditCardUtil.MASTERCARD:
			return LabelUtil.getInstance().getText(locale,
					"label.payment.creditcard.mastercard");
		case CreditCardUtil.DINERS:
			return LabelUtil.getInstance().getText(locale,
					"label.payment.creditcard.diners");
		case CreditCardUtil.DISCOVER:
			return LabelUtil.getInstance().getText(locale,
					"label.payment.creditcard.discovery");
		default:
			return LabelUtil.getInstance().getText(locale,
					"label.patment.creditcard.default");
		}
	}

	public String getCardOwner() {
		return cardOwner;
	}

	public void setCardOwner(String cardOwner) {
		this.cardOwner = cardOwner;
	}

}
