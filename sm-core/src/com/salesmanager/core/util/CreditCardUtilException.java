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

public class CreditCardUtilException extends Exception {

	String message;

	public final static int CREDIT_CARD_NUMBER = 99;
	public final static int CVV = 1;
	public final static int DATE = 2;

	private int errorType = CREDIT_CARD_NUMBER;// default credit card validation

	public CreditCardUtilException(String message) {
		// super(message);
		this.message = message;
	}

	public CreditCardUtilException(String message, int type) {
		// super(message);
		this.message = message;
		this.errorType = type;
	}

	public String getMessage() {
		return message;
	}

	public int getErrorType() {
		return errorType;
	}

}
