/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.checkout.web;

public interface Constants {

	public static final String MERCHANT_ID_PARAM = "merchantId";
	public static final String PRODUCT_ID_PARAM = "productId";
	public static final String LOCALE_PARAM = "locale";
	public static final String QUANTITY_PARAM = "qty";
	public static final String ATTRIBUTE_PARAM = "attributeId";
	public static final String ATTRIBUTE_VALUE_PARAM = "attributeValue";

	public static final int LOW_STOCK_PRODUCT_QUANTITY = 5;
	public static final int OUT_OF_STOCK_PRODUCT_QUANTITY = 0;

	public static final String EMAIL_REGEXPR = "[a-z0-9]+([_\\.-][a-z0-9]+)*@([a-z0-9]+)+[_\\.-]+[a-z.]*[a-z]$";
	public static final String PHONE_REGEXPR = "\\d{3}\\-\\d{3}\\-\\d{4}";

	// public static final String
	// DEFAULT_LANG=(String)PropertiesUtil.getConfiguration().getProperty("core.system.defaultlanguage");
	public static final String DEFAULT_LANG = "en";
}
