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
package com.salesmanager.checkout.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.ShoppingCart;
import com.salesmanager.core.entity.orders.ShoppingCartProduct;
import com.salesmanager.core.util.CurrencyUtil;

public class MiniShoppingCartUtil {

	public static void calculateTotal(ShoppingCart cart, MerchantStore store) {

		Collection productsCollection = cart.getProducts();

		int quantity = 0;

		BigDecimal total = new BigDecimal(0);

		if (productsCollection != null) {
			Iterator i = productsCollection.iterator();
			while (i.hasNext()) {
				ShoppingCartProduct scp = (ShoppingCartProduct) i.next();
				quantity = quantity + scp.getQuantity();
				BigDecimal productPrice = scp.getPrice();
				BigDecimal linePrice = productPrice.multiply(new BigDecimal(scp
						.getQuantity()));
				total = total.add(linePrice);
			}
		}

		String t = CurrencyUtil.displayFormatedAmountWithCurrency(total, store
				.getCurrency());
		cart.setQuantity(quantity);
		cart.setTotal(t);

	}

}
