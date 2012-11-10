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
package com.salesmanager.core.constants;

public class PaymentConstants {

	public final static int CAPTURE = 2;
	public final static int PREAUTH = 1;
	public final static int SALE = 0;
	public final static int REFUND = 3;

	public final static int PRODUCTION_ENVIRONMENT = 1;
	public final static int TEST_ENVIRONMENT = 2;
	
	public final static int PAYMENT_TYPE_REGULAR = 0; //matches core_modules_services table subtype (0=regular [moneyorder, cc, cod, paypal], 1=credit card gateway [authorizenet, beanstream, psigate, moneris...])
	public final static int PAYMENT_TYPE_CREDIT_CARD_GATEWAY = 1;
	
	public final static String MODULE_PAYMENT = "MD_PAY_";
	public final static String MODULE_PAYMENT_GATEWAY = "MD_PAY_GW_";
	public final static String MODULE_PAYMENT_INDICATOR_NAME = "MD_PAY_INDNM";

	public final static int INTEGRATION_SERVICE_PAYMENT_METHODS = 2;

	public final static String MODULE_PAY_GW_NAME = "MD_PAY_GW_NAME";
	public final static String MODULE_PAY_GW_CREDENTIALS = "MD_PAY_GW_CREDENTIALS";
	public final static String MODULE_PAY_GW_MODE = "MDE_PAY_GW_MODE";
	public final static String MODULE_PAY_GW_AUTHSALE = "MD_PAY_GW_AUTHSALE";
	public final static String MODULE_PAY_GW_PROPS = "MD_PAY_GW_PROPS";

	public final static String MODULE_PAYMENT_METHODS = "MD_PAY_METHODS";

	public final static String PAYMENT_FREE = "free";
	public final static String PAYMENT_MONEYORDERNAME = "moneyorder";
	public final static String PAYMENT_CODNAME = "cod";
	public final static String PAYMENT_PAYPALNAME = "paypal";
	public final static String PAYMENT_PAYFLOWPRONAME = "payflowpro";
	public final static String PAYMENT_LINKPOINTNAME = "linkpoint";
	public final static String PAYMENT_AUTHORIZENETNAME = "authorizenet";
	public final static String PAYMENT_MONERIS = "moneris";
	public final static String PAYMENT_PSIGATENAME = "psigate";

}
