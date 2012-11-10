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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.entity.orders.OrderTotal;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.orders.OrderTotalLine;

public class OrderUtil {

	public static Map<String, OrderTotal> getOrderTotals(long orderId,
			OrderTotalSummary summary, String currency, Locale locale)
			throws Exception {

		Map returnMap = new LinkedHashMap();

		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(locale);

		// other fees
		OrderTotalLine[] other = summary.getOtherDueNowAmounts();
		if (other != null) {

			for (int i = 0; i < other.length; i++) {

				OrderTotalLine line = other[i];
				OrderTotal o = new OrderTotal();
				o.setModule(OrderConstants.OT_OTHER_DUE_NOW);
				o.setOrderId(orderId);
				o.setTitle(line.getText());
				o.setText(CurrencyUtil.displayFormatedAmountWithCurrency(line
						.getCost(), currency));
				o.setValue(line.getCost());
				o.setSortOrder(40 + i);
				returnMap.put(OrderConstants.OT_OTHER_DUE_NOW + "_" + i, o);
			}

		}

		// credits
		Collection dueNowCreditsCollection = summary.getDueNowCredits();
		if (dueNowCreditsCollection != null
				&& dueNowCreditsCollection.size() > 0) {

			Iterator dueNowIterator = dueNowCreditsCollection.iterator();
			BigDecimal credit = new BigDecimal("0");
			while (dueNowIterator.hasNext()) {

				OrderTotalLine line = (OrderTotalLine) dueNowIterator
						.next();
				credit = credit.add(line.getCost());

			}

			OrderTotal o = new OrderTotal();

			o.setModule(OrderConstants.OT_CREDITS);
			o.setOrderId(orderId);
			o.setTitle(label.getText(locale, "label.order.ordertotal.credits"));
			o.setText(CurrencyUtil.displayFormatedAmountWithCurrency(credit,
					currency));
			o.setValue(credit);
			o.setSortOrder(100);

			returnMap.put(OrderConstants.OT_CREDITS, o);

		}

		BigDecimal subTotal = summary.getOneTimeSubTotal();
		if (subTotal != null) {
			OrderTotal o = new OrderTotal();
			o.setModule(OrderConstants.OT_SUBTOTAL_MODULE);
			o.setOrderId(orderId);
			o.setTitle(label.getText(locale, "label.cart.subtotal"));
			o.setText(CurrencyUtil.displayFormatedAmountWithCurrency(subTotal,
					currency));
			o.setValue(subTotal);
			o.setSortOrder(200);
			returnMap.put(OrderConstants.OT_SUBTOTAL_MODULE, o);
		}

		BigDecimal shipping = summary.getShippingTotal();
		if (shipping != null && summary.isShipping()) {
			OrderTotal o = new OrderTotal();
			o.setModule(OrderConstants.OT_SHIPPING_MODULE);
			o.setOrderId(orderId);
			o.setTitle(label.getText(locale, "label.cart.shipping"));
			o.setText(CurrencyUtil.displayFormatedAmountWithCurrency(shipping,
					currency));
			o.setValue(shipping);
			o.setSortOrder(300);
			returnMap.put(OrderConstants.OT_SHIPPING_MODULE, o);
		}

		OrderTotalLine[] taxLines = summary.getTaxAmounts();
		if (taxLines != null) {

			for (int i = 0; i < taxLines.length; i++) {

				OrderTotalLine line = taxLines[i];
				OrderTotal o = new OrderTotal();
				o.setModule(OrderConstants.OT_TAX_MODULE);
				o.setOrderId(orderId);
				o.setTitle(line.getText());
				o.setText(CurrencyUtil.displayFormatedAmountWithCurrency(line
						.getCost(), currency));
				o.setValue(line.getCost());
				o.setSortOrder(400 + i);
				returnMap.put(OrderConstants.OT_TAX_MODULE + "_" + i, o);
			}

		}

		BigDecimal total = summary.getTotal();
		if (shipping != null) {
			OrderTotal o = new OrderTotal();
			o.setModule(OrderConstants.OT_TOTAL_MODULE);
			o.setOrderId(orderId);
			o.setTitle(label.getText(locale, "label.cart.total"));
			o.setText(CurrencyUtil.displayFormatedAmountWithCurrency(total,
					currency));
			o.setValue(total);
			o.setSortOrder(500);
			returnMap.put(OrderConstants.OT_TOTAL_MODULE, o);
		}

		// Add recuring
		OrderTotalLine[] recuringCollection = summary.getRecursiveAmounts();
		if (recuringCollection != null && recuringCollection.length > 0) {

			BigDecimal recur = new BigDecimal("0");
			for (int i = 0; i < recuringCollection.length; i++) {

				OrderTotalLine line = (OrderTotalLine) recuringCollection[i];
				recur = recur.add(line.getCost());

			}

			OrderTotal o = new OrderTotal();

			o.setModule(OrderConstants.OT_RECURING);
			o.setOrderId(orderId);
			o
					.setTitle(label.getText(locale,
							"label.order.ordertotal.recuring"));
			o.setText(CurrencyUtil.displayFormatedAmountWithCurrency(recur,
					currency));
			o.setValue(recur);
			o.setSortOrder(600);

			returnMap.put(OrderConstants.OT_RECURING, o);

		}

		// @todo recuring credits

		Collection recuringCreditsCollection = summary.getRecursiveCredits();
		if (recuringCreditsCollection != null
				&& recuringCreditsCollection.size() > 0) {

			Iterator recuringIterator = recuringCreditsCollection.iterator();
			BigDecimal credit = new BigDecimal("0");
			while (recuringIterator.hasNext()) {

				OrderTotalLine line = (OrderTotalLine) recuringIterator
						.next();
				credit = credit.add(line.getCost());

			}

			OrderTotal o = new OrderTotal();

			o.setModule(OrderConstants.OT_RECURING_CREDITS);
			o.setOrderId(orderId);
			o.setTitle(label.getText(locale,
					"label.order.ordertotal.recuringcredits"));
			o.setText(CurrencyUtil.displayFormatedAmountWithCurrency(credit,
					currency));
			o.setValue(credit);
			o.setSortOrder(700);

			returnMap.put(OrderConstants.OT_RECURING_CREDITS, o);

		}

		return returnMap;

	}

}
