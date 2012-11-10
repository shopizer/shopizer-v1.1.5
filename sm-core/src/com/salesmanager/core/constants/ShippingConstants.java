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

public class ShippingConstants {

	public final static int PRODUCTION_ENVIRONMENT = 1;
	public final static int TEST_ENVIRONMENT = 2;

	public final static int INTEGRATION_SERVICE_SHIPPING_RT_QUOTE = 1;
	public final static int INTEGRATION_SERVICE_SHIPPING_PACKING_SUBTYPE = 1;

	/** Goes along with MODULE_SHIPPING_DISPLAY_REALTIME_QUOTES **/
	public final static int DISPLAY_RT_QUOTE_TIME = 1;
	public final static int NO_DISPLAY_RT_QUOTE_TIME = 0;

	public final static int ALL_QUOTES_DISPLAYED = 0;
	public final static int LESS_EXPENSIVE_QUOTE_DISPLAYED = 1;
	public final static int MAX_EXPENSIVE_QUOTE_DISPLAYED = 2;
	/**/

	public final static String DEFAULT_PACKING_MODULE = "packing-item";
	public final static String PACKING_CONFIGURATION_KEY = "SHP_PACK";
	public final static String GLOBAL_SHIPPING = "global";
	public final static String DOMESTIC_SHIPPING = "domestic";
	public final static String INTERNATIONAL_SHIPPING = "international";
	public final static String MODULE_SHIPPING_ZONES_SHIPPING = "SHP_ZONES_SHIPPING";// international
																						// or
																						// domestic
	public final static String MODULE_SHIPPING_ZONES_SKIPPED = "SHP_ZONES_SKIPPED";// countries
																					// skipped
	public final static String MODULE_SHIPPING_RT_GLOBAL = "SHP_RT_GLOBAL";// real-time
																			// shipping
																			// module
																			// international
																			// &
																			// domestic
																			// services
																			// selection

	public final static String MODULE_TAX_BASIS = "MODULE_TAX_BASIS";// Shipping
																		// or
																		// Billing

	// DB KEYS
	public final static String MODULE_SHIPPING_DISPLAY_REALTIME_QUOTES = "SHP_MD_DISP_RTQT";// For
																							// displaying
																							// real
																							// time
																							// quotes
																							// or
																							// not
																							// &
																							// which
																							// quote
																							// to
																							// display
	public final static String MODULE_SHIPPING_INDIC_COUNTRIES_COSTS = "SHP_ZONES_INDCCOSTS";// INDICATOR-5
																								// zones
																								// of
																								// countries-5
																								// costs
	public final static String MODULE_SHIPPING_RT_MODULE_INDIC_NAME = "SHP_MD_RT_INDNM";// RT
																						// indicator
																						// and
																						// name
	public final static String MODULE_SHIPPING_RT_CRED = "SHP_RT_CRED";// RT
																		// credentials
	public final static String MODULE_SHIPPING_RT_PKG_DOM_INT = "SHP_RT_PKGDOMINT";// packages
																					// &
																					// services
																					// domestic
																					// -
																					// international
	public final static String MODULE_SHIPPING_FREE_IND_DEST_AMNT = "SHP_FREE_INDDESTAMNT";
	public final static String MODULE_SHIPPING_ESTIMATE_BYCOUNTRY = "SHP_ESTIMATE_COUNTRY";

	public final static int SHIPPING_RISK = 1;
	public final static int SHIPPING_SAFE = 0;

	public final static String MODULE_SHIPPING_HANDLING_FEES = "SHP_HANDLING_FEES";

	public final static String MODULE_SHIPPING_TAX_CLASS = "SHP_TAX_CLASS";

	public final static String MODULE_SHIPPING_RT_QUOTES_USPS = "uspsxml";
	public final static String MODULE_SHIPPING_RT_QUOTES_CP = "canadapost";
	public final static String MODULE_SHIPPING_RT_QUOTES_FEDEX = "fedex";
	public final static String MODULE_SHIPPING_RT_QUOTES_FEDEXEXPRESS = "fedexexpress";
	public final static String MODULE_SHIPPING_RT_QUOTES_FEDEXGROUND = "fedexground";
	public final static String MODULE_SHIPPING_RT_QUOTES_UPS = "upsxml";
	public final static String MODULE_SHIPPING_RT_QUOTES_FREE = "free";

	public final static int MAX_PRICE_REGION_COUNT = 5;
	public final static int MAX_PRICE_RANGE_COUNT = 5;
}
