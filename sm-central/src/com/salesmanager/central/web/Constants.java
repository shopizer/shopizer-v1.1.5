/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.web;

import java.util.Arrays;
import java.util.List;

public class Constants {

	public final static int ADMIN_MERCHANT_REG_DEF_CODE = 2;
	public final static String ADMIN_TOKEN_PARAM = "ADMIN_TOKEN_PARAM";

	/** should be removed **/

	public final static int US_COUNTRY_ID = 223;
	public final static int CA_COUNTRY_ID = 38;

	public final static int FRENCH = 2;
	public final static int ENGLISH = 1;

	public final static String ENGLISH_CODE = "en";
	public final static String FRENCH_CODE = "fr";

	public final static String US_ISOCODE = "US";
	public final static String CA_ISOCODE = "CA";
	public final static String UK_ISOCODE = "UK";
	public final static String FR_ISOCODE = "FR";

	public final static int WEIGHT_UNITS_TYPE = 1;
	public final static int SIZE_UNITS_TYPE = 2;

	public final static String CURRENCY_CODE_EURO = "EUR";
	public final static String CURRENCY_CODE_POUND = "GBP";
	public final static String CURRENCY_CODE_CAD = "CAD";
	public final static String CURRENCY_CODE_USD = "USD";

	public final static List<Integer> MERCHANT_REG_DEF_CODES = Arrays
			.asList(new Integer[] { 2, 1 });

}
