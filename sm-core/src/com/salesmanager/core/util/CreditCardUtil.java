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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.entity.reference.CentralCreditCard;
import com.salesmanager.core.service.cache.RefCache;

/**
 * Tests credit cards 
 * Master Card (16 Digits) 5105105105105100 
 * Master Card (16 Digits) 5555555555554444 
 * Visa (13 Digits) 4222222222222 
 * Visa (16 Digits) 4111111111111111 
 * Visa (16 Digits) 4012888888881881 
 * American Express (15 Digits) 378282246310005 
 * American Express (15 Digits) 371449635398431 
 * Amex Corporate (15 Digits) 378734493671000 
 * Dinners Club (14 Digits) 38520000023237
 * Dinners Club (14 Digits) 30569309025904 
 * Discover (16 Digits) 6011111111111117
 * Discover (16 Digits) 6011000990139424 
 * JCB (16 Digits) 3530111333300000 
 * JCB (16 Digits) 3566002020360505
 * 
 * @author Administrator
 * 
 */
public class CreditCardUtil {

	public static final int MASTERCARD = 0, VISA = 1;
	public static final int AMEX = 2, DISCOVER = 3, DINERS = 4;

	public static String maskCardNumber(String clearcardnumber)
			throws CreditCardUtilException {

		if (clearcardnumber.length() < 10) {
			throw new CreditCardUtilException(LabelUtil.getInstance().getText(
					"errors.creditcard.invalidnumber"));
		}

		int length = clearcardnumber.length();

		String prefix = clearcardnumber.substring(0, 4);
		String suffix = clearcardnumber.substring(length - 4);

		StringBuffer mask = new StringBuffer();
		mask.append(prefix).append("XXXXXXXXXX").append(suffix);

		return mask.toString();
	}

	public void validate(String number, int type, String month, String date)
			throws CreditCardUtilException {

		try {
			Integer.parseInt(month);
			Integer.parseInt(date);
		} catch (NumberFormatException nfe) {
			throw new CreditCardUtilException(LabelUtil.getInstance().getText(
					"errors.creditcard.invaliddate"),
					CreditCardUtilException.DATE);
		}

		if (number.equals("")) {
			throw new CreditCardUtilException(LabelUtil.getInstance().getText(
					"errors.creditcard.invalidnumber"));
		}

		Matcher m = Pattern.compile("[^\\d\\s.-]").matcher(number);

		if (m.find()) {
			// setMessage("Credit card number can only contain numbers, spaces, \"-\", and \".\"");
			throw new CreditCardUtilException(LabelUtil.getInstance().getText(
					"errors.creditcard.invalidnumber"));
		}

		Matcher matcher = Pattern.compile("[\\s.-]").matcher(number);

		number = matcher.replaceAll("");
		validateDate(Integer.parseInt(month), Integer.parseInt(date));
		validateNumber(number, type);
	}

	private void validateDate(int m, int y) throws CreditCardUtilException {
		java.util.Calendar cal = new java.util.GregorianCalendar();
		int monthNow = cal.get(java.util.Calendar.MONTH) + 1;
		int yearNow = cal.get(java.util.Calendar.YEAR);
		if (yearNow > y) {
			throw new CreditCardUtilException(LabelUtil.getInstance().getText(
					"errors.creditcard.invaliddate"));
		}
		// OK, change implementation
		if (yearNow == y && monthNow > m) {
			throw new CreditCardUtilException(LabelUtil.getInstance().getText(
					"errors.creditcard.invaliddate"));
		}

	}

	public void validateCvv(String cvvNumber, int type)
			throws CreditCardUtilException {
		
		
		if (StringUtils.isBlank(cvvNumber)) {
			throw new CreditCardUtilException(LabelUtil.getInstance().getText(
					"errors.creditcard.invalidcvv"),
					CreditCardUtilException.CVV);
		}

		String expression = "[0-9]*";

		Matcher m = Pattern.compile(expression).matcher(cvvNumber);

		boolean mt = m.matches();

		if (!mt) {
			throw new CreditCardUtilException(LabelUtil.getInstance().getText(
					"errors.creditcard.invalidcvv"),
					CreditCardUtilException.CVV);
		}

		switch (type) {
		case AMEX:
			if (cvvNumber.length() != 4) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidcvv"),
						CreditCardUtilException.CVV);
			}
			break;
		default:
			if (cvvNumber.length() != 3) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidcvv"),
						CreditCardUtilException.CVV);
			}
		}
	}

	// Check that cards start with proper digits for
	// selected card type and are also the right length.

	private void validateNumber(String number, int type)
			throws CreditCardUtilException {
		switch (type) {

		case MASTERCARD:
			if (number.length() != 16
					|| Integer.parseInt(number.substring(0, 2)) < 51
					|| Integer.parseInt(number.substring(0, 2)) > 55) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidnumber"));
			}
			break;

		case VISA:
			if ((number.length() != 13 && number.length() != 16)
					|| Integer.parseInt(number.substring(0, 1)) != 4) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidnumber"));
			}
			break;

		case AMEX:
			if (number.length() != 15
					|| (Integer.parseInt(number.substring(0, 2)) != 34 && Integer
							.parseInt(number.substring(0, 2)) != 37)) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidnumber"));
			}
			break;

		case DISCOVER:
			if (number.length() != 16
					|| Integer.parseInt(number.substring(0, 5)) != 6011) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidnumber"));
			}
			break;

		case DINERS:
			if (number.length() != 14
					|| ((Integer.parseInt(number.substring(0, 2)) != 36 && Integer
							.parseInt(number.substring(0, 2)) != 38)
							&& Integer.parseInt(number.substring(0, 3)) < 300 || Integer
							.parseInt(number.substring(0, 3)) > 305)) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidnumber"));
			}
			break;
		}
		luhnValidate(number);
	}

	// The Luhn algorithm is basically a CRC type
	// system for checking the validity of an entry.
	// All major credit cards use numbers that will
	// pass the Luhn check. Also, all of them are based
	// on MOD 10.

	private void luhnValidate(String numberString)
			throws CreditCardUtilException {
		char[] charArray = numberString.toCharArray();
		int[] number = new int[charArray.length];
		int total = 0;

		for (int i = 0; i < charArray.length; i++) {
			number[i] = Character.getNumericValue(charArray[i]);
		}

		for (int i = number.length - 2; i > -1; i -= 2) {
			number[i] *= 2;

			if (number[i] > 9)
				number[i] -= 9;
		}

		for (int i = 0; i < number.length; i++)
			total += number[i];

		if (total % 10 != 0)
			throw new CreditCardUtilException(LabelUtil.getInstance().getText(
					"errors.creditcard.invalidnumber"));

	}

	public static List<String> getCreditCardStripImages() {

		Map ccs = RefCache.getSupportedCreditCards();
		List returnList = new ArrayList();
		if (ccs != null) {
			Iterator i = ccs.keySet().iterator();
			while (i.hasNext()) {
				int code = (Integer) i.next();
				CentralCreditCard ccc = (CentralCreditCard) ccs.get(code);
				StringBuffer cardImg = new StringBuffer();
				cardImg.append("icon-cc-");
				cardImg.append(ccc.getCentralCreditCardCode());
				cardImg.append(".gif");
				returnList.add(cardImg.toString());
			}

		}

		return returnList;

	}

}
