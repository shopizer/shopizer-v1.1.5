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
import java.util.Locale;

import com.salesmanager.core.entity.catalog.ProductPrice;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductPrice;
import com.salesmanager.core.entity.orders.OrderTotalSummary;

public interface PriceModule {

	public BigDecimal getPrice(ProductPrice productPrice, String currency);

	public String getPriceText(String currency, Locale locale);

	public String getPricePrefixText(String currency, Locale locale);

	public String getPriceSuffixText(String currency, Locale locale);

	public OrderTotalSummary calculateOrderPrice(Order order,
			OrderTotalSummary orderSummary, OrderProduct product,
			OrderProductPrice productPrice, String currency);

	public OrderTotalSummary calculateOrderPrice(Order order,
			OrderTotalSummary orderSummary, OrderProduct product,
			OrderProductPrice productPrice, String currency, Locale locale);

	public boolean isTaxApplicable();

	public String getHtmlPriceFormated(String prefix,
			ProductPrice productPrice, Locale locale, String currency);

}
